/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;


/**
 * A tree can be a single node or a node with children. This class represents
 * the composite of the composite design pattern.
 * 
 * @author Michael Pradel
 *
 */
public class Tree extends TreeComponent {
	
	public void layout() {
	}
	
	public boolean isTree() {
		return true;
	}
	
	public void addChild(TreeComponent c) {
		children.add(c);
	}

	public boolean removeChild(TreeComponent c) {
		return children.remove(c);
	}
	
}
