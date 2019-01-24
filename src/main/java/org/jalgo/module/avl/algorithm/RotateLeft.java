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
import org.jalgo.module.avl.datastructure.AVLNode;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.datastructure.Visualizable;
import org.jalgo.module.avl.datastructure.WorkNode;

/**
 * Realizes a left rotation around a given node.
 * 
 * @author Jean Christoph Jung
 */
public class RotateLeft
extends Command {

	private AVLNode n;
	private WorkNode wn;
	private SearchTree tree;

	private int uBalance = 0, wBalance = 0;

	/**
	 * @param tree in this tree the Rotation is done.
	 * @param wn the rotation is done around the node next to the worknode
	 */
	public RotateLeft(SearchTree tree, WorkNode wn) {
		super();
		this.wn = wn;
		this.n = (AVLNode)wn.getNextToMe();
		this.tree = tree;
	}

	/**
	 * performs the rotation and actualizes the balances and heights
	 */
	@SuppressWarnings("unchecked")
	public void perform() {
		results.add(0, Messages.getString(
			"avl", "Rotate_left_around") + n.getKey()); //$NON-NLS-1$ //$NON-NLS-2$
		results.add(1, "3a"); //$NON-NLS-1$

		n.setVisualizationStatus(Visualizable.NORMAL);
		AVLNode u = n;
		AVLNode w = (AVLNode)u.getRightChild();
		AVLNode b = null;
		if (w.getLeftChild() != null) b = (AVLNode)w.getLeftChild();
		u.setRightChild(b);
		if (b != null) b.setParent(u);
		w.setLeftChild(u);
		w.setParent(u.getParent());
		u.setParent(w);

		if (w.getParent() != null) if (w.getKey() < w.getParent().getKey())
			w.getParent().setLeftChild(w);
		else w.getParent().setRightChild(w);
		else // w is root of the SearchTree
		tree.setRoot(w);

		uBalance = u.getBalance();
		wBalance = w.getBalance();
		SearchTree.calculateBalances(w);

		Command ch = CommandFactory.createCalcHeight(tree.getRoot());
		ch.perform();

		n = w;
		wn.setNextToMe(n);
	}

	/**
	 * undoes the rotation and set the old balances and heights
	 */
	public void undo() {
		AVLNode w = n;
		AVLNode u = (AVLNode)w.getLeftChild();
		AVLNode b = null;
		if (u.getRightChild() != null) b = (AVLNode)u.getRightChild();

		w.setLeftChild(b);
		if (b != null) b.setParent(w);
		u.setRightChild(w);
		u.setParent(w.getParent());
		w.setParent(u);

		if (u.getParent() != null) if (u.getKey() < u.getParent().getKey())
			u.getParent().setLeftChild(u);
		else u.getParent().setRightChild(u);
		else tree.setRoot(u);

		u.setBalance(uBalance);
		w.setBalance(wBalance);

		n = u;
		wn.setNextToMe(n);

		Command ch = CommandFactory.createCalcHeight(tree.getRoot());
		ch.perform();

		n.setVisualizationStatus(Visualizable.NORMAL | Visualizable.BALANCE
			| Visualizable.ROTATE_LEFT_ARROW);
	}
}