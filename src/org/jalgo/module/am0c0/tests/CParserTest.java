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

import org.jalgo.module.am0c0.parser.c0.C0Parser;
import org.junit.Test;

/**
 * Unit tests for the C0 parser.
 * 
 * @author Martin Morgenstern
 */
public class CParserTest {
	/**
	 * A pair of C0 source code and a corresponding flag whether this code is
	 * expected to be valid C0 or not. Used together with
	 * {@link CParserTest#testFixture(CTestFixture)}.
	 */
	private interface CTestFixture {
		/**
		 * Get the C0 code to be tested.
		 */
		public String getSource();

		/**
		 * Returns {@code true}, if the code returned by {@link #getSource()} is
		 * expected to be valid C0, otherwise {@code false}.
		 */
		public boolean isValid();
	}

	/**
	 * Utility method that tests a {@link CTestFixture}. For an example of
	 * usage, see {@link #testSummation()} or {@link #testNullArgument()}.
	 * 
	 * @param fixture
	 *            the fixture to test
	 */
	private void testFixture(final CTestFixture fixture) {
		final C0Parser parser = new C0Parser();
		final boolean result = parser.parse(fixture.getSource());

		assertEquals(fixture.isValid(), result);
	}

	@Test
	public void testNullArgument() {
		final C0Parser parser = new C0Parser();

		try {
			parser.parse(null);
			fail("Should have raised a " + NullPointerException.class);
		} catch (NullPointerException e) {
		}
	}

	@Test
	public void testEmptyArgument() {
		testFixture(new CTestFixture() {
			@Override
			public boolean isValid() {
				return false;
			}

			@Override
			public String getSource() {
				return "";
			}
		});
	}

	@Test
	public void testSummation() {
		testFixture(new CTestFixture() {
			@Override
			public String getSource() {
				return "/* Summation */" + "#include <stdio.h>" + "int main()"
						+ "{" + "int i, n, s;" + "scanf(\"%i\", &n);"
						+ "i = 1;" + "s = 0;" + "while (i <= n)" + "{"
						+ "s = s + i * i;" + "i = i + 1;" + "}"
						+ "printf(\"%d\", s);" + "return 0;" + "}" + "";
			}

			@Override
			public boolean isValid() {
				return true;
			}
		});
	}
}
