package org.jalgo.module.bfsdfs.algorithms.test;

import java.awt.Point;
import java.util.*;
import org.jalgo.module.bfsdfs.algorithms.*;
import org.jalgo.module.bfsdfs.controller.GraphController;

public class DFSTest extends AlgoTest {
	private int[] expectedDistance = {
		Integer.MAX_VALUE,
		0,
		1,
		1,
		2,
		0,
		1,
		3,
		3
	};
	
	private String[] expectedStack = {
			null,
			"0:[1:w] 1:[2:u, 3:u]",
			"0:[1:w] 1:[2:w, 3:u] 2:[]",
			"0:[1:w] 1:[2:f, 3:u]",
			"0:[1:w] 1:[2:f, 3:w] 3:[4:u]",
			"0:[1:w] 1:[2:f, 3:w] 3:[4:w] 4:[7:u, 8:u]",
			"0:[1:w] 1:[2:f, 3:w] 3:[4:w] 4:[7:w, 8:u] 7:[]",
			"0:[1:w] 1:[2:f, 3:w] 3:[4:w] 4:[7:f, 8:u]",
			"0:[1:w] 1:[2:f, 3:w] 3:[4:w] 4:[7:f, 8:w] 8:[]",
			"0:[1:w] 1:[2:f, 3:w] 3:[4:w] 4:[7:f, 8:f]",
			"0:[1:w] 1:[2:f, 3:w] 3:[4:f]",
			"0:[1:w] 1:[2:f, 3:f]",
			"0:[1:f, 5:u]",
			"0:[1:f, 5:w] 5:[6:u]",
			"0:[1:f, 5:w] 5:[6:w] 6:[]",
			"0:[1:f, 5:w] 5:[6:f]",
			"0:[1:f, 5:f]",
			"0:[]"
	};

	private Integer[][] expectedTreeNodes = {
			null,
			{1},
			{1,2},
			{1,2},
			{1,2,3},
			{1,2,3,4},
			{1,2,3,4,7},
			{1,2,3,4,7},
			{1,2,3,4,7},
			{1,2,3,4,7,8},
			{1,2,3,4,7,8},
			{1,2,3,4,7,8},
			{1,2,3,4,7,8},
			{1,2,3,4,7,8},
			{1,2,3,4,7,8},
			{1,2,3,4,7,8,5},
			{1,2,3,4,7,8,5,6},
			{1,2,3,4,7,8,5,6},
			{1,2,3,4,7,8,5,6},
			{1,2,3,4,7,8,5,6}
	};

	private Integer[] expectedPredecessors = {
			null, null, 1, 1, 3, null, 5, 4, 4			
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

		for (int i=1; i<=8; i++)
			gc.addNode(new Point());

		gc.addEdge(1,1);
		gc.addEdge(1,2);
		gc.addEdge(2,1);
		gc.addEdge(1,3);
		gc.addEdge(3,4);
		gc.addEdge(4,3);
		gc.addEdge(3,1);
		gc.addEdge(5,6);
		gc.addEdge(6,6);
		gc.addEdge(6,1);
		gc.addEdge(4,7);
		gc.addEdge(4,8);
		
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
