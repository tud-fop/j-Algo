/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

package org.jalgo.module.avl.datastructure;

/**
 * The class <code>AVLNode</code> represents a node in an AVL tree. In
 * addition to the fields of <code>Node</code>, an <code>AVLNode</code> has
 * also a balance.
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
	 * Constructs an <code>AVLNode</code> object with the given key and the
	 * given node as parent node.
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