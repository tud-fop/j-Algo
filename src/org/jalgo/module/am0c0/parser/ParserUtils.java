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
package org.jalgo.module.am0c0.parser;

import beaver.Symbol;

/**
 * Utility class for parser-related work.
 * 
 * @author Martin Morgenstern
 */
public class ParserUtils {
	/**
	 * Utility method that safely converts symbols into numbers. If the
	 * conversion is not successful, the error is reported through the error
	 * reporting gateway.
	 * 
	 * @param symbol
	 *            the symbol that should be converted
	 * @param report
	 *            the error reporting gateway to use
	 * @return the corresponding integer value, or {@code 0} if conversion
	 *         failed
	 * @see ErrorEvents
	 */
	public static int safeSymbolToInt(final Symbol symbol,
			final ErrorEvents report) {
		int result = 0;

		if ((null == symbol) || (null == symbol.value)) {
			return result;
		}

		final String number = symbol.value.toString();
		final int line = Symbol.getLine(symbol.getStart());
		final int col = Symbol.getColumn(symbol.getStart());

		try {
			result = Integer.parseInt(number);
		} catch (NumberFormatException e) {
			report.conversionError(number, line, col);
		}

		return result;
	}
}
