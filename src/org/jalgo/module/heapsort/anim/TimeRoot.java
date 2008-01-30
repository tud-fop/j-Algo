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
 * A time entity which saves the time supplied to it on update and returns
 * it on a call to <code>now</code>. Use this class for your root nodes
 * to make the <code>now</code> method work for the whole tree.
 * 
 * @author mbue
 */
public class TimeRoot extends TimeEntity {
	private double now_;
	
	@Override
	public double now() {
		return now_;
	}
	
	@Override
	protected void doUpdate(double tloc) {
		// that is totally correct:
		// now has to be expressed in local time
		// (absolute wrt. children)
		now_ = tloc;
	}
	
	/**
	 * Set scale of this time root. Will reset offset such that
	 * there will be no seam wrt. now().
	 * 
	 * @see org.jalgo.module.heapsort.anim.TimeEntity#setScale(double)
	 */
	@Override
	public void setScale(double scale) {
		/* Ansatz:
		 * absolute time and local time don't change
		 * 
		 * s1*tloc + o1 = s2*tloc + o2
		 * --> o2 = (s1-s2)*tloc + 1
		 */
		double s1 = this.scale;
		super.setScale(scale);
		offset += (s1-scale)*now_;
	}
}
