package org.jalgo.module.bfsdfs.graph;

import java.awt.Point;

/**
 * Observer interface to observe {@link ObservableGraph}.
 * @author Johannes Siegert
 *
 */
public interface GraphObserver {
	/**
	 * Is called if a node was added.
	 * @param node identifier of the node which was added
	 * @param pos position of the node which was added
	 */
	public void onNodeAdded(int node,Point pos);
	
	/**
	 * Is called if a node was removed.
	 * @param node identifier of the node which was removed
	 */
	public void onNodeRemoved(int node);
	
	/**
	 * Is called if an edge was added.
	 * @param startNode identifier of the start node
	 * @param endNode identifier of the end node
	 */
	public void onEdgeAdded(int startNode,int endNode);
	
	/**
	 * Is called if an edge was removed.
	 * @param startNode identifier of the start node
	 * @param endNode identifier of the end node
	 */
	public void onEdgeRemoved(int startNode,int endNode);
	
	/**
	 * Is called if a node was changed it's position.
	 * @param node identifier of the node which was moved
	 * @param pos new position of that node
	 */
	public void onNodeMoved(int node,Point pos);
	
	/**
	 * Is called if node identifier is changed.
	 * @param oldNodeId old identifier of the node
	 * @param newNodeId new identifier of the node
	 */
	public void onNodeChanged(int oldNodeId, int newNodeId);
	
	/**
	 * Is called if an edge changed.  
	 * @param oldStartNode identifier of the old start node
	 * @param oldEndNode identifier of the old end node
	 * @param newStartNode identifier of the new start node
	 * @param newEndNode identifier of the new end node
	 */
	public void onEdgeChanged(int oldStartNode, int oldEndNode, int newStartNode, int newEndNode);
}
