package org.jalgo.module.bfsdfs.graph;
/**
 * Observer to observe {@link AlgoGraph}.
 * @author Johannes Siegert
 *
 */
public interface AlgoGraphObserver extends GraphObserver {
	/**
	 * Is called if a node changed its distance to the start node.
	 * @param node identifier of the node which was changed its distance
	 * @param distance the new distance of the node
	 */
	public void onDistanceChanged(int node, int distance);
}
