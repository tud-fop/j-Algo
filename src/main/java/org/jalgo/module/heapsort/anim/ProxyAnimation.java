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

/**
 * Subclass this if you want to decorate an existing animation.
 * 
 * @author mbue
 */
public class ProxyAnimation implements Animation {
	
	protected Animation delegate;
	
	protected ProxyAnimation() {
		// have a hidden empty constructor to allow easy subclassing 
	}
	
	public ProxyAnimation(Animation delegate) {
		this.delegate = delegate;
	}
	
	public void done() {
		delegate.done();
	}

	public double getDuration() {
		return delegate.getDuration();
	}

	public void init() {
		delegate.init();
	}

	public void update(double time) {
		delegate.update(time);
	}

}
