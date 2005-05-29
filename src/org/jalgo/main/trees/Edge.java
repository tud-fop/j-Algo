/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

/**
 * An edge is the connection between two nodes of a tree. 
 * @author Michael Pradel
 *  
 */

public class Edge extends TreeComponent {

	private TreeComponent source, target;
	private String text;
	private org.eclipse.draw2d.graph.Edge edge;
	private EdgeFigure figure;

	public Edge(TreeComponent source, TreeComponent target) {
		if ((source instanceof Edge) || (target instanceof Edge)) {
			throw new RuntimeException("An edge cannot connect other edges.");
		}
		
		this.source = source;
		this.target = target;
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
}