/*
 * j-Algo - a visualization tool for algorithm runs, especially useful for
 * students and lecturers of computer science. j-Algo is written in Java and
 * thus platform independent. Development is supported by Technische Universität
 * Dresden.
 *
 * Copyright (C) 2004-2008 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
package org.jalgo.module.heapsort.model;

import java.util.Map;

/**
 * The interface State is used to represent a state in the computation of the algorithm.
 * Runs can be constructed by using the method <code>computeSuccessors</code>. Note
 * that this restricts us to a finite branching factor, which excludes e. g. algorithms
 * guessing natural numbers.
 * 
 * @author mbue
 */
public interface State {
	/**
	 * Returns the detail level of the state. The system might allow the user to
	 * automatically derive up to the next state which has at most a certain detail
	 * level (that is, not stopping when a state has a greater detail level).
	 * @return
	 */
	int getDetailLevel();
	
	/**
	 * Computes a list of successor states.
	 * 
	 * @return
	 */
	Map<State,Action> computeSuccessors();
}
