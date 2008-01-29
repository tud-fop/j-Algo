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
package org.jalgo.module.heapsort.renderer;

import java.awt.Container;
import java.awt.Rectangle;


/**
 * <p> This interface corresponds to a primary (=output) surface
 * of a renderer. Canvas entities are created wrt. to this surface
 * (because they could be requiring the same hardware as the
 * primary surface). Standard operations are there, as well:
 * Clearing the primary surface, rendering canvas entities,
 * and rendering canvas entities which lie in a certain area.</p>
 * 
 * <p>This interface is preliminary and subject to change!</p>
 *  
 * @author mbue
 *
 */
public interface Renderer {
	
	/**
	 * initialize renderer
	 * 
	 * @param cc
	 */
	void init(Container cc);
	
	/**
	 * finalise renderer
	 *
	 */
	void dispose();
	
	// -- factory stuff
	CanvasEntityFactory createFactory();
	
	// -- rendering stuff
	/**
	 * Returns the rectangle describing the visible area.
	 * 
	 * @return
	 */
	Rectangle getVisible();
	
	/**
	 * Returns whether the image was lost since the
	 * last time it was rendered.
	 * 
	 * @return
	 */
	boolean validate();
	
	/**
	 * <p>Render those canvas entities from the tree given by
	 * <code>root</code> which lie in the rectangle <code>r</code>.
	 * The tree is treated as being immutable. The user is responsible
	 * for clearing dirty regions.</p>
	 * 
	 * <p>Implementors might find CanvasEntity.foldVisible useful.</p>
	 * 
	 * @param root
	 * @param r
	 */
	void renderVisible(CanvasEntity root, Rectangle r);

	/**
	 * Update part of the image on screen
	 * specified by the rectangle <code>r</code>.
	 * If the argument is <code>null</code>,
	 * update everything.
	 * 
	 * @param r
	 * @return <code>true</code>
	 */
	public boolean show(Rectangle r);
}
