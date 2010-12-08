package org.jalgo.module.bfsdfs.algorithms;

import java.awt.Point;
import java.util.*;
import java.util.logging.Logger;

import org.jalgo.module.bfsdfs.algorithms.stack.NodeQueueStack;
import org.jalgo.module.bfsdfs.algorithms.stack.NodeStatus;
import org.jalgo.module.bfsdfs.algorithms.stack.StackObserver;
import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.graph.*;
import org.jalgo.module.bfsdfs.undo.*;

/**
 * Abstraction for algorithms working on a graph. <code>Algo</code> takes a graph as input and
 * generates as output:
 * <ul>
 * <li>a tree containing the graph's nodes; two tree nodes are connected only when
 * the equivalent graph nodes are connected</li>
 * <li>a stack of graph nodes. The construction of this stack must be implemented
 * by the subclasses of <code>Algo</code></li>
 * </ul>
 *
 * @author Thomas G&ouml;rres
 *
 */
public abstract class Algo extends Undoable implements GraphObserver {	
	/** for reading the graph the algorithm works on */
	protected GraphController graph;
	
	/** Tree the algorithm generates */
	private AlgoGraph tree;
	
	/** Stack the algorithm generates */
	private NodeQueueStack stack;
	
	/** Set <code>true</code> if the algorithm is running */
	private boolean isRunning;
	
	/** Set <code>true</code> if the algorithm has finished */
	private boolean isFinished;
	
	/** Set <code>true</code> if the default successor's order should
	 * be shuffled to simulate nondeterminism */
	private boolean shuffleDefaultSuccessors;
	
	private Logger logger;
	
	/**
	 * Remembers the status for all nodes already found
	 */
	private Map<Integer, NodeStatus> statusPerNode;
	
	/**
	 * The current waiting node. It is difficult to read it
	 * from the stack every time it is needed, so it's stored here.
	 */
	private int currentNode;
	
	/**
	 * The current start node. It is difficult to read it
	 * from the stack every time it is needed.
	 */
	private int startNode;
	
	/**
	 * 
	 * @param graphController the graph controller that holds the graph this algorithm is working on
	 */
	public Algo(GraphController graphController) {
		logger = Logger.getLogger(this.getClass().getName());
		
		// store graph and observe it
		this.graph = graphController;
		graphController.addGraphObserver(this);
		
		// create stack and tree
		stack = new NodeQueueStack();		
		tree = new AlgoGraph();
		
		statusPerNode = new HashMap<Integer, NodeStatus>();

		// set default start node
		setDefaultStartNode();
		
		isRunning = false;
		isFinished = false;
		shuffleDefaultSuccessors = false;
	}
	
	/**
	 * Overrides the start node. The start node's successors are the first ones
	 * to be put on the stack. Calling this method has no effect if the algorithm is running
	 * or finished
	 * @param newStartNode index of the new start node
	 * @throws NoSuchElementException thrown if no node exists with the specified index.
	 * In this case, the current start node is kept.
	 */
	public void setStartNode(int newStartNode) throws NoSuchElementException {
		if (newStartNode == startNode) return;
		
		if (isFinished) {
			logger.info("Start node will not be changed, algorithm is finished.");
			return;
		}
		
		// assert that the specified node exists
		if (!graph.getNodes().contains(newStartNode)) {
			logger.severe("Start node cannot be changed, specified node "+newStartNode+" not found.");
			throw new NoSuchElementException("Graph does not contain node "+newStartNode);
		}

		// store the new start node
		this.startNode = newStartNode;
		
		// set start node as current algo node
		setCurrentNode(startNode);
	}
	
	/**
	 * Returns the status of each node already found
	 * @return
	 */
	protected Map<Integer, NodeStatus> getStatusPerNode() {
		return statusPerNode;
	}
	
	
	/**
	 * Sets the node with the smallest index as start node. If the graph is empty,
	 * the start node is left unchanged.
	 */
	private void setDefaultStartNode() {		
		// get all nodes
		Collection<Integer> allNodes = graph.getNodes();
		
		// return if the graph is empty
		if (allNodes.isEmpty())
			return;
		
		// find node with the smallest index
		int smallestYet = Integer.MAX_VALUE;
		for (int node: allNodes)
			if (node < smallestYet)
				smallestYet = node;

		// set that node as start node
		setStartNode(smallestYet);		
	}
	
