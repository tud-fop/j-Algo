package org.jalgo.tests.ebnf.junit.model.wordalgorithm;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.jalgo.tests.ebnf.junit.model.wordalgorithm");
		//$JUnit-BEGIN$
		suite.addTestSuite(WordAlgoModelTest.class);
		//$JUnit-END$
		return suite;
	}

}
