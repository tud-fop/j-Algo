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

/**
 * A leaf component of a tree. Leaves cannot have any children.
 * @author Michael Pradel
 *
 */
public class Leaf extends TreeComponent {

	private LeafFigure figure;
	
	public Leaf() {
		this("NIL");
	}
	
	public Leaf(String text) {
		super();
		figure = new LeafFigure(text);
	}

	public void setText(String text) {
		figure.setText(text);
	}

	public String getText() {
		return figure.getText();
	}

	public void setOuterText(String text) {
		figure.setOuterText(text);
	}

	public String getOuterText() {
		return figure.getOuterText();
	}
	
	public LeafFigure getFigure() {
		return figure;
	}
	
	public void setVisibility(boolean v) {
		figure.setVisible(v);
	}
	
	public boolean getVisibility() {
		return figure.isVisible();
	}

	public Figure getInnerFigure() {
		return figure.getInnerFigure();
	}
	
}