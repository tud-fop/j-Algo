package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jalgo.module.app.core.AvailableSemiRings;
import org.jalgo.module.app.core.Calculation;
import org.jalgo.module.app.core.Matrix;
import org.jalgo.module.app.core.SemiRing;
import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.rationalNumber.PositiveRationalNumber;
import org.jalgo.module.app.core.graph.Graph;
import org.jalgo.module.app.core.graph.Node;
import org.jalgo.module.app.core.step.AtomicStep;
import org.jalgo.module.app.core.step.GroupStep;
import org.jalgo.module.app.core.step.RootStep;
import org.jalgo.module.app.core.step.Step;
import org.junit.Before;
import org.junit.Test;

public class SimpleCalculationTest {
	RootStep step;
	Graph graph;
	
	PositiveRationalNumber inf;
	List<Step> steps;
	int size;
	
	@Before
	public void setUp() {
		SemiRing ring;
		Calculation calc;
		
		ring = AvailableSemiRings.getSemiRings().get(AvailableSemiRings.SHORTEST_PATH_PROBLEM_ID);

		graph = new Graph();
		
		// Build Graph
		graph.newNodes(4);
		
		List<Node> nodeList;
		nodeList = GraphTest.getOrderedNodeList(graph.getNodes()); 
		
		assertEquals(4,nodeList.size());
		
		graph.newEdge(nodeList.get(0), nodeList.get(2), new PositiveRationalNumber(5),true);
		graph.newEdge(nodeList.get(1), nodeList.get(0), new PositiveRationalNumber(8),true);
		graph.newEdge(nodeList.get(1), nodeList.get(3), new PositiveRationalNumber(3),true);
		graph.newEdge(nodeList.get(2), nodeList.get(1), new PositiveRationalNumber(2),true);
		graph.newEdge(nodeList.get(3), nodeList.get(0), new PositiveRationalNumber(2),true);
		
		// Create calculation
		calc = new Calculation();
		calc.setSemiring(ring);
		calc.setGraph(graph);
		
		step = (RootStep)calc.getRootStep();
		steps = step.getSteps();
		size = graph.getNodes().size();
		inf = new PositiveRationalNumber(Infinity.POSITIVE_INFINITE);
	}
	
	@Test
	public void checkBeforeMatrix() {
		Matrix before;
		
		before = step.getBeforeMatrix();
		size = graph.getNodes().size();
		
		for (int i=0; i<size; i++)
			assertEquals(new PositiveRationalNumber(0), before.getValueAt(i, i));
		
		assertEquals(new PositiveRationalNumber(5), before.getValueAt(0, 2));
		assertEquals(new PositiveRationalNumber(8), before.getValueAt(1, 0));
		assertEquals(new PositiveRationalNumber(3), before.getValueAt(1, 3));
		assertEquals(new PositiveRationalNumber(2), before.getValueAt(2, 1));
		assertEquals(new PositiveRationalNumber(2), before.getValueAt(3, 0));
		
		assertEquals(inf, before.getValueAt(0, 1));
		assertEquals(inf, before.getValueAt(0, 3));
		assertEquals(inf, before.getValueAt(1, 2));
		assertEquals(inf, before.getValueAt(2, 0));
		assertEquals(inf, before.getValueAt(2, 3));
		assertEquals(inf, before.getValueAt(3, 1));
		assertEquals(inf, before.getValueAt(3, 2));
	}
	
	@Test
	public void checkStepMatrix1() {
		Matrix m;
		
		// check after step 1
		m = steps.get(0).getAfterMatrix();
		
		for (int i=0; i<size; i++)
			assertEquals(new PositiveRationalNumber(0), m.getValueAt(i, i));
		
		assertEquals(new PositiveRationalNumber(5), m.getValueAt(0, 2));
		assertEquals(new PositiveRationalNumber(8), m.getValueAt(1, 0));
		assertEquals(new PositiveRationalNumber(13), m.getValueAt(1, 2));
		assertEquals(new PositiveRationalNumber(3), m.getValueAt(1, 3));
		assertEquals(new PositiveRationalNumber(2), m.getValueAt(2, 1));
		assertEquals(new PositiveRationalNumber(2), m.getValueAt(3, 0));
		assertEquals(new PositiveRationalNumber(7), m.getValueAt(3, 2));
		
		assertEquals(inf, m.getValueAt(0, 1));
		assertEquals(inf, m.getValueAt(0, 3));
		assertEquals(inf, m.getValueAt(2, 0));
		assertEquals(inf, m.getValueAt(2, 3));
		assertEquals(inf, m.getValueAt(3, 1));
	}
	
