package org.jalgo.tests.ebnf.junit.model.ebnf;

import junit.framework.TestCase;

import org.jalgo.module.ebnf.model.ebnf.DefinitionFormatException;
import org.jalgo.module.ebnf.model.ebnf.ERepetition;
import org.jalgo.module.ebnf.model.ebnf.ETerminalSymbol;
import org.jalgo.module.ebnf.model.ebnf.EVariable;
import org.jalgo.module.ebnf.model.ebnf.Term;

/**
 * 
 * @author Johannes, Tom
 *
 */public class ERepetitionTest extends TestCase {


		Term v1, v2, t1, t2;
		
		ERepetition r1, r2, r3, r4, r5;
		
		protected void setUp() throws Exception {
			v1 = new EVariable("var1");
			v2 = new EVariable("var2");
			t1 = new ETerminalSymbol("term1");
			t2 = new ETerminalSymbol("term2");
			
			r1 = new ERepetition(v1);
			r2 = new ERepetition(v2);
			r3 = new ERepetition(t1);
			r4 = new ERepetition(t2);
			r5 = new ERepetition(r1);
			
			super.setUp();
		}

		/*
		 * Test method for 'org.jalgo.module.ebnf.model.ebnf.ERepetition.isStrict()'
		 */
		public void testIsStrict() {
			assertEquals(v1.isStrict(),r1.isStrict());
			assertEquals(v2.isStrict(),r2.isStrict());
			assertEquals(t1.isStrict(),r3.isStrict());
			assertEquals(t2.isStrict(),r4.isStrict());
		}

		/*
		 * Test method for 'org.jalgo.module.ebnf.model.ebnf.ERepetition.getStrict()'
		 */
		public void testGetStrict() {
			// since the terms are only variable and terminal symbols, the strict
			// term must contain the same term as the original one.
			
			try {
				assertEquals(r1.getStrict().getTerms().get(0),r1.getTerms().get(0));
			} catch (DefinitionFormatException e) {
				fail("unexpected throws Exception");
			}
			try {
				assertEquals(r2.getStrict().getTerms().get(0),r2.getTerms().get(0));
			} catch (DefinitionFormatException e) {
				fail("unexpected throws Exception");
			}
			try {
				assertEquals(r3.getStrict().getTerms().get(0),r3.getTerms().get(0));
			} catch (DefinitionFormatException e) {
				fail("unexpected throws Exception");
			}
			try {
				assertEquals(r4.getStrict().getTerms().get(0),r4.getTerms().get(0));
			} catch (DefinitionFormatException e) {
				fail("unexpected throws Exception");
			}
			
			// this is tricky, because the the Lists are not equal, only the
			// contianed terminals
			try {
				assertEquals(r5.getStrict().getTerms().get(0)
						.getTerms().get(0),r5.getTerms().get(0).getTerms().get(0));
			} catch (DefinitionFormatException e) {
				fail("unexpected throws Exception");
			}
			
		}

		
		/*
		 * Test method for 'org.jalgo.module.ebnf.model.ebnf.ERepetition.contains(ESymbol)'
		 */
		public void testContains() {
			assertTrue(r1.contains(v1));
			assertTrue(r2.contains(v2));
			assertTrue(r5.contains(v1));
			assertTrue(r5.contains(r1));
			assertFalse(r1.contains(v2));
			assertFalse(r3.contains(t2));
			assertFalse(r5.contains(r2));
		}

		/*
		 * Test method for 'org.jalgo.module.ebnf.model.ebnf.ERepetition.ERepetition(Term)'
		 */
		public void testERepetition() {
			// Constructor with invalid names
			try {
				new ERepetition(null);
				fail("Should have raised an DefinitionFormatException");
			} catch (DefinitionFormatException expected) {
			}
			
		}

		/*
		 * Test method for 'org.jalgo.module.ebnf.model.ebnf.ERepetition.setTerm(Term)'
		 */
		public void testSetTerm() {
//			 try to set the term to the Option itself
			try {
				r1.setTerm(r1);
				fail("Should have raised an DefinitionFormatException");
			} catch (DefinitionFormatException expected) {
			}
			//try to set the term to null
			try {
				r2.setTerm(null);
				fail("Should have raised an DefinitionFormatException");
			} catch (DefinitionFormatException expected) {
			}
		}
		
		/*
		 * Test method for 'org.jalgo.module.ebnf.model.ebnf.ERepetition.toString()'
		 */
		public void testToString() {

			assertEquals("{var1}", r1.toString());
			assertEquals("{var2}", r2.toString());
			assertEquals("{term1}", r3.toString());
			assertEquals("{term2}", r4.toString());
			assertEquals("{{var1}}", r5.toString());

		}
		
		/*
		 * Test method for 'org.jalgo.module.ebnf.model.ebnf.ERepetition.getTerm()'
		 */
		public void testGetTerm() {
			assertEquals(v1, r1.getTerm());
			assertEquals(v2, r2.getTerm());
			assertEquals(v2, r2.getTerm());
			assertEquals(r1, r5.getTerm());
		}
		
		/*
		 * Test method for 'org.jalgo.module.ebnf.model.ebnf.ERepetition.getTerms()'
		 */
		public void testGetTerms() {
			java.util.List<Term> returnList = new java.util.ArrayList<Term>();
			returnList.add(v1);
			assertEquals(returnList, r1.getTerms());
			returnList.clear();
			returnList.add(v2);
			assertEquals(returnList, r2.getTerms());
			returnList.clear();
			returnList.add(t1);
			assertEquals(returnList, r3.getTerms());
			returnList.clear();
			returnList.add(r1);
			assertEquals(returnList, r5.getTerms());
		}

	}
