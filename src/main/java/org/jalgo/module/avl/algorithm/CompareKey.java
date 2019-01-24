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
 * The class <code>CompareKey</code> compares two keys with eacher other.
 */
public class CompareKey
extends Command {

	private WorkNode wn;

	/**
	 * @param wn reference to the position in the tree, holds the new key
	 */
	public CompareKey(WorkNode wn) {
		super();
		this.wn = wn;
	}

	/**
	 * Gets the WorkNode and compares its key with the key of the <code>nextToMe
	 * </code>
	 * node, returns 0 if the keys are the same, -1 if the key of the WorkNode
	 * is smaller and 1 otherwise.
	 */
	@SuppressWarnings("unchecked")
	public void perform() {

		Integer worknodekey = wn.getKey();
		Integer nexttomekey = wn.getNextToMe().getKey();

		results.add(worknodekey.compareTo(nexttomekey));
	}

	/**
	 * method is empty, undo not necessary
	 */
	public void undo() {
	// this method has no effect
	}
}