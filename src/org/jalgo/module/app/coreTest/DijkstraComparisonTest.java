package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.jalgo.module.app.core.AvailableSemiRings;
import org.jalgo.module.app.core.Calculation;
import org.jalgo.module.app.core.Matrix;
import org.jalgo.module.app.core.SemiRing;
import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.rationalNumber.PositiveRationalNumber;
import org.jalgo.module.app.core.graph.Graph;
import org.jalgo.module.app.core.graph.Node;
import org.jalgo.module.app.core.step.RootStep;
import org.junit.Before;
import org.junit.Test;

public class DijkstraComparisonTest {
	RootStep step;
	Matrix result;
	Graph graph;
	int size;
	
	@Before
	public void setUp() {
		SemiRing ring;
		Calculation calc;
	
		// Build Graph
		ring = AvailableSemiRings.getSemiRings().get(AvailableSemiRings.SHORTEST_PATH_PROBLEM_ID);
		graph = new Graph();
		
		// Build Graph
		graph.newNodes(9);
		
		List<Node> list;
		
		list = GraphTest.getOrderedNodeList(graph.getNodes());
		
		graph.newEdge(list.get(0), list.get(1), new PositiveRationalNumber(2),true);
		graph.newEdge(list.get(0), list.get(5), new PositiveRationalNumber(4),true);
		graph.newEdge(list.get(0), list.get(6), new PositiveRationalNumber(12),true);
		
		graph.newEdge(list.get(1), list.get(2), new PositiveRationalNumber(10),true);
		graph.newEdge(list.get(1), list.get(6), new PositiveRationalNumber(9),true);

		graph.newEdge(list.get(2), list.get(3), new PositiveRationalNumber(2),true);

		graph.newEdge(list.get(3), list.get(4), new PositiveRationalNumber(1),true);
		
		graph.newEdge(list.get(5), list.get(4), new PositiveRationalNumber(11),true);
		graph.newEdge(list.get(5), list.get(7), new PositiveRationalNumber(2),true);

		graph.newEdge(list.get(6), list.get(7), new PositiveRationalNumber(15),true);
		
		graph.newEdge(list.get(7), list.get(4), new PositiveRationalNumber(9),true);
		graph.newEdge(list.get(7), list.get(8), new PositiveRationalNumber(1),true);
		
		graph.newEdge(list.get(8), list.get(2), new PositiveRationalNumber(4),true);
		graph.newEdge(list.get(8), list.get(3), new PositiveRationalNumber(7),true);
		graph.newEdge(list.get(8), list.get(6), new PositiveRationalNumber(3),true);
		
		// Create calculation
		calc = new Calculation();
		calc.setSemiring(ring);
		calc.setGraph(graph);
		
		step = (RootStep)calc.getRootStep();
		result = step.getAfterMatrix();
		size = graph.getNodes().size();
	}
	
	@Test
	public void checkDistances() {
		assertEquals(new PositiveRationalNumber(0), result.getValueAt(0, 0));
		assertEquals(new PositiveRationalNumber(2), result.getValueAt(0, 1));
		assertEquals(new PositiveRationalNumber(11), result.getValueAt(0, 2));
		assertEquals(new PositiveRationalNumber(13), result.getValueAt(0, 3));
		assertEquals(new PositiveRationalNumber(14), result.getValueAt(0, 4));
		assertEquals(new PositiveRationalNumber(4), result.getValueAt(0, 5));
		assertEquals(new PositiveRationalNumber(10), result.getValueAt(0, 6));
		assertEquals(new PositiveRationalNumber(6), result.getValueAt(0, 7));
		assertEquals(new PositiveRationalNumber(7), result.getValueAt(0, 8));
	}
	
	@Test
	public void checkNodes() {
		List<Integer> list;

		list = new ArrayList<Integer>();		
		assertEquals(list, result.getNodesAt(0, 0));
		assertEquals(list, result.getNodesAt(0, 1));
		assertEquals(list, result.getNodesAt(0, 5));

		list.add(5);
		assertEquals(list, result.getNodesAt(0, 7));
		
		list.add(7);
		assertEquals(list, result.getNodesAt(0, 8));
		
		list.add(8);
		assertEquals(list, result.getNodesAt(0, 2));
		assertEquals(list, result.getNodesAt(0, 6));
	
		list.add(2);
		assertEquals(list, result.getNodesAt(0, 3));
		
		list.add(3);
		assertEquals(list, result.getNodesAt(0, 4));
	}
	
	@Test
	public void checkEmptyNodeLists() {
		for (int i=0; i<size; i++) {
			for (int j=0; j<size; j++) {
				if (((PositiveRationalNumber) result.getValueAt(i, j)).getInfinity() != Infinity.REAL)
					assertNull(result.getNodesAt(i, j));
				if (result.getNodesAt(i, j) == null)
					assertEquals(Infinity.POSITIVE_INFINITE, ((PositiveRationalNumber) result.getValueAt(i, j)).getInfinity());
			}
		}
	}
}
