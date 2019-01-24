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
import org.jalgo.module.avl.datastructure.Visualizable;
import org.jalgo.module.avl.datastructure.WorkNode;

/**
 * The class <code>Search</codes> searches a key in the tree.
 * 
 * @author Ulrike Fischer, Jean Christoph Jung
 */
public class SearchAlg
extends MacroCommand
implements Constants {

	private WorkNode wn;
	private boolean firstundo = false;

	/**
	 * @param wn the WorkNode with the key to be searched in the tree
	 */
	@SuppressWarnings("unchecked")
	public SearchAlg(WorkNode wn) {
		super();
		name = Messages.getString("avl", "Alg_name.Search"); //$NON-NLS-1$ //$NON-NLS-2$
		this.wn = wn;

		// checks if the tree is empty
		if (wn.getNextToMe() == null) {
			results.add(0, Messages.getString(
				"avl", "Tree_empty_key_not_found")); //$NON-NLS-1$ //$NON-NLS-2$
			results.add(1, "search1"); //$NON-NLS-1$
			results.add(2, NOTFOUND);
			wn.setVisualizationStatus(Visualizable.INVISIBLE);
		}
		else {
			commands.add(CommandFactory.createCompareKey(wn));
			results.add(0, wn.getKey() + Messages.getString(
				"avl", "Key_search")); //$NON-NLS-1$ //$NON-NLS-2$
			results.add(1, "search1"); //$NON-NLS-1$
			results.add(2, WORKING);
			wn.getNextToMe().setVisualizationStatus(Visualizable.FOCUSED);
		}
	}

	/**
	 * Searches the key of the WorkNode in the tree, returns FOUND if the key
	 * was found, WORKING if the algorithm isn't yet finished or NOTFOUND if the
	 * key was not found. In the last case Search also returns the position to
	 * insert (LEFT or RIGHT).
	 */
	@SuppressWarnings("unchecked")
	public void perform() {
		results.clear();
		Command c = commands.get(currentPosition);
		c.perform();
		currentPosition++;

		Integer compareresult = (Integer)c.getResults().get(0);

		switch (compareresult) {
			case 0: {
				results.add(0, wn.getKey() + " = " + wn.getNextToMe().getKey()); //$NON-NLS-1$
				results.add(1, "search1"); //$NON-NLS-1$
				results.add(2, FOUND);
				wn.setVisualizationStatus(Visualizable.INVISIBLE);
				setNodesTo(wn.getNextToMe(), Visualizable.NORMAL);
				wn.getNextToMe().setVisualizationStatus(
					Visualizable.FOCUSED | Visualizable.LINE_NORMAL);
				firstundo = true;
				break;
				// key found
			}

			case -1: {
				if (wn.getNextToMe().getLeftChild() == null) {
					results.add(0, wn.getKey() + " < " //$NON-NLS-1$
						+ wn.getNextToMe().getKey()
						+ Messages.getString("avl", "Search_not_found")); //$NON-NLS-1$ //$NON-NLS-2$
					results.add(1, "search1"); //$NON-NLS-1$
					results.add(2, NOTFOUND);
					results.add(3, LEFT);
					wn.setVisualizationStatus(Visualizable.INVISIBLE);
					setNodesTo(wn.getNextToMe(), Visualizable.NORMAL);
					firstundo = true;
					// end of Search, key not found
				}
				else {
					results.add(0, wn.getKey() + " < " //$NON-NLS-1$
						+ wn.getNextToMe().getKey()
						+ Messages.getString("avl", "Search_step_to_left")); //$NON-NLS-1$ //$NON-NLS-2$
					results.add(1, "search1"); //$NON-NLS-1$
					results.add(2, WORKING);
					wn.setNextToMe(wn.getNextToMe().getLeftChild());
					wn.getNextToMe().setVisualizationStatus(
						Visualizable.FOCUSED);
					if (currentPosition == commands.size())
						commands.add(CommandFactory.createCompareKey(wn));
				}
				break;
			}

			case 1: {
				if (wn.getNextToMe().getRightChild() == null) {
					results.add(0, wn.getKey() + " > " //$NON-NLS-1$
						+ wn.getNextToMe().getKey()
						+ Messages.getString("avl", "Search_not_found")); //$NON-NLS-1$ //$NON-NLS-2$
					results.add(1, "search1"); //$NON-NLS-1$
					results.add(2, NOTFOUND);
					results.add(3, RIGHT);
					wn.setVisualizationStatus(Visualizable.INVISIBLE);
					setNodesTo(wn.getNextToMe(), Visualizable.NORMAL);
					firstundo = true;
					// end of Search, key not found
				}
				else {
					results.add(0, wn.getKey() + " > " //$NON-NLS-1$
						+ wn.getNextToMe().getKey()
						+ Messages.getString("avl", "Search_step_to_right")); //$NON-NLS-1$ //$NON-NLS-2$
					results.add(1, "search1"); //$NON-NLS-1$
					results.add(2, WORKING);
					wn.setNextToMe(wn.getNextToMe().getRightChild());
					wn.getNextToMe().setVisualizationStatus(
						Visualizable.FOCUSED);
					if (currentPosition == commands.size())
						commands.add(CommandFactory.createCompareKey(wn));
				}
				break;
			}
		}
	}

	/**
	 * Realizes undo by changing the position of the WorkNode
	 */
	@SuppressWarnings("unchecked")
	public void undo() {
		results.clear();
		results.add(Messages.getString("avl", "Step_undone")); //$NON-NLS-1$ //$NON-NLS-2$
		results.add("search1"); //$NON-NLS-1$

		currentPosition--;
		//TODO: for what this line is here?
//		Command c = commands.get(currentPosition);

		if (firstundo) {
			wn.setVisualizationStatus(Visualizable.NORMAL);
			setNodesTo(wn.getNextToMe(), Visualizable.FOCUSED);
			firstundo = false;
		}
		else {
			wn.getNextToMe().setVisualizationStatus(Visualizable.NORMAL);
			wn.setNextToMe(wn.getNextToMe().getParent());
		}
	}

	public void performBlockStep() {
		while (this.hasNext()) perform();
	}

	public void undoBlockStep() {
		while (this.hasPrevious()) undo();
	}

	private void setNodesTo(Node n, int status) {
		while (n != null) {
			n.setVisualizationStatus(status);
			n = n.getParent();
		}
	}
}