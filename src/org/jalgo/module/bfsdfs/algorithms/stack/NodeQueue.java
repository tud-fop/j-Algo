package org.jalgo.module.bfsdfs.algorithms.stack;

import java.util.*;

/**
 * A queue of nodes. Nodes are represented by an unique integer index. A queue cannot contain the same node twice.
 * <p>
 * Each <code>NodeQueue</code> has an "owner", a node it belongs to. The queue cannot contain nodes with the same
 * index as its owner.
 * <p> 
 * Each node is in one of three states:
 * <ul>
 * <li>untouched: {@link NodeStatus#UNTOUCHED}</li>
 * <li>waiting: {@link NodeStatus#WAITING}</li>
 * <li>finished: {@link NodeStatus#FINISHED}</li>
 * </ul>
 * @author Thomas G&ouml;rres
 *
 */
class NodeQueue {
	/** Default status for newly added nodes */
	public static final NodeStatus DEFAULT_STATUS = NodeStatus.UNTOUCHED;
	
	private List<Node> nodes;
	private int owner;
	private Collection<QueueObserver> observers;
	
	/**
	 * Initializes an empty <code>NodeQueue</code> with the specified owner
	 * @param owner index of the node that owns this queue
	 */
	public NodeQueue(int owner) {
		nodes = new LinkedList<Node>();
		observers = new ArrayList<QueueObserver>();
		this.owner = owner;
	}
	
	/**
	 * Initializes an empty <code>NodeQueue</code> with the specified owner and adds
	 * @param owner index of the node that owns this queue
	 * @param observers observers that should be added to this <code>NodeQueue</code>
	 * the specified observers
	 */
	public NodeQueue(int owner, Collection<? extends QueueObserver> observers) {
		this(owner);
		this.observers.addAll(observers);
	}
	
	/**
	 * Checks if the specified node is contained in this queue
	 * @param nodeIndex index of the searched node
	 * @return <code>true</code> if this queue contains that node
	 */
	public boolean containsNode(int nodeIndex) {
		return null != findNode(nodeIndex);
	}
	
	/**
	 * Returns the number of nodes in this queue
	 * @return number of nodes in this queue
	 */
	public int size() {
		return nodes.size();
	}
	
	/**
	 * Adds the specified observer
	 * @param observer
	 */
	public void addQueueObserver(QueueObserver observer) {
		if (null == observer) return;
		observers.add(observer);
	}
	
	/**
	 * Removes the specified observer
	 * @param observer
	 */
	public void removeQueueObserver(QueueObserver observer) {
		if (null == observer) return;
		observers.remove(observer);
	}
	
	/**
	 * Returns the status of the specified node 
	 * @param node
	 * @return the node's status or <code>null</code>, if the node
	 * is not contained in the topmost queue
	 */
	public NodeStatus getNodeStatus(int node) {
		Node targetNode = findNode(node);
		if (null == targetNode) return null;
		else return targetNode.status;
	}
	
	
	/**
	 * Returns all untouched nodes
	 * @return indices of all untouched nodes, ordered as they are in the queue
	 */
	public List<Integer>getUntouched() {
		return nodesToInts(findNodes(NodeStatus.UNTOUCHED));
	}
	
	/**
	 * Returns all nodes in this queue
	 * @return indices of all nodes, ordered as they are in the queue
	 */
	public List<Integer>getAllNodes() {
		return nodesToInts(nodes);
	}
	
	
	/**
	 * Returns all waiting nodes
	 * @return indices of all waiting nodes, ordered as they are in the queue
	 */
	public List<Integer>getWaiting() {
		return nodesToInts(findNodes(NodeStatus.WAITING));
	}
	
	
	/**
	 * Returns all finished nodes
	 * @return indices of all finished nodes, ordered as they are in the queue
	 */
	public List<Integer>getFinished() {
		return nodesToInts(findNodes(NodeStatus.FINISHED));
	}
	
	
	/**
	 * Sets a new status for the specified node
	 * @param nodeIndex index of the node. If the queue does not contain the specified node,
	 * calling this method has no effect.
	 * @param status new status for that node. If <code>null</code>, the current status is kept.
	 */
	private void setNodeStatus(int nodeIndex, NodeStatus status) {
		if (null == status) return;
		
		Node targetNode = findNode(nodeIndex);		
		if (null == targetNode) return;
		
		targetNode.status = status;
		
		// notify observers
		for (QueueObserver o: observers)
			o.onStatusChanged(nodeIndex, targetNode.status);
	}
	
