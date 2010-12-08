package org.jalgo.module.bfsdfs.algorithms.test;

import static org.junit.Assert.*;

import org.jalgo.module.bfsdfs.algorithms.Algo;
import org.jalgo.module.bfsdfs.algorithms.stack.*;
import org.jalgo.module.bfsdfs.controller.GraphController;

import java.util.*;
import org.junit.Test;

/**
 * Abstraction for unit tests of {@link Algo} implementations.
 * @author Thomas G&ouml;rres
 *
 */
abstract public class AlgoTest {
	private Algo algo;
	
	protected abstract Algo getAlgo();
	
	protected abstract GraphController getGraph();
	
	public AlgoTest() {
		algo = getAlgo();
	}
	
	@Test public void testSteps() {
		int stepNumber = 0;
		Collection<Integer> usedStartNodes = new TreeSet<Integer>();
		
		while (!algo.isFinished()) {
			algo.step();
			stepNumber++;
			usedStartNodes.add(algo.getStartNode());
			
			algo.setSuccessorOrder(algo.getStack().getUntouched());
			
			/*
			 * Asserts that stack content is as expected
			 */
			String expectedStack = null;
			try {
				expectedStack = expectedStack(stepNumber);
			} catch (IndexOutOfBoundsException e) {
				fail("No expected stack content defined for step "+stepNumber+".");
			}
			assertEquals("Step "+stepNumber+" did not work on stack.", expectedStack, stackAsString());
			
			/*
			 * Asserts that tree contains all expected nodes
			 */
			Collection<Integer> expectedTreeNodes = null;
			try {
				expectedTreeNodes = expectedTreeNodes(stepNumber);
			} catch (IndexOutOfBoundsException e) {
				fail("No expected tree content defined for step "+stepNumber+".");
			}
			assertTrue("Step "+stepNumber+" did not work on tree. expected nodes: "+expectedTreeNodes+" but was: "+treeNodesAsString(),
					algo.getTree().getNodesAsInteger().containsAll(expectedTreeNodes));
			
			/*
			 * Assert node distance
			 */
			for (int node: expectedTreeNodes) {
				int expectedDistance = 0;
				try {
					expectedDistance  = expectedDistance(node);
				} catch (IndexOutOfBoundsException e) {
					fail("No node distance defined for node "+node+".");
				}
				assertEquals("Wrong distance for node "+node+".", expectedDistance, algo.getTree().getNodeDistance(node));
			}
			
			/*
			 * Asserts that new edges are as expected
			 */
			for (int treeNode: expectedTreeNodes) {
				if (usedStartNodes.contains(treeNode)) continue;
				
				// get expected predecessor
				int expectedPredecessor = 0;
				try {
					expectedPredecessor = expectedPredecessor(treeNode);
				} catch (IndexOutOfBoundsException e) {
					fail("No expected predecessor defined for tree node "+treeNode+".");
				}
				
				// get actual predecessor
				int actualPredecessor = 0;
				try {
					actualPredecessor = algo.getTree().getPredecessors(treeNode).get(0);
				} catch (IndexOutOfBoundsException e) {
					fail("Tree node "+treeNode+" has no predecessor. Expected: "+expectedPredecessor);
				}
				
				// compare
				assertEquals("Wrong predecessor for tree node "+treeNode+".", expectedPredecessor, actualPredecessor);
			}
		}
	}
	
	@Test public void runAllResetAllRunAll() {
		while (!algo.isFinished())
			algo.step();
		algo.undoAll();
		testSteps();
	}
	
	@Test public void testUndoRedo() {
		String stackBefore1, stackAfter1, treeBefore1, treeAfter1;
		String stackBefore2, stackAfter2, treeBefore2, treeAfter2;
		while(!algo.isFinished()) {
			stackBefore1 = stackAsString();
			treeBefore1 = treeNodesAsString();
			algo.step();
			stackAfter1 = stackAsString();
			treeAfter1 = treeNodesAsString();
			stackBefore2 = stackAsString();
			treeBefore2 = treeNodesAsString();
			algo.step();
			stackAfter2 = stackAsString();
			treeAfter2 = treeNodesAsString();
			
			algo.undo();
			assertEquals("Undo from "+stackAfter2+" did not work on stack.", stackBefore2, stackAsString());
			assertEquals("Undo from "+stackAfter2+" did not work in tree.", treeBefore2, treeNodesAsString());
			algo.undo();
			assertEquals("Undo from "+stackAfter1+" did not work on stack.", stackBefore1, stackAsString());
			assertEquals("Undo from "+stackAfter1+" did not work in tree.", treeBefore1, treeNodesAsString());
			algo.redo();
			assertEquals("Redo did not work on stack", stackAfter1, stackAsString());
			assertEquals("Redo did not work in tree", treeAfter1, treeNodesAsString());
			algo.redo();
			assertEquals("Redo did not work on stack", stackAfter2, stackAsString());
			assertEquals("Redo did not work in tree", treeAfter2, treeNodesAsString());
		}		
		algo.undoAll();

		assertFalse("All steps undone, algorithm still running.", algo.isRunning());
		assertFalse("All steps undone, algorithm still finished.", algo.isFinished());
//		assertTrue("Stack is not empty after reset.", algo.getStack().isEmpty());
		assertTrue("Tree is not empty after reset.", algo.getTree().getNodes().isEmpty());
	}
	
	private String stackAsString() {
		return algo.getStack().toString();
	}
	
	private String treeNodesAsString() {
		return algo.getTree().getNodesAsInteger().toString();
	}
	
	/**
	 * Returns the expected stack content after the specified step.
	 * @param stepNumber number of the step. First step is <code>1</code>!
	 * @return expected stack content, formatted as in {@link NodeQueueStack#toString()}
	 */
	protected abstract String expectedStack(int stepNumber) throws IndexOutOfBoundsException;

	
	/**
	 * Returns the expected tree content after the specified step.
	 * @param stepNumber number of the step. First step is <code>1</code>!
	 * @return indices of the nodes expected in the tree
	 */
	protected abstract Collection<Integer> expectedTreeNodes(int stepNumber) throws IndexOutOfBoundsException;
	
	/**
	 * Returns the expected distance for the specified node
	 * @param nodeIndex index of the node
	 * @return epxected distance for that node
	 */
	protected abstract int expectedDistance(int nodeIndex) throws IndexOutOfBoundsException;
	
	/**
	 * Returns the expected predecessor of the specified tree node
	 * @param nodeIndex
	 * @return
	 */
	protected abstract int expectedPredecessor(int nodeIndex) throws IndexOutOfBoundsException;
	
	@Test public void testDefaultStartNode() {
		assertEquals("Default start node is not 1.", 1, algo.getStartNode());
	}

	@Test public void setInvalidStartNode() {
		// find an invalid node
		int invalidNode = -999;
		while (algo.getTree().getNodesAsInteger().contains(invalidNode))
			invalidNode--;

		try {
			algo.setStartNode(invalidNode);
		} catch(NoSuchElementException e) {
			/* expected exception */
			return;
		}
		fail("Setting invalid start node threw no IndexOutOfBoundsException.");
	}
}
