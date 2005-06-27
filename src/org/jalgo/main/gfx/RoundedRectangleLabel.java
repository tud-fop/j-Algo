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
 * Created on May 17, 2004
 */
 
package org.jalgo.main.gfx;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;


/**
 * This class represents a graphical rounded rectangle with 2 Labels.<p>
 * One Label is in the middel (mainLabel) and one Label (indexLabel)
 * is at the top-right corner.
 * 
 * @author Cornelius Hald
 */
public class RoundedRectangleLabel extends RoundedRectangle {
	
	private Label mainLabel;
	private Label indexLabel;

	/**
	 * Contructs new GfxEllipseLabel
	 */
	public RoundedRectangleLabel() {
		super();
		
		//setLayoutManager(new ToolbarLayout());
		
		mainLabel = new Label();
		add(mainLabel);
		indexLabel = new Label();
		add(indexLabel);
		
		
		setOpaque(true);
	}
	
	/**
	 * @return The mainLabel, which is at the center of this Figure
	 */
	public Label getLabel() {
		return mainLabel;
	}
	
	public void setIndexVisible(boolean bool) {
		indexLabel.setVisible(bool);
	}
	
	/**
	 * @return The indexLabel, which is at the top-right on this Figure
	 */
	public Label getIndexLabel() {
		return indexLabel;
	}
	
	/**
	 * Sets text on the label
	 * FIXME: Textposition is wrong
	 * @param text
	 */
	public void setText(String text) {
		mainLabel.setText(text);
		setSize(mainLabel.getPreferredSize().width, getSize().height);
		mainLabel.setSize(mainLabel.getPreferredSize());
	}
	
	/**
	 * @return The text from the mainLabel
	 */
	public String getText() {
		return mainLabel.getText();
	}
	
	public void setTextBackground(Color color) {
		mainLabel.setBackgroundColor(color);
	}
	
	/**
	 * @param bool Enable or disable a line under the mainLabel
	 */
	public void setTextUnderline(boolean bool) {
	}
	
	public void setFont(Font font) {
		mainLabel.setFont(font);
	}
	
	/**
	 * Set text on the indexLabel
	 * @param text
	 */
	public void setIndexText(String text) {
		indexLabel.setText(text);
	}
}
