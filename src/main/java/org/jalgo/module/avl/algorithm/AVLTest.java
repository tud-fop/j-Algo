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

package org.jalgo.module.avl.algorithm;

import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.datastructure.*;
import java.util.*;

/**
 * @author Ulrike Fischer
 * 
 * The class <code>AVLTest</code> checks if the current search tree has the
 * AVL characteristic.
 */

public class AVLTest
extends MacroCommand {

	private SearchTree tree;
	private boolean result = true;
	private Set<Node> nodes;

	/**
	 * @param st the searchtree
	 */
	public AVLTest(SearchTree st) {
		tree = st;
		name = Messages.getString("avl", "Alg_name.AVL_test"); //$NON-NLS-1$ //$NON-NLS-2$
		nodes = new HashSet<Node>();
	}

	/**
	 * returns true, if the tree is an AVLTree
	 */
	@SuppressWarnings("unchecked")
	public void perform() {
		results.clear();
		if (currentPosition == 0) {
			SearchTree.calculateBalances((AVLNode)tree.getRoot());
			test((AVLNode)tree.getRoot());
			results.add(0, Messages.getString("avl", "Alg_name.AVL_test")); //$NON-NLS-1$ //$NON-NLS-2$
			results.add(1, ""); //$NON-NLS-1$
			results.add(2, result);
			currentPosition++;
		}
		else {
			for (Node n : nodes)
				n.setVisualizationStatus(Visualizable.NORMAL);
			results.add(0, ""); //$NON-NLS-1$
			results.add(1, ""); //$NON-NLS-1$
		}
		currentPosition++;
	}

	private void test(AVLNode n) {
		if (n == null) return;
		if (n.getBalance() >= 2 || n.getBalance() <= -2) {
			n.setVisualizationStatus(Visualizable.FOCUSED
				& Visualizable.LINE_NORMAL | Visualizable.BALANCE);
			nodes.add(n);
			result = false;
		}
		test((AVLNode)n.getLeftChild());
		test((AVLNode)n.getRightChild());
	}

	/**
	 * This method has no effect.
	 */
	public void abort() {
	// this method has no effect
	}

	/**
	 * This method has no effect.
	 */
	public void undo() {
	// this method has no effect
	}

	/**
	 * This method has no effect.
	 */
	public void performBlockStep() {
	// this method has no effect
	}

	/**
	 * This method has no effect.
	 */
	public void undoBlockStep() {
	// this method has no effect
	}
}