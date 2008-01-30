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
package org.jalgo.module.heapsort.renderer;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

/**
 * <p>Interface used to implement the visitor pattern for canvas entities.
 * (A pattern not explicitly found in languages more capable than Java,
 * like... Python or every other language.)</p>
 * 
 * <p>Note: We will often call our visitor objects arnie, because Arnie is the
 * one who visits people most effectively.</p>
 *  
 * @author mbue
 *
 */
public interface CanvasEntityVisitor {
	/**
	 * This method is called in the visiting process. The canvas entity
	 * to be visited is given by <code>e</code>, and <code>trans</code> is
	 * the affine transform yielding absolute coordinates wrt. to the root
	 * of the subtree on which the process is started. The third parameter,
	 * <code>clip</code>, determines a rectangle on which operations
	 * <em>have</em> to be limited. Often this will be <code>null</code>.
	 * 
	 * @param e
	 * @param trans
	 */
	void invoke(CanvasEntity e, AffineTransform trans, Rectangle clip);
}