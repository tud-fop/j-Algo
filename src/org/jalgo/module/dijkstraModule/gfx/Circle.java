/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on 07.05.2005
 */

package org.jalgo.module.dijkstraModule.gfx;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * An ellipse with equal horizontal and vertical dimensions, defined by a radius, 
 * and positioned through a center point instead of bounds.
 * <br><br><i>Note:</i> The circle's diameter is (2 * radius) + 1 pixels,
 * so the center always lies on a pixel and not between two pixels.
 * @author Martin Winter
 */
public class Circle 
extends Ellipse {

	private Point circleCenter;
	private int circleRadius;
	
	/**
	 * Creates a circle with the given center and radius.
	 * @param center the circle's center in pixel coordinates
	 * @param radius the circle's radius in pixels
	 */
	public Circle(Point center, int radius) {
		circleCenter = center;
		circleRadius = radius;
		updateBounds();
	}

	/**
	 * Sets the circle's center.
	 * @param center the circle's center in pixel coordinates
	 */
	public void setCenter(Point center) {
		circleCenter = center;
		updateBounds();
	}
	
	/**
	 * Returns the circle's center.
	 * @return the circle's center in pixel coordinates
	 */
	public Point getCenter() {
		return circleCenter;
	}
	
	/**
	 * Sets the circle's radius.
	 * @param radius the circle's radius in pixels
	 */
	public void setRadius(int radius) {
		circleRadius = radius;
		updateBounds();
	}
	
	/**
	 * Gets the circle's radius.
	 * @return the circle's radius in pixels
	 */
	public int getRadius() {
		return circleRadius;
	}
	
	/**
	 * Updates the circle's bounds according to center and radius.
	 */
	private void updateBounds() {
		Point topLeft = new Point(circleCenter.getTranslated(-circleRadius, -circleRadius));
		Point bottomRight = new Point(circleCenter.getTranslated(circleRadius, circleRadius));
		Rectangle bounds = new Rectangle(topLeft, bottomRight);
		setBounds(bounds);
	}
}
