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
package org.jalgo.module.heapsort.anim;


/**
 * <p>This interface models animations on a canvas.
 * Required canvas entities can be set up on <code>init</code>,
 * they can be disposed of in <code>done</code>,
 * and animated entities can be updated on <code>update</code>.</p>
 * 
 * <p>Time values presented to the animation for <code>update</code>
 * are in the closed real interval [0..1]. The reason for this is that
 * the boundaries of that interval are contained in the floating point
 * domain. The animation is garuanteed to be called with these exact
 * values by the AnimationTimeEntity.</p>
 * 
 * <p>It is assumed that the implementing class is able
 * to gain access to the necessary canvas entities. This has
 * several reasons: a) a root canvas entity (as an argument
 * to the methods declared here) might not suffice
 * because the animation could rely on a naming service;
 * b) animations are rather short-lived objects, so storing
 * the extra pointer should not be harmful.</p>
 * 
 * @author mbue
 *
 */
public interface Animation {
	void init();
	void done();
	void update(double time);
	double getDuration();
}
