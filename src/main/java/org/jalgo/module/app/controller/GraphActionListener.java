package org.jalgo.module.app.controller;

import java.awt.geom.Point2D;

import org.jalgo.module.app.controller.undoRedo.UndoManager;
import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.graph.Edge;
import org.jalgo.module.app.core.graph.Graph;
import org.jalgo.module.app.core.graph.Node;

/**
 * This interface is used for notifying changes made in the graph (core,
 * <code>GraphComponent</code> or <code>GraphTextComponent</code>) to the
 * core and the components which display the graph.
 * 
 */
public interface GraphActionListener {

	/**
	 * Returns the graph all actions below deal with.
	 * 
	 * @return the graph of the listener
	 */
	public Graph getGraph();

	/**
	 * Returns the data type of all edge weights in the graph.
	 * 
	 * @return the weight data type
	 */
	public Class<? extends DataType> getDataType();

	/**
	 * Adds <code>newObserver</code> to the listener as observer, notifying it
	 * of all changes done to the graph (also initiated by itself).
	 * 
	 * @param newObserver
	 *            the observer to add
	 */
	public void addGraphObserver(GraphObserver newObserver);

	/**
	 * Provides the mode that says which element is edited.
	 */
	public enum EditMode {
		/**
		 * No element is currently edited. 
		 */
		NONE,
		/**
		 * A node is edited. 
		 */
		EDITING_NODES,
		/**
		 * A edge is edited.
		 */
		EDITING_EDGES,
		/**
		 * The weight of an edge is edited. 
		 */
		EDITING_WEIGHT,
	};

	/**
	 * Gets the current edit mode.
	 * 
	 * @return the current edit mode.
	 */
	public EditMode getEditMode();

	/**
	 * Sets the current edit mode.
	 * 
	 * @param mode
	 *            the new edit mode.
	 */
	public void setEditMode(EditMode mode);

	/**
	 * Gets the node that is currently selected.
	 * 
	 * @return the currently selected node. If no node is selected,
	 *         <code>null</code> is returned.
	 */
	public Node getSelectedNode();

	/**
	 * Sets the currently selected node.
	 * 
	 * @param node
	 *            the currently selected node. If no node is selected,
	 *            <code>node</code> has to be set to <code>null</code>.
	 */
	public void setSelectedNode(Node node);

	/**
	 * Gets the edge that is currently selected.
	 * 
	 * @return the currently selected edge. If no edge is selected,
	 *         <code>null</code> is returned.
	 */
	public Edge getSelectedEdge();

	/**
	 * Sets the currently selected edge.
	 * 
	 * @param edge
	 *            the currently selected edge. If no edge is selected,
	 *            <code>edge</code> has to be set to <code>null</code>.
	 */
	public void setSelectedEdge(Edge edge);

	/**
	 * Begins grouping of change notifications. Makes only sense when editing
	 * multiple things.
	 * 
	 * @see #endEditing()
	 */
	public void beginEditing();

	/**
	 * Ends grouping of change notifications.
	 * 
	 * @see #beginEditing()
	 */
	public void endEditing();

	/*
	 * NODES
	 * 
	 */

	/**
	 * Adds a new node to the graph.
	 * 
	 * @return the new node.
	 */
	public Node addNode();

	/**
	 * Adds a new node to the graph, setting the given position.
	 * 
	 * @param position
	 *            the position of the new node
	 */
	public Node addNode(Point2D position);

	/**
	 * Changes the position of the given node to <code>position</code>.
	 * 
	 * @param node
	 *            the node to change
	 * @param position
	 *            the position to set
	 */
	public void alterNodePosition(Node node, Point2D position);

	/**
	 * Removes the given node from the graph. This also removes all attached
	 * edges as well.
	 * 
	 * @param node
	 *            the node to remove
	 */
	public void removeNode(Node node);

	/*
	 * EDGES
	 * 
	 */

	/**
	 * Adds a edge between the given start and end node to the graph.
	 * 
	 * @param start
	 *            the start node
	 * @param end
	 *            the end node
	 */
	public Edge addEdge(Node start, Node end);

	/**
	 * Adds a edge between the given start and end node to the graph, setting
	 * it's weight to <code>weight</code>.
	 * 
	 * @param start
	 *            the start node
	 * @param end
	 *            the end node
	 * @param weight
	 *            the weight for the new edge
	 */
	public Edge addEdge(Node start, Node end, DataType weight);

	/**
	 * Changes the weight of the given edge to <code>weight</code>.
	 * 
	 * @param edge
	 *            the edge to change
	 * @param weight
	 *            the weight to set
	 */
	public void alterEdgeWeight(Edge edge, DataType weight);

	/**
	 * Removes the given edge from the graph.
	 * 
	 * @param edge
	 *            the edge to remove
	 */
	public void removeEdge(Edge edge);

	/**
	 * Gets the undo manager to provide undo redo actions.
	 * 
	 * @return The undo manager.
	 */
	public UndoManager getUndoManager();

	/**
	 * Sets the position of a node before dragging to remember for undo/redo.
	 * 
	 * @param previousPosition
	 *            the previousPosition to set
	 */
	public void setPreviousPosition(Point2D previousPosition);

	/**
	 * Method which is called when a dragged node has reached its actual
	 * position.
	 * 
	 * @param node The node which was dragged.
	 * @param point The final position.
	 */
	public void setFinalNodePosition(Node node, Point2D point);

}
