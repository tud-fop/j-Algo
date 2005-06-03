/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import java.util.LinkedList;

import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.Node;

/**
 * Abstract class to represent a component of a tree, independant of its specific
 * properties (following the "Composite" design pattern).
 * 
 * @author Michael Pradel
 *  
 */
public abstract class TreeComponent {

	private TreeComponent parent;

	private LinkedList outgoing;

	private int weight = 0;

	private boolean visible = true;

	public TreeComponent() {
		outgoing = new LinkedList();
	}

	public LinkedList getOutgoing() {
		return outgoing;
	}

	/**
	 * Adds a new outgoing tree component. You cannot add components to leaves.
	 * This method doesn't change the weight of any component.
	 * 
	 * @param newOut
	 *            The new outgoing component.
	 */
	public void addOutgoing(TreeComponent newOut) {
		// leaves cannot have children
		if (this instanceof Leaf) {
			throw new RuntimeException(
					"Trying to add a component to a leave of a tree.");
		}

		outgoing.add(newOut);

		// verify that the new outgoing element has this element as parent
		if ((newOut.getParent() == null) || !(newOut.getParent().equals(this))) {
			newOut.setParent(this);
		}
	}

	public void deleteOutgoing(TreeComponent oldOut) {
		if (!outgoing.contains(oldOut)) {
			throw new RuntimeException(
					"Trying to delete outgoing component of a tree component that is not contained by the parent component.");
		}
		outgoing.remove(oldOut);
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

	public TreeComponent getParent() {
		return parent;
	}

	/**
	 * Set the parent of this tree component.
	 * 
	 * @param newParent
	 *            The new parent.
	 */
	public void setParent(TreeComponent newParent) {
		if (newParent instanceof Leaf) {
			throw new RuntimeException(
					"A leaf cannot be the parent of a tree component.");
		}

		parent = newParent;
		
		// verify that parent has connection to this element
		if (newParent == null) return;
		if (!(newParent.getOutgoing().contains(this))) {
			newParent.addOutgoing(this);
		}
	}

	public boolean getVisibility() {
		return visible;
	}

	public void setVisibility(boolean visible) {
		this.visible = visible;
	}
	
	public Node getNode() {
		return null;
	}
	
	public Edge getEdge() {
		return null;
	}
}