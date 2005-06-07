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

	private String text;

	private org.eclipse.draw2d.graph.Edge edge;

	private EdgeFigure figure;

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public EdgeFigure getFigure() {
		return figure;
	}
	
	public org.eclipse.draw2d.graph.Edge getEdge() {
		return edge;
	}
	
}