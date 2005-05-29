/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.swt.graphics.Color;

/**
 * A {@link Figure} that shows the edge between two nodes of a tree.
 * @author Michael Pradel
 *  
 */
public class EdgeFigure extends Figure {

	private int arrowMode;

	private Color textColor;

	private Color color;

	private PolylineConnection connection;

	private Arrows myArrow;

	private Label label;

	public void setTextColor(Color color) {
		textColor = color;
	}

	public Color getTextColor() {
		return textColor;
	}

	/**
	 * Chose if and how to display the arrows.
	 * @param mode no arrows, left or right rotation, see also {@link ITreeConstants}
	 */
	public void displayArrows(int mode) {
		arrowMode = mode;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public Color getColor() {
		return color;
	}
}