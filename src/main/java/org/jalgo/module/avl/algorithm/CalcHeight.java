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

import org.jalgo.module.avl.datastructure.*;

/**
 * @author Ulrike Fischer
 * 
 * The class <code>CalcHeight</code> calculates the height of one node.
 */
public class CalcHeight
extends Command {

	private Node startNode;

	/**
	 * @param n the node where the height is calculated
	 */
	public CalcHeight(Node n) {
		super();
		startNode = n;
	}

	/**
	 * Calculates the height.
	 */
	public void perform() {
		calc(startNode);
	}

	private int calc(Node node) {
		if (node == null) return 0;
		node.setHeight(1 + Math.max(calc(node.getLeftChild()),
			calc(node.getRightChild())));
		return node.getHeight();
	}

	/**
	 * This method has no effect.
	 */
	public void undo() {
	// this method has no effect
	}
}