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