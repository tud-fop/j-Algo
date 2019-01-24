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
import org.jalgo.module.avl.*;
import org.jalgo.module.avl.datastructure.*;

import java.util.*;

/**
 * @author Ulrike Fischer
 * 
 * The class <code>UpdateBalance</code> gets a start node and calculates all
 * balances using <code>CalculateBalance</code> beginning from this node.
 */
public class UpdateBalance
extends MacroCommand
implements Constants {

	private WorkNode wn;
	private AVLNode currentNode = null;
	private Set<Node> updatednodes;
	private String oldsection;

	/**
	 * @param wn reference to the position in the tree, holds the
	 *            <code>nextToMe</code> node where <code>UpdateBalance</code>
	 *            begins
	 */
	public UpdateBalance(WorkNode wn) {
		super();
		this.wn = wn;
		updatednodes = new HashSet<Node>();
		commands.add(CommandFactory.createCalcBalance(wn));
	}

	/**
	 * Calculates all balances from the inserted node until it reaches the root
	 * or the balance of a node is 2 or -2.
	 */
	@SuppressWarnings("unchecked")
	public void perform() {
		results.clear();
		results.add(0, ""); //$NON-NLS-1$
		results.add(1, "absatz"); //$NON-NLS-1$

		updatednodes.add(wn.getNextToMe());

		Command c = commands.get(currentPosition);
		c.perform();
		currentPosition++;

		if (c instanceof NoOperation) {
			if (wn.getNextToMe().getParent() == null) results.add(2, ROOT);
			else results.add(2, DONE);
			results.set(1, oldsection);
			return;
		}
		int balanceresult = (Integer)c.getResults().get(2);

		// if (currentNode==null)
		// results.set(0,"Balance auf 0 gesetzt");
		// else
		// results.set(0,"Balance aktualisiert: " + balanceresult);

		results.set(0, Messages.getString(
			"avl", "UpdateBalance.Balance_set_to") + //$NON-NLS-1$ //$NON-NLS-2$
			balanceresult +
			Messages.getString("avl", "UpdateBalance.Balance_set")); //$NON-NLS-1$ //$NON-NLS-2$

		currentNode = (AVLNode)wn.getNextToMe();

		if (balanceresult == 2) {
			AVLNode r = (AVLNode)currentNode.getRightChild();
			if (r.getBalance() == -1) {
				results.add(2, DOUBLEROTATE);
				results.add(3, RIGHT);
				results.add(4, r);
				results.add(5, LEFT);
				results.add(6, currentNode);
			}
			else {
				results.add(2, ROTATE);
				results.add(3, LEFT);
			}
		}

		else if (balanceresult == -2) {
			AVLNode l = (AVLNode)currentNode.getLeftChild();
			if (l.getBalance() == 1) {
				results.add(2, DOUBLEROTATE);
				results.add(3, LEFT);
				results.add(4, l);
				results.add(5, RIGHT);
				results.add(6, currentNode);
			}
			else {
				results.add(2, ROTATE);
				results.add(3, RIGHT);
			}
		}
		else if (balanceresult == 0 && currentPosition != 1) {
			oldsection = (String)c.getResults().get(1);
			results.add(2, WORKING);
			if (currentPosition == commands.size())
				commands.add(CommandFactory.createNoOperation());
		}
		else if (wn.getNextToMe().getParent() == null) {
			oldsection = (String)c.getResults().get(1);
			results.add(2, WORKING);
			if (currentPosition == commands.size())
				commands.add(CommandFactory.createNoOperation());
		}
		else {
			results.add(2, WORKING);
			wn.setNextToMe(wn.getNextToMe().getParent());
			if (currentPosition == commands.size())
				commands.add(CommandFactory.createCalcBalance(wn));
		}

		results.set(1, c.getResults().get(1));
	}

	/**
	 * Sets all balances back to there previous ones.
	 */
	@SuppressWarnings("unchecked")
	public void undo() {
		if (currentPosition == 0) throw new NullPointerException();

		results.clear();
		results.add(0, Messages.getString("avl", "Step_undone")); //$NON-NLS-1$ //$NON-NLS-2$

		// necessary for DoubleRotation, sets RotateVisualisationStatus back
		// to normal
		if (!hasNext()) for (Node n : updatednodes) n.setVisualizationStatus(
			Visualizable.BALANCE | Visualizable.NORMAL);
		currentPosition--;
		Command c = commands.get(currentPosition);

		if (c instanceof NoOperation) {
			for (Node n : updatednodes) {
				n.setVisualizationStatus(Visualizable.NORMAL
					| Visualizable.BALANCE);
			}
			results.add(1, oldsection);
			results.add(2, WORKING);
		}
		else {
			c.undo();
			results.add(1, c.getResult(1));
			if (currentPosition == 0) {
				results.add(2, LASTUNDO);
				currentNode = null;
			}
			else results.add(2, DONE);
		}
	}

	public void performBlockStep() {
		while (this.hasNext()) perform();
	}

	public void undoBlockStep() {
		while (this.hasNext()) undo();
	}

	public Set<Node> getUpdatednodes() {
		return updatednodes;
	}
}