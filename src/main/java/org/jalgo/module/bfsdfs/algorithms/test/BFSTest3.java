package org.jalgo.module.bfsdfs.algorithms.test;

import java.awt.Point;
import java.util.*;
import org.jalgo.module.bfsdfs.algorithms.*;
import org.jalgo.module.bfsdfs.controller.GraphController;

public class BFSTest3 extends AlgoTest {
	private int[] expectedDistance = {
			Integer.MAX_VALUE,
			0,
			1,
			1,
			2,
			3,
			2
	};
	
	private String[] expectedStack = {
			null,
			"0:[1:w, 2:u, 3:u]",
			"0:[1:f, 2:w, 3:w, 4:u]",
			"0:[1:f, 2:f, 3:w, 4:w, 6:u]",
			"0:[1:f, 2:f, 3:f, 4:w, 6:w, 5:u]",
			"0:[1:f, 2:f, 3:f, 4:f, 6:w, 5:w]",
			"0:[1:f, 2:f, 3:f, 4:f, 6:f, 5:w]",
			"0:[1:f, 2:f, 3:f, 4:f, 6:f, 5:f]",
			""
	};
	
	private Integer[][] expectedTreeNodes = {
			null,
			{1},
			{1,2,3},
			{1,2,3,4},
			{1,2,3,4,6},
			{1,2,3,4,6},
			{1,2,3,4,6,5},
			{1,2,3,4,6,5},
			{1,2,3,4,6,5},
			{1,2,3,4,6,5}
	};
	
	private Integer[] expectedPredecessors = {
			null, null, 1, 1, 2, 4, 3			
	};
	
	@Override
	protected Algo getAlgo() {
		return new BFS(getGraph());
	}

	@Override
	protected String expectedStack(int stepNumber) {
		return expectedStack[stepNumber];
	}

	@Override
	protected Collection<Integer> expectedTreeNodes(int stepNumber) {
		return Arrays.asList(expectedTreeNodes[stepNumber]);
	}
	
	

	@Override
	protected GraphController getGraph() {
		GraphController gc = new GraphController();

		for (int i=1; i<=6; i++)
			gc.addNode(new Point());

		gc.addEdge(1,2);
		gc.addEdge(1,3);
		gc.addEdge(3,6);
		gc.addEdge(2,4);
		gc.addEdge(4,5);
		
		return gc;
	}

	@Override
	protected int expectedPredecessor(int nodeIndex)
			throws IndexOutOfBoundsException {
		try {
			return expectedPredecessors[nodeIndex];
		} catch(NullPointerException e) {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	protected int expectedDistance(int nodeIndex)
			throws IndexOutOfBoundsException {
		return expectedDistance[nodeIndex];
	}
}