	/**
	 * Sets the specified node <em>finished</em>. This sets the node's status to
	 * {@link NodeStatus#FINISHED}. If the queue does not contain the specified node,
	 * calling this method has no effect.
	 * @param nodeIndex index of the node
	 */
	public void setNodeFinished(int nodeIndex) {
		setNodeStatus(nodeIndex, NodeStatus.FINISHED);
	}
	
	/**
	 * Sets the specified node <em>waiting</em>. This sets the node's status to
	 * {@link NodeStatus#WAITING}. If the queue does not contain the specified node,
	 * calling this method has no effect.
	 * @param nodeIndex index of the node
	 */
	public void setNodeWaiting(int nodeIndex) {
		setNodeStatus(nodeIndex, NodeStatus.WAITING);
	}
	
	/**
	 * Sets the specified node <em>untouched</em>. This sets the node's status to
	 * {@link NodeStatus#UNTOUCHED}. If the queue does not contain the specified node,
	 * calling this method has no effect.
	 * @param nodeIndex index of the node
	 */
	public void setNodeUntouched(int nodeIndex) {
		setNodeStatus(nodeIndex, NodeStatus.UNTOUCHED);
	}
	
	public void nodeStatusDown(List<Integer> nodeIndices) {
		if (null == nodeIndices) return;
		for (int index: nodeIndices)
			nodeStatusDown(index);
	}
	
	/**
	 * Changes the status of the specified node, depending on its current status.
	 * <ul>
	 * <li>if the node is <em>finished</em>, it is set <em>waiting</em></li>
	 * <li>if the node is <em>waiting</em>, it is set <em>untouched</em></li>
	 * <li>if the node is <em>untouched</em>, its status is not changed</li>
	 * </ul>
	 * If the queue does not contain the specified node, calling this method has no
	 * effect.
	 * @param nodeIndex index of the node
	 */
	public void nodeStatusDown(int nodeIndex) {
		Node targetNode = findNode(nodeIndex);
		if (null == targetNode) return;
		
		boolean statusChanged = false;
		
		switch(targetNode.status) {
		case FINISHED:
			targetNode.status = NodeStatus.WAITING;
			statusChanged = true;
			break;
		case WAITING:
			targetNode.status = NodeStatus.UNTOUCHED;
			statusChanged = true;
		}
		
		// notify observers
		if (statusChanged)
			for (QueueObserver o: observers)
				o.onStatusChanged(nodeIndex, targetNode.status);
	}
	
	public void nodeStatusUp(List<Integer> nodeIndices) {
		if (null == nodeIndices) return;
		for (int index: nodeIndices)
			nodeStatusUp(index);
	}
	
	/**
	 * Changes the status of the specified node, depending on its current status.
	 * <ul>
	 * <li>if the node is <em>untouched</em>, it is set <em>waiting</em></li>
	 * <li>if the node is <em>waiting</em>, it is set <em>finished</em></li>
	 * <li>if the node is <em>finished</em>, its status is not changed</li>
	 * </ul>
	 * If the queue does not contain the specified node, calling this method has no
	 * effect.
	 * @param nodeIndex index of the node
	 */
	public void nodeStatusUp(int nodeIndex) {
		Node targetNode = findNode(nodeIndex);
		if (null == targetNode) return;
		
		boolean statusChanged = false;
				
		switch(targetNode.status) {
		case UNTOUCHED:
			targetNode.status = NodeStatus.WAITING;
			statusChanged = true;
			break;
		case WAITING:
			targetNode.status = NodeStatus.FINISHED;
			statusChanged = true;
			break;
		}
		
		// notify observers
		if (statusChanged)
			for (QueueObserver o: observers)
				o.onStatusChanged(nodeIndex, targetNode.status);
	}
	
	
	/**
	 * Deletes all untouched nodes and adds the specified nodes to the queue. If the specified node list is
	 * <code>null</code> or empty, calling this method has no effect
	 * @param replacement indices of the replacing nodes
	 */
	public void replaceUntouched(List<Integer> replacement) {
		// remove untouched nodes
		List<Node> nodesToRemove = new LinkedList<Node>();
		for (Node node: nodes)
			for (int targetNode: getUntouched())
				if (node.index == targetNode)
					nodesToRemove.add(node);
		List<Integer> oldUntouched = getUntouched();
		nodes.removeAll(nodesToRemove);

		// add replacement
		addNodesSilently(replacement);
		
		// notify observers
		for (QueueObserver o: observers)
			o.onUntouchedReplaced(oldUntouched, replacement);
	}
	