	/**
	 * Overrides the order in which the successors of the current node will be handled by <code>Algo</code>.
	 * This method has no effect if the algorithm is running. This method replaces all {@linkplain NodeStatus untouched}
	 * nodes on the stack with the specified ones.
	 * @param successors indices of all direct successor nodes of the current node, in the desired order. Node
	 * indices that do not belong to a direct successor are ignored. If <code>null</code>, the current successor order
	 * is kept.
	 * @throws IllegalArgumentException thrown if the argument does not contain the indices of all direct successor
	 * nodes. In this case, the current successor order is kept. <em>Not</em> thrown if the argument is <code>null</code>!
	 */
	public void setSuccessorOrder(List<Integer> successors) throws IllegalArgumentException {
		if (!isRunning) return;
		if (!successors.containsAll(stack.getUntouched()) || !stack.getUntouched().containsAll(successors))
			throw new IllegalArgumentException("Specified successors "+successors+" do not match current successors "+stack.getUntouched()+".");
		logger.info("Successors set to "+successors+" (was "+stack.getUntouched()+").");
		stack.replaceUntouched(successors);
		chooseNextNode();
	}
	
	/**
	 * Performs an algorithm step. The step is responsible for
	 * <ul>
	 * <li>modifying the tree</li>
	 * <li>modifying the stack</li>
	 * <li>starting, finishing and resetting the algorithm</li>
	 * </ul>
	 */
	abstract public void step();
	
	/**
	 * Toggles the nondeterminism on or off. If it's on, this algorithm chooses the
	 * order of next nodes randomly
	 * @return <code>true</code> if the nondeterminism is toggled on
	 */
	public boolean toggleNondeterminism() {
		return shuffleDefaultSuccessors = !shuffleDefaultSuccessors;
	}
	
	/**
	 * Returns the start node
	 * @return index of the start node
	 */
	public int getStartNode() {
		return startNode;
	}
	
	/**
	 * Calculates the distance from the start node to the specified node
	 * @return the node's distance from the start node or {@link Integer#MAX_VALUE}
	 * if the node is not contained in the tree yet
	 */
	protected int calculateDistance(int node) {
		if (!tree.getNodesAsInteger().contains(node))
			return Integer.MAX_VALUE;
		
		// start node always has distance 0 from itself
		if (startNode == node)
			return 0;
		
		// get preceding nodes
		List<Integer> predecessors = graph.getPredecessors(node);
		
		// nodes that are not the start node and have no predecessors
		// cannot be reached - their distance is set infinite
		if (predecessors.isEmpty())
			return Integer.MAX_VALUE;
		
		// get distance of one predecessor. In the tree generated by an algorithm,
		// a node only has one predecessor. This assumption is not critical to the
		// module, so other predecessors are just ignored.
		int predecessorDistance = Integer.MAX_VALUE;
		for (int pre: predecessors) {
			predecessorDistance = tree.getNodeDistance(pre);
			if (startNode == pre) {
				predecessorDistance = 0;
				break;
			} else if (Integer.MAX_VALUE != predecessorDistance)
				break;
		}
		
		// MAX_VALUE is used as infinite, so no distance can be greater than MAX_VALUE
		if (Integer.MAX_VALUE == predecessorDistance)
			return Integer.MAX_VALUE;
		 
		return predecessorDistance + 1;
	}
	
	/**
	 * Returns the tree generated by this algorithm. The tree may not be complete if the algorithm
	 * has not yet finished.
	 * @return tree generated by this algorithm
	 */
	public AlgoGraph getTree() {
		return tree;
	}
	
	/**
	 * Returns the node stack generated by this algorithm. The stack may change after each step.
	 * @return node stack generated by this algorithm
	 */
	public NodeQueueStack getStack() {
		return stack;
	}
	
	/**
	 * Returns the node this algorithm is currently working on. That is the last
	 * waiting node on the stack.
	 * @return index of the node this algorithm is currently working on
	 */
	protected int getCurrentNode() {
		return currentNode;
	}
	
	/**
	 * Adds the specified observer to the node queue stack's observers 
	 * @param observer
	 */
	public void addStackObserver(StackObserver observer) {
		stack.addStackObserver(observer);
	}
	
	/**
	 * Removes the specified observer from the node queue stack's observers
	 * @param observer
	 */
	public void removeStackObserver(StackObserver observer) {
		stack.removeStackObserver(observer);
	}
	
	/**
	 * Adds the specified observer to the tree's observers
	 * @param observer
	 */
	public void addTreeObserver(AlgoGraphObserver observer) {
		tree.addAlgoGraphObserver(observer);
	}
	
