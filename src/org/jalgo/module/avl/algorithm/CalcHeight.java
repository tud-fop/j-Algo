package org.jalgo.module.avl.algorithm;

import org.jalgo.module.avl.datastructure.*;

/**
 * @author Ulrike Fischer
 * 
 * The class <code>CalcHeight</code> calculates the height of one node.
 */

public class CalcHeight extends Command {
	
	private Node startNode;
	
	/**
	 * @param n the node where the height is calculated
	 */
	
	public CalcHeight(Node n) {
		super();
		startNode = n;
	}

	/**
	 * Calculates the height.
	 */
	
	public void perform() {
		calc(startNode);
	}
	
	private int calc(Node node) {
		if (node == null) return 0;
		node.setHeight(1 + Math.max(calc(node.getLeftChild()), calc(node.getRightChild())));
		return node.getHeight();
	}
	
	/**
	 * This method has no effect.
	 */
	
	public void undo() {
	}
}