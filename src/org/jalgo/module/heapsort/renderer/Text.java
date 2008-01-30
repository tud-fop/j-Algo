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
 * A canvas entity which displays text. It serves many purposes to some degree
 * and no purpose fully. E. g., the "regular" property is a real mess, it's only
 * there for displaying the sequence indices and it relies on the undocumented
 * feature that the renderer uses another font. Well, now it is documented,
 * but that doesn't really help it. 
 * 
 * @author mbue
 */
public class Text extends CanvasEntity {
	
	private String text;
	private float opacity;
	private Point position = new Point();
	private double scale = 1.0;
	private boolean regular = false;
	private int width;
	private int height;
	
	protected Text(String text, int width, int height) {
		// don't use setText:
		//   a) this.text is null (would crash)
		//   b) too early to invalidate
		this.text = text;
		opacity = 1.0f;
		this.width = width;
		this.height = height;
		bounds.setBounds(-width/2, -height/2, width, height);
	}

	/**
	 * @see Node#update()
	 *
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

	public String getText() {
		return text;
	}

	public void setText(String text) {
		if (!this.text.equals(text)) {
			this.text = text;
			invalidate();
			update();
		}
	}

	public Point getPosition() {
		return position;
	}
	
	public void setPosition(double x, double y) {
		this.position.setLocation(x, y);
		invalidate();
		trans.setToTranslation(position.x, position.y);
		trans.scale(scale, scale);
		invalidate();
	}

	public void setPosition(Point position) {
		if (!this.position.equals(position)) {
			this.position.setLocation(position);
			invalidate();
			trans.setToTranslation(position.x, position.y);
			trans.scale(scale, scale);
			invalidate();
		}		
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		if (height != this.height) {
			this.height = height;
			invalidate();
			bounds.setBounds(0, 0, width, height);
			invalidate();
		}
		
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		if (width != this.width) {
			this.width = width;
			invalidate();
			bounds.setBounds(0, 0, width, height);
			invalidate();
		}
	}
	
	public double getScale() {
		return scale;
	}
	
	public void setScale(double scale) {
		if (this.scale != scale) {
			this.scale = scale;
			invalidate();
			trans.setToTranslation(position.x, position.y);
			trans.scale(scale, scale);
			invalidate();
			update();
		}
	}

	public boolean isRegular() {
		return regular;
	}

	public void setRegular(boolean regular) {
		if (this.regular != regular) {
			this.regular = regular;
			invalidate();
			update();
		}
	}

}
