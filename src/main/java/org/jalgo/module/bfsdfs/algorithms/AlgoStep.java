package org.jalgo.module.bfsdfs.algorithms;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jalgo.module.bfsdfs.algorithms.stack.NodeStatus;
import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.undo.Step;

/**
 * Abstraction for steps of algorithms working on a graph. This class takes care of
 * <ul>
 * <li>modifying the tree</li>
 * <li>modifying the stack (implemented by subclasses)</li>
 * <li>starting, finishing and resetting the algorithm</li>
 * </ul>
 * @author Thomas G&ouml;rres
 *
 */
abstract class AlgoStep implements Step {
	/** Log level for the {@link #log} method */
	protected static final Level LOGGER_LEVEL = Level.INFO;
	
	protected Logger logger;
	protected Algo algo;
	protected GraphController graphController;

	/** Nodes added to the tree by {@link execute} **/
	private List<Integer> newTreeNodes;

	/** Edges added to the tree by {@link execute} */
	private Map<Integer, Collection<Integer>> newTreeEdges;
	
	/** Stores the removed finished nodes, if {@link execute} removed a queue */
	private List<Integer> removedFinishedNodes;
	
	/** Set <code>true</code>, if {@link execute} removed the top queue */
	private boolean topQueueRemoved;

	/** Stores the owner of the removed queue if {@link execute} removed a queue */
	private int removedQueueOwner;
	
	/** Value of {@link Algo#isFinished()} before execution */
	private boolean oldIsFinished;

	/** Value of {@link Algo#isRunning()} before execution */
	private boolean oldIsRunning;

	/** Value of {@link Algo#getCurrentNode()} before execution */
	private int oldCurrentNode;
	
	/**
	 * Creates a new <code>AlgoStep</code>
	 * @param algo an <code>Algo</code> which knows the stack and tree this step
	 * will modify
	 * @param graphController a <code>GraphController</code> to read out the graph
	 * this step will work on
	 */
	public AlgoStep(Algo algo, GraphController graphController) {
		logger = Logger.getLogger(getClass().getName());
		
		this.graphController = graphController;
		this.algo = algo;
		
		topQueueRemoved = false;
	}

