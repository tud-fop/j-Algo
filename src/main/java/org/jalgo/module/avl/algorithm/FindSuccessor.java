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
import org.jalgo.module.avl.datastructure.WorkNode;

/**
 * @author Jean Christoph Jung
 * 
 * This Command searches the searchtree for the key, that is next in size to the
 * key next to the worknode. It uses the classes
 */
public class FindSuccessor
extends MacroCommand
implements Constants {

	private WorkNode wn;

	/**
	 * @param wn the worknode indicates the position in the tree, where the
	 *            search is started
	 */
	@SuppressWarnings("unchecked")
	public FindSuccessor(WorkNode wn) {
		super();
		this.wn = wn;
		results.add(0, ""); //$NON-NLS-1$
		results.add(1, "absatz"); //$NON-NLS-1$
		results.add(2, WORKING);
		commands.add(CommandFactory.createFindSuccessorStart(wn));
	}

	/**
	 * runs stepwise through the tree. first step (=first call) goes to the
	 * right child of the node next to the worknode the next steps (calls) are
	 * going to the left child until there is no left child
	 * 
	 * @see org.jalgo.module.avl.algorithm.Command#perform()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void perform() {
		Command c = commands.get(currentPosition);
		c.perform();
		results = c.getResults();
		int stepresult = (Integer)results.get(2);
		if (stepresult == WORKING)
			commands.add(CommandFactory.createFindSuccessorStep(wn));
		if (stepresult == FOUND)
			results.set(0, Messages.getString("avl", "Successor_found")); //$NON-NLS-1$ //$NON-NLS-2$
		currentPosition++;
	}

	/**
	 * @see org.jalgo.module.avl.algorithm.Command#undo()
	 * 
	 * recovers the state before the last called <code>perform</code>, i.e.
	 * goes to the parent of the current node
	 */
	@Override
	public void undo() {
		if (currentPosition > 0) currentPosition--;
		else return;

		Command c = commands.get(currentPosition);
		c.undo();

		if (commands.size() > currentPosition + 1)
			commands.remove(currentPosition + 1);

		results = c.getResults();
	}

	/**
	 * recovers the state before calling <code>perform</code> the first time,
	 * i.e. undoes all steps, that have been performed by this class it also
	 * clears the commands list apart from the first element.
	 */
	@Override
	public void undoBlockStep() {
		while (currentPosition > 0) {
			currentPosition--;
			Command c = commands.get(currentPosition);
			c.undo();
			commands.remove(currentPosition);
		}
	}
}