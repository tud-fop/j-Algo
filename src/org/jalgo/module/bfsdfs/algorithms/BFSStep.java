package org.jalgo.module.bfsdfs.algorithms;

import java.util.*;
import org.jalgo.module.bfsdfs.controller.GraphController;

/**
 * Step of a breadth-first-search algorithm
 * @author Thomas G&ouml;rres
 *
 */
public class BFSStep extends AlgoStep {
	private boolean nodeUpped;
	private int nodeUppedIndex;
	
	public BFSStep(Algo algo, GraphController graphController) {
		super(algo, graphController);
		nodeUpped = false;
	}
	
	@Override protected void addSuccessorsToStack() {		
		List<Integer> successors = algo.getDefaultSuccessors();
		System.out.println("succ: "+successors);
		
		// If current node is waiting and has no successors, it should be finished.
		//
		// If another node is waiting, it must be the current node's predecessor.
		// As the current node is on the stack now, it's predecessor should be
		// finished.
		if (!algo.getStack().getWaiting().isEmpty()) {
			int waiting = algo.getStack().getWaiting().get(0);
			if (successors.isEmpty() || algo.getCurrentNode() != waiting) {
				nodeUpped = true;
				nodeUppedIndex = waiting;
				algo.getStack().nodeStatusUp(waiting);
			}
			
			// if there still is another waiting nodes, proceed with it instead of the current node
			if (!algo.getStack().getWaiting().isEmpty())
				algo.setCurrentNode(algo.getStack().getWaiting().get(0));
		}
		algo.getStack().addNodes(algo.getDefaultSuccessors());
	}

	@Override protected void undoAddSuccessorsToStack() {
		if (nodeUpped)
			algo.getStack().nodeStatusDown(nodeUppedIndex);
		algo.getStack().replaceUntouched(null);
	}

	@Override
	protected List<Integer> getNewTreeNodes() {
		return algo.getStack().getUntouched();
	}

	@Override
	protected int getNodesPredecessor() {
		if (algo.getCurrentNode() == algo.getStartNode())
			throw new NoSuchElementException("Current node is start node, no predecessor exists.");
		
		if (!algo.getStack().getWaiting().isEmpty())
			return algo.getStack().getWaiting().get(0);
		
		throw new NoSuchElementException("No predecessor found.");
	}

	@Override
	protected void newTreeOrFinish() {
		// BFS cannot create more than one tree
		algo.finish();
	}

	@Override
	protected void undoNewTreeOrFinish() {}
}
