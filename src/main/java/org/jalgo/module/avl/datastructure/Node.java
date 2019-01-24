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

import org.jalgo.module.avl.Constants;

/**
 * The class <code>Node</code> represents a node in a tree. It has a key value
 * and references to his parent and child nodes. The height of the partial tree
 * with this as root node is stored in the node directly. This approach is more
 * efficient than calculating the height everytime it is needed.
 * 
 * @author Ulrike Fischer, Alexander Claus
 */
public class Node
implements Visualizable, Constants {

	protected Node rightChild;
	protected Node leftChild;
	protected Node parent;
	protected int key;
	protected int visualizationStatus;
	protected int height;

	/**
	 * Constructs a <code>Node</code> object with the given key. A call to
	 * this constructor is equal to <code>Node(key, null)</code>.
	 * 
	 * @param key the key of the node
	 */
	public Node(int key) {
		this(key, null);
	}

	/**
	 * Constructs a <code>Node</code> object with the given key and the given
	 * node as parent node.
	 * 
	 * @param key the key of the node
	 * @param parent the parent node of this node
	 */
	public Node(int key, Node parent) {
		this.key = key;
		this.parent = parent;
		leftChild = null;
		rightChild = null;
		setVisualizationStatus(NORMAL);
	}

	/**
	 * Sets the parent node of this node to the given node.
	 * 
	 * @param p the new parent node of this node
	 */
	public void setParent(Node p) {
		parent = p;
	}

	/**
	 * Retrieves the parent node of this node. If this is the root node of a
	 * tree, <code>null</code> is returned.
	 * 
	 * @return the parent node of this node
	 */
	public Node getParent() {
		return parent;
	}

	/**
	 * Sets the left child of this node to the given node.
	 * 
	 * @param n the new left child of this node
	 */
	public void setLeftChild(Node n) {
		leftChild = n;
	}

	/**
	 * Retrieves the left child of this node.
	 * 
	 * @return the left child of this node
	 */
	public Node getLeftChild() {
		return leftChild;
	}

	/**
	 * Sets the right child of this node to the given node.
	 * 
	 * @param n the new right child of this node
	 */
	public void setRightChild(Node n) {
		rightChild = n;
	}

	/**
	 * Retrieves the right child of this node.
	 * 
	 * @return the right child of this node
	 */
	public Node getRightChild() {
		return rightChild;
	}

	/**
	 * Sets the key of this node to the given integer
	 * 
	 * @param key the new key of the node
	 */
	public void setKey(int key) {
		this.key = key;
	}

	/**
	 * Retrieves the key of this node.
	 * 
	 * @return the key of this node
	 */
	public int getKey() {
		return key;
	}

	/**
	 * Sets the height of the partial tree with this node as root node to the
	 * given integer.
	 * 
	 * @param h the height of the child tree
	 */
	public void setHeight(int h) {
		height = h;
	}

	/**
	 * Retrieves the stored height of the partial tree with this node as root
	 * node.
	 * 
	 * @return the height of the child tree
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Sets the visualization status of this node.
	 */
	public void setVisualizationStatus(int s) {
		visualizationStatus = s;
	}

	/**
	 * Retrieves the visualization status of this node.
	 */
	public int getVisualizationStatus() {
		return visualizationStatus;
	}

	/**
	 * Returns a String representation of this <code>Node</code>. Currently
	 * this String representation consists only of the key value.
	 * 
	 * @return a String representation of this <code>Node</code>
	 */
	public String toString() {
		return new StringBuffer().append(key).toString();
	}

	/**
	 * Tests if this instance of <code>Node</code> is equal to the given
	 * parameter. If the given parameter is an instance of <code>Node</code>,
	 * the key of the node is compared with the key of the other node.
	 * 
	 * @param other the <code>Object</code> to be compared with
	 * 
	 * @return <code>true</code>, if the given parameter is a node with the
	 *         same key, <code>false</code> otherwise
	 */
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other instanceof Node) return (key == ((Node)other).key);
		return false;
	}
}