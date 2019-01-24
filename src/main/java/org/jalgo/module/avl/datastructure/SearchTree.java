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

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.jalgo.module.avl.algorithm.Command;
import org.jalgo.module.avl.algorithm.CommandFactory;
import org.jalgo.module.avl.algorithm.MacroCommand;

/**
 * The class <code>SearchTree</code> is the main data structure of the AVL
 * module. It holds a reference to the root node and provides several methods
 * for working with the tree. Some methods provide information about the tree,
 * such as the weight, the height, the level of a specified node and so on.
 * Other methods provide traversion of the tree in different orders.
 * 
 * @author Matthias Schmidt, Jean Christoph Jung, Alexander Claus
 */
public class SearchTree {

	/** The root of the tree */
	private Node root;

	/**
	 * Constructs an empty <code>SearchTree</code> object. A call to this
	 * constructor is equal to <code>SearchTree(null)</code>.
	 */
	public SearchTree() {
		this(null);
	}

	/**
	 * Constructs a new <code>SearchTree</code> object with the given root
	 * node. This root node can represent a complete partial tree, but
	 * attention: ensure to calculate the height of the tree!
	 * 
	 * @param root the root of the tree
	 */
	public SearchTree(Node root) {
		this.root = root;
	}

	/**
	 * Sets the root of the tree to the given node.
	 * 
	 * @param root the new root node
	 */
	public void setRoot(Node root) {
		this.root = root;
	}

	/**
	 * Retrieves the root node of this <code>SearchTree</code>.
	 * 
	 * @return the root node of the tree
	 */
	public Node getRoot() {
		return root;
	}

	/**
	 * Prints this <code>SearchTree</code> in inorder to the standard output
	 * stream. This method is useful for debugging.
	 */
	public void printToConsole() {
		System.out.println(exportInOrder());
	}

	/**
	 * Calculates the weight of this <code>SearchTree</code>. This means, the
	 * number of <code>Node</code>s is returned.
	 * 
	 * @return the weight of this <code>SearchTree</code>
	 */
	public int getWeight() {
		return calcWeight(root);
	}

	/**
	 * Calculates the weight of the given partial tree.
	 * 
	 * @param node the root of the tree for which weight has to be calculated
	 * 
	 * @return the weight of the given partial tree
	 */
	private int calcWeight(Node node) {
		if (node == null) return 0;
		return calcWeight(node.leftChild) + calcWeight(node.rightChild) + 1;
	}

	/**
	 * Returns the height of this <code>SearchTree</code>. This method does
	 * not calculate the height. It only returns a precalculated value. So all
	 * algorithms, which can change the structure of the tree, have to calculate
	 * the height newly.<br>
	 * This approach is introduced in order to have a efficient implementation,
	 * because the height of the tree is requested relatively often.
	 * 
	 * @return the height of this <code>SearchTree</code>
	 */
	public int getHeight() {
		if (root == null) return 0;
		return root.getHeight();
	}

	/**
	 * Calculates the level of the given <code>Node</code>. This method is
	 * interesting for displaying the tree and for calculating the average
	 * height of this <code>SearchTree</code>.
	 * 
	 * @param node the node, for which the level has to be calculated
	 * 
	 * @return the level of the node
	 */
	public int getLevelFor(Node node) {
		if (node == null) return -1;

		// here no Search-command is used, because this command changes the
		// visualization status of some nodes
		int level = 0;
		Node temp = root;
		while (temp != null) {
			if (temp.getKey() < node.getKey()) temp = temp.getRightChild();
			else if (temp.getKey() > node.getKey()) temp = temp.getLeftChild();
			else return level + 1;
			level++;
		}
		return level;
	}

	/**
	 * Searches for the <code>Node</code> with the given key.
	 * 
	 * @param key the key of the searched Node
	 * 
	 * @return the <code>Node</code> with the given key, if it is in the tree,
	 *         <code>null</code> otherwise
	 */
	public Node getNodeFor(int key) {
		Node node = root;
		while (node != null) {
			if (node.getKey() < key) node = node.getRightChild();
			else if (node.getKey() > key) node = node.getLeftChild();
			else return node;
		}
		return null;
	}

	/* Traversion of the Tree */

	/**
	 * Traverses this <code>SearchTree</code> in inorder and puts the
	 * <code>Node</code>s in a list.
	 * 
	 * @return the <code>Node</code>s of this <code>SearchTree</code> in
	 *         inorder
	 */
	public List<Node> exportInOrder() {
		List<Node> nodeList = new LinkedList<Node>();
		copyNodeToInOrderList(root, nodeList);
		return nodeList;
	}

	/**
	 * A helper method to get an inorder list of the <code>Node</code>s of
	 * this <code>SearchTree</code>.
	 * 
	 * @param node the currently watched node in traversion
	 * @param list an empty <code>Node</code> list to be filled
	 */
	private void copyNodeToInOrderList(Node node, List<Node> list) {
		if (node == null) return;
		copyNodeToInOrderList(node.getLeftChild(), list);
		list.add(node);
		copyNodeToInOrderList(node.getRightChild(), list);
	}

	/**
	 * Traverses this <code>SearchTree</code> in postorder and puts the
	 * <code>Node</code>s in a list.
	 * 
	 * @return the <code>Node</code>s of this <code>SearchTree</code> in
	 *         postorder
	 */
	public List<Node> exportPostOrder() {
		List<Node> nodeList = new LinkedList<Node>();
		copyNodeToPostOrderList(root, nodeList);
		return nodeList;
	}

