package org.jalgo.module.app.controller;

import org.jalgo.module.app.core.graph.Edge;
import org.jalgo.module.app.core.graph.Graph;
import org.jalgo.module.app.core.graph.Node;

/**
 * This interface is used for notifying all components which need to be
 * consistent with the graph. Every time a change is made in the graph, the
 * <code>GraphController</code> notifies all components that something in the
 * graph has changed. It is part of the GoF Observer-Pattern.
 */
public interface GraphObserver {

	/**
	 * Notifies components that a node has been added to the graph.
	 * 
	 * @param graph
	 *            the current graph.
	 * @param node
	 *            the new node.
	 */
	public void nodeAdded(Graph graph, Node node);

	/**
	 * Notifies components that a node has been altered, i.e. when its
	 * position has been changed.
	 * 
	 * @param graph
	 *            the current graph.
	 * @param node
	 *            the altered node.
	 */
	public void nodeAltered(Graph graph, Node node);

	/**
	 * Notifies components that a node has been removed from the graph.
	 * 
	 * @param graph
	 *            the current graph.
	 * @param node
	 *            the node that has to be removed from all components.
	 */
	public void nodeRemoved(Graph graph, Node node);

	/**
	 * Notifies components that an edge has been added to the graph.
	 * 
	 * @param graph
	 *            the current graph.
	 * @param edge
	 *            the new edge.
	 */
	public void edgeAdded(Graph graph, Edge edge);

	/**
	 * Notifies components that an edge has been altered, i.e. when its weight
	 * has been changed.
	 * 
	 * @param graph
	 *            the current graph.
	 * @param edge
	 *            the altered edge.
	 */
	public void edgeAltered(Graph graph, Edge edge);

	/**
	 * Notifies components that an edge has been removed from the graph.
	 * 
	 * @param graph
	 *            the current graph.
	 * @param edge
	 *            the edge that has to be removed from all components.
	 */
	public void edgeRemoved(Graph graph, Edge edge);

	/**
	 * Notifies components that something has been changed in the graph (when a
	 * node/edge has been added, removed or altered).
	 */
	public void graphUpdated();

	/**
	 * Notifies components that the focus of a graph element (node or edge) has
	 * be changed, i.e. whenever an element has been colored red in the graph
	 * editor.
	 */
	public void graphSelectionChanged();
}
