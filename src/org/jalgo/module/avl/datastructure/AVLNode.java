package org.jalgo.module.avl.datastructure;

/**
 * The class <code>AVLNode</code> represents a node in an AVL tree. In addition to
 * the fields of <code>Node</code>, an <code>AVLNode</code> has also a balance.
 * 
 * @author Ulrike Fischer
 */
public class AVLNode
extends Node {
	
	private int balance;

	/**
	 * Constructs an <code>AVLNode</code> with the given key. A call to this
	 * constructor is equal to <code>AVLNode(key, null)</code>.
	 * 
	 * @param key the key of the node
	 */
	public AVLNode(int key) {
		this(key, null);
	}

	/**
	 * Constructs an <code>AVLNode</code> object with the given key and the given
	 * node as parent node.
	 * 
	 * @param key the key of the node
	 * @param parent the parent node of this node
	 */
	public AVLNode(int key, AVLNode parent) {
		super(key, parent);
		balance = 0;
	}
	
	/**
	 * Sets the balance of this node to the given integer.
	 * 
	 * @param balance the new balance of this node
	 */
	public void setBalance(int balance) {
		this.balance = balance;
	}
	
	/**
	 * Retrieves the balance of this <code>AVLNode</code>.
	 * 
	 * @return the balance of this node
	 */
	public int getBalance() {
		return balance;
	}
}