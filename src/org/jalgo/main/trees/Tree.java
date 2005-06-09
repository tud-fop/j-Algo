/*
 * Created on 27.05.2005
 *
 */
package org.jalgo.main.trees;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * A tree can be a single node or a node with children. This class represents
 * the composite of the composite design pattern.
 * 
 * @author Michael Pradel
 *  
 */
public class Tree extends TreeComponent {

	private NodeFigure nodeFigure;
	
	private LinkedList children; // LinkedList of TreeComponent

	private LinkedList edges; // LinkedList of Edge

	private boolean firstVisibility;
	
	public Tree() {
		this("");
	}
	
	public Tree(String text) {
		super();
		nodeFigure = new NodeFigure(text);
		children = new LinkedList();
		edges = new LinkedList();
	}

	public void layout() {
	}

	public boolean isTree() {
		return true;
	}

	public void addChild(TreeComponent c) {
		children.add(c);
		Edge newEdge = new Edge(this, c);
		edges.add(newEdge);
		c.setParent(this);
		c.setEdgeToParent(newEdge);
	}

	public boolean removeChild(TreeComponent c) {
		if (!children.contains(c))
			return false;
		int i = children.indexOf(c);
		children.remove(i);
		edges.remove(i);
		c.setParent(null);
		c.setEdgeToParent(null);
		return true;
	}

	public LinkedList getChildren() {
		return children;
	}
	
	public Edge getEdgeTo(TreeComponent c) {
		int i = children.indexOf(c);
		return (Edge) edges.get(i);
	}

	public void setAllVisibility(boolean visible) {
		// change visibility of children
		Iterator i = children.iterator();
		while(i.hasNext()) {
			((TreeComponent) i.next()).setVisibility(visible);
		}
		// change visibility of edges to children
		i = edges.iterator();
		while(i.hasNext()) {
			((Edge) i.next()).setVisibility(visible);
		}
	}
	
	public void setVisibility(boolean visible) {
		firstVisibility = visible;
	}

	public void setText(String text) {
		nodeFigure.setText(text);
	}

	public String getText() {
		return nodeFigure.getText();
	}

	public void setOuterText(String text) {
		nodeFigure.setOuterText(text);
	}

	public String getOuterText() {
		return nodeFigure.getOuterText();
	}
	
	public NodeFigure getNodeFigure() {
		return nodeFigure;
	}
	
}
