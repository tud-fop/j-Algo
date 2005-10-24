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
 * Created on 08.05.2005
 *
 */
package org.jalgo.module.dijkstra.model;

import java.io.Serializable;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.PrecisionPoint;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * Defines a data structure which indicates the position of a {@link Node} in a virtual "world coordinate system".
 * <br><br><i>Note:</i> The parameter "screenSize" does not mean the size of the entire screen; 
 * it refers to the area of the screen that represents the coordinate system and is to contrast with "worldSize".
 * @author Hannes Strass, Martin Winter
 */
public class Position implements Serializable {

	private static final long serialVersionUID = 4599666519626602617L;

	/**
	 * Use preciseX and preciseY fields for double values.
	 */
	private PrecisionPoint worldCoordinates = new PrecisionPoint();

	/**
	 * Basic constructor. Creates a new position using world coordinates.
	 * @param worldCoordinates range -1 ... +1 inclusive
	 */
	public Position(PrecisionPoint worldCoordinates) {
		this.worldCoordinates = (PrecisionPoint) worldCoordinates.getCopy();
	}

	/**
	 * Creates a new position using world coordinates as individual doubles.
	 * @param worldX horizontal coordinate, range -1 ... +1 inclusive
	 * @param worldY vertical coordinate, range -1 ... +1 inclusive
	 */
	public Position(double worldX, double worldY) {
		this(new PrecisionPoint(worldX, worldY));
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
		worldCoordinates.setLocation(screenCoordinates);

		// Translate coordinates by the distance of the screen (area) center from the origin,
		// so we get four quadrants with positive _and_ negative coordinates.
		double horizontalOffset = screenSize.width / 2.0; // Must be double in case dimensions are odd.
		double verticalOffset = screenSize.height / 2.0;
		worldCoordinates.preciseX -= horizontalOffset; // Can't use performTranslate, which uses ints.
		worldCoordinates.preciseY -= verticalOffset;

		// Scale screen coordinates to world coordinate system (range -1 ... +1 inclusive).
		worldCoordinates.preciseX /= horizontalOffset; // Can't use performScale, which is uniform;
		worldCoordinates.preciseY /= verticalOffset * (-1.0); // Flip vertical coordinates.

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
		PrecisionPoint screenCoordinates = new PrecisionPoint();
		screenCoordinates.setLocation(worldCoordinates);

		// Scale world coordinates to screen coordinate system.
		double horizontalOffset = screenSize.width / 2.0; // Must be doubles in case dimensions are odd.
		double verticalOffset = screenSize.height / 2.0;
		screenCoordinates.preciseX *= horizontalOffset; // Can't use performScale, which is uniform.
		screenCoordinates.preciseY *= verticalOffset * (-1.0); // Flip vertical coordinates.

		// Translate coordinates by the distance of the screen (area) center from the origin,
		// so we get only positive coordinates.
		screenCoordinates.preciseX += horizontalOffset; // Can't use performTranslate, which uses ints.
		screenCoordinates.preciseY += verticalOffset;

		// Return integer coordinates.
		screenCoordinates.updateInts();
		Point integralScreenCoordinates = new Point(screenCoordinates.x, screenCoordinates.y);
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
	public void setWorldCoordinates(PrecisionPoint worldCoordinates) {
		setWorldX(worldCoordinates.preciseX);
		setWorldY(worldCoordinates.preciseY);
	}

	/**
	 * Sets horizontal world coordinate.
	 * Automatically clipped to the range -1 ... +1 inclusive.
	 * @param newWorldX world x coordinate as a double
	 */
	public void setWorldX(double newWorldX) {
		if (newWorldX <= -1) {
			newWorldX = -1;
		} else if (newWorldX >= 1) {
			newWorldX = 1;
		}
		worldCoordinates.preciseX = newWorldX;
	}

	/**
	 * Sets vertical world coordinate.
	 * Automatically clipped to the range -1 ... +1 inclusive.
	 * @param newWorldY world y coordinate as a double
	 */
	public void setWorldY(double newWorldY) {
		if (newWorldY <= -1) {
			newWorldY = -1;
		} else if (newWorldY >= 1) {
			newWorldY = 1;
		}
		worldCoordinates.preciseY = newWorldY;
	}

	/**
	 * Returns world coordinates.
	 * @return world coordinates as a PrecisionPoint (double)
	 */
	public PrecisionPoint getWorldCoordinates() {
		return worldCoordinates;
	}

	/**
	 * Returns horizontal world coordinate.
	 * @return world x coordinate as a double
	 */
	public double getWorldX() {
		return worldCoordinates.preciseX;
	}

	/**
	 * Returns vertical world coordinate.
	 * @return world y coordinate as a double
	 */
	public double getWorldY() {
		return worldCoordinates.preciseY;
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
		return "Position (" + getWorldX() + ", " + getWorldY() + ")";
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
