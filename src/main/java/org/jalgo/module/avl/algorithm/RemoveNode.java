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

/**
 * 
 */
package org.jalgo.module.avl.algorithm;

import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.Constants;
import org.jalgo.module.avl.datastructure.Node;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.datastructure.Visualizable;
import org.jalgo.module.avl.datastructure.WorkNode;

/**
 * @author Jean Christoph Jung
 * 
 * Eliminates a node by replacing it according to the remove-algorithm.
 */
public class RemoveNode
extends Command
implements Constants {

	private WorkNode wn;
	private SearchTree tree;
	private Node nodeToRemove;

	private int fall = -1;
	private boolean wasRoot = true;
	private Node help = null;

	/**
	 * @param wn is next to the node that is successor of
	 *            <code>nodeToRemove</code>
	 * @param tree is the searchtree that the key is removed from
	 * @param nodeToRemove the node to be deleted
	 */
	@SuppressWarnings("unchecked")
	public RemoveNode(WorkNode wn, SearchTree tree, Node nodeToRemove) {
		this.wn = wn;
		this.tree = tree;
		this.nodeToRemove = nodeToRemove;
		results.add(0, ""); //$NON-NLS-1$
		results.add(1, "absatz"); //$NON-NLS-1$
		results.add(2, WORKING);
		results.add(3, "helpnode"); //$NON-NLS-1$
	}

	/**
	 * differentiates the 3 basic cases (according to the algorithm) and
	 * replaces the node to remove by its successor or its left child
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void perform() {
		results.set(0, Messages.getString("avl", "Node_removed")); //$NON-NLS-1$ //$NON-NLS-2$
		/* Case 1: right child of node to delete is null */
		if (nodeToRemove.getRightChild() == null) {
			fall = 1;
			if (nodeToRemove == tree.getRoot()) {
				tree.setRoot(nodeToRemove.getLeftChild());
				Node root = tree.getRoot();
				if (root != null) root.setParent(null);
				results.set(2, DONE);
				results.set(3, tree.getRoot());
			}
			else {
				Node parent = nodeToRemove.getParent();
				Node n = nodeToRemove.getLeftChild();
				if (n != null) n.setParent(parent);
				if (parent.getKey() < nodeToRemove.getKey())
					parent.setRightChild(n);
				else parent.setLeftChild(n);
				wasRoot = false;
				results.set(2, FOUND);
				results.set(3, parent);
			}
		}
		/* Case 2: the right child of nodetodelete has no left child */
		else if (nodeToRemove.getRightChild().getLeftChild() == null) {
			fall = 2;
			Node n = nodeToRemove.getRightChild();
			n.setLeftChild(nodeToRemove.getLeftChild());
			if (nodeToRemove.getLeftChild() != null)
				nodeToRemove.getLeftChild().setParent(n);
			if (nodeToRemove == tree.getRoot()) {
				tree.setRoot(nodeToRemove.getRightChild());
				tree.getRoot().setParent(null);
			}
			else {
				Node parent = nodeToRemove.getParent();
				n.setParent(parent);
				if (parent.getKey() < n.getKey()) parent.setRightChild(n);
				else parent.setLeftChild(n);
				wasRoot = false;
			}
			n.setVisualizationStatus(Visualizable.NORMAL);
			results.set(2, FOUND);
			results.set(3, n);
		}
		/* Case 3: else */
		else {
			fall = 3;
			Node n = wn.getNextToMe();
			help = n.getParent();
			help.setLeftChild(null);
			n.getParent().setLeftChild(n.getRightChild());
			if (n.getRightChild() != null)
				n.getRightChild().setParent(n.getParent());
			n.setRightChild(nodeToRemove.getRightChild());
			nodeToRemove.getRightChild().setParent(n);
			n.setParent(nodeToRemove.getParent());
			if (nodeToRemove == tree.getRoot()) {
				tree.setRoot(n);
				n.setParent(null);
			}
			else {
				Node parent = nodeToRemove.getParent();
				if (parent.getKey() < n.getKey()) parent.setRightChild(n);
				else parent.setLeftChild(n);
				wasRoot = false;
			}

			n.setLeftChild(nodeToRemove.getLeftChild());
			if (nodeToRemove.getLeftChild() != null)
				nodeToRemove.getLeftChild().setParent(n);
			n.setVisualizationStatus(Visualizable.NORMAL);

			results.set(2, FOUND);
			results.set(3, help);
		}

		CalcHeight ch = CommandFactory.createCalcHeight(tree.getRoot());
		ch.perform();
	}

	/**
	 * recovers the state before the call of <code> perform </code>
	 */
	@Override
	public void undo() {
		switch (fall) {
			case 1:
				if (wasRoot == false) {
					Node parent = nodeToRemove.getParent();
					if (nodeToRemove.getKey() < parent.getKey()) {
						if (parent.getLeftChild() != null)
							parent.getLeftChild().setParent(nodeToRemove);
						parent.setLeftChild(nodeToRemove);
					}
					else {
						if (parent.getRightChild() != null)
							parent.getRightChild().setParent(nodeToRemove);
						parent.setRightChild(nodeToRemove);
					}
				}
				else {
					if (tree.getRoot() == null) tree.setRoot(nodeToRemove);
					else {
						tree.getRoot().setParent(nodeToRemove);
						tree.setRoot(nodeToRemove);
					}
				}
				wn.setNextToMe(nodeToRemove);
				break;
			case 2:
				if (wasRoot == false) {
					nodeToRemove.getRightChild().setParent(nodeToRemove);
					if (nodeToRemove.getLeftChild() != null)
						nodeToRemove.getLeftChild().setParent(nodeToRemove);
					Node parent = nodeToRemove.getParent();
					if (parent.getKey() < nodeToRemove.getKey())
						parent.setRightChild(nodeToRemove);
					else parent.setLeftChild(nodeToRemove);
					nodeToRemove.getRightChild().setLeftChild(null);
				}
				else {
					tree.setRoot(nodeToRemove);
					nodeToRemove.getRightChild().setLeftChild(null);
					nodeToRemove.getRightChild().setParent(nodeToRemove);
					if (nodeToRemove.getLeftChild() != null)
						nodeToRemove.getLeftChild().setParent(nodeToRemove);
				}
				nodeToRemove.getRightChild().setVisualizationStatus(
					Visualizable.FOCUSED | Visualizable.LINE_NORMAL);
				wn.setNextToMe(nodeToRemove.getRightChild());
				break;
			case 3:
				Node n = nodeToRemove.getRightChild().getParent();
				if (nodeToRemove.getLeftChild() != null)
					nodeToRemove.getLeftChild().setParent(nodeToRemove);
				nodeToRemove.getRightChild().setParent(nodeToRemove);
				n.setRightChild(help.getLeftChild());
				if (help.getLeftChild() != null)
					help.getLeftChild().setParent(n);
				n.setLeftChild(null);
				n.setParent(help);
				help.setLeftChild(n);
				if (wasRoot == false) {
					Node parent = nodeToRemove.getParent();
					if (parent.getKey() < nodeToRemove.getKey())
						parent.setRightChild(nodeToRemove);
					else parent.setLeftChild(nodeToRemove);
				}
				else tree.setRoot(nodeToRemove);
				wn.setNextToMe(help.getLeftChild());
				wn.getNextToMe().setVisualizationStatus(
					Visualizable.FOCUSED | Visualizable.LINE_NORMAL);
				break;
			default:
				return;
		}
		wasRoot = true;
	}
}