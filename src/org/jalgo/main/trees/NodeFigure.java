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