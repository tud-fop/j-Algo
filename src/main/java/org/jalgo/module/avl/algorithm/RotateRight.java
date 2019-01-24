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
public class RotateRight
extends Command {

	private WorkNode wn;
	private AVLNode n;
	private SearchTree tree;

	private int uBalance, vBalance;

	/**
	 * @param tree in this tree the Rotation is done.
	 * @param wn the rotation is done around the node next to the worknode
	 */
	public RotateRight(SearchTree tree, WorkNode wn) {
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
			"avl", "Rotate_right_around") + n.getKey()); //$NON-NLS-1$ //$NON-NLS-2$
		results.add(1, "3a"); //$NON-NLS-1$

		n.setVisualizationStatus(Visualizable.NORMAL);
		AVLNode v = n;
		AVLNode u = (AVLNode)v.getLeftChild();
		AVLNode b = null;
		if (u.getRightChild() != null) b = (AVLNode)u.getRightChild();

		v.setLeftChild(b);
		if (b != null) b.setParent(v);
		u.setRightChild(v);
		u.setParent(v.getParent());
		v.setParent(u);

		if (u.getParent() != null) if (u.getKey() > u.getParent().getKey())
			u.getParent().setRightChild(u);
		else u.getParent().setLeftChild(u);
		else // u is the root of the SearchTree
		tree.setRoot(u);

		uBalance = u.getBalance();
		vBalance = v.getBalance();
		SearchTree.calculateBalances(u);
		n = u;
		wn.setNextToMe(n);

		Command ch = CommandFactory.createCalcHeight(tree.getRoot());
		ch.perform();
	}

	/**
	 * undoes the rotation and set the old balances and heights
	 */
	public void undo() {
		AVLNode u = n;
		AVLNode v = (AVLNode)u.getRightChild();
		AVLNode b = null;
		if (v.getLeftChild() != null) b = (AVLNode)v.getLeftChild();
		u.setRightChild(b);
		if (b != null) b.setParent(u);
		v.setParent(u.getParent());
		u.setParent(v);
		v.setLeftChild(u);

		if (v.getParent() != null) if (v.getKey() > v.getParent().getKey())
			v.getParent().setRightChild(v);
		else v.getParent().setLeftChild(v);
		else // v is the root of the SearchTree
		tree.setRoot(v);

		u.setBalance(uBalance);
		v.setBalance(vBalance);

		n = v;
		wn.setNextToMe(n);

		Command ch = CommandFactory.createCalcHeight(tree.getRoot());
		ch.perform();

		n.setVisualizationStatus(Visualizable.NORMAL | Visualizable.BALANCE
			| Visualizable.ROTATE_RIGHT_ARROW);
	}
}