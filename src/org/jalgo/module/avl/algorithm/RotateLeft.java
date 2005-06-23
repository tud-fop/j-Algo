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
public class RotateLeft extends Command {
	
	private AVLNode n;
	private WorkNode wn;
	private SearchTree tree;
	private int uBalance = 0 , wBalance = 0;
	

	/**
	 * @param tree: in this tree the Rotation is done.
	 * @param wn: the rotation is done around the node next to the worknode 
	 */
	public RotateLeft(SearchTree tree, WorkNode wn) {
		super();
		this.wn = wn;
		this.n = (AVLNode) wn.getNextToMe();
		this.tree = tree;
	}

	/**
	 * performs the rotation and actualizes the balances and heights
	 */
	public void perform() {
		results.add(0, "Linksrotation um: "+n.getKey());
		results.add(1, "3a");

		n.setVisualizationStatus(Visualizable.NORMAL);
		AVLNode u = n;
		AVLNode w = (AVLNode) u.getRightChild();
		AVLNode b = null;
		if (w.getLeftChild()!=null) 
			b = (AVLNode) w.getLeftChild();
		u.setRightChild(b);
		if (b!=null) b.setParent(u);
		w.setLeftChild(u);
		w.setParent(u.getParent());
		u.setParent(w);
		
		if (w.getParent()!=null)
			if (w.getKey() < w.getParent().getKey())
				w.getParent().setLeftChild(w);
			else
				w.getParent().setRightChild(w);
		else       // w is root of the SearchTree
			tree.setRoot(w);
		
		uBalance = u.getBalance();
		wBalance = w.getBalance();
		SearchTree.calculateBalances(w);
		
		Command ch = CommandFactory.createCalcHeight(tree.getRoot());
		ch.perform();
	
		n = w;
		wn.setNextToMe(n);
	}
	
	
	/**
	 * undoes the rotation and set the old balances and heights
	 */
	
	public void undo() {
		AVLNode w = n;
		AVLNode u = (AVLNode) w.getLeftChild();
		AVLNode b = null;
		if (u.getRightChild()!=null)
			b = (AVLNode) u.getRightChild();
		
		w.setLeftChild(b);
		if (b!=null) b.setParent(w);
		u.setRightChild(w);
		u.setParent(w.getParent());
		w.setParent(u);
		
		if (u.getParent()!=null)
			if (u.getKey() < u.getParent().getKey())
				u.getParent().setLeftChild(u);
			else
				u.getParent().setRightChild(u);
		else
			tree.setRoot(u);
		
		u.setBalance(uBalance);
		w.setBalance(wBalance);
		
		n = u;
		wn.setNextToMe(n);
		
		Command ch = CommandFactory.createCalcHeight(tree.getRoot());
		ch.perform();

		n.setVisualizationStatus(Visualizable.NORMAL | Visualizable.BALANCE | Visualizable.ROTATE_LEFT_ARROW);
	}
}
