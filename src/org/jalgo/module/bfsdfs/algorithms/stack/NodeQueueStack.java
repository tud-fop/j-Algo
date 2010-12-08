package org.jalgo.module.bfsdfs.algorithms.stack;

import java.util.*;

/**
 * A stack of node queues. Nodes are represented by integer indices.
 * @author Thomas G&ouml;rres
 *
 */
public class NodeQueueStack {
	/** Default queue owner */
	public static final int DEFAULT_OWNER = 0;
	
	private List<NodeQueue> queues;
	private List<StackObserver> observers;
	
	private int currentNode;
	
	/**
	 * Initializes an empty <code>NodeStack</code>
	 */
	public NodeQueueStack() {
		queues = new LinkedList<NodeQueue>();
		observers = new LinkedList<StackObserver>();
	}
	
	/**
	 * Returns a list of all queue owners
	 * @return index of each queue owner, beginning with the bottommost queue
	 */
	protected List<Integer> getQueueOwners() {
		List <Integer> owners = new LinkedList<Integer>();
		for (NodeQueue q: queues) owners.add(q.getOwner());
		return owners;
	}
	
	/**
	 * Changes the status of the specified node, depending on its current status.
	 * <ul>
	 * <li>if the node is <em>finished</em>, it is set <em>waiting</em></li>
	 * <li>if the node is <em>waiting</em>, it is set <em>untouched</em></li>
	 * <li>if the node is <em>untouched</em>, its status is not changed</li>
	 * </ul>
	 * If the stack does not contain the specified node, calling this method has no
	 * effect.
	 * @param nodeIndex index of the node
	 */
	public void nodeStatusDown(int nodeIndex) {
		for (NodeQueue q: queues)
			q.nodeStatusDown(nodeIndex);
		
		this.updateCurrentNode();
	}
	
	public void nodeStatusDown(List<Integer> nodeIndices) {
		if (null == nodeIndices) return;
		for (NodeQueue q: queues)
			q.nodeStatusDown(nodeIndices);
		
		updateCurrentNode();
	}
	
	/**
	 * Changes the status of the specified node, depending on its current status.
	 * <ul>
	 * <li>if the node is <em>untouched</em>, it is set <em>waiting</em></li>
	 * <li>if the node is <em>waiting</em>, it is set <em>finished</em></li>
	 * <li>if the node is <em>finished</em>, its status is not changed</li>
	 * </ul>
	 * If the stack does not contain the specified node, calling this method has no
	 * effect.
	 * @param nodeIndex index of the node
	 */
	public void nodeStatusUp(int nodeIndex) {
		for (NodeQueue q: queues)
			q.nodeStatusUp(nodeIndex);

		// notify observers if all nodes are finished now
		if (1 == queues.size() && isFinished())
			for (StackObserver o: observers)
				o.onAllNodesFinished();
		
		this.updateCurrentNode();
	}
	
	public void nodeStatusUp(List<Integer> nodeIndices) {
		if (null == nodeIndices) return;
		for (NodeQueue q: queues)
			q.nodeStatusUp(nodeIndices);
		
		this.updateCurrentNode();
	}
	
	/**
	 * Returns the owner of the topmost queue
	 * @return index of the of the topmost queue. If no queue is in the stack,
	 * <code>-1</code> is returned.
	 */
	public int getOwner() {
		NodeQueue firstQueue = getTopQueue();
		if (null == firstQueue) return -1;
		
		return firstQueue.getOwner();
	}
	
	/**
	 * Returns the status of the specified node 
	 * @param node
	 * @return the node's status or <code>null</code>, if the node
	 * is not contained in the topmost queue
	 */
	public NodeStatus getNodeStatus(int node) {
		if (isEmpty()) return null;
		return getTopQueue().getNodeStatus(node);
	}
	
	/**
	 * Returns the topmost queue
	 * @return the topmost queue. If no queue is in the stack, <code>null</code>
	 * is returned.
	 */
	private NodeQueue getTopQueue() {
		if (queues.isEmpty()) return null;
		return (NodeQueue)(queues.get(queues.size()-1));
	}
	
	/**
	 * Returns all waiting nodes of the topmost queue
	 * @return indices of all waiting nodes, ordered as they are in the queue
	 */
	public List<Integer> getWaiting() {
		if (queues.isEmpty()) return Collections.emptyList();
		return getTopQueue().getWaiting();
	}
	
	/**
	 * Returns all finished nodes of the topmost queue
	 * @return indices of all finished nodes, ordered as they are in the queue
	 */
	public List<Integer> getFinished() {
		if (queues.isEmpty()) return Collections.emptyList();
		return getTopQueue().getFinished();
	}
	
	/**
	 * Checks if all nodes of the topmost queue are finished
	 * @return <code>true</code> if the topmost queue contains only finished
	 * nodes
	 */
	public boolean isFinished() {
		if (queues.isEmpty()) return false;
		return getTopQueue().isFinished();
	}
	
