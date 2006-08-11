package org.jalgo.tests.ebnf.junit.util;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.jalgo.tests.ebnf.junit.model.ebnf");
		//$JUnit-BEGIN$
		suite.addTestSuite(ActionStackTest.class);
		//$JUnit-END$
		return suite;
	}

}
