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
import org.junit.Test;

/**
 * Unit tests for {@link Symbol}.
 * 
 * @author Felix Schmitt
 * @author Martin Morgenstern
 */
public class SymbolTest {
	@Test
	public void testSymbolIdNotNull() {
		try {
			Symbol.varSymbol(null, 1);
			fail("Should have raised an exception");
		} catch (NullPointerException e) {
		}

		try {
			Symbol.constSymbol(null, 0);
			fail("Should have raised an exception");
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testVarSymbolRaisesException() {
		try {
			Symbol.varSymbol("x", 0);
			fail("Should have raised an exception");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testConstSymbolRaisesException() {
		Symbol sym = Symbol.constSymbol("a", 0);

		try {
			sym.getAddress();
			fail("Should have raised an exception");
		} catch (SymbolException e) {
		}
	}

	@Test
	public void testVarSymbol() throws SymbolException {
		final String id = "a";
		final int address = 10;
		Symbol sym = Symbol.varSymbol(id, address);

		assertEquals(SymbolType.ST_VAR, sym.getType());
		assertEquals(address, sym.getAddress());
		assertEquals(id, sym.getID());
	}

	@Test
	public void testConstSymbol() {
		final String id = "a";
		final int value = -20;
		Symbol sym = Symbol.constSymbol(id, value);

		assertEquals(SymbolType.ST_CONST, sym.getType());
		assertEquals(value, sym.getValue());
		assertEquals(id, sym.getID());
	}
}
