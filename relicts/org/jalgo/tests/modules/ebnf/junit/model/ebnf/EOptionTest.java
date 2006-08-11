package org.jalgo.tests.ebnf.junit.model.ebnf;

import org.jalgo.module.ebnf.model.ebnf.*;

import junit.framework.TestCase;

/**
 * 
 * @author Johannes, Tom
 *
 */public class EOptionTest extends TestCase {

	Term t1, t2, t3, t4;
	
	EOption o1, o2, o3, o4, o5;
	
	protected void setUp() throws Exception {
		t1 = new EVariable("var1");
		t2 = new EVariable("var2");
		t3 = new ETerminalSymbol("term1");
		t4 = new ETerminalSymbol("term2");
		
		o1 = new EOption(t1);
		o2 = new EOption(t2);
		o3 = new EOption(t3);
		o4 = new EOption(t4);
		o5 = new EOption(o1);
		
		super.setUp();
	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.EOption.isStrict()'
	 */
	public void testIsStrict() {
		assertEquals(t1.isStrict(),o1.isStrict());
		assertEquals(t2.isStrict(),o2.isStrict());
		assertEquals(t3.isStrict(),o3.isStrict());
		assertEquals(t4.isStrict(),o4.isStrict());
	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.EOption.getStrict()'
	 */
	public void testGetStrict() {
		// since the terms are only variable and terminal symbols, the strict
		// term must contain the same term as the original one.
		
		try {
			assertEquals(o1.getStrict().getTerms().get(0),o1.getTerms().get(0));
		} catch (DefinitionFormatException e) {
			fail("unexpected throws Exception");
		}
		try {
			assertEquals(o2.getStrict().getTerms().get(0),o2.getTerms().get(0));
		} catch (DefinitionFormatException e) {
			fail("unexpected throws Exception");
		}
		try {
			assertEquals(o3.getStrict().getTerms().get(0),o3.getTerms().get(0));
		} catch (DefinitionFormatException e) {
			fail("unexpected throws Exception");
		}
		try {
			assertEquals(o4.getStrict().getTerms().get(0),o4.getTerms().get(0));
		} catch (DefinitionFormatException e) {
			fail("unexpected throws Exception");
		}
		
		// this is tricky, because the the Lists are not equal, only the
		// contianed terminals
		try {
			assertEquals(o5.getStrict().getTerms().get(0)
					.getTerms().get(0),o5.getTerms().get(0).getTerms().get(0));
		} catch (DefinitionFormatException e) {
			fail("unexpected throws Exception");
		}
		
	}

	
	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.EOption.contains(ESymbol)'
	 */
	public void testContains() {
		assertTrue(o1.contains(t1));
		assertTrue(o2.contains(t2));
		assertTrue(o5.contains(t1));
		assertTrue(o5.contains(o1));
		assertFalse(o1.contains(t2));
		assertFalse(o3.contains(t4));
		assertFalse(o5.contains(o2));
	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.EOption.EOption(Term)'
	 */
	public void testEOption() {
		// Constructor with invalid names
		try {
			new EOption(null);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}
		
	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.EOption.setTerm(Term)'
	 */
	public void testSetTerm() {
//		 try to set the term to the Option itself
		try {
			o1.setTerm(o1);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}
		//try to set the term to null
		try {
			o2.setTerm(null);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}
	}
	
	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.EOption.toString()'
	 */
	public void testToString() {

		assertEquals("[var1]", o1.toString());
		assertEquals("[var2]", o2.toString());
		assertEquals("[term1]", o3.toString());
		assertEquals("[term2]", o4.toString());
		assertEquals("[[var1]]", o5.toString());

	}
	
	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.EOption.getTerm()'
	 */
	public void testGetTerm() {
		assertEquals(t1, o1.getTerm());
		assertEquals(t2, o2.getTerm());
		assertEquals(t2, o2.getTerm());
		assertEquals(o1, o5.getTerm());
	}
	
	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.EOption.getTerms()'
	 */
	public void testGetTerms() {
		java.util.List<Term> returnList = new java.util.ArrayList<Term>();
		returnList.add(t1);
		assertEquals(returnList, o1.getTerms());
		returnList.clear();
		returnList.add(t2);
		assertEquals(returnList, o2.getTerms());
		returnList.clear();
		returnList.add(t3);
		assertEquals(returnList, o3.getTerms());
		returnList.clear();
		returnList.add(o1);
		assertEquals(returnList, o5.getTerms());
	}

}
