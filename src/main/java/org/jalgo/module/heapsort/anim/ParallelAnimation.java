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
package org.jalgo.module.heapsort.anim;

import java.util.LinkedList;
import java.util.List;

/**
 * Subclass this if your animations run in parallel.
 * Don't forget the super calls!
 * 
 * @author mbue
 */
public class ParallelAnimation implements Animation {
	private List<Animation> anim;
	private double duration;
	
	public ParallelAnimation(double duration) {
		anim = new LinkedList<Animation>();
		this.duration = duration;
	}
	
	protected void add(Animation a) {
		anim.add(a);
	}

	public void done() {
		for (Animation a: anim)
			a.done();
	}

	public double getDuration() {
		return duration;
	}

	public void init() {
		for (Animation a: anim)
			a.init();
	}

	public void update(double time) {
		for (Animation a: anim)
			a.update(time);
	}
}