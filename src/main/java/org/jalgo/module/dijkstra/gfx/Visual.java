/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
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

/*
 * Created on 07.05.2005
 */

package org.jalgo.module.dijkstra.gfx;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.Serializable;

import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.model.GraphElement;

/**
 * Semi-abstract class that serves as a foundation for visual representation
 * objects. Visuals are wrapper objects which contain other actual drawing
 * elements that are displayed.<br>
 * <br>
 * The flags of the underlying model element are interpreted by the update()
 * method, which is implemented differently by each visual subclass and changes
 * the appearance of the drawing elements. Most flags are not exclusive but can
 * be interpreted in a hierarchic manner. For example, the ACTIVE flag overrides
 * all other flags, CHANGED has second priority, and so on. In addition, some
 * flags have different meanings depending on the mode (editing or algorithm) or
 * may have no meaning at all in one of the modes.
 * 
 * @author Martin Winter
 */
public abstract class Visual
implements Serializable {

	/** A custom red color (#CC0000). */
	public static Color RED = new Color(204, 0, 0);

	/** A custom orange color (#CC9900). */
	public static Color ORANGE = new Color(204, 153, 0);

	/** A custom green color (#009900). */
	public static Color GREEN = new Color(0, 153, 0);

	/** A custom blue color (#000099). */
	public static Color BLUE = new Color(0, 0, 153);

	/** A custom white color (#FFFFFF). */
	public static Color WHITE = new Color(255, 255, 255);

	/** A custom gray color (#999999). */
	public static Color GRAY = new Color(153, 153, 153);

	/** A custom black color (#000000). */
	public static Color BLACK = new Color(0, 0, 0);

	private int controllerMode;

	/**
	 * Returns the mode of the controller.
	 * 
	 * @return the mode of the controller or -1 controller is <code>null</code>
	 */
	public int getControllerMode() {
		return controllerMode;
	}

	public void setControllerMode(int controllerMode) {
		this.controllerMode = controllerMode;
	}

	/**
	 * Returns <code>true</code> if the controller is in editing mode.
	 * 
	 * @return <code>true</code> if the controller is in editing mode
	 */
	public boolean isInEditingMode() {
		return (getControllerMode() != Controller.MODE_ALGORITHM);
	}

	/**
	 * Update appearance according to flags. Call this method after modifying
	 * any flags.
	 */
	public abstract void update();

	/**
	 * Determines, if the given point on screen hits the current visual. For
	 * additional information to determine the current scale, the screenSize is
	 * given.
	 * 
	 * @param screenSize the current size of the display component
	 * @param p the point to be tested
	 * @return <code>true</code>, if <code>p</code> hits this visual,
	 *         <code>false</code> otherwise
	 */
	public abstract boolean hit(Dimension screenSize, Point p);

	/**
	 * This method is called, when graph is repainted. Every visual component
	 * has to implement this method to determine how to draw itself.
	 * 
	 * @param g the <code>Graphics2D</code> context to draw on it
	 * @param screensize the size of the <code>GraphDisplay</code> component
	 */
	public abstract void draw(Graphics2D g, Dimension screensize);

	/**
	 * Updates the reference to the backing model element. Without use of this,
	 * sometimes there occur strange things... (Reason: the graph is cloned all
	 * the way.)
	 * 
	 * @param modelElement the model element to synchronize
	 */
	public abstract void updateModel(GraphElement modelElement);

	/**
	 * Updates the location of this visual component regarding to the given size
	 * of the display component.
	 * 
	 * @param screenSize the size of the display component
	 */
	public abstract void updateLocation(Dimension screenSize);
}