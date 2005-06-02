/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;


/**
 * This class seems to be senseless :-)
 * Don't use it.
 * @author Michael Pradel
 *
 */
public class Tree extends TreeComponent {
	
	public void layout() {
	}
	
	public void addOutgoing(TreeComponent newOut) {
		throw new RuntimeException("Cannot add outgoing elements to a tree.");
	}
	
	public void setParent(TreeComponent newParent) {
		if (!(newParent instanceof Edge)) {
			throw new RuntimeException("Parent of a tree has to be an edge.");
		}
		super.setParent(newParent);
	}
}
