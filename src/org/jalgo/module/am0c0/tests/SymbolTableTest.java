/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2010 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jalgo.module.am0c0.tests;

import static org.junit.Assert.*;

import org.jalgo.module.am0c0.model.c0.trans.Symbol;
import org.jalgo.module.am0c0.model.c0.trans.Symbol.SymbolType;
import org.jalgo.module.am0c0.model.c0.trans.SymbolException;
import org.jalgo.module.am0c0.model.c0.trans.SymbolTable;
import org.junit.Test;

/**
 * Unit tests for {@link SymbolTable}.
 * 
 * @author Felix Schmitt
 * @author Martin Morgenstern
 */
public class SymbolTableTest {

	@Test
	public void testAdd1() {
		try {
			SymbolTable table = new SymbolTable();
			Symbol symbol1 = Symbol.varSymbol("x", 1);
			table.add(symbol1);
			Symbol symbol2 = Symbol.varSymbol("x", 2);

			table.add(symbol2);
			fail("Should have raised an exception");
		} catch (SymbolException e) {

		}
	}

	@Test
	public void testAdd2() {
		try {
			SymbolTable table = new SymbolTable();
			Symbol symbol1 = Symbol.varSymbol("x", 1);
			table.add(symbol1);
			Symbol symbol2 = Symbol.varSymbol("y", 1);

			table.add(symbol2);
			fail("Should have raised an exception");
		} catch (SymbolException e) {

		}
	}

	@Test
	public void testAdd3() {
		try {
			SymbolTable table = new SymbolTable();
			Symbol symbol1 = Symbol.constSymbol("x", 1);
			table.add(symbol1);
			Symbol symbol2 = Symbol.constSymbol("y", 1);

			table.add(symbol2);
		} catch (SymbolException e) {
			fail("Should not have raised an exception");
		}
	}

	@Test
	public void testGetType() {
		SymbolTable table = new SymbolTable();
		try {
			table.getType("x");
			fail("Should have raised an exception");
		} catch (SymbolException e) {

		}
	}

	@Test
	public void testGetAddress() {
		SymbolTable table = new SymbolTable();
		try {
			table.getAddress("x");
			fail("Should have raised an exception");
		} catch (SymbolException e) {

		}
	}

	@Test
	public void testInsertedConstSymbol() throws SymbolException {
		final String id = "id";
		final int value = 42;
		final Symbol sym = Symbol.constSymbol(id, value);
		final SymbolTable tab = new SymbolTable();

		tab.add(sym);

		assertTrue(tab.exists(id));
		assertTrue(tab.exists(new String(id)));
		assertEquals(value, tab.getValue(id));
		assertEquals(SymbolType.ST_CONST, tab.getType(id));
	}

	@Test
	public void testInsertedVarSymbol() throws SymbolException {
		final String id = "id";
		final int address = 24;
		final Symbol sym = Symbol.varSymbol(id, address);
		final SymbolTable tab = new SymbolTable();

		tab.add(sym);

		assertTrue(tab.exists(id));
		assertTrue(tab.exists(new String(id)));
		assertEquals(address, tab.getAddress(id));
		assertEquals(SymbolType.ST_VAR, tab.getType(id));
	}

	@Test
	public void testClear() throws SymbolException {
		final String id1 = "a", id2 = "b";

		final Symbol sym1 = Symbol.varSymbol(id1, 2);
		final Symbol sym2 = Symbol.constSymbol(id2, 0);

		final SymbolTable tab = new SymbolTable();

		tab.add(sym1);
		tab.add(sym2);

		tab.clear();

		assertFalse(tab.exists(id1));
		assertFalse(tab.exists(id2));
	}
}
