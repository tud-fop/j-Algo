/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.StackLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;

/**
 * A {@link Figure}that shows a node of a tree.
 * 
 * @author Michael Pradel
 *  
 */

public class NodeFigure extends Figure {

	private final int spacing = 5;

	private Label outerLabel;

	private textFigure circle;

	private class textFigure extends Ellipse {
		public Label label;

		public textFigure(String t) {
			label = new Label(t);
			setLayoutManager(new StackLayout());
			add(label, new Rectangle(-1, -1, -1, -1));
		}

		public Dimension getPreferredSize(int wHint, int hHint) {
			Dimension pref = super.getPreferredSize(wHint, hHint);
			pref.width += spacing;
			pref.height = pref.width;
			return pref;
		}
	}

	public NodeFigure() {
		this("");
	}

	public NodeFigure(String text) {
		super();
		outerLabel = new Label();
		circle = new textFigure(text);

		// TODO: remove after testing
		outerLabel.setText("abc");

		FlowLayout layout = new FlowLayout();
		setLayoutManager(layout);

		add(outerLabel, new Rectangle(0, 0, -1, -1));
		add(circle, new Rectangle(0, 0, -1, -1));

	}

	public void setTextColor(Color color) {
		circle.label.setForegroundColor(color);
	}

	public Color getTextColor() {
		return circle.label.getForegroundColor();
	}

	public void setText(String text) {
		circle.label.setText(text);
	}

	public String getText() {
		return circle.label.getText();
	}

	public void setOuterText(String text) {
		outerLabel.setText(text);
	}

	public String getOuterText() {
		return outerLabel.getText();
	}

	public Figure getInnerFigure() {
		return circle;
	}
	
}