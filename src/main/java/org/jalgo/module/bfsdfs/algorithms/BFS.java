package org.jalgo.module.bfsdfs.algorithms;

import org.jalgo.module.bfsdfs.controller.GraphController;

/**
 * Breadth-first-search algorithm for graphs
 * @author Thomas G&ouml;rres
 *
 */
public class BFS extends Algo {
	public BFS(GraphController graphController) {
		super(graphController);
	}
	
	@Override public void step() {
		makeUndoableStep(new BFSStep(this, graph));
	}
}
