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

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.draw2d.Figure;

/**
 * A tree can be a single node or a node with children. This class represents
 * the composite of the composite design pattern.
 * 
 * @author Michael Pradel
 *  
 */
public class Tree extends TreeComponent {

	private NodeFigure nodeFigure;
	
	private LinkedList children; // LinkedList of TreeComponent

	private LinkedList edges; // LinkedList of Edge
	
	public Tree() {
		this("");
	}
	
	public Tree(String text) {
		super();
		nodeFigure = new NodeFigure(text);
		children = new LinkedList();
		edges = new LinkedList();
	}

	public void layout() {
	}

	public boolean isTree() {
		return true;
	}

	public void addChild(TreeComponent c) {
		children.add(c);
		Edge newEdge = new Edge(this, c);
		edges.add(newEdge);
		c.setParent(this);
		c.setEdgeToParent(newEdge);
	}

	public boolean removeChild(TreeComponent c) {
		if (!children.contains(c))
			return false;
		int i = children.indexOf(c);
		children.remove(i);
		edges.remove(i);
		c.setParent(null);
		c.setEdgeToParent(null);
		return true;
	}

	public LinkedList getChildren() {
		return children;
	}
	
	public Edge getEdgeTo(TreeComponent c) {
		int i = children.indexOf(c);
		return (Edge) edges.get(i);
	}

	public void setAllVisibility(boolean visible) {
		// change visibility of children
		Iterator i = children.iterator();
		while(i.hasNext()) {
			((TreeComponent) i.next()).setVisibility(visible);
		}
		// change visibility of edges to children
		i = edges.iterator();
		while(i.hasNext()) {
			((Edge) i.next()).setVisibility(visible);
		}
	}
	
	public void setVisibility(boolean visible) {
		nodeFigure.setVisible(visible);
	}
	
	public boolean getVisibility() {
		return nodeFigure.isVisible();
	}

	public void setText(String text) {
		nodeFigure.setText(text);
	}

	public String getText() {
		return nodeFigure.getText();
	}

	public void setOuterText(String text) {
		nodeFigure.setOuterText(text);
	}

	public String getOuterText() {
		return nodeFigure.getOuterText();
	}
	
	public NodeFigure getNodeFigure() {
		return nodeFigure;
	}
	
	public Figure getInnerFigure() {
		return nodeFigure.getInnerFigure();
	}
	
}