	@Test
	public void checkStepMatrix2() {
		Matrix m;
		
		// check after step 2
		m = steps.get(1).getAfterMatrix();
		
		for (int i=0; i<size; i++)
			assertEquals(new PositiveRationalNumber(0), m.getValueAt(i, i));
		
		assertEquals(new PositiveRationalNumber(5), m.getValueAt(0, 2));
		assertEquals(new PositiveRationalNumber(8), m.getValueAt(1, 0));
		assertEquals(new PositiveRationalNumber(13), m.getValueAt(1, 2));
		assertEquals(new PositiveRationalNumber(3), m.getValueAt(1, 3));
		assertEquals(new PositiveRationalNumber(10), m.getValueAt(2, 0));
		assertEquals(new PositiveRationalNumber(2), m.getValueAt(2, 1));
		assertEquals(new PositiveRationalNumber(5), m.getValueAt(2, 3));
		assertEquals(new PositiveRationalNumber(2), m.getValueAt(3, 0));
		assertEquals(new PositiveRationalNumber(7), m.getValueAt(3, 2));
		
		assertEquals(inf, m.getValueAt(0, 1));
		assertEquals(inf, m.getValueAt(0, 3));
		assertEquals(inf, m.getValueAt(3, 1));
	}
	
	@Test
	public void checkStepMatrix3() {
		Matrix m;
		
		// check after step 3
		m = steps.get(2).getAfterMatrix();
		
		for (int i=0; i<size; i++)
			assertEquals(new PositiveRationalNumber(0), m.getValueAt(i, i));

		assertEquals(new PositiveRationalNumber(7), m.getValueAt(0, 1));
		assertEquals(new PositiveRationalNumber(5), m.getValueAt(0, 2));
		assertEquals(new PositiveRationalNumber(10), m.getValueAt(0, 3));
		assertEquals(new PositiveRationalNumber(8), m.getValueAt(1, 0));
		assertEquals(new PositiveRationalNumber(13), m.getValueAt(1, 2));
		assertEquals(new PositiveRationalNumber(3), m.getValueAt(1, 3));
		assertEquals(new PositiveRationalNumber(10), m.getValueAt(2, 0));
		assertEquals(new PositiveRationalNumber(2), m.getValueAt(2, 1));
		assertEquals(new PositiveRationalNumber(5), m.getValueAt(2, 3));
		assertEquals(new PositiveRationalNumber(2), m.getValueAt(3, 0));
		assertEquals(new PositiveRationalNumber(9), m.getValueAt(3, 1));
		assertEquals(new PositiveRationalNumber(7), m.getValueAt(3, 2));
	}
	
	@Test
	public void checkAfterMatrix() {
		Matrix m,m2;
		
		// get end matrix
		m = step.getAfterMatrix();
		m2 = steps.get(3).getAfterMatrix();
		
		// should be the same as after step 4
		assertEquals(m, m2);
		
		for (int i=0; i<size; i++)
			assertEquals(new PositiveRationalNumber(0), m.getValueAt(i, i));

		assertEquals(new PositiveRationalNumber(7), m.getValueAt(0, 1));
		assertEquals(new PositiveRationalNumber(5), m.getValueAt(0, 2));
		assertEquals(new PositiveRationalNumber(10), m.getValueAt(0, 3));
		assertEquals(new PositiveRationalNumber(5), m.getValueAt(1, 0));
		assertEquals(new PositiveRationalNumber(10), m.getValueAt(1, 2));
		assertEquals(new PositiveRationalNumber(3), m.getValueAt(1, 3));
		assertEquals(new PositiveRationalNumber(7), m.getValueAt(2, 0));
		assertEquals(new PositiveRationalNumber(2), m.getValueAt(2, 1));
		assertEquals(new PositiveRationalNumber(5), m.getValueAt(2, 3));
		assertEquals(new PositiveRationalNumber(2), m.getValueAt(3, 0));
		assertEquals(new PositiveRationalNumber(9), m.getValueAt(3, 1));
		assertEquals(new PositiveRationalNumber(7), m.getValueAt(3, 2));
	
	}
	
	@Test
	public void checkStepCount() {
		int changedCount;
		
		changedCount = 0;
		
		for (int k=0; k<size; k++) {
			GroupStep nodeStep;
			nodeStep = (GroupStep) ((GroupStep)step).getSteps().get(k);
			
			assertEquals(size*size, nodeStep.getSteps().size());
			
			for (int v=0; v<size*size; v++) {
				AtomicStep itemStep;
				itemStep = (AtomicStep) nodeStep.getSteps().get(v);
				
				if (itemStep.isChanged())
					changedCount++;
			}
		}
		
		assertEquals(10, changedCount);
	}
}
