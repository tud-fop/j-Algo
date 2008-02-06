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

import java.awt.Container;
import java.awt.Rectangle;

/**
 * <p>This interface corresponds to a primary (=output) surface
 * of a renderer. Actually, the renderer may and should use
 * a backbuffer if applicable. From my current point of view,
 * a Java2D renderer should use a backbuffer while a JOGL renderer
 * need not, but I am not sure about this.</p>
 * 
 * <p>Canvas entities are created wrt. to this because they could
 * be requiring hardware resources.</p>
 * 
 * @author mbue
 *
 */
public interface Renderer {
	/**
	 * Initialize renderer. Creates a component which will be used to display
	 * everything.
	 * 
	 * @param cc A container into which the output component will be placed.
	 */
	void init(Container cc);
	
	/**
	 * Finalise renderer.
	 */
	void dispose();
	
	// -- factory stuff
	
	/**
	 * Returns a canvas entity factory for use with this renderer.
	 * 
	 * @return
	 */
	CanvasEntityFactory createFactory();
	
	// -- rendering stuff
	/**
	 * Returns the rectangle describing the visible area, which can be
	 * used for intersection with some dirty region when calling
	 * <code>renderVisible</code>.
	 * 
	 * @return
	 */
	Rectangle getVisible();
	
	/**
	 * Returns whether the image was lost since the
	 * last time it was rendered. This MUST be called
	 * before <code>renderVisible</code> on every frame!
	 * 
	 * @return
	 */
	boolean validate();
	
	/**
	 * <p>Render those canvas entities from the tree given by
	 * <code>root</code> which lie in the rectangle <code>r</code>.
	 * Dirty regions are left unchanged.</p>
	 * 
	 * <p>Implementors might find CanvasEntity.foldVisible useful.</p>
	 * 
	 * @param root
	 * @param r
	 */
	void renderVisible(CanvasEntity root, Rectangle r);

	/**
	 * <p>Update the part of the image on screen specified by the rectangle
	 * <code>r</code>. If the argument is <code>null</code>,
	 * update everything.</p>
	 * 
	 * <p>This method is necessary for renderers using a back buffer and
	 * must be called after the rendering.</p>
	 * 
	 * @param r
	 * @return <code>true</code>
	 */
	boolean show(Rectangle r);
}
