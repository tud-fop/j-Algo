package org.jalgo.module.hoare.model;

import java.awt.Dimension;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * abstract class of a verification formula
 * 
 * @author Gerald
 * 
 */
public abstract class VerificationFormula implements Serializable {

	private Assertion preAssertion;

	private Assertion postAssertion;

	private boolean ruleApplied;

	/**
	 * beginning of the associated piece of code in the source code file
	 * 
	 */
	private Dimension codeStart;

	/**
	 * end of the associated piece of code in the source code file
	 */
	private Dimension codeEnd;

	private int id;

	private static int nextID = 1;

	protected List<VerificationFormula> children;

	protected VerificationFormula parent;

	public VerificationFormula(Assertion pre, Assertion post,
			VerificationFormula parent) {
		this.parent = parent;
		preAssertion = (pre);
		postAssertion = (post);
		id = nextID++;
		children = new ArrayList<VerificationFormula>();
		ruleApplied = false;
	}

	/**
	 * collects all VerificationFormulas in the VF-tree, if the NodeMap is
	 * updated
	 */
	public Map<Integer, VerificationFormula> collectNodes(
			Map<Integer, VerificationFormula> nodes) {
		if (hasChildren()) {
			if (children.size() >= 1)
				nodes = children.get(0).collectNodes(nodes);
			if (children.size() == 2)
				nodes = children.get(1).collectNodes(nodes);
		}
		nodes.put(id, this);
		return nodes;
	}

	/**
	 * inserts after this VF the VF newVF as a new level in the VF-Tree
	 * 
	 * @param newVF
	 *            the vf to insert
	 * @return true on success
	 */
	public boolean insertChild(VerificationFormula newVF) {

		if (newVF == null)
			return false;
		newVF.setParent(this);
		newVF.setChildren(this.children);
		for (VerificationFormula child : this.getChildren()) {
			child.setParent(newVF);
		}
		List<VerificationFormula> newChildren = new ArrayList<VerificationFormula>();
		newChildren.add(newVF);
		setChildren(newChildren);
		return true;
	}

	/**
	 * deletes the successing level of the VF-tree (WeakPostVF or StrongPreVF).
	 * deleteChild must not be used on a VF with more than one child, otherwise
	 * there may be inconsistencies!
	 * 
	 * @return true on success
	 */
	public boolean deleteChild() {

		if (!hasChildren())
			return false;
		children = children.get(0).getChildren();
		for (VerificationFormula child : getChildren()) {
			child.setParent(this);
		}
		return true;
	}

	public boolean hasChildren() {
		return !children.isEmpty();
	}

	public Assertion getPreAssertion() {
		return preAssertion;
	}

	public Assertion getPostAssertion() {
		return postAssertion;
	}

	public int getID() {
		return id;
	}

	public void setPreAssertion(Assertion a) {
		this.preAssertion = a;
	}

	public void setPostAssertion(Assertion a) {
		this.postAssertion = a;
	}

	public void setID(int id) {
		this.id = id;
	}

	public List<VerificationFormula> getChildren() {
		return children;
	}

	/**
	 * sets a new list of children
	 * 
	 * @param children
	 *            the new list of children
	 */
	public void setChildren(List<VerificationFormula> children) {
		this.children = children;
	}

	/**
	 * adds a child to the current list of children
	 * 
	 * @param child
	 *            the child to add
	 */
	public void addChild(VerificationFormula child) {
		children.add(child);
	}

	public Dimension getCodeEnd() {
		return codeEnd;
	}

	public void setCodeEnd(Dimension codeEnd) {
		this.codeEnd = codeEnd;
	}

	public Dimension getCodeStart() {
		return codeStart;
	}

	public void setCodeStart(Dimension codeStart) {
		this.codeStart = codeStart;
	}

	public Class<? extends VerificationFormula> getType() {
		return getClass();
	}

	public void replaceAssertion(Assertion o, Assertion n) {
		if (preAssertion == o)
			setPreAssertion(n);
		else
			preAssertion.replaceTarget(o, n);

		if (postAssertion == o)
			setPostAssertion(n);
		else
			postAssertion.replaceTarget(o, n);

		for (VerificationFormula child : children)
			child.replaceAssertion(o, n);
	}

	public VerificationFormula getParent() {
		return parent;
	}

	public void setParent(VerificationFormula parent) {
		this.parent = parent;
	}

	public boolean isRuleApplied() {
		return ruleApplied;
	}

	public void setRuleApplied(boolean ruleApplied) {
		this.ruleApplied = ruleApplied;
	}

	/**
	 * skipping StrongPre, WeakPost => parent instead
	 * 
	 * @return
	 */
	public VerificationFormula pureVF() {
		return this;
	}

	public static int getNextID() {
		return nextID;
	}

	public static void setNextID(int nextID) {
		VerificationFormula.nextID = nextID;
	}
}
