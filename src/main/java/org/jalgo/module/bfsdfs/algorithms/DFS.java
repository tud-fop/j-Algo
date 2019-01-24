package org.jalgo.module.bfsdfs.algorithms;

import org.jalgo.module.bfsdfs.controller.GraphController;

/**
 * Depth-first-search algorithm for graphs
 * @author Thomas G&ouml;rres
 *
 */
public class DFS extends Algo {
	public DFS(GraphController graphController) {
		super(graphController);
	}
	
	@Override public void step() {
		makeUndoableStep(new DFSStep(this, graph));
	}
}
