/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

/**
 * A leaf component of a tree. Leaves cannot have any children.
 * @author Michael Pradel
 *
 */
public class Leaf extends TreeComponent {

	private org.eclipse.draw2d.graph.Node node;
	private String text;
	private String outerText;
	private LeafFigure figure;

	public Leaf() {
		super();
		node = new org.eclipse.draw2d.graph.Node();
	}
	
	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public LeafFigure getFigure() {
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