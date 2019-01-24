package org.jalgo.module.bfsdfs.algorithms.test;

import java.awt.Point;
import java.util.*;
import org.jalgo.module.bfsdfs.algorithms.*;
import org.jalgo.module.bfsdfs.controller.GraphController;

public class DFSTest6 extends AlgoTest {
	private int[] expectedDistance = {
		Integer.MAX_VALUE,
		0,
		2,
		2,
		1,
		1
	};
	
	private String[] expectedStack = {
			null,
			"0:[1:w] 1:[4:u, 5:u]",
			"0:[1:w] 1:[4:w, 5:u] 4:[3:u]",
			"0:[1:w] 1:[4:w, 5:u] 4:[3:w] 3:[]",
			"0:[1:w] 1:[4:w, 5:u] 4:[3:f]",
			"0:[1:w] 1:[4:f, 5:u]",
			"0:[1:w] 1:[4:f, 5:w] 5:[2:u]",
			"0:[1:w] 1:[4:f, 5:w] 5:[2:w] 2:[]",
			"0:[1:w] 1:[4:f, 5:w] 5:[2:f]",
			"0:[1:w] 1:[4:f, 5:f]",
			"0:[1:f]",
			"",
	};

	private Integer[][] expectedTreeNodes = {
			null,
			{1},
			{1,4},
			{1,4,3},
			{1,4,3},
			{1,4,3},
			{1,4,3,5},
			{1,4,3,5},
			{1,4,3,5},
			{1,4,3,5},
			{1,4,3,5},
			{1,4,3,5}
	};

	private Integer[] expectedPredecessors = {
			null, null, 5, 4, 1, 1			
	};
	
	@Override
	protected Algo getAlgo() {
		return new DFS(getGraph());
	}

	@Override
	protected String expectedStack(int stepNumber) throws IndexOutOfBoundsException {
		try {
			return expectedStack[stepNumber];
		} catch(ArrayIndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException();
		}
	}

	@Override
	protected Collection<Integer> expectedTreeNodes(int stepNumber) throws IndexOutOfBoundsException {
		try {
			return Arrays.asList(expectedTreeNodes[stepNumber]);
		} catch (ArrayIndexOutOfBoundsException e) {
			throw new IndexOutOfBoundsException();
		}
	}
	
	@Override
	protected GraphController getGraph() {
		GraphController gc = new GraphController();

		for (int i=1; i<=5; i++)
			gc.addNode(new Point());

		gc.addEdge(1,5);
		gc.addEdge(5,1);
		gc.addEdge(5,2);
		gc.addEdge(2,5);
		gc.addEdge(1,4);
		gc.addEdge(4,1);
		gc.addEdge(4,3);
		gc.addEdge(3,4);
		
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
