package org.jalgo.tests.ebnf.junit.model.ebnf;

import junit.framework.TestCase;

import org.jalgo.module.ebnf.model.ebnf.DefinitionFormatException;
import org.jalgo.module.ebnf.model.ebnf.EAlternative;
import org.jalgo.module.ebnf.model.ebnf.EOption;
import org.jalgo.module.ebnf.model.ebnf.ERepetition;
import org.jalgo.module.ebnf.model.ebnf.ETerminalSymbol;
import org.jalgo.module.ebnf.model.ebnf.EVariable;
import org.jalgo.module.ebnf.model.ebnf.Term;

public class EAlternativeTest extends TestCase{
	
	Term v1, v2, t1, t2;
	
	EAlternative a1, a2, a3, a4, a5;
	EOption o1;
	ERepetition r1;
	
	protected void setUp() throws Exception {
		java.util.List<Term> terms = new java.util.ArrayList<Term>();
		v1 = new EVariable("var1");
		v2 = new EVariable("var2");
		t1 = new ETerminalSymbol("term1");
		t2 = new ETerminalSymbol("term2");
		o1 = new EOption(v1);
		r1 = new ERepetition(t2);
		
		a1 = new EAlternative(v1,t1);
		terms.clear();
		terms.add(v2);
		terms.add(t2);
		terms.add(t2);
		a2 = new EAlternative(terms);
		terms.clear();
		terms.add(v2);
		terms.add(v1);
		terms.add(t1);
		terms.add(o1);
		a3 = new EAlternative(terms);
		terms.clear();
		terms.add(t1);
		terms.add(r1);
		a4 = new EAlternative(terms);
		terms.clear();
		terms.add(o1);
		terms.add(r1);
		terms.add(a2);
		a5 = new EAlternative(terms);
		
		super.setUp();
	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.EAlternative.isStrict()'
	 */
	public void testIsStrict() {
		assertTrue(a1.isStrict());
		assertFalse(a2.isStrict());
		assertFalse(a3.isStrict());
		assertTrue(a4.isStrict());
		assertFalse(a5.isStrict());
	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.EAlternative.getStrict()'
	 */
	public void testGetStrict() {
		// now here have to test if getStrict *really* works for the first time
		
		// now a1 should be strict already
		try {
			assertEquals(a1.getStrict().getTerms().get(0),a1.getTerms().get(0));
			assertEquals(a1.getStrict().getTerms().get(1),a1.getTerms().get(1));
		} catch (DefinitionFormatException e) {
			fail("unexpected throws Exception");
		}
		
		// a2 (v2, t2, t2) isn't strict at the moment - so there should be a structure change
		try {
			assertEquals("(var2|(term2|term2))",a2.getStrict().toString());
		} catch (DefinitionFormatException e) {
			fail("unexpected throws Exception");
		}
		// same thing here with a3 (v2,v1,t1,o1(v1))
		try {
			assertEquals("(var2|(var1|(term1|[var1])))",a3.getStrict().toString());
		} catch (DefinitionFormatException e) {
			fail("unexpected throws Exception");
		}
		
		// and a4 is already binary
		try {
			assertEquals(a4.toString(),a4.getStrict().toString());
		} catch (DefinitionFormatException e) {
			fail("unexpected throws Exception");
		}
		
//		 same thing here with a5 (o1(v1), r(t2), a2(v2, t2, t2))
		try {
			assertEquals("([var1]|({term2}|(var2|(term2|term2))))",a5.getStrict().toString());
		} catch (DefinitionFormatException e) {
			fail("unexpected throws Exception");
		}
		
	}

	
	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.EAlternative.contains(ESymbol)'
	 */
	public void testContains() {
		assertTrue(a1.contains(v1));
		assertTrue(a2.contains(v2));
		assertTrue(a5.contains(v1));
		assertTrue(a5.contains(a2));
		assertFalse(a1.contains(v2));
		assertFalse(a3.contains(t2));
		assertFalse(a5.contains(a1));
	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.EAlternative.EAlternative(Term)'
	 */
	public void testEAlternative() {
		java.util.List<Term> terms = new java.util.ArrayList<Term>();
		// Constructor with valid names
		try {
			new EAlternative(v1,v2);
		} catch (DefinitionFormatException e) {
			fail("unexpected throws Exception");
		}
		try {
			new EAlternative(o1,a2);
		} catch (DefinitionFormatException e) {
			fail("unexpected throws Exception");
		}
		
		
//		 Constructor with invalid names
		try {
			new EAlternative(null);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}
		
		terms.clear();
		terms.add(v1);
		try {
			new EAlternative(terms);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}
		
		terms.clear();
		terms.add(null);
		terms.add(null);
		terms.add(null);
		try {
			new EAlternative(terms);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}
	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.EAlternative.addTerm(Term)'
	 */
	public void testAddTerm() {
		EAlternative a6 = null;
		
		// try to get new EAlternative
		try {
			a6 = new EAlternative(t1,t2);
		} catch (DefinitionFormatException e) {
			fail("unexpected throws Exception");
		}
		
//		 try to add correct terms
		try {
			a6.addTerm(v1);
			if (!a6.contains(v1)) fail("Should contain v1");
		} catch (DefinitionFormatException expected) {
			fail("unexpected throws Exception");
		}
		
		//try to set the term to null
		try {
			a6.addTerm(null);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}
//		try to set the term to itself
		try {
			a6.addTerm(a6);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}
	}
	
	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.EAlternative.toString()'
	 */
	public void testToString() {

		assertEquals("(var1|term1)", a1.toString());
		assertEquals("(var2|term2|term2)", a2.toString());
		assertEquals("(var2|var1|term1|[var1])", a3.toString());
		assertEquals("(term1|{term2})", a4.toString());
		assertEquals("([var1]|{term2}|(var2|term2|term2))", a5.toString());

	}
	
	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.EAlternative.getTerms()'
	 */
	public void testGetTerms() {
		//Check for various list contents if they are equal to the terms got from the function
		java.util.List<Term> returnList = new java.util.ArrayList<Term>();
		returnList.clear();
		returnList.add(v1);
		returnList.add(t1);
		assertEquals(returnList, a1.getTerms());
		returnList.clear();
		returnList.add(v2);
		returnList.add(t2);
		returnList.add(t2);
		assertEquals(returnList, a2.getTerms());
		returnList.clear();
		returnList.add(v2);
		returnList.add(v1);
		returnList.add(t1);
		assertNotSame(returnList, a3.getTerms());
		returnList.add(o1);
		assertEquals(returnList, a3.getTerms());
		returnList.clear();
		returnList.add(a1);
		returnList.add(o1);
		returnList.add(r1);
		assertNotSame(returnList, a5.getTerms());
		returnList.clear();
		returnList.add(o1);
		returnList.add(r1);
		returnList.add(a2);
		assertEquals(returnList, a5.getTerms());
	}

}
