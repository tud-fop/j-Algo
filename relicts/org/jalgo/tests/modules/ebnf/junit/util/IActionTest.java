package org.jalgo.tests.ebnf.junit.util;

import org.jalgo.module.ebnf.util.IAction;

public class IActionTest implements IAction {

	ControllerTest myController;

	String undoneString;

	String performString;

	/*
	 * undoneString and performString are used to test the right perform-methods
	 * of diferent IActionsTests.
	 */
	public IActionTest(ControllerTest aController, String performString, String undoneString) {
		myController = aController;
		this.undoneString = undoneString;
		this.performString = performString;
	}

	public void perform() {
		myController.myString = performString;
		System.out.println("Action " + performString + ".");
	}

	public void undo() {
		myController.myString = undoneString;
		System.out.println("Action " + undoneString + ".");
	}

	public String getUndoneString() {
		return undoneString;
	}

	public String getPerformString() {
		return performString;
	}
}