	/**
	 * Checks if all nodes of <em>every</em> queue in the stack are finished
	 * @return <code>true</code> if the stack contains only finished queues
	 * @see #isFinished()
	 */
	public boolean isAllFinished() {
		if (queues.isEmpty()) return false;
		
		for (NodeQueue q: queues)
			if (!q.isFinished()) return false;
		return true;
	}
	
	/**
	 * Removes the top queue and sets its owner finished
	 */
	public void removeTopQueueAndFinishOwner() {
		if (queues.isEmpty()) return;
		
		int finishedOwner = getOwner();
		removeTopQueue();
		setNodeFinished(finishedOwner);
	}
	
	/**
	 * Returns all untouched nodes of the topmost queue
	 * @return indices of all waiting nodes, ordered as they are in the queue
	 */
	public List<Integer> getUntouched() {
		if (queues.isEmpty()) return Collections.emptyList();
		return getTopQueue().getUntouched();
	}
	
	/**
	 * Returns the nodes of <em>all</em> queues in this stack
	 * @return indices of all nodes in the stack
	 */
	public Collection<Integer> getAllNodes() {
		Collection<Integer> nodes = new LinkedList<Integer>();
		
		for (NodeQueue q: queues)
			nodes.addAll(q.getAllNodes());
		
		return nodes;
	}
	
	/**
	 * Returns the untouched nodes of <em>all</em> queues in this stack
	 * @return indices of all nodes in the stack
	 */
	public Collection<Integer> getAllUntouched() {
		Collection<Integer> nodes = new LinkedList<Integer>();
		
		for (NodeQueue q: queues)
			nodes.addAll(q.getUntouched());
		
		return nodes;
	}
	
	/**
	 * Returns the waiting nodes of <em>all</em> queues in this stack
	 * @return indices of all nodes in the stack
	 */
	public Collection<Integer> getAllWaiting() {
		Collection<Integer> nodes = new LinkedList<Integer>();
		
		for (NodeQueue q: queues)
			nodes.addAll(q.getWaiting());
		
		return nodes;
	}
	
	/**
	 * Returns the finished nodes of <em>all</em> queues in this stack
	 * @return indices of all nodes in the stack
	 */
	public Collection<Integer> getAllFinished() {
		Collection<Integer> nodes = new LinkedList<Integer>();
		
		for (NodeQueue q: queues)
			nodes.addAll(q.getFinished());
		
		return nodes;
	}
	
	/**
	 * Puts a queue on the top of the stack and fills it with the specified nodes
	 * @param owner index of the node that owns the new queue. If a queue with this
	 * owner already exists, calling this method has no effect
	 * @param nodeIndices indices of the nodes which the new queue should contain
	 * @return <code>true</code> if the stack changed in result of this call
	 */
	public boolean addQueue(int owner, List<Integer> nodeIndices) {
		if (getQueueOwners().contains(owner)) return false;
		
		// add queue
		queues.add(new NodeQueue(owner, observers));
		
		// notify observers
		if (1 == queues.size())
			for (StackObserver o: observers)
				o.onFirstQueueAdded(owner);
		else
			for (StackObserver o: observers)
				o.onQueueAdded(owner);
		
		// add nodes
		addNodes(nodeIndices);
		
		return true;
	}
	
	/**
	 * Puts an empty queue on the top of the stack
	 * @param owner index of the node that owns the new queue. If a queue with this
	 * owner already exists, calling this method has no effect
	 * @return <code>true</code> if the stack changed in result of this call
	 */
	public boolean addQueue(int owner) {
		return addQueue(owner, null);
	}
	
	/**
	 * Adds a node to the topmost queue. If there is no queue in the stack,
	 * a queue is created first, with {@link NodeQueue#DEFAULT_OWNER} as owner.
	 * @param node the node's index
	 * @return <code>true</code> if the stack changed in result of this call
	 * @see NodeQueue#addNode(int)
	 */
	public boolean addNode(int node) {
		// create queue first if necessary
		if (queues.isEmpty()) addQueue(DEFAULT_OWNER);
		
		// add node to topmost queue
		boolean wasAdded = getTopQueue().addNode(node);
		
		if (wasAdded) updateCurrentNode();
		
		return wasAdded;
	}
	
	/**
	 * Adds several nodes to the topmost queue. If there is no queue in the stack,
	 * a queue is created first, with {@link NodeQueue#DEFAULT_OWNER} as owner.
	 * @param nodes the nodes' indices. <code>null</code> or an empty list is ignored
	 * @return <code>true</code> if the stack changed in result of this call
	 * @see NodeQueue#addNode(int)
	 */
	public boolean addNodes(List<Integer> nodes) {
		// ignore null or empty list
		if (null == nodes || nodes.isEmpty()) return false;
		
		// create queue first if necessary
		if (queues.isEmpty()) addQueue(DEFAULT_OWNER);
		
		// add nodes to topmost queue
		boolean wasAdded = getTopQueue().addNodes(nodes);
		
		if (wasAdded && 1 == nodes.size())
			updateCurrentNode();
		
		return wasAdded;
	}

