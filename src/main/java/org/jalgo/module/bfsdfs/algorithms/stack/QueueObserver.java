package org.jalgo.module.bfsdfs.algorithms.stack;

import java.util.*;

/**
 * Interface for observers of an algorithm's {@link NodeQueueStack}
 * @author Thomas G&ouml;rres
 *
 */
interface QueueObserver {	
	/**
	 * Invoked when nodes were added to the topmost queue 
	 * @param nodes indices of the added nodes
	 */
	public void onNodesAdded(List<Integer> nodes);
	
	/**
	 * Invoked when the status of a node changed
	 * @param node index of the affected node
	 * @param newStatus the node's new status
	 */
	public void onStatusChanged(int node, NodeStatus newStatus);
	
	/**
	 * Invoked when the untouched nodes where replaced
	 * @param oldNodes indices of the replaced untouched nodes
	 * @param newNodes indices of the new untouched nodes
	 */
	public void onUntouchedReplaced(List<Integer> oldNodes, List<Integer> newNodes);
}