	/**
	 * A helper method to get an postorder list of the <code>Node</code>s of
	 * this <code>SearchTree</code>.
	 * 
	 * @param node the currently watched node in traversion
	 * @param list an empty <code>Node</code> list to be filled
	 */
	private void copyNodeToPostOrderList(Node node, List<Node> list) {
		if (node == null) return;
		copyNodeToPostOrderList(node.getLeftChild(), list);
		copyNodeToPostOrderList(node.getRightChild(), list);
		list.add(node);
	}

	/**
	 * Traverses this <code>SearchTree</code> in levelorder and puts the
	 * <code>Node</code>s in a list. This method is currently interesting for
	 * serializing the <code>SearchTree</code>.
	 * 
	 * @return the <code>Node</code>s of this <code>SearchTree</code> in
	 *         levelorder
	 */
	public List<Node> exportLevelOrder() {
		Node[] uncompressedList = exportUncompressedLevelOrder();
		int size = uncompressedList.length;
		List<Node> compressedList = new LinkedList<Node>();
		for (int i = 0; i < size; i++) {
			if (uncompressedList[i] != null)
				compressedList.add(uncompressedList[i]);
		}
		uncompressedList = null;
		return compressedList;
	}

	/**
	 * Traverses this <code>SearchTree</code> in levelorder and puts the
	 * <code>Node</code>s in a list. This list has 2^(getHeight()-1)-1
	 * entries. So it can contain many <code>null</code> values, but such an
	 * uncompressed list is interesting e.g. for displaying the tree.
	 * 
	 * @return a riddled array of the <code>Node</code>s of the tree
	 */
	public Node[] exportUncompressedLevelOrder() {
		Node[] uncompressedList = new Node[(1 << getHeight()) - 1];
		copyNodeToLevelOrderList(root, uncompressedList, 1);
		return uncompressedList;
	}

	/**
	 * A helper method to get a levelordered list of the <code>Node</code>s
	 * of this <code>SearchTree</code>. This list is riddled because of the
	 * possible unbalanced structure of the tree. The list to be filled has to
	 * be of the following type: <code>Node[(1&lt;&lt;getHeight())-1]</code>
	 * 
	 * @param node the currently watched node in traversion
	 * @param list the <code>Node</code> array to be filled
	 * @param index the index of the currently watched node, important for level
	 *            ordering
	 */
	private static void copyNodeToLevelOrderList(Node node, Node[] list,
		int index) {
		if (node == null) return;
		list[index - 1] = node;
		copyNodeToLevelOrderList(node.getLeftChild(), list, 2 * index);
		copyNodeToLevelOrderList(node.getRightChild(), list, 2 * index + 1);
	}

	/**
	 * Fills this <code>SearchTree</code> with the given keys, so that the
	 * structure of the tree is supported. Attention: the
	 * <code>SearchTree</code> is not cleared before inserting the values. If
	 * there are entries in the tree, they are keeped. If You want to use this
	 * operation to create a new tree with the given values, e.g. during
	 * deserializing, You have to ensure that this <code>SearchTree</code> is
	 * cleared before calling this operation.
	 * 
	 * @param levelOrder the key values in levelorder
	 */
	public void importLevelOrder(List<Integer> levelOrder) {
		MacroCommand insert;
		WorkNode wn;
		for (int key : levelOrder) {
			wn = new WorkNode(key);
			wn.setNextToMe(root);
			insert = CommandFactory.createInsert(wn, this);
			while (insert.hasNext())
				insert.perform();
		}

		// update height fields
		Command calcHeight = CommandFactory.createCalcHeight(root);
		calcHeight.perform();
		// update balances
		calculateBalances((AVLNode)root);
		// TODO: nur Übergangslösung, auskommentiertes soll hier rein
		// Command calcAllBalances = CommandFactory.createCalcAllBalances(root);
		// calcAllBalances.perform();
	}

	/**
	 * Removes all <code>Node</code>s from this <code>SearchTree</code>.
	 */
	public void clear() {
		root = null;
	}

	// TODO: raus mit dieser methode! daf�r haben wir ein command.
	// avltest, left- und rightrotation k�nnen das command benutzen.
	// sonst duplizierter code...
	public static int calculateBalances(AVLNode n) {
		if (n == null) return 0;
		int left = SearchTree.calculateBalances((AVLNode)n.getLeftChild());
		int right = SearchTree.calculateBalances((AVLNode)n.getRightChild());

		n.setBalance(right - left);
		return Math.max(right, left) + 1;
	}

	/**
	 * Retrieves the node with the minimal key in the partial tree with the
	 * given root node.
	 * 
	 * @param root the root node of the partial tree
	 * 
	 * @return the <code>Node</code> with the minimal key in the tree
	 */
	public static Node getMinimumNode(Node root) {
		if (root == null) return null;
		if (root.getLeftChild() != null)
			return getMinimumNode(root.getLeftChild());
		return root;
	}

	/**
	 * Retrieves the node with the minimal key in the partial tree with the
	 * given root node.
	 * 
	 * @param root the root node of the partial tree
	 * 
	 * @return the <code>Node</code> with the minimal key in the tree
	 */
	public static Node getMaximumNode(Node root) {
		if (root == null) return null;
		if (root.getRightChild() != null)
			return getMaximumNode(root.getRightChild());
		return root;
	}

	/**
	 * Tests, if the given parameter is equal to this instance of
	 * <code>SearchTree</code>.
	 */
	public boolean equals(Object other) {
		if (other == null) return false;
		if (other instanceof SearchTree) return Arrays.asList(
			exportUncompressedLevelOrder()).equals(
			Arrays.asList(((SearchTree)other).exportUncompressedLevelOrder()));
		return false;
	}
}