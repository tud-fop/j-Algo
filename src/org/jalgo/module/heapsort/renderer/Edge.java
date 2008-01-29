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

import java.awt.Point;

/**
 * @author mbue
 *
 */
public class Edge extends CanvasEntity {
	protected Point from;
	protected Point to;
	protected float opacity = 1.0f;

	protected Edge(Point from, Point to) {
		this.from = from;
		this.to = to;
		updateBounds();
	}
	
	private void updateBounds() {
		invalidate();
		if (to.x < from.x)
			bounds.setBounds(to.x, from.y, from.x-to.x, to.y-from.y);
		else
			bounds.setBounds(from.x, from.y, to.x-from.x, to.y-from.y);
		update();
	}

	/**
	 * @see Node#update()
	 */
	protected void update() {
	}
	
	public float getOpacity() {
		return opacity;
	}

	public void setOpacity(float opacity) {
		this.opacity = opacity;
		invalidate();
		update();
	}	
}
