/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/*
 * Created on 07.05.2005
 * 
 */
package org.jalgo.module.dijkstra.model;

import java.io.Serializable;

import org.jalgo.module.dijkstra.gfx.NodeVisual;
import org.jalgo.module.dijkstra.gui.components.GraphDisplay;

/**
 * Represents a Node which is characterized by its index, which is an integer
 * between 1 and 9.
 * 
 * @author Hannes Strass, Martin Winter
 */
public class Node
extends GraphElement
implements Serializable, Comparable<Node>, Cloneable {

	private static final long serialVersionUID = 3662600887046879993L;
	private int index;
	private Position position;
	private Node predecessor;
	private int distance;

	/**
	 * Retrieves a copy of the current <code>Node</code>.
	 */
	@Override
	protected Node clone() {
		try {
			Node clone = (Node)super.clone();
			clone.visual = new NodeVisual(clone);
			return clone;
		}
		catch (CloneNotSupportedException ex) {
			throw new RuntimeException(ex);
		}
	}

	/**
	 * Returns the distance from the start node. This is used by
	 * {@link DijkstraAlgorithm}.
	 * 
	 * @return Returns the distance from the start node to this one.
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * Sets the distance from the start node. This is used by
	 * {@link DijkstraAlgorithm}.
	 * 
	 * @param distance The distance to set.
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	/**
	 * Creates a Node with Position(0.0, 0.0) and given index, sets changed-flag
	 * true
	 * 
	 * @param index the index
	 */
	public Node(int index) {
		this(index, new Position(0, 0));
	}

	/**
	 * creates a node with given Position and given index, sets changed-flag
	 * true
	 * 
	 * @param index the index
	 * @param position the Position
	 */
	public Node(int index, Position position) {
		this.index = index;
		this.position = position;
		visual = new NodeVisual(this);
		setChanged(true); // new Nodes are always changed
	}

	/**
	 * @param anotherNode
	 * @return true, if indexes of this and anotherNode are equal
	 */
	public boolean equals(Node anotherNode) {
		if (anotherNode == null) return false;
		return (this.index) == anotherNode.getIndex();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "(Node " + index + ", " + position + ");" + super.toString(); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}

	/**
	 * @return the index as String
	 */
	public String getLabel() {
		return (String.valueOf(index));
	}

	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param newIndex the index to set
	 * @return true, if index is in range (from 1 to 9). index will not be set
	 *         otherwise
	 */
	public boolean setIndex(int newIndex) {
		if ((0 < newIndex) && (newIndex < 10)) {
			this.index = newIndex;
			return true;
		}

		return false;
	}

	/**
	 * @return The Position of the Node in the virtual "world coordinate system"
	 */
	public Position getPosition() {
		return this.position;
	}

	/**
	 * @return the predecessor of the node concerning shortest paths
	 */
	public Node getPredecessor() {
		return this.predecessor;
	}

	/**
	 * @return the shortest path as string
	 */
	public String getShortestPath() {
		if (getPredecessor() != null) {
			String strPath = getPredecessor().getShortestPath();
			if (strPath.length() == 0) return getPredecessor().getLabel();
			return strPath + "," + getPredecessor().getLabel(); //$NON-NLS-1$
		}
		return ""; //$NON-NLS-1$
	}

	/**
	 * compares Nodes
	 * 
	 * @param anotherNode Node to compare with
	 * @return -1 for less, 0 for equal and 1 for greater (this than
	 *         anotherNode)
	 */
	public int compareTo(Node anotherNode) {
		if (anotherNode.getIndex() < this.index) return 1;
		if (anotherNode.getIndex() == this.index) return 0;
		// (node.getIndex() > this.index)
		return -1;
	}

	/**
	 * Sets the position of this node on the screen.
	 * 
	 * @param newPosition the new position
	 */
	public void setPosition(Position newPosition) {
		this.position = newPosition;
		((NodeVisual)visual).setCenter(
			newPosition.getScreenPoint(GraphDisplay.getScreenSize()));
	}

	/**
	 * Sets the predecessor of a node. This forms the spanning graph where we
	 * extract the shortest paths.
	 * 
	 * @param newPredecessor the predecessor to set
	 */
	public void setPredecessor(Node newPredecessor) {
		this.predecessor = newPredecessor;
	}
}