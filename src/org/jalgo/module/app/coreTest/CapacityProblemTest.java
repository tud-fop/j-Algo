package org.jalgo.module.app.coreTest;


import static org.junit.Assert.assertEquals;

import java.util.List;

import org.jalgo.module.app.core.Calculation;
import org.jalgo.module.app.core.Matrix;
import org.jalgo.module.app.core.SemiRing;
import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.Operation;
import org.jalgo.module.app.core.dataType.rationalNumber.PositiveRationalNumber;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMaximum;
import org.jalgo.module.app.core.dataType.rationalNumber.RationalMinimum;
import org.jalgo.module.app.core.graph.Graph;
import org.jalgo.module.app.core.graph.Node;
import org.jalgo.module.app.core.step.RootStep;
import org.jalgo.module.app.core.step.Step;
import org.junit.Before;
import org.junit.Test;

public class CapacityProblemTest {
	RootStep step;
	Graph graph;
	List<Step> steps;
	int size;
	PositiveRationalNumber inf;
	
	@Before
	public void setUp() throws Exception {
		SemiRing ring;
		Class<PositiveRationalNumber> type;
		Operation plus, dot;
		Calculation calc;
		inf = new PositiveRationalNumber(Infinity.POSITIVE_INFINITE);
		
		type = PositiveRationalNumber.class;
		plus = null;
		dot = null;
		
		for (Operation o : PositiveRationalNumber.getOperations()) {
			if (o instanceof RationalMaximum)
				plus = o;
			if (o instanceof RationalMinimum)
				dot = o;
		}
		
		ring = new SemiRing(type, plus, dot);
		graph = new Graph();

		graph.newNodes(4);
		
		List<Node> list;
		list = GraphTest.getOrderedNodeList(graph.getNodes());
		
		graph.newEdge(list.get(0), list.get(2), new PositiveRationalNumber(30.5f),true);
		graph.newEdge(list.get(1), list.get(0), new PositiveRationalNumber(20.5f),true);
		graph.newEdge(list.get(1), list.get(3), new PositiveRationalNumber(30.5f),true);
		graph.newEdge(list.get(2), list.get(1), new PositiveRationalNumber(10.5f),true);
		graph.newEdge(list.get(3), list.get(0), new PositiveRationalNumber(25.5f),true);

		calc = new Calculation();
		calc.setSemiring(ring);
		calc.setGraph(graph);
		
		step = (RootStep)calc.getRootStep();
		steps = step.getSteps();
		size = graph.getNodes().size();
	}
	
	@Test
	public void checkStepMatrix1() {
		Matrix m;
		
		// check after step 1
		m = steps.get(0).getAfterMatrix();
		
		for (int i=0; i<size; i++)
			assertEquals(inf, m.getValueAt(i, i));
		
		assertEquals(new PositiveRationalNumber(30.5f), m.getValueAt(0, 2));
		assertEquals(new PositiveRationalNumber(20.5f), m.getValueAt(1, 0));
		assertEquals(new PositiveRationalNumber(20.5f), m.getValueAt(1, 2));
		assertEquals(new PositiveRationalNumber(30.5f), m.getValueAt(1, 3));
		assertEquals(new PositiveRationalNumber(0.0f), m.getValueAt(2, 0));
		assertEquals(new PositiveRationalNumber(25.5f), m.getValueAt(3, 0));
		assertEquals(new PositiveRationalNumber(25.5f), m.getValueAt(3, 2));
		
	}
	
	@Test
	public void checkFinalMatrix() {
		Matrix m;
		
		// check after step 1
		m = steps.get(3).getAfterMatrix();
		
		for (int i=0; i<size; i++)
			assertEquals(inf, m.getValueAt(i, i));
		
		assertEquals(new PositiveRationalNumber(30.5f), m.getValueAt(0, 2));
		assertEquals(new PositiveRationalNumber(25.5f), m.getValueAt(1, 0));
		assertEquals(new PositiveRationalNumber(25.5f), m.getValueAt(1, 2));
		assertEquals(new PositiveRationalNumber(30.5f), m.getValueAt(1, 3));
		assertEquals(new PositiveRationalNumber(10.5f), m.getValueAt(2, 0));
		assertEquals(new PositiveRationalNumber(10.5f), m.getValueAt(3, 1));
		assertEquals(new PositiveRationalNumber(25.5f), m.getValueAt(3, 2));
		
	}
}
