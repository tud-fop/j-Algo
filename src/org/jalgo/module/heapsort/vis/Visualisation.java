/*
 * j-Algo - a visualization tool for algorithm runs, especially useful for
 * students and lecturers of computer sience. j-Algo is written in Java and
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
package org.jalgo.module.heapsort.vis;

import org.jalgo.module.heapsort.anim.Animation;
import org.jalgo.module.heapsort.model.Action;
import org.jalgo.module.heapsort.model.State;

/**
 * Implementing classes will be provided with a
 * root canvas entity and a canvas entity factory
 * used to display everything.
 * 
 * @author mbue
 */
public interface Visualisation {
	/**
	 * Configures root canvas entity and children for state <code>q</code>.
	 * @param q
	 */
	void setupState(State q);
	
	/**
	 * Remove everything from canvas.
	 */
	void teardown();
	
	/**
	 * Configures root canvas entity and children for transition from
	 * state <code>q</code> to <code>q1</code> and returns animation
	 * object.
	 * 
	 * @param q
	 * @param a
	 * @param q1
	 * @return
	 */
	Animation[] setupAnimation(State q, Action a, State q1);
	
	Animation setupAnimationLecture(State q, Action a, State q1);
	Animation setupAnimationMacro(State q, Action a, State q1);
	Animation setupAnimationBack(State q, Action a, State q1);
	
}
