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
