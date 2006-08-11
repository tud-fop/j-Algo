package org.jalgo.tests.ebnf.junit.model.ebnf;

import junit.framework.Test;
import junit.framework.TestSuite;

public class AllTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(
				"Test for org.jalgo.tests.ebnf.junit.model.ebnf");
		//$JUnit-BEGIN$
		suite.addTestSuite(RuleTest.class);
		suite.addTestSuite(EOptionTest.class);
		suite.addTestSuite(ERepetitionTest.class);
		suite.addTestSuite(EVariableTest.class);
		suite.addTestSuite(ETerminalSymbolTest.class);
		suite.addTestSuite(EAlternativeTest.class);
		//$JUnit-END$
		return suite;
	}

}
