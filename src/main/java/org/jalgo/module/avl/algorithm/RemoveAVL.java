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

import java.util.List;

import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.Constants;
import org.jalgo.module.avl.datastructure.AVLNode;
import org.jalgo.module.avl.datastructure.Node;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.datastructure.Visualizable;
import org.jalgo.module.avl.datastructure.WorkNode;

/**
 * Realizes the Deletion of a node (given by a key) in a searchtree, holding the
 * searchtree property and the avl property.
 * 
 * @author Jean Christoph Jung
 */
public class RemoveAVL
extends MacroCommand
implements Constants {

	private WorkNode wn;
	private SearchTree tree;
	private List updatebalancelist;

	private int help = 1;

	/**
	 * @param wn Reference to the the current Node; the key of the worknode is
	 *            the key that will be deleted from the serchtree st.
	 * @param tree the tree, the algorithm is working on.
	 */
	@SuppressWarnings("unchecked")
	public RemoveAVL(WorkNode wn, SearchTree tree) {
		super();
		name = Messages.getString("avl", "Alg_name.Remove_AVL"); //$NON-NLS-1$ //$NON-NLS-2$
		this.wn = wn;
		this.tree = tree;
		if (tree.getRoot() == null) {
			results.add(0, Messages.getString(
				"avl", "Tree_empty_key_not_found")); //$NON-NLS-1$ //$NON-NLS-2$
			results.add(1, "1"); //$NON-NLS-1$
			results.add(2, NOTFOUND);
			wn.setVisualizationStatus(Visualizable.INVISIBLE);
		}
		else { // Tree not empty
			commands.add(CommandFactory.createRemove(wn, tree));
			results.add(0, name +
				Messages.getString("avl", "_of") + //$NON-NLS-1$ //$NON-NLS-2$
				wn.getKey() +
				Messages.getString("avl", "Started") + //$NON-NLS-1$ //$NON-NLS-2$
				System.getProperty("line.separator") +
				wn.getKey() +
				Messages.getString("avl", "Key_search")); //$NON-NLS-1$ //$NON-NLS-2$
			results.add(1, "avlremove1"); //$NON-NLS-1$
			results.add(2, WORKING);
		}
	}

	/**
	 * <code> perform </code> does one step in the algorithm.
	 */
	@SuppressWarnings("unchecked")
	public void perform() {
		Command c = commands.get(currentPosition);
		c.perform();
		List resultlist = c.getResults();

		if (c instanceof Remove) {
			int removeresult = (Integer)resultlist.get(2);
			switch (removeresult) {
				case DONE:
					currentPosition++;
					if (tree.getHeight() == 0) {
						results.set(0, Messages.getString(
							"avl", "Remove_finished")); //$NON-NLS-1$ //$NON-NLS-2$
						results.set(2, DONE);
						break;
					}
					results = resultlist;
					Node n = (Node)resultlist.get(3);
					wn.setNextToMe(n);
					commands.add(CommandFactory.createUpdateBalance(wn));
					results.set(1, "avlremove5"); //$NON-NLS-1$
					break;
				case FOUND:
				case WORKING:
					results = resultlist;
					results.set(1, "avl" + results.get(1)); //$NON-NLS-1$
					break;
				case NOTFOUND:
					results = resultlist;
					results.set(1, "avl" + results.get(1)); //$NON-NLS-1$
					currentPosition++;
					break;
				default:
					break;
			}
		}

		else if (c instanceof UpdateBalance) {
			int balanceresult = ((Integer)resultlist.get(2)).intValue();
			updatebalancelist = resultlist;

			if (balanceresult == ROOT) {
				results.set(0, resultlist.get(0) + Messages.getString(
					"avl", "Root_reached_tree_balanced")); //$NON-NLS-1$ //$NON-NLS-2$
				currentPosition++;
				setBalancesNormal((AVLNode)wn.getNextToMe());
			}

			else if (balanceresult == ROTATE) {
				results.set(0, resultlist.get(0));
				results.set(1, resultlist.get(1));
				currentPosition++;
				int direction = (Integer)resultlist.get(3);
				if (direction == LEFT) {
					if (currentPosition == commands.size()) commands.add(
						CommandFactory.createRotateLeft(wn, tree));
					wn.getNextToMe().setVisualizationStatus(
						Visualizable.NORMAL | Visualizable.ROTATE_LEFT_ARROW
							| Visualizable.BALANCE);
				}
				else {
					if (currentPosition == commands.size()) commands.add(
						CommandFactory.createRotateRight(wn, tree));
					wn.getNextToMe().setVisualizationStatus(
						Visualizable.NORMAL | Visualizable.ROTATE_RIGHT_ARROW
							| Visualizable.BALANCE);
				}
			}

			else if (balanceresult == DOUBLEROTATE) {
				currentPosition++;
				AVLNode n = (AVLNode)resultlist.get(4);
				wn.setNextToMe(n);
				int direction = (Integer)resultlist.get(3);
				if (direction == LEFT) {
					results.set(0, resultlist.get(0));
					results.set(1, resultlist.get(1));
					if (currentPosition == commands.size()) commands.add(
						CommandFactory.createRotateLeft(wn, tree));
					wn.getNextToMe().setVisualizationStatus(
						Visualizable.NORMAL | Visualizable.ROTATE_LEFT_ARROW
							| Visualizable.BALANCE);
				}
				else {
					results.set(0, resultlist.get(0));
					results.set(1, resultlist.get(1));
					if (currentPosition == commands.size()) commands.add(
						CommandFactory.createRotateRight(wn, tree));
					wn.getNextToMe().setVisualizationStatus(
						Visualizable.NORMAL | Visualizable.ROTATE_RIGHT_ARROW
							| Visualizable.BALANCE);
				}
				help = 0;
			}
			else if (balanceresult == DONE) {
				currentPosition++;
				results.set(0, resultlist.get(0));
				results.set(1, resultlist.get(1));
				wn.setNextToMe(wn.getNextToMe().getParent());
				commands.add(CommandFactory.createUpdateBalance(wn));
				perform();
				return;
			}
			else {
				results.set(0, resultlist.get(0));
				results.set(1, resultlist.get(1));
			}
			results.set(1, "avlremove5"); //$NON-NLS-1$
		}

		else if (c instanceof RotateLeft || c instanceof RotateRight) {
			setBalancesNormal((AVLNode)wn.getNextToMe());
			currentPosition++;
			int balanceresult = (Integer)updatebalancelist.get(2);
			results.set(0, resultlist.get(0));
			results.set(1, resultlist.get(1));
			if (balanceresult == DOUBLEROTATE && help == 0) {
				wn.setNextToMe((AVLNode)updatebalancelist.get(6));
				if (((Integer)updatebalancelist.get(5)) == LEFT) {
					if (currentPosition == commands.size()) commands.add(
						CommandFactory.createRotateLeft(wn, tree));
					wn.getNextToMe().setVisualizationStatus(
						Visualizable.NORMAL | Visualizable.ROTATE_LEFT_ARROW
							| Visualizable.BALANCE);
				}
				else {
					if (currentPosition == commands.size()) commands.add(
						CommandFactory.createRotateRight(wn, tree));
					wn.getNextToMe().setVisualizationStatus(
						Visualizable.NORMAL | Visualizable.ROTATE_RIGHT_ARROW
							| Visualizable.BALANCE);
				}
				help = 1;
			}
			else {
				if (wn.getNextToMe().getParent() != null) {
					wn.setNextToMe(wn.getNextToMe().getParent());
					commands.add(CommandFactory.createUpdateBalance(wn));
					results.set(0, resultlist.get(0) + Messages.getString(
						"avl", "Partial_tree_balanced")); //$NON-NLS-1$ //$NON-NLS-2$
				}
				else results.set(0, resultlist.get(0) + Messages.getString(
					"avl", "N_tree_balanced")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			results.set(1, "avlremove5"); //$NON-NLS-1$
		}
	}

	/**
	 * recovers the state before the last call of <code> perform </code>
	 */
	@SuppressWarnings("unchecked")
	public void undo() {
		Command c = null;
		if (currentPosition >= commands.size()) {
			currentPosition--;
			c = commands.get(currentPosition);
		}
		else {
			c = commands.get(currentPosition);
			if ((c instanceof MacroCommand) && !((MacroCommand)c).hasPrevious()) {
				currentPosition--;
				c = commands.get(currentPosition);
			}
		}

		if ((c instanceof UpdateBalance) && !((UpdateBalance)c).hasNext()) {
			c.undo();
			int k = currentPosition - 1;
			Command c1 = commands.get(k);
			while (k > 0
				&& !(c1 instanceof RotateLeft || c1 instanceof RotateRight)) {
				if (c1 instanceof UpdateBalance) c1.undo();
				k--;
				c1 = commands.get(k);
			}
			results.set(1, "avlremove5"); //$NON-NLS-1$
		}
		else {
			c.undo();
			if (c instanceof Remove) results.set(1, "avl" + results.get(1)); //$NON-NLS-1$
			else results.set(1, "avlremove5"); //$NON-NLS-1$
		}

		if ((c instanceof Remove) || (c instanceof UpdateBalance)) {
			while (commands.size() > currentPosition + 1)
				commands.remove(currentPosition + 1);
		}
		results.set(0, Messages.getString("avl", "Step_undone")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Blocksteps: 1. Remove 2. UpdateBalance then either RotateLeft/RotateRight
	 * or UpdateBalance, perhaps more of them
	 */
	@SuppressWarnings("unchecked")
	public void performBlockStep() {
		if (!hasNext()) return;
		Command c = commands.get(currentPosition);
		if (c instanceof Remove) {
			((Remove)c).performBlockStep();
			results = c.getResults();
			results.set(1, "avl" + results.get(1)); //$NON-NLS-1$
			int removeresult = (Integer)results.get(2);
			switch (removeresult) {
				case DONE:
					currentPosition++;
					if (tree.getHeight() == 0) {
						results.set(0, Messages.getString(
							"avl", "Remove_finished")); //$NON-NLS-1$ //$NON-NLS-2$
						results.set(2, DONE);
						break;
					}
					Node n = (Node)results.get(3);
					wn.setNextToMe(n);
					commands.add(CommandFactory.createUpdateBalance(wn));
					results.set(1, "avlremove5"); //$NON-NLS-1$
					break;
				case FOUND:
				case WORKING:
				default:
					break;
			}
		}
		else if (c instanceof UpdateBalance) {
			while (c instanceof UpdateBalance) {
				perform();
				if (!hasNext()) return;
				c = commands.get(currentPosition);
			}
		}
		else perform();
	}

	/**
	 * Blocksteps: 1. Remove 2. UpdateBalance then either RotateLeft/RotateRight
	 * or UpdateBalance, perhaps more of them
	 */
	public void undoBlockStep() {
		if (!hasPrevious()) return;
		if (commands.size() <= currentPosition) currentPosition--;

		Command c = commands.get(currentPosition);

		if (c instanceof MacroCommand && !((MacroCommand)c).hasPrevious()) {
			currentPosition--;
			c = commands.get(currentPosition);
		}

		if (c instanceof Remove && ((Remove)c).hasPrevious()) {
			((Remove)c).undoBlockStep();
		}
		else if (c instanceof UpdateBalance) {
			do {
				while (((UpdateBalance)c).hasPrevious())
					undo();
				c = commands.get(--currentPosition);
			}
			while (c instanceof UpdateBalance);
			currentPosition++;
		}
		else if (c instanceof RotateLeft || c instanceof RotateRight) undo();
	}

	private void setBalancesNormal(AVLNode n) {
		if (n == null) return;
		n.setVisualizationStatus(Visualizable.NORMAL);
		setBalancesNormal((AVLNode)n.getLeftChild());
		setBalancesNormal((AVLNode)n.getRightChild());
	}
}