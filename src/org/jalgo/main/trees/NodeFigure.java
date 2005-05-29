/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.graphics.Color;

/**
 * A {@link Figure} that shows a node of a tree.
 * @author Michael Pradel
 *  
 */

public class NodeFigure extends Ellipse {

	private Color textColor;

	private Color backgroundColor;

	private Label label;

	public void setTextColor(Color color) {
		this.textColor = color;
	}

	public Color getTextColor() {
		return textColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color color) {
		this.backgroundColor = color;
	}
}