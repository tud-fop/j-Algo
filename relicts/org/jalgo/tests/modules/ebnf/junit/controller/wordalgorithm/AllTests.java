package org.jalgo.tests.ebnf.junit.controller.wordalgorithm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.jalgo.tests.ebnf.junit.model.ebnf");
		//$JUnit-BEGIN$
		suite.addTestSuite(WordAlgorithmControllerTest.class);
		//$JUnit-END$
		return suite;
	}

}
