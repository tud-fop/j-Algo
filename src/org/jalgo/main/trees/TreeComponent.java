/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import java.util.LinkedList;

/**
 * Abstract class to represent a component of a tree, independant of its
 * specific properties (following the "Composite" design pattern).
 * 
 * @author Michael Pradel
 *  
 */
public abstract class TreeComponent {

	protected int weight = 0;

	protected boolean visible = true;
	
	protected TreeComponent parent;
	
	protected Edge edgeToParent; 
	
	protected LinkedList children; // LinkedList of TreeComponents 

	public TreeComponent() {
		children = new LinkedList();
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
	
	public Edge getEdgeToParent() {
		return edgeToParent;
	}
	
	protected void setEdgeToParent(Edge e) {
		edgeToParent = e;
	}

}