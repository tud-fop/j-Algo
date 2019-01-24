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
 * @author Jean Christoph Jung
 * 
 * <code>FindSuccessorStep</code> is one step in the search for the symmetric
 * order successor to the key that will be removed.
 */
public class FindSuccessorStep
extends Command
implements Constants {

	private WorkNode wn;

	/**
	 * @param wn the worknode indicates the position in the tree
	 */
	@SuppressWarnings("unchecked")
	public FindSuccessorStep(WorkNode wn) {
		this.wn = wn;
		results.add(0, ""); //$NON-NLS-1$
		results.add(1, "absatz"); //$NON-NLS-1$
		results.add(2, WORKING);
	}

	/**
	 * <code> perform </code> moves the worknode to the left child (if exists)
	 * of the current node next to the worknode, and changes the visualisations
	 * of both the old and new node next to the worknode
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void perform() {
		Node n = wn.getNextToMe();
		n.setVisualizationStatus(Visualizable.NORMAL);
		n = n.getLeftChild();
		n.setVisualizationStatus(Visualizable.FOCUSED
			| Visualizable.LINE_NORMAL);
		wn.setNextToMe(n);
		if (n.getLeftChild() != null) results.set(2, WORKING);
		else results.set(2, FOUND);
		results.set(0, Messages.getString(
			"avl", "FindSuccessor.Step_to_left")); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * <code> undo </code> moves the worknode to the parent of the current node
	 * next to the worknode, and changes the visualisations of both the old and
	 * new node next to the worknode
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void undo() {
		Node n = wn.getNextToMe();
		n.setVisualizationStatus(Visualizable.NORMAL);
		n = n.getParent();
		n.setVisualizationStatus(Visualizable.FOCUSED
			| Visualizable.LINE_NORMAL);
		wn.setNextToMe(n);
		results.set(2, WORKING);
	}
}