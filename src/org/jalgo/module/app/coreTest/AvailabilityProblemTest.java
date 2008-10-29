package org.jalgo.module.app.coreTest;


import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.jalgo.module.app.core.AvailableSemiRings;
import org.jalgo.module.app.core.Calculation;
import org.jalgo.module.app.core.Matrix;
import org.jalgo.module.app.core.SemiRing;
import org.jalgo.module.app.core.dataType.booleanType.BooleanType;
import org.jalgo.module.app.core.graph.Graph;
import org.jalgo.module.app.core.graph.Node;
import org.jalgo.module.app.core.step.RootStep;
import org.jalgo.module.app.core.step.Step;
import org.junit.Before;
import org.junit.Test;

public class AvailabilityProblemTest {
	RootStep step;
	Graph graph;
	List<Step> steps;
	int size;
	
	@Before
	public void setUp() throws Exception {
		SemiRing ring;
		Calculation calc;
		
		ring = AvailableSemiRings.getSemiRings().get(AvailableSemiRings.AVAILABILITY_PROBLEM_ID);

		graph = new Graph();
		
		graph.newNodes(7);
		
		List<Node> nodeList;
		nodeList = GraphTest.getOrderedNodeList(graph.getNodes()); 
		
		assertEquals(7,nodeList.size());
		
		graph.newEdge(nodeList.get(0), nodeList.get(1), new BooleanType(true),true);
		graph.newEdge(nodeList.get(1), nodeList.get(2), new BooleanType(true),true);
		graph.newEdge(nodeList.get(2), nodeList.get(3), new BooleanType(true),true);
		graph.newEdge(nodeList.get(3), nodeList.get(4), new BooleanType(true),true);
		graph.newEdge(nodeList.get(4), nodeList.get(5), new BooleanType(true),true);
		graph.newEdge(nodeList.get(5), nodeList.get(6), new BooleanType(true),true);
		graph.newEdge(nodeList.get(6), nodeList.get(1), new BooleanType(false),true);
		graph.newEdge(nodeList.get(5), nodeList.get(2), new BooleanType(true),true);
		graph.newEdge(nodeList.get(4), nodeList.get(2), new BooleanType(false),true);
		graph.newEdge(nodeList.get(3), nodeList.get(1), new BooleanType(false),true);
		
		calc = new Calculation();
		calc.setSemiring(ring);
		calc.setGraph(graph);
		
		step = (RootStep)calc.getRootStep();
		steps = step.getSteps();
		size = graph.getNodes().size();
	}
	
	@Test
	public void checkBeforeMatrix() {
		Matrix before;
		
		before = step.getBeforeMatrix();
		size = graph.getNodes().size();
		
		// diagonal
		for (int i=0; i<size; i++)
			assertEquals(new BooleanType(true), before.getValueAt(i, i));
	
		assertEquals(new BooleanType(true),before.getValueAt(0, 1));
		assertEquals(new BooleanType(false),before.getValueAt(0, 5));
		assertEquals(new BooleanType(true),before.getValueAt(1, 2));
		assertEquals(new BooleanType(true),before.getValueAt(2, 3));
		assertEquals(new BooleanType(false),before.getValueAt(5, 1));
		assertEquals(new BooleanType(true),before.getValueAt(5, 6));
	}
	
	@Test
	public void checkMatrix6() {
		Matrix before;
		
		before = step.getSteps().get(5).getAfterMatrix();
		
		for (int i = 0; i < size; i++)
			assertEquals(new BooleanType(true),before.getValueAt(0, i));
		for (int i = 1; i < size; i++)
			assertEquals(new BooleanType(true),before.getValueAt(1, i));
		for (int i = 2; i < 5; i++) {
			for (int j = 2; j < size; j++)
				assertEquals(new BooleanType(true),before.getValueAt(i, j));
		}
		for (int i = 0; i < size-1; i++)
			assertEquals(new BooleanType(false),before.getValueAt(6, i));
	}
	@Test
	public void checkViaNodes6() {
		Matrix before;
		List<Integer> viaNodes;
		
		before = step.getSteps().get(5).getAfterMatrix();
		size = graph.getNodes().size();
		viaNodes = new LinkedList<Integer>();
		
		for (int i = 0; i < size; i++)
			// should return empty list
			assertEquals(viaNodes,before.getNodesAt(i, i));
		
		// path from node 0 to node 2
		viaNodes.add(1);
		assertEquals(viaNodes,before.getNodesAt(0, 2));
		// path from node 0 to node 6 (max. length of a path in this graph)
		viaNodes.add(2);
		viaNodes.add(3);
		viaNodes.add(4);
		viaNodes.add(5);
		assertEquals(viaNodes,before.getNodesAt(0, 6));
		viaNodes.clear();
		// path from node 5 to node 4
		viaNodes.add(2);
		viaNodes.add(3);
		assertEquals(viaNodes,before.getNodesAt(5, 4));
		
	}
}
