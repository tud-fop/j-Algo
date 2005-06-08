/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import org.eclipse.draw2d.graph.Node;

/**
 * Abstract class to represent a component of a tree, independant of its
 * specific properties (following the "Composite" design pattern).
 * 
 * @author Michael Pradel
 *  
 */
public abstract class TreeComponent {

	protected String text;

	protected String outerText;

	protected int weight = 0;

	protected boolean visible = true;

	protected TreeComponent parent;

	protected Edge edgeToParent;

	private org.eclipse.draw2d.graph.Node node;

	public TreeComponent() {
		node = new Node(this);
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
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
	 * Get the weight of this component. A node, leaf or subtree with a higher
	 * weight will appear on the right of one with a lower weigth.
	 * 
	 * @return The weight of this component, see also {@link ITreeConstants}.
	 */
	public int getWeight() {
		return weight;
	}

	/**
	 * Set the weight of this component. A node, leaf or subtree with a higher
	 * weight will appear on the right of one with a lower weigth.
	 * 
	 * @param weight
	 *            The new weight of this component, see also
	 *            {@link ITreeConstants}.
	 */
	public void setWeight(int weight) {
		this.weight = weight;
	}

	public boolean getVisibility() {
		return visible;
	}

	public void setVisibility(boolean visible) {
		this.visible = visible;
	}

	public boolean isTree() {
		return false;
	}

	public TreeComponent getParent() {
		return parent;
	}

	protected void setParent(TreeComponent p) {
		parent = p;
	}
	
	public Edge getEdgeToParent() {
		return edgeToParent;
	}

	protected void setEdgeToParent(Edge e) {
		edgeToParent = e;
	}
}