	/**
	 * Adds a node to the queue.
	 * <p>
	 * This method ignores nodes that:
	 * <ul>
	 * <li>already are in the queue</li>
	 * <li>have the same index as the queue owner</li>
	 * </ul>
	 * @param nodeIndex index of the node to add
	 * @return <code>true</code> if the stack changed in result of this call
	 */
	public boolean addNode(int nodeIndex) {
		return addNodes(Arrays.asList(nodeIndex));
	}
	
	
	/**
	 * Adds several nodes to the queue.
	 * <p>
	 * This method ignores nodes that:
	 * <ul>
	 * <li>already are in the queue</li>
	 * <li>have the same index as the queue owner</li>
	 * </ul>
	 * <p>
	 * If the specified node list is
	 * <code>null</code> or empty, calling this method has no effect.
	 * @param nodeIndices indices of the nodes to add
	 * @return <code>true</code> if the stack changed in result of this call
	 */
	public boolean addNodes(List<Integer> nodeIndices) {
		if (!addNodesSilently(nodeIndices))
			return false;

		// notify observers
		for (QueueObserver o: observers)
			o.onNodesAdded(nodeIndices);
		
		return true;
	}
	
	/**
	 * Adds several nodes to the queue without notifying the observers
	 * <p>
	 * This method ignores nodes that:
	 * <ul>
	 * <li>already are in the queue</li>
	 * <li>have the same index as the queue owner</li>
	 * </ul>
	 * <p>
	 * If the specified node list is
	 * <code>null</code> or empty, calling this method has no effect.
	 * @param nodeIndices indices of the nodes to add
	 * @return <code>true</code> if the stack changed in result of this call
	 */
	protected boolean addNodesSilently(List<Integer> nodeIndices) {
		if (null == nodeIndices || nodeIndices.isEmpty())
			return false;
		
		for (int index: nodeIndices) {
			if (index == owner) continue;
			Node n = new Node(index);
			if (nodes.contains(n)) continue;
			nodes.add(n);
		}
		
		return true;
	}
	
	/**
	 * Returns the owner of this queue
	 * @return the owning node's index
	 */
	public int getOwner() {
		return owner;
	}
	
	
	/**
	 * Finds all nodes with the specified status
	 * @param wantedStatus the status to search for
	 * @return all nodes with that status
	 */
	protected List<Node> findNodes(NodeStatus wantedStatus) {
		// guard clause for null argument
		if (null == wantedStatus) return Collections.emptyList();
		
		// search nodes
		List<Node> result = new LinkedList<Node>();
		for (Node n: nodes) {
			if (wantedStatus.equals(n.status))
				result.add(n);
		}
		
		return result;
	}
	
	
	/**
	 * Finds all nodes with the specified index
	 * @param wantedID the index to search for
	 * @return node with that index
	 */
	protected Node findNode(int wantedIndex) {
		for (Node n: nodes)
			if (wantedIndex == n.index)
				return n;
		
		return null;
	}
	
	
	/**
	 * Creates a list of the specified nodes' indices
	 * @param nodeList a list of nodes
	 * @return that nodes' indices as unmodifiable list
	 */
	protected List<Integer> nodesToInts(List<Node> nodeList) {
		if (null == nodeList) return null;
		
		List<Integer> intList = new LinkedList<Integer>();
		for (Node n: nodeList)
			intList.add(n.index);
		return Collections.unmodifiableList(intList);
	}
	
	/**
	 * Returns a human-readable form of this <code>NodeQueue</code>s
	 */
	@Override public String toString() {
		List<String> nodeStrings = new LinkedList<String>();
		for (Node n: nodes)	nodeStrings.add(n.toString());
		return owner+":"+nodeStrings.toString();
	}
	
	/**
	 * Checks if all nodes are finished
	 * @return <code>true</code> if the queue contains only finished
	 * nodes
	 */
	public boolean isFinished() {
		for (Node n: nodes)
			if (n.status != NodeStatus.FINISHED)
				return false;
		return true;
	}
	
	/**
	 * Simple data class for a node
	 * @author Thomas G&ouml;rres
	 *
	 */
	private class Node {
		public NodeStatus status;
		public int index;
		
		/**
		 * Initializes a node with the specified index. Default status: untouched.
		 * @param id index of the node
		 */
		public Node(int index) {
			status = DEFAULT_STATUS;
			this.index = index;
		}
		
		/**
		 * A node equals another node, if their indices are equal
		 */
		@Override public boolean equals(Object o) {
			if (!(o instanceof Node)) return false;
			Node n = (Node)o;
			return n.index == index;
		}
		
		/**
		 * Returns a human-readable form of this node
		 */
		@Override public String toString() {
			StringBuffer buf = new StringBuffer(String.valueOf(index));
			switch(status) {
			case UNTOUCHED:
				buf.append(":u");
				break;
			case WAITING:
				buf.append(":w");
				break;
			case FINISHED:
				buf.append(":f");
			}
			return buf.toString();
		}
	}
}
