package org.jalgo.module.avl.algorithm;
import org.jalgo.module.avl.datastructure.*;
import java.util.*;

/**
 * @author Ulrike Fischer
 * 
 * The class <code>AVLTest</code> checks if the current search tree has the 
 * AVL characteristic.
 */

public class AVLTest extends MacroCommand {
	
	private SearchTree tree;
	private boolean result=true;
	private Set<Node> nodes;
	
	/**
	 * @param st the searchtree
	 */
	
	public AVLTest(SearchTree st) {
		tree = st;
		name = "AVL-Test";
		nodes = new HashSet<Node>(); 
	}
	
	/**
	 * returns true, if the tree is an AVLTree
	 */
	
	public void perform() {
		results.clear();
		if (currentPosition==0) {
			SearchTree.calculateBalances((AVLNode) tree.getRoot());
			test((AVLNode) tree.getRoot());
			results.add(0,"AVL-Test");
			results.add(1,"");
			results.add(2,result);
			currentPosition++;
		}
		else {
			for (Node n:nodes) {
				n.setVisualizationStatus(Visualizable.NORMAL);
			}
			results.add(0,"");
			results.add(1,"");
		}
		currentPosition++;
	}
	
	private void test(AVLNode n) {
		if (n==null) return;
		if (n.getBalance()>=2 || n.getBalance()<=-2) {
			n.setVisualizationStatus(Visualizable.FOCUSED&Visualizable.LINE_NORMAL | Visualizable.BALANCE);
			nodes.add(n);
			result=false;
		}
		test((AVLNode) n.getLeftChild());
		test((AVLNode) n.getRightChild());
	}

	/**
	 * This method has no effect.
	 */
	public void abort() {}
	
	/**
	 * This method has no effect.
	 */
	public void undo() {}
	
	/**
	 * This method has no effect.
	 */
	public void performBlockStep() {}
	
	/**
	 * This method has no effect.
	 */
	public void undoBlockStep() {}
}
