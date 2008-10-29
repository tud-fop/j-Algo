package org.jalgo.module.app.coreTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.jalgo.module.app.core.graph.Edge;
import org.jalgo.module.app.core.graph.Graph;
import org.jalgo.module.app.core.graph.Node;
import org.junit.Before;
import org.junit.Test;

public class GraphTest {
	Graph graph;
	Node n1,n2,n3;
	Edge e1,e2,e3,e4;
	
	public static List<Node> getOrderedNodeList(Set<Node> nodeSet) {
		List<Node> nodeList;
		nodeList = new ArrayList<Node>();
				
		for (int i = 0; i < nodeSet.size(); i++) {
			nodeList.add(null);
		}
		for (Node n : nodeSet) {
			nodeList.set(n.getId(), n);
		}
		
		return nodeList;
	}
	
	@Before
	public void setUp() {
		graph = new Graph();
		n1 = graph.newNode();
		n2 = graph.newNode();
		n3 = graph.newNode();
		
		e1 = graph.newEdge(n1, n2,null,true);
		e2 = graph.newEdge(n2, n1,null,true);
		e3 = graph.newEdge(n2, n3,null,true);
		e4 = graph.newEdge(n1, n3,null,true);
	}
	
	@Test
	public void addNodes() {
		assertEquals(0,n1.getId());
		assertEquals(2,n3.getId());
		
		assertNotNull(n2.getLocation());
	}
	
	@Test(expected = NoSuchElementException.class)
	public void deleteNodes() {
		graph.removeNode(n2);
		assertEquals(1,n3.getId());
		assertNotNull(n2);
		graph.removeNode(n2);
		n2 = graph.newNode();
		assertEquals(2,n2.getId());
	}
	
	@Test
	public void testID() {
		for(int i = 0; i < 10; i++) {
			graph.newNode();
		}
		checkNodeID();
		for (int i = 0; i < 5;i++) {
			graph.removeNode(graph.getNode(11-i*2));
		}
		checkNodeID();
		for(int i = 0; i < 7;i++) {
			graph.newNode();
		}
		checkNodeID();
	}
	
	@Test
	public void checkNodeID() {
		for (int i = 0; i < graph.getNodes().size(); i++) {
			assertNotNull(graph.getNode(i));
		}
	}
	
	@Test
	public void addFaultyEdge() {
		try {
			graph.newEdge(n3, n3,null,true);
			throw new RuntimeException();
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		try {
			graph.newEdge(n2, n3,null,true);
			throw new RuntimeException();
		} catch (Exception e) {
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	@Test
	public void removeNodesAndCheckEdges() {
		graph.removeNode(n1);
		assertFalse(graph.getEdges().contains(e1));
		assertEquals(1,graph.getEdges().size());	
	}
}
