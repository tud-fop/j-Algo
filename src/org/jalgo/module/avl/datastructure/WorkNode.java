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

/* Created on 25.04.2005 */
package org.jalgo.module.avl.datastructure;

/**
 * The class <code>WorkNode</code> represents the working node for AVL tree
 * algorithms. It contains a key and holds a reference to the <code>Node</code>,
 * which is the currently watched node in algorithm.
 * 
 * @author Ulrike Fischer
 */
public class WorkNode
extends Node {

	private Node nextToMe;

	/**
	 * Constructs a <code>WorkNode</code> object with the given key. A call to
	 * this constructor is equal to <code>WorkNode(key, null)</code>.
	 * 
	 * @param key the key of the working node
	 */
	public WorkNode(int key) {
		this(key, null);
	}

	/**
	 * Constructs a <code>WorkNode</code> object with the given key and the
	 * given <code>Node</code> reference.
	 * 
	 * @param key the key of the working node
	 * @param next the currently watched node in algorithm
	 */
	public WorkNode(int key, Node next) {
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