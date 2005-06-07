/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

/**
 * A node component of a tree.
 * @author Michael Pradel
 *
 */
public class Node extends TreeComponent {

	private String text;
	private String outerText;
	private org.eclipse.draw2d.graph.Node node;
	private NodeFigure figure;

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public NodeFigure getFigure() {
		return figure;
	}

	public void setOuterText(String text) {
		this.outerText = text;
	}

	public String getOuterText() {
		return outerText;
	}
	
	public org.eclipse.draw2d.graph.Node getNode() {
		return node;
	}
	
}