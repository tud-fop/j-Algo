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
import org.jalgo.module.avl.Constants;
import java.util.*;

/**
 * Realizes the insertion of a key into a given searchtree by holding the
 * searchtree property
 * 
 * @author Jean Christoph Jung
 */
public class Insert
extends MacroCommand
implements Constants {

	private WorkNode wn;
	private SearchTree tree;

	/**
	 * @param w Reference to the the current Node; the key of the worknode is
	 *            the key that will be inserted into the serchtree st.
	 * @param st the tree, the algorithm is working on.
	 */
	@SuppressWarnings("unchecked")
	public Insert(WorkNode w, SearchTree st) {
		super();
		name = Messages.getString("avl", "Alg_name.Insert"); //$NON-NLS-1$ //$NON-NLS-2$
		wn = w;
		tree = st;
		if (wn.getNextToMe() != null) { // Tree not empty
			results.add(0, new String(Messages.getString(
				"avl", "Alg_name.Insert") + //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("avl", "_of") + wn.getKey() + //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("avl", "Started"))); //$NON-NLS-1$ //$NON-NLS-2$
			results.add(1, "insert1"); //$NON-NLS-1$
			commands.add(CommandFactory.createSearch(w));
		}
		else { // Tree empty, search not necessary
			results.add(0, new String(Messages.getString(
				"avl", "Alg_name.Insert") + //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("avl", "_of") + //$NON-NLS-1$ //$NON-NLS-2$
				wn.getKey() +
				Messages.getString("avl", "Started"))); //$NON-NLS-1$ //$NON-NLS-2$
			results.add(1, "insert2"); //$NON-NLS-1$
			commands.add(CommandFactory.createCreateNode(wn, tree));
		}
		results.add(2, WORKING);
	}

	/**
	 * <code> perform </code> does one step in the insertion of a new node.
	 * writes into the resultlist: DONE, if Insert is finished, WORKING if not
	 * finished, FOUND if key already exists
	 */
	@SuppressWarnings("unchecked")
	public void perform() {
		Command c = commands.get(currentPosition);
		c.setParameters(parameters);
		c.perform();
		results.clear();
		List r = c.getResults();
		if (c instanceof Search) {
			int searchresult = (Integer)r.get(2);
			switch (searchresult) {
				case FOUND:
					currentPosition++;
					results.add(0, r.get(0) + Messages.getString(
						"avl", "Insert.Key_found")); //$NON-NLS-1$ //$NON-NLS-2$
					results.add(1, "insert1"); //$NON-NLS-1$
					results.add(2, FOUND);
					break;
				case WORKING:
					results.add(0, r.get(0));
					results.add(1, "insert1"); //$NON-NLS-1$
					results.add(2, WORKING);
					break;
				case NOTFOUND:
					Integer child = (Integer)r.get(3);
					if (child == LEFT) {
						results.add(0, r.get(0) + Messages.getString(
							"avl", "Insert_left")); //$NON-NLS-1$ //$NON-NLS-2$
						results.add(1, "insert2"); //$NON-NLS-1$
						wn.setVisualizationStatus(Visualizable.NORMAL
							| Visualizable.LEFT_ARROW);
					}
					else {
						results.add(0, r.get(0) + Messages.getString(
							"avl", "Insert_right")); //$NON-NLS-1$ //$NON-NLS-2$
						results.add(1, "insert2"); //$NON-NLS-1$
						wn.setVisualizationStatus(Visualizable.NORMAL
							| Visualizable.RIGHT_ARROW);
					}
					currentPosition++;
					if (commands.size() < 2) commands.add(
						1, CommandFactory.createCreateNode(wn, tree));
					parameters.add(0, child);
					results.add(2, WORKING);
					break;
				default:
					results.add(2, WORKING);
					break;
			}
		}
		if (c instanceof CreateNode) {
			Command ch = CommandFactory.createCalcHeight(tree.getRoot());
			ch.perform();

			results.add(0, Messages.getString(
				"avl", "Insert_finished")); //$NON-NLS-1$ //$NON-NLS-2$
			results.add(1, "insert2"); //$NON-NLS-1$
			results.add(2, DONE);
			currentPosition++;
			wn.setVisualizationStatus(Visualizable.INVISIBLE);
		}
	}

	/**
	 * First blockstep: the search Second blockstep: insert of the new node
	 */
	@SuppressWarnings("unchecked")
	public void performBlockStep() {
		if (!hasNext()) return;
		Command c = commands.get(currentPosition);
		results.clear();
		results.add("Absatznummer"); //$NON-NLS-1$
		if (c instanceof Search) while (((Search)c).hasNext()) perform();
		else perform();
	}

	/**
	 * recovers the state before the last call of <code> perform </code>
	 */
	@SuppressWarnings("unchecked")
	public void undo() {
		results.clear();
		results.add(0, Messages.getString("avl", "Step_undone")); //$NON-NLS-1$ //$NON-NLS-2$
		if (commands.size() <= currentPosition) currentPosition--;
		else if (currentPosition == 1) currentPosition--;

		Command c = commands.get(currentPosition);

		if (c instanceof Search) {
			results.add(1, "insert1"); //$NON-NLS-1$
			if (((Search)c).hasPrevious()) {
				c.undo();
				results.add(2, DONE);
			}
			else results.add(2, DONE);
		}
		if (c instanceof CreateNode) {
			c.undo();
			results.add(1, "insert2"); //$NON-NLS-1$
			results.add(2, DONE);
		}
	}

	/**
	 * First blockstep: the search Second blockstep: insert of the new node
	 */
	public void undoBlockStep() {
		if ((commands.size() <= currentPosition) || (currentPosition == 1))
			currentPosition--;

		Command c = commands.get(currentPosition);
		if (c instanceof Search) while (((Search)c).hasPrevious()) undo();
		else undo();
	}
}