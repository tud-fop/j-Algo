/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.swt.graphics.Color;

/**
 * A {@link Figure} that shows a leaf of a tree.
 * @author Michael Pradel
 *  
 */
public class LeafFigure extends RoundedRectangle {

	private Color backgroundColor;
	private Label label;

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
}