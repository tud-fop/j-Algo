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

import org.jalgo.module.avl.Constants;
import org.jalgo.module.avl.datastructure.*;

/**
 * This class provides the creation of a new node. The node is inserted into the
 * searchtree at the place, specified by the worknode and the parameterlist.
 * 
 * @author Jean Christoph Jung
 */
public class CreateNode
extends Command {

	private WorkNode wn;
	private SearchTree tree;

	/**
	 * @param w reference to the position in the tree, holds the new key too.
	 * @param st the searchtree the new node is inserted in.
	 */
	public CreateNode(WorkNode w, SearchTree st) {
		super();
		wn = w;
		tree = st;
	}

	/**
	 * This method creates a new node and inserts it into the searchtree.
	 */
	@Override
	public void perform() {
		if (wn.getNextToMe() == null) {
			AVLNode node = new AVLNode(wn.getKey());
			tree.setRoot(node);
			wn.setNextToMe(node);
		}
		else {
			AVLNode parent = (AVLNode)wn.getNextToMe();
			AVLNode node = new AVLNode(wn.getKey(), parent);
			int child = ((Integer)parameters.get(0)).intValue();
			if (child == Constants.LEFT) {
				parent.setLeftChild(node);
			}
			else {
				parent.setRightChild(node);
			}
			wn.setNextToMe(node);
		}
	}

	/**
	 * This method deletes the node, which was created by the
	 * <code>perform</code>-method the behaviour is undefined, if perform has
	 * not been called yet; otherwise <code>undo</code> recovers the state
	 * before calling <code>perform</code>
	 */
	@Override
	public void undo() {
		if (wn.getNextToMe() == tree.getRoot()) {
			wn.setNextToMe(null);
			tree.setRoot(null);
			wn.setVisualizationStatus(Visualizable.NORMAL);
		}
		else {
			AVLNode parent = (AVLNode)wn.getNextToMe().getParent();
			wn.setNextToMe(parent);
			int child = ((Integer)parameters.get(0)).intValue();
			if (child == Constants.LEFT) {
				parent.setLeftChild(null);
				wn.setVisualizationStatus(Visualizable.NORMAL
					| Visualizable.LEFT_ARROW);
			}
			else {
				parent.setRightChild(null);
				wn.setVisualizationStatus(Visualizable.NORMAL
					| Visualizable.RIGHT_ARROW);
			}
		}

		Command ch = CommandFactory.createCalcHeight(tree.getRoot());
		ch.perform();
	}
}