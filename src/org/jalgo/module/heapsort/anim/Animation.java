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
 * <p>The recommended policy for implementors is as follows:
 * canvas entities needed temporarily in this animation can be created
 * in the constructor, others be passed to it. All "new" entities can
 * and should be added to the parent node on <code>init</code>, but by
 * no means before. In addition, you may assume that the animation won't
 * be used any longer after a call to <code>done</code>.</p>
 * 
 * <p>Nothing is determined as to how the canvas entities are to be
 * created. Implementors could use some kind of context (surrounding
 * class), which would hold a reference to a canvas entity factory.
 * In most cases, temporary canvas entities are not needed and thus
 * this is no issue anyway.</p>
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
