package org.jalgo.module.app.core.graph;

import java.io.Serializable;

import org.jalgo.module.app.core.dataType.DataType;

/**
 * Represents an edge of a graph. It has a start node, an end node and a weight.
 * 
 */
public class Edge implements Serializable {

	private static final long serialVersionUID = 7514261796341292879L;
	private Node begin;
	private Node end;
	private DataType weight;

	/**
	 * Initialized the edge with start and end node.
	 * 
	 * @param begin
	 *            The start node.
	 * @param end
	 *            The end node.
	 */
	public Edge(Node begin, Node end) {
		this.begin = begin;
		this.end = end;
		this.weight = null;
	}

	/**
	 * Returns the start node of the edge.
	 * 
	 * @return the start node of the edge.
	 */
	public Node getBegin() {
		return begin;
	}

	/**
	 * Sets the edge to new start node.
	 * 
	 * @param begin
	 *            The start node
	 */
	public void setBegin(Node begin) {
		this.begin = begin;
	}

	/**
	 * Returns the end node of the edge.
	 * 
	 * @return the end node of the edge.
	 */
	public Node getEnd() {
		return end;
	}

	/**
	 * Sets the edge to anew end node.
	 * 
	 * @param end
	 *            The end node.
	 */
	public void setEnd(Node end) {
		this.end = end;
	}

	/**
	 * Gets the current weight of the edge.
	 * 
	 * @return The weight of the edge. This can be an arbitrary
	 *         <code>DataType</code>, based on the used Semiring.
	 */
	public DataType getWeight() {
		return weight;
	}

	/**
	 * Sets the edge to a new weight.
	 * 
	 * @param weight
	 *            the new weight to set.
	 */
	public void setWeight(DataType weight) {
		this.weight = weight;
	}
}
