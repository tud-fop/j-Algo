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

	private TreeComponent parent;

	private TreeComponent child;
	
	private boolean visible =true;

	public Edge(TreeComponent p, TreeComponent c) {
		parent = p;
		child = c;
		edge = new org.eclipse.draw2d.graph.Edge(p.getNode(), c.getNode());
	}
	
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

	public boolean getVisibility() {
		return visible;
	}
	
	public void setVisibility(boolean v) {
		visible = v;
	}
	
}