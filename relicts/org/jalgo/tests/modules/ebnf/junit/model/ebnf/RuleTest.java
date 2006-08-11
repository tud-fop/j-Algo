package org.jalgo.tests.ebnf.junit.model.ebnf;

import org.jalgo.module.ebnf.model.ebnf.*;


import junit.framework.TestCase;

public class RuleTest extends TestCase {

	Term t1, t2, t3, t4, t5, t6, t7, t8;
	Rule r1, r2, r3, r4, r5, r6, r7, r8;
	EVariable v1, v2, v3, v4, v5, v6, v7, v8;
	
	protected void setUp() throws Exception {
		
		// create the variables
		v1 = new EVariable("var1");
		v2 = new EVariable("var2");
		v3 = new EVariable("var3");
		v4 = new EVariable("var4");
		v5 = new EVariable("var5");
		v6 = new EVariable("var6");
		v7 = new EVariable("var7");
		v8 = new EVariable("var8");
		
		
		// create the terms
		t1 = new ETerminalSymbol("term1");
		t2 = new EOption(t1);
		t2 = new EOption(t2);
		t3 = new EAlternative(t1,v1);
		java.util.List<Term> list = new java.util.ArrayList<Term>();
		list.add(t1);
		list.add(v1);
		list.add(t2);
		list.add(t3);
		t4 = new EConcatenation(list);
		t5 = new ERepetition(t3);
		t6 = new EAlternative(list);
		t7 = new EAlternative(t6,t3);
		t8 = new EAlternative(t6,t7);
		
		r1 = new Rule(v1,t1);
		r2 = new Rule(v2,t2);
		r3 = new Rule(v3,t3);
		r4 = new Rule(v4,t4);
		r5 = new Rule(v5,t5);
		r6 = new Rule(v6,t6);
		r7 = new Rule(v7,t7);
		r8 = new Rule(v8,t8);
		
		super.setUp();
	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Rule.Rule(EVariable, Term)'
	 */
	public void testRule() {
		// create the rules
		try {
			r1 = new Rule(v1,t1);
			r2 = new Rule(v2,t2);
			r3 = new Rule(v3,t3);
			r4 = new Rule(v4,t4);
			r5 = new Rule(v5,t5);
		} catch (DefinitionFormatException e) {
			fail("creation of rules failed");
		}

		try {
			r1 = new Rule(null, t1);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException e) {
			
		}

		try {
			r1 = new Rule(v1, null);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException e) {
			
		}
	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Rule.isStrict()'
	 */
	public void testIsStrict() {
		assertTrue(r1.isStrict());
		assertTrue(r2.isStrict());
		assertTrue(r3.isStrict());
		assertTrue(r4.isStrict());
		assertTrue(r5.isStrict());
		
		assertFalse(r6.isStrict());
		assertFalse(r7.isStrict());
		assertFalse(r8.isStrict());
	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Rule.getStrict()'
	 */
	public void testGetStrict() {
		// this should be the same as in the toString test
		try {
			assertEquals("var1 ::= term1", r1.getStrict().toString());
			assertEquals("var2 ::= [[term1]]",r2.getStrict().toString());
			assertEquals("var3 ::= (term1|var1)",r3.getStrict().toString());
			assertEquals("var4 ::= term1var1[[term1]](term1|var1)",
					r4.getStrict().toString());
			assertEquals("var5 ::= {(term1|var1)}",r5.getStrict().toString());
		} catch (DefinitionFormatException e) {
			fail("unhandled DefinitionFormatException in Rule.getStrict");
		}

		try {
			assertEquals("var6 ::= (term1|(var1|([[term1]]|(term1|var1))))",
					r6.getStrict().toString());
			assertEquals("var7 ::= ((term1|(var1|([[term1]]|(term1|var1))))"
					+ "|(term1|var1))",r7.getStrict().toString());
			assertEquals("var8 ::= ((term1|(var1|([[term1]]|(term1|var1))))"
					+ "|((term1|(var1|([[term1]]|(term1|var1))))|(term1|var1)))"
					,r8.getStrict().toString());
		} catch (DefinitionFormatException e) {
			fail("unhandled DefinitionFormatException in Rule.getStrict");
		}
		
	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Rule.toString()'
	 */
	public void testToString() {
		assertEquals("var1 ::= term1", r1.toString());
		assertEquals("var2 ::= [[term1]]",r2.toString());
		assertEquals("var3 ::= (term1|var1)",r3.toString());
		assertEquals("var4 ::= term1var1[[term1]](term1|var1)",r4.toString());
		assertEquals("var5 ::= {(term1|var1)}",r5.toString());
		assertEquals("var6 ::= (term1|var1|[[term1]]|(term1|var1))",
				r6.toString());
		assertEquals("var7 ::= ((term1|var1|[[term1]]|(term1|var1))"
				+ "|(term1|var1))",r7.toString());
		assertEquals("var8 ::= ((term1|var1|[[term1]]|(term1|var1))"
				+ "|((term1|var1|[[term1]]|(term1|var1))|(term1|var1)))"
				,r8.toString());
	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Rule.getName()'
	 */
	public void testGetName() {
		assertEquals(v1,r1.getName());
		assertEquals(v2,r2.getName());
		assertEquals(v3,r3.getName());
		assertEquals(v4,r4.getName());
		assertEquals(v5,r5.getName());
		

	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Rule.setName(EVariable)'
	 */
	public void testSetName() {
		try {
			r1.setName(v2);
		} catch (DefinitionFormatException e) {
			fail("unhandled DefinitionFormatException in Rule.setName");
		}
		assertEquals(v2,r1.getName());
		
		try {
			r1.setName(null);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException e) {
			
		}

	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Rule.getTerm()'
	 */
	public void testGetTerm() {
		assertEquals(t1, r1.getTerm());
		assertEquals(t2, r2.getTerm());
		assertEquals(t3, r3.getTerm());
		assertEquals(t4, r4.getTerm());
		assertEquals(t5, r5.getTerm());

	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Rule.setTerm(Term)'
	 */
	public void testSetTerm() {
		try {
			r1.setTerm(t2);
		} catch (DefinitionFormatException e) {
			fail("unhandled DefinitionFormatException in Rule.setName");
		}
		assertEquals(t2,r1.getTerm());
		
		try {
			r1.setTerm(null);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException e) {
			
		}

	}

}
