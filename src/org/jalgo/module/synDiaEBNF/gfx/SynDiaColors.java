/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
 * Created on 15.06.2004
 */
package org.jalgo.module.synDiaEBNF.gfx;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.swt.graphics.Color;

/**
 * This interface defines some colors for highlighting components of a syntactical diagram.
 * 
 * @author Marco Zimmerling
 */
public interface SynDiaColors {
	
	static final Color normal = ColorConstants.black;							// ordinary color of components
	static final Color hide = ColorConstants.gray;
	static final Color currentFigure = ColorConstants.yellow;				// hide parts of a syntactical diagram
	static final Color highlightEntireFigure = ColorConstants.red;			// cursor is over the figure's area
	static final Color figureAlongPath = ColorConstants.red;				// terminal/syntactical variable along the chosen path
	static final Color connectionToChoice = ColorConstants.blue;		// user can choose the favored path
	static final Color connectionAlongPath = ColorConstants.red; 		// connection along the chosen path
	static final Color diagramNormal = ColorConstants.white;				// diagram normal
	static final Color diagramHighlight = ColorConstants.lightGray; 	// diagram highlighted
	static final Color textHighlight = ColorConstants.red;
}
