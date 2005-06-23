package org.jalgo.module.avl.algorithm;

import org.jalgo.module.avl.datastructure.AVLNode;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.datastructure.Visualizable;
import org.jalgo.module.avl.datastructure.WorkNode;


/**
 * Realizes a left rotation around a given node. 
 * 
 * @author Jean Christoph Jung
 */
public class RotateRight extends Command {

	private WorkNode wn;
	private AVLNode n;
	private SearchTree tree;
	private int uBalance, vBalance;

	/**
	 * @param tree: in this tree the Rotation is done.
	 * @param wn: the rotation is done around the node next to the worknode 
	 */
	public RotateRight(SearchTree tree, WorkNode wn) {
		super();
		this.wn = wn;
		this.n = (AVLNode) wn.getNextToMe();
		this.tree = tree;
	}
	
	/**
	 * performs the rotation and actualizes the balances and heights
	 */
	public void perform() {
		results.add(0, "Rechtsrotation um: "+n.getKey());
		results.add(1, "3a");

		n.setVisualizationStatus(Visualizable.NORMAL);
		AVLNode v = n;
		AVLNode u = (AVLNode) v.getLeftChild();
		AVLNode b = null;
		if (u.getRightChild()!=null)
			b = (AVLNode) u.getRightChild();
		
		v.setLeftChild(b);
		if (b!=null) 
			b.setParent(v);
		u.setRightChild(v);
		u.setParent(v.getParent());
		v.setParent(u);
		
		if (u.getParent()!=null)
			if (u.getKey() > u.getParent().getKey()) 
				u.getParent().setRightChild(u);
			else 
				u.getParent().setLeftChild(u);
		else                   // u is the root of the SearchTree
			tree.setRoot(u);
		
		uBalance = u.getBalance();
		vBalance = v.getBalance();
		SearchTree.calculateBalances(u);
		n = u;
		wn.setNextToMe(n);
	
		Command ch = CommandFactory.createCalcHeight(tree.getRoot());
		ch.perform();
	}
	
	/**
	 * undoes the rotation and set the old balances and heights
	 */
	public void undo() {
		AVLNode u = n;
		AVLNode v = (AVLNode) u.getRightChild();
		AVLNode b = null;
		if (v.getLeftChild()!=null)
			b = (AVLNode) v.getLeftChild();
		u.setRightChild(b);
		if (b!=null) b.setParent(u);
		v.setParent(u.getParent());
		u.setParent(v);
		v.setLeftChild(u);

		if (v.getParent()!=null)
			if (v.getKey() > v.getParent().getKey())
				v.getParent().setRightChild(v);
			else
				v.getParent().setLeftChild(v);
		else               // v is the root of the SearchTree
			tree.setRoot(v);
		
		u.setBalance(uBalance);
		v.setBalance(vBalance);
		
		n = v;
		wn.setNextToMe(n);
		
		Command ch = CommandFactory.createCalcHeight(tree.getRoot());
		ch.perform();

		n.setVisualizationStatus(Visualizable.NORMAL | Visualizable.BALANCE | Visualizable.ROTATE_RIGHT_ARROW);
	}
}
