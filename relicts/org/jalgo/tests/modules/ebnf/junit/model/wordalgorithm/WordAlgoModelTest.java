package org.jalgo.tests.ebnf.junit.model.wordalgorithm;

import org.jalgo.module.ebnf.model.syndia.SynDiaSystem;
import org.jalgo.module.ebnf.model.syndia.SynDiaSystemLibrary;
import org.jalgo.module.ebnf.model.syndia.Variable;
import org.jalgo.module.ebnf.model.wordalgorithm.WordAlgoModel;
import junit.framework.TestCase;

public class WordAlgoModelTest extends TestCase {

	SynDiaSystem consistentSystem;

	SynDiaSystem inconsistentSystem;

	WordAlgoModel myModel;

	protected void setUp() {
		consistentSystem = SynDiaSystemLibrary.getSynDiaSystem2();
		inconsistentSystem = new SynDiaSystem();
		try {
			System.out.println(consistentSystem.getStartDiagram());
			myModel = new WordAlgoModel(consistentSystem);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
	
	public void testConsistenceFailure() {
		boolean initializationFailed = false;
		try {
			@SuppressWarnings("unused") WordAlgoModel newModel = new WordAlgoModel(inconsistentSystem);
		}
		catch (Exception e) {
			initializationFailed = true;
		}
		assertTrue(initializationFailed);
	}
	
	public void testIsAlgorithmRunning() {
		assertFalse(myModel.isAlgorithmRunning());
		myModel.enableAlgorithmRunning();
		assertTrue(myModel.isAlgorithmRunning());
		myModel.disableAlgorithmRunning();
		assertFalse(myModel.isAlgorithmRunning());
		myModel.enableAlgorithmRunning();
		assertTrue(myModel.isAlgorithmRunning());
		try {
			myModel.reset();
			assertFalse(myModel.isAlgorithmRunning());
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void testIsWarningsOn() {
		assertTrue(myModel.isWarningsOn());
		myModel.disableWarnings();
		assertFalse(myModel.isWarningsOn());
		try {
			myModel.reset();
			assertTrue(myModel.isWarningsOn());
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void testIsStackEmpty() {
		assertTrue(myModel.isStackEmpty());
		myModel.pushToStack(new Variable(null, "a", null));
		assertFalse(myModel.isStackEmpty());
		myModel.pushToStack(new Variable(null, "a", null));
		assertFalse(myModel.isStackEmpty());
		myModel.popFromStack();
		assertFalse(myModel.isStackEmpty());
		myModel.popFromStack();
		assertTrue(myModel.isStackEmpty());
		myModel.pushToStack(new Variable(null, "a", null));
		try {
			myModel.reset();
			assertTrue(myModel.isStackEmpty());
		}
		catch (Exception e) {
			System.out.println(e);
		}
		myModel.pushToStack(new Variable(null, "a", null));
		assertFalse(myModel.isStackEmpty());
		myModel.emptyStack();
		assertTrue(myModel.isStackEmpty());
	}

	// Getters and Setters are not tested
}
