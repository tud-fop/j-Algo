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

import java.util.*;

import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.datastructure.*;
import org.jalgo.module.avl.*;

/**
 * @author Ulrike Fischer
 * 
 * The class <code>InsertAVL</code> realizes the insert of a node in an AVL
 * tree. It uses the classes <code>Insert</code> to find the position and
 * insert the key. It then uses the classes <code>UpdateBalance</code>,
 * <code>RotateLeft</code> or <code>RotateRight</code> to restore the AVL
 * characteristic.
 */
public class InsertAVL
extends MacroCommand
implements Constants {

	private WorkNode wn;
	private SearchTree tree;
	private List updatebalancelist;
	private int help = 1;
	private int balanceperformed = 0;
	private String oldsection;

	/**
	 * @param w reference to the position in the tree, holds the new key
	 * @param st the searchtree the new node is inserted in
	 */
	@SuppressWarnings("unchecked")
	public InsertAVL(WorkNode w, SearchTree st) {
		super();
		name = Messages.getString("avl", "Alg_name.Insert_AVL"); //$NON-NLS-1$ //$NON-NLS-2$
		wn = w;
		tree = st;
		commands.add(CommandFactory.createInsert(w, tree));
		results.add(0, name +
			Messages.getString("avl", "_of") + //$NON-NLS-1$ //$NON-NLS-2$
			wn.getKey() +
			Messages.getString("avl", "Started")); //$NON-NLS-1$ //$NON-NLS-2$
		results.add(1, "avlinsert1"); //$NON-NLS-1$
		results.add(2, WORKING);
	}

	/**
	 * Inserts the key and restores the AVL characteristic.
	 */
	@SuppressWarnings("unchecked")
	public void perform() {
		Command c = commands.get(currentPosition);
		c.perform();
		results.clear();
		List resultlist = c.getResults();

		if (c instanceof Insert) {
			int insertresult = ((Integer)resultlist.get(2)).intValue();
			switch (insertresult) {
				case DONE: {
					currentPosition++;
					if (currentPosition == commands.size()) commands.add(
						CommandFactory.createUpdateBalance(wn));
					results.add(0, Messages.getString("avl", "Node_inserted")); //$NON-NLS-1$ //$NON-NLS-2$
					results.add(1, "avlinsert2"); //$NON-NLS-1$
					break;
				}
				case FOUND: {
					currentPosition++;
					results.add(0, resultlist.get(0));
					results.add(1, "avlinsert1"); //$NON-NLS-1$
					results.add(2, FOUND);
					break;
					// key already there, AVLInsert is finished
				}
				case WORKING:
					results.add(0, resultlist.get(0));
					results.add(1, "avl" + resultlist.get(1)); //$NON-NLS-1$
					break;
			}
		}

		else if (c instanceof UpdateBalance) {
			balanceperformed = 1;
			int balanceresult = (Integer)resultlist.get(2);
			updatebalancelist = resultlist;
			oldsection = "avlinsert" + resultlist.get(1); //$NON-NLS-1$
			if (balanceresult == DONE || balanceresult == ROOT) {
				results.add(0, resultlist.get(0)
					+ Messages.getString("avl", "Tree_balanced")); //$NON-NLS-1$ //$NON-NLS-2$
				results.add(1, "avlinsert" + resultlist.get(1)); //$NON-NLS-1$
				results.add(2, DONE);
				currentPosition++;
				setBalancesNormal((AVLNode)wn.getNextToMe());
				// AVL-Insert is finished
			}

			else if (balanceresult == ROTATE) {
				results.add(0, resultlist.get(0));
				results.add(1, oldsection);
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
					results.add(0, resultlist.get(0));
					results.add(1, oldsection);
					if (currentPosition == commands.size()) commands.add(
						CommandFactory.createRotateLeft(wn, tree));
					wn.getNextToMe().setVisualizationStatus(
						Visualizable.NORMAL | Visualizable.ROTATE_LEFT_ARROW
							| Visualizable.BALANCE);
				}
				else {
					results.add(0, resultlist.get(0));
					results.add(1, oldsection);
					if (currentPosition == commands.size()) commands.add(
						CommandFactory.createRotateRight(wn, tree));
					wn.getNextToMe().setVisualizationStatus(
						Visualizable.NORMAL | Visualizable.ROTATE_RIGHT_ARROW
							| Visualizable.BALANCE);
				}
				help = 0;
			}

			else {
				results.add(0, resultlist.get(0));
				results.add(1, "avlinsert" + resultlist.get(1)); //$NON-NLS-1$
			}
		}

		else if (c instanceof RotateLeft || c instanceof RotateRight) {
			setBalancesNormal((AVLNode)wn.getNextToMe());
			currentPosition++;
			int balanceresult = (Integer)updatebalancelist.get(2);
			results.add(0, resultlist.get(0));
			results.add(1, oldsection);
			results.add(2, DONE);
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
			else results.set(0, resultlist.get(0) + Messages.getString(
				"avl", "Tree_balanced")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	/**
	 * This method recovers the state before calling <code>perform</code>.
	 */
	@SuppressWarnings("unchecked")
	public void undo() {
		results.clear();
		if (commands.size() <= currentPosition) currentPosition--;
		// Checks if there are RotationObjects which haven't been performed yet
		else if (currentPosition == 2 || currentPosition == 3) currentPosition--;

		Command c = commands.get(currentPosition);

		results.add(0, Messages.getString("avl", "Step_undone")); //$NON-NLS-1$ //$NON-NLS-2$

		if (c instanceof UpdateBalance) {

			// UpdateBalance is in the Command-List but there was no perform yet
			if (balanceperformed == 0) {
				currentPosition--;
				Command c2 = commands.get(currentPosition);
				c2.undo();
				results.add(1, "avlinsert2"); //$NON-NLS-1$
				return;
			}

			c.undo();
			// Check if it was the last UpdateBalance-Undo
			LinkedList resultlist = (LinkedList)c.getResults();
			int undoresult = ((Integer)resultlist.get(2)).intValue();
			if (undoresult == LASTUNDO) balanceperformed = 0;
			results.add(1, "avlinsert" + resultlist.get(1)); //$NON-NLS-1$
		}

		else if (c instanceof Insert) {
			if (((Insert)c).hasPrevious()) {
				c.undo();
				LinkedList resultlist = (LinkedList)c.getResults();
				results.add(1, "avl" + resultlist.get(1)); //$NON-NLS-1$
			}
		}

		else {
			// necessary for DoubleRotation, sets old RotateVisualisationStatus
			// back to Normal
			Command c3 = commands.get(currentPosition - 1);
			if (c3 instanceof UpdateBalance) {
				help = 0;
				Set<Node> updatednodes = ((UpdateBalance)c3).getUpdatednodes();
				for (Node n : updatednodes) {
					n.setVisualizationStatus(Visualizable.NORMAL);
				}
			}

			c.undo();

			// sets all balances red
			Command c4 = commands.get(currentPosition - 1);
			if (c4 instanceof UpdateBalance) {
				Set<Node> updatednodes = ((UpdateBalance)c4).getUpdatednodes();
				for (Node n : updatednodes) {
					if (n.getVisualizationStatus() == Visualizable.NORMAL)
						n.setVisualizationStatus(Visualizable.NORMAL
						| Visualizable.BALANCE);
				}
			}
			results.add(1, oldsection);
		}
	}

	@SuppressWarnings("unchecked")
	public void performBlockStep() {
		Command c = commands.get(currentPosition);
		if (c instanceof Insert) {
			((Insert)c).performBlockStep();
			List resultlist = c.getResults();
			int insertresult = ((Integer)resultlist.get(2)).intValue();
			results.clear();
			switch (insertresult) {
				case DONE:
					currentPosition++;
					if (currentPosition == commands.size()) commands.add(
						CommandFactory.createUpdateBalance(wn));
					results.add(0, resultlist.get(0));
					results.add(1, "avlinsert2"); //$NON-NLS-1$
					results.add(2, DONE);
					break;
				case FOUND:
					currentPosition++;
					results.add(0, resultlist.get(0));
					results.add(1, "avlinsert1"); //$NON-NLS-1$
					results.add(2, FOUND);
					break;
				// key already there, AVLInsert is finished
				case WORKING:
					results.add(0, resultlist.get(0));
					results.add(1, resultlist.get(1));
					results.add(2, WORKING);
					break;
			}
		}
		else if ((c instanceof RotateLeft) || (c instanceof RotateRight))
			perform();
		else if (c instanceof UpdateBalance) {
			while (((UpdateBalance)c).hasNext()) perform();
		}
	}

	@SuppressWarnings("unchecked")
	public void undoBlockStep() {
		if (commands.size() <= currentPosition) currentPosition--;
		else if (currentPosition == 2 || currentPosition == 3) currentPosition--;

		Command c = commands.get(currentPosition);
		results.clear();
		results.add(Messages.getString("avl", "Step_undone")); //$NON-NLS-1$ //$NON-NLS-2$
		results.add("absatz"); //$NON-NLS-1$
		if (c instanceof Insert) {
			((Insert)c).undoBlockStep();
			results.set(1, "avlinsert1"); //$NON-NLS-1$
		}
		else if ((c instanceof RotateLeft) || (c instanceof RotateRight))
			c.undo();
		else if (c instanceof UpdateBalance) {
			if (balanceperformed == 0) {
				currentPosition--;
				Command c2 = commands.get(currentPosition);
				c2.undo();
				return;
			}
			currentPosition++;
			while (((UpdateBalance)c).hasPrevious()) undo();
		}
	}

	private void setBalancesNormal(AVLNode n) {
		if (n == null) return;
		n.setVisualizationStatus(Visualizable.NORMAL);
		setBalancesNormal((AVLNode)n.getLeftChild());
		setBalancesNormal((AVLNode)n.getRightChild());
	}
}