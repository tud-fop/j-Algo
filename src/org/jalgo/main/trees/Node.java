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

	public Node() {
		super();
		node = new org.eclipse.draw2d.graph.Node();
	}
	
	public void addOutgoing(TreeComponent newOut) {
		if (!(newOut instanceof Edge)) {
			throw new RuntimeException("Trying to add a component that is not an edge to a node.");
		}
		super.addOutgoing(newOut);
	}
	
	public void setParent(TreeComponent newParent) {
		if (!(newParent instanceof Edge)) {
			throw new RuntimeException("Trying to set a component as parent of a node that is not an edge.");
		}
		super.setParent(newParent);
	}
	
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
	
	/**
	 * Layoutn the tree or subtree starting from this node.
	 *
	 */
	public void treeLayout() {
		
	}
}