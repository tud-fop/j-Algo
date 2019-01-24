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
import org.jalgo.module.avl.datastructure.Node;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.datastructure.Visualizable;
import org.jalgo.module.avl.datastructure.WorkNode;

/**
 * Realizes the Deletion of a node (given by a key) in a searchtree, holding the
 * searchtree property.
 * 
 * @author Jean Christoph Jung
 */
public class Remove
extends MacroCommand
implements Constants {

	private WorkNode wn;
	private SearchTree tree;
	private Node nodeToDelete;

	/**
	 * @param w Reference to the the current Node; the key of the worknode is
	 *            the key that will be deleted from the serchtree st.
	 * @param st the tree, the algorithm is working on.
	 */
	@SuppressWarnings("unchecked")
	public Remove(WorkNode w, SearchTree st) {
		super();
		name = Messages.getString("avl", "Alg_name.Remove"); //$NON-NLS-1$ //$NON-NLS-2$
		wn = w;
		tree = st;
		if (st.getRoot() == null) {
			results.add(0, Messages.getString(
				"avl", "Tree_empty_key_not_found")); //$NON-NLS-1$ //$NON-NLS-2$
			results.add(1, "remove1"); //$NON-NLS-1$
			results.add(2, NOTFOUND);
			wn.setVisualizationStatus(Visualizable.INVISIBLE);
		}
		else { // Tree not empty
			commands.add(CommandFactory.createSearchAlg(w));
			results.add(0, name +
				Messages.getString("avl", "_of") + //$NON-NLS-1$ //$NON-NLS-2$
				wn.getKey() +
				Messages.getString("avl", "Started") + //$NON-NLS-1$ //$NON-NLS-2$
				System.getProperty("line.separator") +
				wn.getKey() +
				Messages.getString("avl", "Key_search")); //$NON-NLS-1$ //$NON-NLS-2$
			results.add(1, "remove1"); //$NON-NLS-1$
			results.add(2, WORKING);
		}
		tree.exportUncompressedLevelOrder();
	}

	/**
	 * <code> perform </code> does one step in the algorithm.
	 */
	@SuppressWarnings("unchecked")
	public void perform() {
		Command c = commands.get(currentPosition);
		c.perform();

		if (c instanceof SearchAlg) {
			List resultlist = c.getResults();
			int SearchAlgresult = (Integer)resultlist.get(2);
			switch (SearchAlgresult) {
				case FOUND:
					currentPosition++;
					results.set(0, resultlist.get(0) + Messages.getString(
						"avl", "Node_found")); //$NON-NLS-1$ //$NON-NLS-2$
					results.set(1, "remove2"); //$NON-NLS-1$
					results.set(2, WORKING);
					nodeToDelete = wn.getNextToMe();
					nodeToDelete.setVisualizationStatus(Visualizable.FOCUSED
						| Visualizable.LINE_NORMAL);
					if (nodeToDelete.getRightChild() == null) commands.add(
						CommandFactory.createRemoveNode(wn, tree, nodeToDelete));
					else commands.add(CommandFactory.createFindSuccessor(wn));
					break;
				case WORKING:
					results.set(0, resultlist.get(0));
					results.set(1, "remove1"); //$NON-NLS-1$
					results.add(2, WORKING);
					break;
				case NOTFOUND:
					results.set(0, resultlist.get(0) + Messages.getString(
						"avl", "Remove_aborted")); //$NON-NLS-1$ //$NON-NLS-2$
					results.set(1, "remove1"); //$NON-NLS-1$
					results.set(2, NOTFOUND);
					currentPosition++;
					break;
				default:
					results.set(2, WORKING);
					break;
			}
		}
		if (c instanceof FindSuccessor) {
			List resultlist = c.getResults();
			int result = (Integer)resultlist.get(2);
			switch (result) {
				case WORKING:
					results.set(0, resultlist.get(0));
					results.set(1, "remove3"); //$NON-NLS-1$
					results.set(2, WORKING);
					break;
				case FOUND:
					currentPosition++;
					commands.add(CommandFactory.createRemoveNode(wn, tree,
						nodeToDelete));
					results.set(0, resultlist.get(0));
					results.set(1, "remove4"); //$NON-NLS-1$
					results.set(2, FOUND);
					break;
				default:
					break;
			}
		}
		if (c instanceof RemoveNode) {
			results = c.getResults();
			results.set(1, "remove4"); //$NON-NLS-1$
			results.set(2, DONE);
			currentPosition++;
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
			if (c instanceof RemoveNode) {
				currentPosition--;
				c = commands.get(currentPosition);
			}
		}
		if ((c instanceof SearchAlg) && !((SearchAlg)c).hasPrevious()) return;
		else if ((c instanceof FindSuccessor)
			&& !((FindSuccessor)c).hasPrevious()) {
			currentPosition--;
			c = commands.get(currentPosition);
		}

		c.undo();

		if (c instanceof FindSuccessor) results.set(1, "remove3"); //$NON-NLS-1$
		else if (c instanceof RemoveNode) results.set(1, "remove4"); //$NON-NLS-1$
		else if (c instanceof SearchAlg) results.set(1, "remove1"); //$NON-NLS-1$

		// delete old objects, perform creates new ones
		if ((c instanceof SearchAlg) || (c instanceof FindSuccessor)) {
			if (commands.size() > currentPosition + 1)
				commands.remove(currentPosition + 1);
		}
		results.set(0, Messages.getString("avl", "Step_undone")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Blocksteps: 1. SearchAlg 2. FindSuccessor 3. RemoveNode
	 */
	public void performBlockStep() {
		if (!hasNext()) return;
		Command c = commands.get(currentPosition);
		if (c instanceof SearchAlg) {
			while (c instanceof SearchAlg) {
				perform();
				if (!hasNext()) return;
				c = commands.get(currentPosition);
			}
		}
		else if (c instanceof FindSuccessor) {
			while (c instanceof FindSuccessor) {
				perform();
				if (!hasNext()) return;
				c = commands.get(currentPosition);
			}
		}
		else if (c instanceof RemoveNode) {
			perform();
			results = c.getResults();
		}
	}

	/**
	 * Blocksteps: 1. SearchAlg 2. FindSuccessor 3. RemoveNode
	 */
	public void undoBlockStep() {
		Command c = null;
		if (currentPosition >= commands.size()) {
			currentPosition--;
			c = commands.get(currentPosition);
		}
		else {
			c = commands.get(currentPosition);
			if (c instanceof RemoveNode) {
				currentPosition--;
				c = commands.get(currentPosition);
			}
		}

		if ((c instanceof SearchAlg) && !((SearchAlg)c).hasPrevious()) return;
		else if ((c instanceof FindSuccessor)
			&& !((FindSuccessor)c).hasPrevious()) {
			currentPosition--;
			c = commands.get(currentPosition);
		}

		if (c instanceof SearchAlg) while (((SearchAlg)c).hasPrevious()) undo();
		else if (c instanceof FindSuccessor)
			while (((FindSuccessor)c).hasPrevious()) undo();
		else c.undo();

		// delete old objects, perform creates new ones
		if ((c instanceof SearchAlg) || (c instanceof FindSuccessor)) {
			if (commands.size() > currentPosition + 1)
				commands.remove(currentPosition + 1);
		}
	}
}