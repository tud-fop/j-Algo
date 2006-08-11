package org.jalgo.tests.ebnf.junit.util;

import org.jalgo.module.ebnf.util.ActionStack;
import org.jalgo.module.ebnf.util.IAction;

import junit.framework.TestCase;

public class ActionStackTest extends TestCase {

	ControllerTest myController;

	ActionStack myActionStack;

	IAction firstAction, secondAction, thirdAction, fourthAction, fifthAction;

	protected void setUp() {
		/* A Controller used as a Model to perform some IActions */
		myController = new ControllerTest();

		/* An ActionStack to check the work of the ActionStacks. */
		myActionStack = new ActionStack(myController);

		/*
		 * Create some Actions Because IAction is an Interface, there is an
		 * TestAction class in this package.
		 */
		firstAction = new IActionTest(myController, "performed 1", "undone 1");
		secondAction = new IActionTest(myController, "performed 2", "undone 2");
		thirdAction = new IActionTest(myController, "performed 3", "undone 3");
		fourthAction = new IActionTest(myController, "performed 4", "undone 4");
		fifthAction = new IActionTest(myController, "performed 5", "undone 5");

	}
	
	protected void tearDown() {
		myActionStack = null;
		myController = null;
		firstAction = null;
		secondAction = null;
		thirdAction = null;
		fourthAction = null;
		fifthAction = null;
	}

	public void testIsUndoPossible() throws Exception {
		try {
			assertFalse(myActionStack.isUndoPossible());
			
			myActionStack.perform(firstAction);
			System.out.println(myController.myString);
			/* Undo is now possible */
			assertTrue(myActionStack.isUndoPossible());
			
			myActionStack.undo();
			System.out.println(myController.myString);
			/* Undo is not possible any longer */
			assertFalse(myActionStack.isUndoPossible());
			/* Undo is now possible */
			
			myActionStack.redo();
			System.out.println(myController.myString);
			assertTrue(myActionStack.isUndoPossible());
	
			System.out.println("testIsUndoPossbile finished.");
			System.out.println();
		}
		catch (Exception e) {
			throw e;
		}
	}

	public void testIsRedoPossible() throws Exception {
		try {
			assertFalse(myActionStack.isRedoPossible());
			myActionStack.perform(firstAction);
			assertFalse(myActionStack.isRedoPossible());
			myActionStack.undo();
			assertTrue(myActionStack.isRedoPossible());
			myActionStack.redo();
			assertFalse(myActionStack.isRedoPossible());
	
			System.out.println("testIsRedoPossbile finished.");
			System.out.println();
		}
		catch (Exception e) {
			throw e;
		}
}

	public void testUndo() throws Exception {
		try {
			myActionStack.perform(firstAction);
			myActionStack.perform(secondAction);
			myActionStack.undo();
			assertEquals("undone 2", myController.myString);
			myActionStack.perform(thirdAction);
			myActionStack.undo();
			assertEquals("undone 3", myController.myString);
			myActionStack.undo();
			assertEquals("undone 1", myController.myString);
	
			System.out.println("testUndo finished.");
			System.out.println();
		}
		catch (Exception e) {
			throw e;
		}
}

	public void testRedo() throws Exception {
		try {
			/* On RedoStack: { } */
			myActionStack.perform(firstAction);
			myActionStack.undo();
			/* On RedoStack: { Action 1 } */
		
			myActionStack.redo();
			assertEquals("performed 1", myController.myString);
			myActionStack.perform(secondAction);
			myActionStack.undo();
			/* On RedoStack: { Action 2 } */
		
			myActionStack.undo();
			/* On RedoStack: { Action 1, Action 2 } */
		
			myActionStack.redo();
			/* On RedoStack: { Action 2 } */
			assertEquals("performed 1", myController.myString);
		
			myActionStack.redo();
			/* On RedoStack: { } */
			assertEquals("performed 2", myController.myString);
		
			myActionStack.perform(thirdAction);
			myActionStack.undo();
			/* On RedoStack: { Action 3 } */
			
			myActionStack.undo();
			/* On RedoStack: { Action 2, Action 3 } */
		
			myActionStack.redo();
			/* On RedoStack: { Action 3 } */
			assertEquals("performed 2", myController.myString);
		
			myActionStack.undo();
			/* On RedoStack: { Action2, Action 3 } */
		
			myActionStack.perform(thirdAction);
			/* On RedoStack: { } */
		
			myActionStack.undo();
			/* On RedoStack: { Action 3 } */
		
			myActionStack.undo();
			/* On RedoStack: { Action1, Action 3 } */
		
			myActionStack.redo();
			/* On RedoStack: { Action 3 } */
			assertEquals("performed 1", myController.myString);
		
			myActionStack.redo();
			/* On RedoStack: { } */
			assertEquals("performed 3", myController.myString);
		
			System.out.println("testRedo finished.");
			System.out.println();
		}
		catch (Exception e) {
			throw e;
		}
	}

	public void testPerform() throws Exception {
		try {
			myActionStack.perform(firstAction);
			assertEquals("performed 1", myController.myString);
			myActionStack.perform(secondAction);
			assertEquals("performed 2", myController.myString);
			myActionStack.perform(thirdAction);
			assertEquals("performed 3", myController.myString);
			myActionStack.perform(fourthAction);
			assertEquals("performed 4", myController.myString);
			myActionStack.perform(fifthAction);
			assertEquals("performed 5", myController.myString);
	
			System.out.println("testPerform finished.");
			System.out.println();
		}
		catch (Exception e) {
			throw e;
		}
	}

	public void testObservers() throws Exception {
		try {
			assertEquals(0, myController.observerTest);
			myActionStack.perform(firstAction);
			/* Undo is now possible */
			assertEquals(1, myController.observerTest);
			myActionStack.perform(secondAction);
			assertEquals(1, myController.observerTest);
			myActionStack.perform(thirdAction);
			assertEquals(1, myController.observerTest);
			myActionStack.perform(fourthAction);
			assertEquals(1, myController.observerTest);
			myActionStack.undo();
			/* Redo is now possible */
			assertEquals(2, myController.observerTest);
			myActionStack.redo();
			/* Redo is now not possible any longer */
			assertEquals(3, myController.observerTest);
			myActionStack.undo();
			/* Redo is now possible */
			assertEquals(4, myController.observerTest);
			myActionStack.perform(fifthAction);
			/* Redo is now not possible any longer */
			assertEquals(5, myController.observerTest);
	
			System.out.println("testObservers finished.");
			System.out.println();
		}
		catch (Exception e) {
			throw e;
		}
	}
}
