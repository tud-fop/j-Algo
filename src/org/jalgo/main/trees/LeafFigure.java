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

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * A {@link Figure}that shows a leaf of a tree.
 * 
 * @author Michael Pradel
 *  
 */
public class LeafFigure extends Figure {

	private final int spacing = 10;

	private textFigure rect;

	private Label label;

	private Label outerLabel;

	private class textFigure extends RoundedRectangle {
		public Label label;
		
		public textFigure(String t) {
			label = new Label(t);
			FlowLayout textLayout = new FlowLayout();
			textLayout.setMajorAlignment(FlowLayout.ALIGN_CENTER);
			setLayoutManager(textLayout);
			add(label, new Rectangle(-1,-1,-1,-1));
		}

		public Dimension getPreferredSize(int wHint, int hHint) {
			Dimension pref = super.getPreferredSize(wHint, hHint);
			pref.width += spacing;
			return pref;
		}
	}
	
	public LeafFigure() {
		this("NIL");
	}
	
	public LeafFigure(String text) {
		super();
		outerLabel = new Label();

		rect = new textFigure(text);
		
		// TODO: remove after testing
		outerLabel.setText("xyz");

		FlowLayout layout = new FlowLayout();
		setLayoutManager(layout);

		add(outerLabel, new Rectangle(0, 0, -1, -1));
		add(rect, new Rectangle(0, 0, -1, -1));

	}

	public void setText(String text) {
		rect.label.setText(text);
	}
	
	public String getText() {
		return rect.label.getText();
	}
	
	public void setOuterText(String text) {
		outerLabel.setText(text);
	}
	
	public String getOuterText() {
		return outerLabel.getText();
	}
	
	public Figure getInnerFigure() {
		return rect;
	}
}