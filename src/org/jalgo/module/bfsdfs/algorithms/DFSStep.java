package org.jalgo.module.bfsdfs.algorithms;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;

import org.jalgo.module.bfsdfs.algorithms.stack.NodeStatus;
import org.jalgo.module.bfsdfs.controller.GraphController;

/**
 * Step of a depth-first-search algorithm
 * @author Thomas G&ouml;rres
 *
 */
class DFSStep extends AlgoStep {
	boolean queueAdded;
	
	private int oldStartNode, newStartNode;
	
	private boolean startNodeSet;
	
	public DFSStep(Algo algo, GraphController graphController) {
		super(algo, graphController);
		queueAdded = false;
		startNodeSet = false;
		oldStartNode = algo.getStartNode();
	}
	
	@Override protected void addSuccessorsToStack() {
		queueAdded = algo.getStack().addQueue(algo.getCurrentNode(), algo.getDefaultSuccessors());
	}

	@Override protected void undoAddSuccessorsToStack() {
		if (queueAdded) algo.getStack().removeTopQueue();
	}

	@Override
	protected List<Integer> getNewTreeNodes() {
		if (algo.getStack().getUntouched().isEmpty())
			return Collections.emptyList();
		
		int firstNodeUntouched = algo.getStack().getUntouched().get(0);
		return Arrays.asList(firstNodeUntouched);
	}

	@Override
	protected int getNodesPredecessor() {
		if (algo.getCurrentNode() == algo.getStartNode())
			throw new NoSuchElementException("Current node is start node, no predecessor exists.");

		return algo.getStack().getOwner();
	}
	
	@Override
	protected void newTreeOrFinish() {
		for (int node: graphController.getNodes())
			if (algo.getStatusPerNode().get(node) == null) {
				newStartNode = node;
				startNodeSet = true;
				algo.setStartNode(node);
				algo.getStack().addNode(node);
				algo.getStatusPerNode().put(node, NodeStatus.UNTOUCHED);
				algo.chooseNextNode();
				return;
			}
		algo.finish();
	}
	
	@Override
	protected void undoNewTreeOrFinish() {
		if (!startNodeSet) return;
		algo.getStatusPerNode().put(newStartNode, null);
		algo.getStack().replaceUntouched(null);
		algo.setStartNode(oldStartNode);
	}
}
