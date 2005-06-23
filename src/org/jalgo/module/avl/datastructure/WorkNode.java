/* Created on 25.04.2005 */
package org.jalgo.module.avl.datastructure;

/**
 * The class <code>WorkNode</code> represents the working node for AVL tree
 * algorithms. It contains a key and holds a reference to the <code>Node</code>,
 * which is the currently watched node in algorithm.
 * 
 * @author Ulrike Fischer
 */
public class WorkNode extends Node {

	private Node nextToMe;

	/**
	 * Constructs a <code>WorkNode</code> object with the given key. A call to this
	 * constructor is equal to <code>WorkNode(key, null)</code>.
	 * 
	 * @param key the key of the working node
	 */
	public WorkNode(int key) {
		this(key, null);
	}

	/**
	 * Constructs a <code>WorkNode</code> object with the given key and the given
	 * <code>Node</code> reference.
	 * 
	 * @param key the key of the working node
	 * @param next the currently watched node in algorithm
	 */
	public WorkNode(int key, Node next){
		super(key); 
		nextToMe = next;		
	}
	
	/**
	 * Retrieves the currently watched node in algorithm.
	 * 
	 * @return the currently watched node
	 */
	public Node getNextToMe() {
		return nextToMe;
	}
	
	/**
	 * Sets the currently watched node to the given node.
	 * 
	 * @param node the currently watched node in algorithm
	 */
	public void setNextToMe(Node node) {
		nextToMe = node;
	}
}