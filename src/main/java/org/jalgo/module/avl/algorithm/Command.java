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

/**
 * Part of the design pattern "Command/Action/Transaction" (class Command of the
 * pattern). the abstract methods <code> perform</code> and <code> undo</code>
 * make possible to do/undo operations stepwise (implemented in the inherited
 * classes). the other methods are necessary to communicate between different
 * command objects, e.g. to set parameters and get the results of a command.
 * 
 * @author Ulrike Fischer
 */

public abstract class Command {

	protected List results;
	protected List parameters;

	/**
	 * Constructs a <code>Command</code>. <br>
	 * Initializes the result and the parameters list.
	 */
	public Command() {
		results = new LinkedList();
		parameters = new LinkedList();
	}

	/**
	 * This methode need to be specifed by the concrete command.
	 */
	public abstract void perform();

	/**
	 * This methode need to be specifed by the concrete command.
	 */
	public abstract void undo();

	/**
	 * Sets the parameters of the <code>Command</code>.
	 * 
	 * @param p <code>List</code> of parameters.
	 */
	@SuppressWarnings("unchecked")
	public void setParameters(List p) {
		parameters = new LinkedList(p);
	}

	/**
	 * Retrieves the whole result list of the <code>Command</code>
	 * 
	 * @return <code>List</code> list of results.
	 */
	public List getResults() {
		return results;
	}

	/**
	 * Retrieves the entry of the result list from the given index.
	 * 
	 * @param index position of the wanted result
	 * @return <code>Object</code> the result
	 */
	public Object getResult(int index) {
		if (index < results.size()) return results.get(index);
		return null;
	}
}