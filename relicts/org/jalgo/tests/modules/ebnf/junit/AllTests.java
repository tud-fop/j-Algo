package org.jalgo.tests.ebnf.junit;


import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite("Test for org.jalgo.tests.ebnf.junit");
		
		//$JUnit-BEGIN$
		suite.addTest(org.jalgo.tests.ebnf.junit.model.ebnf.AllTests.suite());
		suite.addTest(org.jalgo.tests.ebnf.junit.model.wordalgorithm.AllTests.suite());
		suite.addTest(org.jalgo.tests.ebnf.junit.controller.wordalgorithm.AllTests.suite());
		suite.addTest(org.jalgo.tests.ebnf.junit.util.AllTests.suite());
		//$JUnit-END$
		return suite;
	}

}
