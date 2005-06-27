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

/**
 * An edge is the connection between two nodes of a tree.
 * 
 * @author Michael Pradel
 *  
 */

public class Edge {

	private org.eclipse.draw2d.graph.Edge edge;
	
	private EdgeFigure figure;

	private TreeComponent parent;

	private TreeComponent child;
	
	public Edge(TreeComponent p, TreeComponent c) {
		parent = p;
		child = c;
		figure = new EdgeFigure();
		edge = new org.eclipse.draw2d.graph.Edge(p.getNode(), c.getNode());
	}
	
	public void setText(String text) {
		figure.setText(text);
	}

	public String getText() {
		return figure.getText();
	}

	public EdgeFigure getFigure() {
		return figure;
	}

	public boolean getVisibility() {
		return figure.isVisible();
	}
	
	public void setVisibility(boolean v) {
		figure.setVisible(v);
	}
	
	public org.eclipse.draw2d.graph.Edge getEdge() {
		return edge;
	}
	
}