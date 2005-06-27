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

import org.eclipse.draw2d.Border;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Point;

/**
 * This class represents a graphical rectangle with 2 Labels.<p>
 * One Label is in the middel (mainLabel) and one Label (indexLabel)
 * is at the top-right corner.
 * 
 * @author Malte Blumberg
 */
public class RectangleLabel extends Figure {

	private Label mainLabel;
	private Label indexLabel;
	private XYLayout layout;
	
	/**
	 * Contructs new GfxEllipseLabel
	 */
	public RectangleLabel() {
		
		// DEBUG
		setBorder(new LineBorder());
		
		layout = new XYLayout();
		setLayoutManager(layout);

		indexLabel = new Label();
		add(indexLabel, new Rectangle(50, 15, -1, -1));

		mainLabel = new Label();
		add(mainLabel, new Rectangle(5, 15, -1, -1));

		setOpaque(true);
	}
	
	
	public Dimension getPreferredSize(int arg0, int arg1) {
		int width = mainLabel.getPreferredSize().width + 20;
		int heigth = 36;
		
		return new Dimension(width, heigth);
	}
	

	/**
	 * @return The mainLabel, which is at the center of this Figure
	 */
	public Label getLabel() {
		return mainLabel;
	}

	/**
	 * @return The indexLabel, which is at the top-right on this Figure
	 */
	public Label getIndexLabel() {
		return indexLabel;
	}

	public void setIndexVisible(boolean bool) {
		indexLabel.setVisible(bool);
	}

	/**
	 * Sets text on the label
	 * @param text
	 */
	public void setText(String text) {
		mainLabel.setText(text);
		mainLabel.setTextAlignment(PositionConstants.CENTER);
		mainLabel.setLabelAlignment(PositionConstants.CENTER);
		
		readjustText();
	}

	private void readjustText(){
		Point textPoint = new Point((getPreferredSize().width/2)-(mainLabel.getPreferredSize().width/2),(getPreferredSize().height)/2-mainLabel.getPreferredSize().height/2);
		layout.setConstraint(mainLabel, new Rectangle(textPoint.x, textPoint.y, mainLabel.getPreferredSize().width, mainLabel.getPreferredSize().height));
		textPoint = new Point((getPreferredSize().width)-(indexLabel.getPreferredSize().width)-3,3);
		layout.setConstraint(indexLabel, new Rectangle(textPoint.x,textPoint.y, indexLabel.getPreferredSize().width, indexLabel.getPreferredSize().height));
		
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
		if(bool){
			Border labelBorder=new BottomLineBorder();
			mainLabel.setBorder(labelBorder);
		}
		else{
			mainLabel.setBorder(null);
		}
	}

	public void setFont(Font font) {
		mainLabel.setFont(font);
		indexLabel.setFont(font);
	}

	/**
	 * Set text on the indexLabel
	 * @param text
	 */
	public void setIndexText(String text) {
		indexLabel.setText(text);

		indexLabel.setTextAlignment(PositionConstants.RIGHT);
		indexLabel.setLabelAlignment(PositionConstants.RIGHT);

		readjustText();		
	}
	
	public Label getMainLabel() {
		return mainLabel;
	}
}