	/*
	 * (non-Javadoc)
	 * @see org.jalgo.module.bfsdfs.undo.Step#execute()
	 */
	public void execute() {
		oldIsFinished = algo.isFinished();
		oldIsRunning = algo.isRunning();
		oldCurrentNode = algo.getCurrentNode();
		
		if (algo.isFinished()) return;

		if (!algo.isRunning()) algo.start();
		
		undoShowSuccessorsInTree();
		
		if (algo.getStack().isFinished()) {
			removedQueueOwner = algo.getStack().getOwner(); 
			removeSuccessors();
		}
		
		/*
		 * Process new tree nodes
		 */
		else {
			newTreeNodes = getNewTreeNodes();
			
			// modify tree: add new tree nodes
			if (!newTreeNodes.isEmpty()) {
				addTreeNodes(newTreeNodes);
				if (algo.getStartNode() != algo.getCurrentNode())
					addTreeEdges(getNodesPredecessor(), newTreeNodes);
			}
			
			// modify stack: set new tree nodes waiting
			algo.getStack().nodeStatusUp(newTreeNodes);
			addSuccessorsToStack();
			
			// modify status map: set new tree nodes waiting
			for (int node: newTreeNodes)
				algo.getStatusPerNode().put(node, NodeStatus.WAITING);
		}
		
		/*
		 * Continue within the current tree, if it is not finished
		 */
		if (!algo.getStack().isAllFinished() && !algo.getStack().isEmpty()) {
			algo.chooseNextNode();
			showSuccessorsInTree();
		}
		
		/*
		 * Find a new tree or finish algorithm, if the current tree is finished
		 */
		else newTreeOrFinish();
		
		log(algo.getClass().getSimpleName()+" step executed");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.jalgo.module.bfsdfs.undo.Step#undo()
	 */
	public void undo() {
		undoShowSuccessorsInTree();
		
		if (oldIsFinished)
			return;

		
		if (!algo.getStack().isAllFinished() && !algo.getStack().isEmpty())
		undoNewTreeOrFinish();

		if (topQueueRemoved) {
			undoRemoveSuccessors();
			algo.setCurrentNode(oldCurrentNode);
		} else {
			algo.setCurrentNode(oldCurrentNode);
			undoAddSuccessorsToStack();
			
			algo.getStack().nodeStatusDown(newTreeNodes);
			
			undoAddTreeEdges();
			undoAddTreeNodes();

			// update status map
			for (int node: newTreeNodes)
				algo.getStatusPerNode().put(node, NodeStatus.UNTOUCHED);
			
			// re-add untouched nodes so that StackObserver#onNodesAdded(List) is called
			if (mayChooseSuccessorsAgain()) {
				List<Integer> untouched = algo.getStack().getUntouched();
				algo.getStack().replaceUntouched(null);
				algo.getStack().addNodes(untouched);
			}
		}
		
		showSuccessorsInTree();

		if (!oldIsRunning) {
			algo.reset();
			return;
		}
		else
			algo.start();
		
		log(algo.getClass().getSimpleName()+" step undone");
	}
	
	/**
	 * Shows the possible successors of the current node in the tree. This method
	 * adds nodes to the tree, so #undoShowSuccessorsInTree must be called in order
	 * prior to any tree manipulation.
	 */
	private void showSuccessorsInTree() {
		for (int node: algo.getStack().getUntouched())
			if (node != algo.getStartNode() )
				algo.getTree().addNode(node, graphController.getNodePosition(node));		
	}
	
	/**
	 * Decides whether the successor order can be chosen again after an undo operation
	 * @return <code>true</code> if the successor order can be chosen again
	 */
	private boolean mayChooseSuccessorsAgain() {
		if (algo.getStack().getUntouched().isEmpty()) return false;
		List<Integer> graphPreds = graphController.getPredecessors(algo.getStack().getUntouched().get(0));
		Collection<Integer> neighbors = algo.getTree().getSuccessors(graphPreds.get(0));		
		return neighbors.isEmpty();
	}
	
	/**
	 * Removes all untouched nodes except the start node from the tree
	 */
	private void undoShowSuccessorsInTree() {
		for (int node: algo.getStack().getUntouched())
			if (node != algo.getStartNode() && algo.getTree().containsNode(node))
				algo.getTree().removeNode(node);	
	}
	
	/**
	 * Logs the current node, tree nodes and stack
	 * @param title first line of the log entry
	 */
	private void log(String title) {
		StringBuffer buf = new StringBuffer(title);
		buf.append("\n  new tree: "+algo.getTree().getNodesAsInteger()+", "+algo.getTree().getEdges());
		buf.append("\n  new stack: "+algo.getStack());
		buf.append("\n  new current node: "+algo.getCurrentNode());
		logger.log(LOGGER_LEVEL, buf.toString());
	}
	
	/**
	 * Manipulates the stack
	 */
	protected abstract void addSuccessorsToStack();
	
	/**
	 * Undoes {@link #addSuccessorsToStack()}
	 */
	protected abstract void undoAddSuccessorsToStack();
	
	/**
	 * Removes the top queue and sets its owner <em>finished</em>
	 */
	private void removeSuccessors() {
		// remember top queue and its owner for undo
		removedFinishedNodes = algo.getStack().getFinished();
		removedQueueOwner = algo.getStack().getOwner();
			
		// remove top queue and set its owner finished
		algo.getStack().removeTopQueueAndFinishOwner();
		
		// update status map
		algo.getStatusPerNode().put(removedQueueOwner, NodeStatus.FINISHED);
		
		algo.setCurrentNode(removedQueueOwner);
		
		topQueueRemoved = true;
	}

	/**
	 * Undoes {@link #removeSuccessors()} if it was executed
	 */
	private void undoRemoveSuccessors() {
		if (!topQueueRemoved) return;
		
		algo.getStack().nodeStatusDown(removedQueueOwner);

		algo.getStack().addQueue(removedQueueOwner);
		algo.getStack().addNodes(removedFinishedNodes);
		for (int node: removedFinishedNodes) {
			algo.getStack().setNodeFinished(node);
		}
	}

	/**
	 * Adds the specified nodes to the tree
	 */
	private void addTreeNodes(Collection<Integer> nodes) {
		newTreeNodes = new LinkedList<Integer>();
		for (int node: nodes) {
			algo.getTree().addNode(node, graphController.getNodePosition(node));
			algo.getTree().setNodeDistance(node, algo.calculateDistance(node));
			newTreeNodes.add(node);
		}
	}
	
	/**
	 * Adds the specified edges to the tree
	 * @param predecessor index of the node at each edge's beginning
	 * @param nodes indices of the nodes at the edge's ending
	 */
	private void addTreeEdges(int predecessor, Collection<Integer> nodes) {
		newTreeEdges = new HashMap<Integer, Collection<Integer>>();
		for (int node: nodes) {
			algo.getTree().addEdge(predecessor, node);
			newTreeEdges.put(predecessor, nodes);
		}
	}
	
	
	/**
	 * Undoes {@link #addTreeNodes(Collection)} if it was executed
	 */
	private void undoAddTreeNodes() {
		if (null == newTreeNodes) return;
		for (int node: newTreeNodes) {
			algo.getTree().removeNode(node);}
	}
	
	/**
	 * Undoes {@link #addTreeEdges(int, Collection)} if it was executed
	 */
	private void undoAddTreeEdges() {
		if (null == newTreeEdges) return;
		for (int node1: newTreeEdges.keySet())
			for (int node2: newTreeEdges.get(node1))
				algo.getTree().removeEdge(node1, node2);
	}

	/**
	 * Chooses the untouched nodes that should be added to the tree and set waiting
	 */
	protected abstract List<Integer> getNewTreeNodes();
	
	/**
	 * Returns the predecessor for all nodes that are found in this step
	 * @return index of the preceding node
	 */
	protected abstract int getNodesPredecessor();
	
	/**
	 * Tries to start a new tree or finishes if none could be started. This method sets the algorithm's start node
	 * and current node to a graph node that is not yet found. This is necessary
	 * to find all nodes, as there may still be nodes in the graph that are not found
	 * even when the current tree is finished.
	 */
	protected abstract void newTreeOrFinish();
	
	protected abstract void undoNewTreeOrFinish();
}
