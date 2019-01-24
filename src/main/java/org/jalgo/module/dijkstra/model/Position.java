/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer science. It is written in Java and platform independent. j-Algo is developed with the help of Dresden University of Technology.
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
 * Created on 08.05.2005
 *
 */
package org.jalgo.module.dijkstra.model;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.io.Serializable;

/**
 * Defines a data structure which indicates the position of a {@link Node} in a virtual "world coordinate system".
 * <br><br><i>Note:</i> The parameter "screenSize" does not mean the size of the entire screen; 
 * it refers to the area of the screen that represents the coordinate system and is to contrast with "worldSize".
 * @author Hannes Strass, Martin Winter
 */
public class Position implements Serializable {

	private static final long serialVersionUID = 4599666519626602617L;

	// here no Point2D is used because of the lack of serializability
	private double worldX;
	private double worldY;

	/**
	 * Creates a new position using world coordinates as individual doubles.
	 * @param worldX horizontal coordinate, range -1 ... +1 inclusive
	 * @param worldY vertical coordinate, range -1 ... +1 inclusive
	 */
	public Position(double worldX, double worldY) {
		this.worldX = worldX;
		this.worldY = worldY;
	}

	/**
	 * Creates a new position using screen coordinates and the size of the screen area as a Dimension.
	 * @param screenCoordinates point (pixels) of the position
	 * @param screenSize screen size as a dimension (see class description above)
	 */
	public Position(Point screenCoordinates, Dimension screenSize) {
		setScreenCoordinates(screenCoordinates, screenSize);
	}

	/**
	 * Creates a new position using screen coordinates and the size of the screen area as a Rectangle.
	 * Location of screenBounds is ignored.
	 * @param screenCoordinates point (pixels) of the position
	 * @param screenBounds screen size as a rectangle (see class description above)
	 */
	public Position(Point screenCoordinates, Rectangle screenBounds) {
		this(screenCoordinates, screenBounds.getSize());
	}

	/**
	 * Sets screen coordinates and computes world coordinates.
	 * @param screenCoordinates point (pixels) of the position
	 * @param screenSize screen size as a dimension (see class description above)
	 */
	public void setScreenCoordinates(Point screenCoordinates, Dimension screenSize) {
		// Copy screen coordinates as base for calculations.
		worldX = screenCoordinates.getX();
		worldY = screenCoordinates.getY();

		// Translate coordinates by the distance of the screen (area) center from the origin,
		// so we get four quadrants with positive _and_ negative coordinates.
		double horizontalOffset = screenSize.width / 2.0; // Must be double in case dimensions are odd.
		double verticalOffset = screenSize.height / 2.0;
		worldX -= horizontalOffset;
		worldY -= verticalOffset;

		// Scale screen coordinates to world coordinate system (range -1 ... +1 inclusive).
		worldX /= horizontalOffset;
		worldY /= verticalOffset * -1.0; // Flip vertical coordinates.
	}

	/**
	 * Sets screen coordinates and computes world coordinates.
	 * @param screenCoordinates point (pixels) of the position
	 * @param screenBounds screen size as a rectangle (see class description above)
	 */
	public void setScreenCoordinates(Point screenCoordinates, Rectangle screenBounds) {
		setScreenCoordinates(screenCoordinates, screenBounds.getSize());
	}

	/**
	 * Returns the point on screen, computed from internal world coordinates and relative to screenSize.
	 * Performs operations symmetric to those of setScreenCoordinates().
	 * @param screenSize screen size as a dimension (see class description above)
	 * @return point on screen (pixels)
	 */
	public Point getScreenPoint(Dimension screenSize) {
		// Copy world coordinates as base for calculations.
		// Passing worldCoordinates to the constructor of screenCoordinates
		// would copy only integer values, not preciseX and preciseY!
		Point2D screenCoordinates = new Point2D.Double();
		screenCoordinates.setLocation(worldX, worldY);

		// Scale world coordinates to screen coordinate system.
		double horizontalOffset = screenSize.width / 2.0; // Must be doubles in case dimensions are odd.
		double verticalOffset = screenSize.height / 2.0;
		screenCoordinates.setLocation(
			screenCoordinates.getX() * horizontalOffset,
			screenCoordinates.getY() * verticalOffset * -1.0); // Flip vertical coordinates.

		// Translate coordinates by the distance of the screen (area) center from the origin,
		// so we get only positive coordinates.
		screenCoordinates.setLocation(
			screenCoordinates.getX() + horizontalOffset,
			screenCoordinates.getY() + verticalOffset);

		// Return integer coordinates.
		Point integralScreenCoordinates = new Point(
			(int)screenCoordinates.getX(), (int)screenCoordinates.getY());
		return integralScreenCoordinates;
	}

	/**
	 * Returns the point on screen, computed from internal world coordinates and relative to screenSize.
	 * Performs operations symmetric to those of setScreenCoordinates().
	 * @param screenBounds screen size as a rectangle (see class description above)
	 * @return point on screen (pixels)
	 */
	public Point getScreenPoint(Rectangle screenBounds) {
		return getScreenPoint(screenBounds.getSize());
	}

	/**
	 * Sets world coordinates.
	 * Automatically clipped to the range -1 ... +1 inclusive.
	 * @param worldCoordinates world coordinates as a PrecisionPoint (double)
	 */
	public void setWorldCoordinates(Point2D worldCoordinates) {
		setWorldX(worldCoordinates.getX());
		setWorldY(worldCoordinates.getY());
	}

	/**
	 * Sets horizontal world coordinate.
	 * Automatically clipped to the range -1 ... +1 inclusive.
	 * @param newWorldX world x coordinate as a double
	 */
	public void setWorldX(double newWorldX) {
		worldX = Math.min(1, Math.max(-1, newWorldX));
	}

	/**
	 * Sets vertical world coordinate.
	 * Automatically clipped to the range -1 ... +1 inclusive.
	 * @param newWorldY world y coordinate as a double
	 */
	public void setWorldY(double newWorldY) {
		worldY = Math.min(1, Math.max(-1, newWorldY));
	}

	/**
	 * Returns horizontal world coordinate.
	 * @return world x coordinate as a double
	 */
	public double getWorldX() {
		return worldX;
	}

	/**
	 * Returns vertical world coordinate.
	 * @return world y coordinate as a double
	 */
	public double getWorldY() {
		return worldY;
	}

	/**
	 * Returns <code>true</code> if world coordinates are in the range -1 ... +1 inclusive.
	 * @return <code>true</code> if world coordinates are in the range -1 ... +1 inclusive
	 */
	public boolean isInRange() {
		return (-1 <= getWorldX()) && (getWorldX() <= 1) && (-1 <= getWorldY()) && (getWorldY() <= 1);
	}

	/**
	 * Returns a string representation of this position, including its world coordinates.
	 */
	public String toString() {
		return "Position (" + getWorldX() + ", " + getWorldY() + ")"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * Returns <code>true</code> if the world coordinates of this position and the argument are equal.
	 */
	public boolean equals(Object arg0) {
		if (arg0.getClass().equals(getClass())) {
			Position pos = (Position) arg0;
			return ((pos.getWorldX() == getWorldX()) && (pos.getWorldY() == getWorldY()));
		}
		return false;
	}
}
