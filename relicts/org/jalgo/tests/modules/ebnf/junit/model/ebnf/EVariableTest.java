package org.jalgo.tests.ebnf.junit.model.ebnf;

import org.jalgo.module.ebnf.model.ebnf.DefinitionFormatException;
import org.jalgo.module.ebnf.model.ebnf.EVariable;
import junit.framework.TestCase;

public class EVariableTest extends TestCase {

	EVariable v1, v2, v3, v4, v5, v6;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {

		v1 = new EVariable("a");
		v2 = new EVariable("IchBinEinTerminalsymbol");
		v3 = new EVariable("Ich auch");
		v4 = new EVariable(" x");
		v5 = new EVariable("!!!!!!");
		v6 = new EVariable("a");

		super.setUp();
	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Terminal.isStrict()'
	 */
	public void testIsStrict() {

		assertTrue(v1.isStrict());
		assertTrue(v2.isStrict());
		assertTrue(v3.isStrict());
		assertTrue(v4.isStrict());
		assertTrue(v5.isStrict());

	}
	

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Terminal.isStrict()'
	 */
	public void testGetStrict() {

		assertEquals(v1,v1.getStrict());
		assertEquals(v2,v2.getStrict());
		assertEquals(v3,v3.getStrict());
		assertEquals(v4,v4.getStrict());
		assertEquals(v5,v5.getStrict());

	}	

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Terminal.isStrict()'
	 */
	public void testContains() {

		assertTrue(v1.contains(v1));
		assertTrue(v2.contains(v2));
		assertTrue(v3.contains(v3));
		assertFalse(v4.contains(v3));
		assertFalse(v5.contains(v1));
		assertFalse(v1.contains(v6));
	}	
	
	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Terminal.toString()'
	 */
	public void testToString() {

		assertEquals("a", v1.toString());
		assertEquals("IchBinEinTerminalsymbol", v2.toString());
		assertEquals("Ich auch", v3.toString());
		assertEquals(" x", v4.toString());
		assertEquals("!!!!!!", v5.toString());

	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Terminal.toString()'
	 */

	/*
	 * Test method for
	 * 'org.jalgo.module.ebnf.model.ebnf.Terminal.Terminal(String)'
	 */
	public void testEVariable() {

		// Constructor with valid names
		assertEquals("a", v1.toString());
		assertEquals("IchBinEinTerminalsymbol", v2.toString());
		assertEquals("Ich auch", v3.toString());
		assertEquals(" x", v4.toString());
		assertEquals("!!!!!!", v5.toString());

		// Constructor with invalid names
		try {
			new EVariable(null);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}

		try {
			new EVariable("");
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}

		try {
			new EVariable(" ");
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}
	}

	/*
	 * Test method for
	 * 'org.jalgo.module.ebnf.model.ebnf.Terminal.setName(String)'
	 */
	public void testSetName() {

		try {
			v1.setName("another name");
		} catch (DefinitionFormatException unexpected) {
			fail("unexpected throws Exception");
		}

		assertEquals("another name", v1.getName());

		// Constructor with invalid names
		try {
			v3.setName(null);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}

		try {
			v4.setName("");
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}

		try {
			v5.setName(" ");
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}
	}

}
