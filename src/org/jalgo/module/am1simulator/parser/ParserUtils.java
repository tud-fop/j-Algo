/**
 * AM1 Simulator - simulating am1 code in an abstract machine based on the
 * definitions of the lectures 'Programmierung' at TU Dresden.
 * Copyright (C) 2010 Max Leuthäuser
 * Contact: s7060241@mail.zih.tu-dresden.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jalgo.module.am1simulator.parser;

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