	/**
	 * Removes the specified observer from the tree's observers
	 * @param observer
	 */
	public void removeTreeObserver(AlgoGraphObserver observer) {
		tree.removeAlgoGraphObserver(observer);
	}
	
	/**
	 * Indicates whether this algorithm is running
	 * @return <code>true</code> if this algorithm started and did not finish yet
	 */
	public boolean isRunning() {
		return isRunning;
	}
	
	/**
	 * Indicates whether this algorithm has finished
	 * @return <code>true</code> if this algorithm has finished
	 */
	public boolean isFinished() {
		return isFinished;
	}
	
	/**
	 * Sets this algorithm running and adds the start node to stack and tree. Calling this
	 * method has no effect if the algorithm is already running.
	 */
	protected void start() {
		if (isRunning) return;
		isFinished = false;
		isRunning = true;
		stack.addNode(startNode);
	}
	
	/**
	 * Sets this algorithm finished and not running.
	 */
	protected void finish() {
		isFinished = true;
		isRunning = false;
	}
	
	/**
	 * Sets this algorithm not finished and not running.
	 */
	protected void reset() {
		isRunning = false;
		isFinished = false;
		stack.setNodeUntouched(startNode);
		stack.removeTopQueue();
		//stack.addNode(startNode);
	}
	
	/**
	 * Sets the node the algorithm should work on
	 * @param currentNode the new current node
	 */
	protected void setCurrentNode(int currentNode) {
		this.currentNode = currentNode;
	}
	
	/**
	 * Resets the algorithm when an edge was added
	 */
	public void onEdgeAdded(int startNode, int endNode) {
		undoAll();
	}

	/**
	 * Resets the algorithm when an edge was removed
	 */
	public void onEdgeRemoved(int startNode, int endNode) {
		undoAll();
	}

	/**
	 * Resets the algorithm when the graph was reloaded
	 */
	public void onGraphLoaded() {
		undoAll();
		setDefaultStartNode();
	}

	/**
	 * Resets the algorithm when a node was added
	 */
	public void onNodeAdded(int node, Point pos) {
		undoAll();
	}

	/**
	 * Not implemented
	 */
	public void onNodeMoved(int node, Point pos) {}

	/**
	 * Resets the algorithm when a node was removed. If the
	 * start node was removed, a new start node is chosen.
	 */
	public void onNodeRemoved(int node) {
		undoAll();
		setDefaultStartNode();
	}

	/**
	 * Resets the algorithm when a node was changed
	 */
	public void onNodeChanged(int oldNodeId, int newNodeId) {
		undoAll();
	}

	/**
	 * Resets the algorithm when an edge was changed
	 */
	public void onEdgeChanged(int oldStartNode, int oldEndNode,
			int newStartNode, int newEndNode) {
		undoAll();
	}
	
	/**
	 * Reads the successors of the current node from the graph. Successors
	 * which are already in the tree. The successors never include the current node itself.
	 * @return indices of the current node's successors that are not yet in
	 * the tree
	 */
	protected List<Integer> getDefaultSuccessors() {
		// get direct successors from the graph
		List<Integer> successors = graph.getSuccessors(currentNode);
		
		// filter successors: ignore nodes already in the tree
		Collection<Integer> nodesToRemove = tree.getNodesAsInteger();
		nodesToRemove.removeAll(stack.getUntouched());
		successors.removeAll(nodesToRemove);
		
		if (shuffleDefaultSuccessors)
			Collections.shuffle(successors);
		
		return successors;
	}
	
	/**
	 * Sets a new "current node". This method chooses the next waiting
	 * node from the topmost stack line. If no node is waiting, the next untouched node is chosen.
	 * If no waiting and no untouched nodes are in the topmost stack line, the
	 * current node is chosen.
	 */
	protected void chooseNextNode() {
		// choose current node if the stack is empty
		if (getStack().isEmpty())
			return;
		
		// choose next untouched node if there is one
		if (!getStack().getUntouched().isEmpty()) {
			setCurrentNode(getStack().getUntouched().get(0));
			return;
		}
		
		// choose next waiting node if there is one
		if (!getStack().getWaiting().isEmpty()) {
			int firstWaiting = getStack().getWaiting().get(0);
			if (getCurrentNode() != firstWaiting)
			setCurrentNode(firstWaiting);
			return;
		}
	}
}
