package org.jalgo.tests.ebnf.junit.util;

import java.util.Observer;
import java.util.Observable;

/* This is a Controller which has just a public String.
 * Used in the TestClasses.
 */
public class ControllerTest implements Observer {

	public String myString;

	public int observerTest = 0;

	public void update(Observable o, Object obj) {
		observerTest++;
		System.out.println("Observer notified.");
	}

}