	/**
	 * Sets the specified node <em>untouched</em>. This sets the node's status to
	 * {@link NodeStatus#UNTOUCHED}. If the stack does not contain the specified node,
	 * calling this method has no effect.
	 * @param nodeIndex index of the node
	 */
	public void setNodeUntouched(int node) {
		if (isEmpty()) return;
		for (NodeQueue q: queues)
			q.setNodeUntouched(node);
		
		this.updateCurrentNode();
	}

	/**
	 * Sets the specified node <em>waiting</em>. This sets the node's status to
	 * {@link NodeStatus#WAITING}. If the stack does not contain the specified node,
	 * calling this method has no effect.
	 * @param nodeIndex index of the node
	 */
	public void setNodeWaiting(int node) {
		if (isEmpty()) return;
		for (NodeQueue q: queues)
			q.setNodeWaiting(node);
		
		this.updateCurrentNode();
	}

	/**
	 * Sets the specified node <em>finished</em>. This sets the node's status to
	 * {@link NodeStatus#FINISHED}. If the stack does not contain the specified node,
	 * calling this method has no effect.
	 * @param nodeIndex index of the node
	 */
	public void setNodeFinished(int node) {
		if (isEmpty()) return;
		
		// change status of all affected nodes
		for (NodeQueue q: queues)
			q.setNodeFinished(node);
		
		// notify observers if all nodes are finished now
		if (1 == queues.size() && isFinished())
			for (StackObserver o: observers)
				o.onAllNodesFinished();
		
		this.updateCurrentNode();
	}
	
	/**
	 * Adds the specified observer
	 * @param o
	 */
	public void addStackObserver(StackObserver o) {
		if (null == o) return;

		observers.add(o);

		// Adds this observer to all queues. This way, this class does not have
		// to notify the observers itself after a queue was modified 
		for (NodeQueue q: queues)
			q.addQueueObserver(o);
	}
	
	/**
	 * Removes the specified observer
	 * @param o
	 */
	public void removeStackObserver(StackObserver o) {
		if (null == o) return;
		
		observers.remove(o);

		for (NodeQueue q: queues)
			q.removeQueueObserver(o);
	}
	
	/**
	 * Deletes all untouched nodes of the topmost queue and adds the specified nodes to the queue.
	 * If the specified node list is <code>null</code> or empty, calling this method has no effect
	 * @param replacement indices of the replacing nodes
	 */
	public void replaceUntouched(List<Integer> replacement) {
		if (queues.isEmpty()) return;
		
		// replace untouched nodes of topmost queue
		getTopQueue().replaceUntouched(replacement);
		
		if (null != replacement && 1 == replacement.size())
			updateCurrentNode();
	}
	
	/**
	 * Removes the topmost queue from the stack. If the stack is empty, calling
	 * this method has no effect
	 */
	public void removeTopQueue() {
		if (queues.isEmpty()) return;
		
		// remove topmost queue
		queues.remove(queues.size()-1);
		
		// notify observers
		for (StackObserver o: observers) {
			o.onTopQueueRemoved();
			if (queues.isEmpty())
				o.onAllQueuesRemoved();
		}
	}
	
	/**
	 * Returns a human-readable form of this <code>NodeQueueStack</code>
	 */
	@Override public String toString() {
		StringBuffer buf = new StringBuffer();
		for (NodeQueue q: queues)
			buf.append(q+" ");
		if (0 != buf.length()) buf.deleteCharAt(buf.lastIndexOf(" "));
		return new String(buf);
	}
	
	/**
	 * Checks if the stack is empty
	 * @return <code>true</code> if the stack contains no queues
	 */
	public boolean isEmpty() {
		return queues.isEmpty();
	}
	
	/**
	 * Returns the number of queues in this stack
	 * @return number of queues
	 */
	public int size() {
		return queues.size();
	}
	
	/**
	 * Reads the current node from the stack and notifies the observers
	 * if it changed.
	 * @see #getCurrentNode()
	 */
	protected void updateCurrentNode() {
		int newCurrentNode = currentNode;
		
		// read current node
		if (1 == queues.size() && 1 == queues.get(0).size())
			newCurrentNode = queues.get(0).getAllNodes().get(0);
		else
			for (NodeQueue q: queues)
				if (!q.getWaiting().isEmpty())
					newCurrentNode = q.getWaiting().get(0);

		// change current node
		if (newCurrentNode == currentNode) return;
		currentNode = newCurrentNode;
		
		// notify observers
		for (StackObserver o: observers)
			o.onCurrentNodeChanged(currentNode);
	}
	
	/**
	 * Returns the current node. The current node is...
	 * <ul>
	 * <li>the first node, if there is only one node in the stack</li>
	 * <li>unchanged, if there are more than one but no waiting nodes in the stack</li>
	 * <li>the first waiting node of the topmost queue (which contains waiting nodes)</li>
	 * </ul>
	 * @return index of the current node
	 */
	public int getCurrentNode() {
		return currentNode;
	}
}
