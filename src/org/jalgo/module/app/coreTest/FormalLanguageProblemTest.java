package org.jalgo.module.app.coreTest;


import java.util.List;

import org.jalgo.module.app.core.AvailableSemiRings;
import org.jalgo.module.app.core.Calculation;
import org.jalgo.module.app.core.SemiRing;
import org.jalgo.module.app.core.dataType.formalLanguage.FormalLanguage;
import org.jalgo.module.app.core.graph.Graph;
import org.jalgo.module.app.core.graph.Node;
import org.jalgo.module.app.core.step.RootStep;
import org.jalgo.module.app.core.step.Step;
import org.junit.Before;
import org.junit.Test;

public class FormalLanguageProblemTest {
	RootStep step;
	Graph graph;
	List<Step> steps;
	int size;


	@Before
	public void setUp() throws Exception {
		SemiRing ring;
		Calculation calc;
				
		ring = AvailableSemiRings.getSemiRings().get(AvailableSemiRings.PROCESS_PROBLEM_ID);
		graph = new Graph();
		
		graph.newNodes(4);
		
		List<Node> nodeList;
		nodeList = GraphTest.getOrderedNodeList(graph.getNodes());
		
		graph.newEdge(nodeList.get(1), nodeList.get(3), new FormalLanguage("a"),true);
		graph.newEdge(nodeList.get(1), nodeList.get(0), new FormalLanguage("c"),true);
		graph.newEdge(nodeList.get(0), nodeList.get(2), new FormalLanguage("a"),true);
		graph.newEdge(nodeList.get(2), nodeList.get(1), new FormalLanguage("b"),true);
		graph.newEdge(nodeList.get(3), nodeList.get(0), new FormalLanguage("b"),true);
		
		calc = new Calculation();
		calc.setSemiring(ring);
		calc.setGraph(graph);
		
		step = (RootStep)calc.getRootStep();
		steps = step.getSteps();
		size = graph.getNodes().size();
	}
	
	@Test
	public void checkBeforeMatrix() {
		System.out.println(step.getBeforeMatrix());
		System.out.println(step.getStep(1).getAfterMatrix());
		System.out.println(step.getStep(2).getAfterMatrix());
		System.out.println(step.getStep(3).getAfterMatrix());
		System.out.println(step.getAfterMatrix());
	}

}
