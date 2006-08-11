package org.jalgo.tests.ebnf.junit.model.ebnf;

import org.jalgo.module.ebnf.model.ebnf.DefinitionFormatException;
import org.jalgo.module.ebnf.model.ebnf.ETerminalSymbol;
import junit.framework.TestCase;

public class ETerminalSymbolTest extends TestCase {

	ETerminalSymbol t1, t2, t3, t4, t5, t6;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {

		t1 = new ETerminalSymbol("a");
		t2 = new ETerminalSymbol("IchBinEinTerminalsymbol");
		t3 = new ETerminalSymbol("Ich auch");
		t4 = new ETerminalSymbol(" x");
		t5 = new ETerminalSymbol("!!!!!!");

		super.setUp();
	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Terminal.isStrict()'
	 */
	public void testIsStrict() {

		assertTrue(t1.isStrict());
		assertTrue(t2.isStrict());
		assertTrue(t3.isStrict());
		assertTrue(t4.isStrict());
		assertTrue(t5.isStrict());

	}
	

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Terminal.isStrict()'
	 */
	public void testGetStrict() {

		assertEquals(t1,t1.getStrict());
		assertEquals(t2,t2.getStrict());
		assertEquals(t3,t3.getStrict());
		assertEquals(t4,t4.getStrict());
		assertEquals(t5,t5.getStrict());

	}	

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Terminal.isStrict()'
	 */
	public void testContains() {

		assertTrue(t1.contains(t1));
		assertTrue(t2.contains(t2));
		assertTrue(t3.contains(t3));
		assertFalse(t4.contains(t3));
		assertFalse(t5.contains(t1));
		assertFalse(t1.contains(t6));
	}	
	
	
	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Terminal.toString()'
	 */
	public void testToString() {

		assertEquals("a", t1.toString());
		assertEquals("IchBinEinTerminalsymbol", t2.toString());
		assertEquals("Ich auch", t3.toString());
		assertEquals(" x", t4.toString());
		assertEquals("!!!!!!", t5.toString());

	}

	/*
	 * Test method for 'org.jalgo.module.ebnf.model.ebnf.Terminal.toString()'
	 */

	/*
	 * Test method for
	 * 'org.jalgo.module.ebnf.model.ebnf.Terminal.Terminal(String)'
	 */
	public void testTerminal() {

		// Constructor with valid names
		assertEquals("a", t1.toString());
		assertEquals("IchBinEinTerminalsymbol", t2.toString());
		assertEquals("Ich auch", t3.toString());
		assertEquals(" x", t4.toString());
		assertEquals("!!!!!!", t5.toString());

		// Constructor with invalid names
		try {
			new ETerminalSymbol(null);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}

		try {
			new ETerminalSymbol("");
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}

		try {
			new ETerminalSymbol(" ");
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
			t1.setName("another name");
		} catch (DefinitionFormatException unexpected) {
			fail("unexpected throws Exception");
		}

		assertEquals("another name", t1.getName());

		// Constructor with invalid names
		try {
			t3.setName(null);
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}

		try {
			t4.setName("");
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}

		try {
			t5.setName(" ");
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}
		
		try {
			t5.setName("         ");
			fail("Should have raised an DefinitionFormatException");
		} catch (DefinitionFormatException expected) {
		}
	}

}
