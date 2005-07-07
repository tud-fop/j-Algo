/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.swt.graphics.Color;

/**
 * A {@link Figure}that shows the edge between two nodes of a tree.
 * 
 * @author Michael Pradel
 *  
 */
public class EdgeFigure extends PolylineConnection {

	private Color textColor;

	private ImageFigure myArrow;

	private Label label;

	private MidpointLocator arrowLocator;
	
	private MidpointLocator textLocator;

	public EdgeFigure() {
		this(ITreeConstants.NO_ROT);
	}

	public EdgeFigure(int arrowMode) {
		setArrows(arrowMode);
		textLocator = new MidpointLocator(this, 0);
		textLocator.setRelativePosition(PositionConstants.WEST);
		textLocator.setGap(30);
		label = new Label();
		add(label, textLocator);
	}

	public void setText(String t) {
		label.setText(t);
	}
	
	public String getText() {
		return label.getText();
	}
	
	public void setTextColor(Color color) {
		label.setForegroundColor(color);
	}

	public Color getTextColor() {
		return label.getForegroundColor();
	}

	/**
	 * Chose if and how to display the arrows.
	 * 
	 * @param mode
	 *            no arrows, left or right rotation, see also
	 *            {@link ITreeConstants}
	 */
	public void setArrows(int mode) {
		if ((mode == ITreeConstants.LEFT_ROT)
				|| (mode == ITreeConstants.RIGHT_ROT)) {
			myArrow = Arrows.getArrows(mode);
			arrowLocator = new MidpointLocator(this, 0);
			add(myArrow, arrowLocator);
		} else if (mode == ITreeConstants.NO_ROT) {
			// remove old arrows if their exist
			if (myArrow != null)
				remove(myArrow);
		}
	}

}