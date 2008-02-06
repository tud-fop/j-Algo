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

import java.awt.Point;

/**
 * A canvas entity representing a node as used in Heapsort; this means:
 * a circle with a two-digit number in it.
 * 
 * @author mbue
 */
public class Node extends CanvasEntity {
	private float highlight = 0.0f;
	private String label;
	private Point position = new Point();
	private double scale = 1.0;
	
	public Node(Point pos, String label) {
		bounds.setBounds(-20, -20, 40, 40);
		// use a trick:
		// if label == null, invalidation will only occur after setting
		setPosition(pos);
		// don't use setLabel:
		//   a) this.label is null (would crash)
		//   b) too early to invalidate
		this.label = label;
	}
	
	/**
	 * <p>This is called whenever the visual appearance
	 * parameters have changed and can thus be used by
	 * the descendant class (typically rendering-specific)
	 * to update its internal structures (textures etc.)</p>
	 * 
	 * <p>However, the renderer could as well detect changes
	 * on demand (lazily, so to speak), which would of
	 * course require caching the old values...</p>
	 */
	protected void update() {
		// "nothing is being done..." (Jeff Beck)
	}
	
	public final float getHighlight() {
		return highlight;
	}
	
	public final void setHighlight(float value) {
		if (value != highlight) {
			highlight = value;
			invalidate();
			update();
		}
	}
	
	public final String getLabel() {
		return label;
	}
	
	public final void setLabel(String label) {
		if (!this.label.equals(label)) {
			this.label = label;
			invalidate();
			update();
		}
	}
	
	public final Point getPosition() {
		return position;
	}

	// newly added method just for allowing efficient animations
	public final void setPosition(double x, double y) {
		invalidate();
		this.position.setLocation(x, y);
		trans.setToTranslation(position.x, position.y);
		trans.scale(scale, scale);
		invalidate();
		//update(); -- nothing is changed concerning the appearance
	}
	
	public final void setPosition(Point position) {
		if (!this.position.equals(position)) {
			this.position.setLocation(position);
			if (label != null)
				invalidate();
			trans.setToTranslation(position.x, position.y);
			trans.scale(scale, scale);
			invalidate();
			//update(); -- nothing is changed concerning the appearance
		}
	}
	
	public final double getScale() {
		return scale;
	}
	
	public final void setScale(double scale) {
		if (this.scale != scale) {
			this.scale = scale;
			invalidate();
			trans.setToTranslation(position.x, position.y);
			trans.scale(scale, scale);
			invalidate();
			update();
		}
	}
}
