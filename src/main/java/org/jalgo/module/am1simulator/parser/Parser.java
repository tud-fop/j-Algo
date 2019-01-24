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

/**
 * The {@code Parser} interface provides common methods for dealing with the AM1
 * parser.
 * 
 * @author Martin Morgenstern
 * @author Max Leuth&auml;user
 * @see org.jalgo.module.am1simulator.parser.am1.AM1Parser
 */
public interface Parser {
	/**
	 * Parse the source code contained in {@code text} and check the source code
	 * for syntax errors. If there was no syntax error, {@code true} is
	 * returned, else {@code false} is returned and a detailed error message
	 * will be available by invoking the method {@link #getErrorText()}.
	 * 
	 * @param text
	 *            the program source code
	 * @return {@code true}, if the program source code contains no syntax
	 *         errors, {@code false} otherwise
	 */
	public boolean parse(String text);

	/**
	 * If a previous call to {@link #parse(String)} returned {@code false}, this
	 * method returns a detailed message describing the error that occured while
	 * parsing. Otherwise it will return {@code null}.
	 * 
	 * @return a detailed error message from the parser
	 */
	public String getErrorText();
}
