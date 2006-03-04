/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.graph.Node;

/**
 * Abstract class to represent a component of a tree, independant of its
 * specific properties (following the "Composite" design pattern).
 * 
 * @author Michael Pradel
 * 
 */
public abstract class TreeComponent {

	protected int weight = 0;

	protected TreeComponent parent;

	protected Edge edgeToParent;

	private org.eclipse.draw2d.graph.Node node;

	//protected Figure outerFigure;

	public TreeComponent() {
		node = new Node(this);
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

	public abstract boolean getVisibility();

	public abstract void setVisibility(boolean visible);

	public boolean isTree() {
		return false;
	}

	public TreeComponent getParent() {
		return parent;
	}

	public Edge getEdgeToParent() {
		return edgeToParent;
	}

	public abstract Figure getInnerFigure();

	protected void setParent(TreeComponent p) {
		parent = p;
	}

	protected void setEdgeToParent(Edge e) {
		edgeToParent = e;
	}

	/**
	 * Get a figure that containing the whole
	 * <code>TreeComponent</code (e.g. a node and all children with their children if it is a tree).
	 * @return The outer figure.
	 *//*
	protected Figure getOuterFigure() {
		return outerFigure;
	}*/

}