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

package org.jalgo.module.am1simulator.parser.am1;

import java.io.IOException;
import java.io.StringReader;

import org.jalgo.module.am1simulator.model.am1.AM1Program;
import org.jalgo.module.am1simulator.parser.ErrorEvents;
import org.jalgo.module.am1simulator.parser.Parser;

/**
 * Provides an abstraction to the generated AM1 lexer and parser as well as an
 * implementation of the {@code Parser} interface.
 * 
 * @author Martin Morgenstern
 * @author Max Leuth&auml;user
 * @see Parser
 */
public class AM1Parser implements Parser {
	/**
	 * The resulting AM1 program.
	 */
	private AM1Program program = null;

	/**
	 * Contains the parser error message in case of an error, otherwise
	 * {@code null}.
	 */
	private String errorText;

	/**
	 * Get the abstract representation of the parsed program, or {@code null} if
	 * there was an error while parsing.
	 * 
	 * <p>
	 * <strong>Note:</strong> You must invoke {@link #parse(String)} first and
	 * do error handling as needed.
	 * 
	 * @return an AM1Program containing the abstract representation of the
	 *         parsed program
	 * 
	 * @throws IllegalAccessException
	 *             if you call this method before calling {@link #parse(String)}
	 *             .
	 */
	public AM1Program getProgram() throws IllegalAccessException {
		if (program == null) {
			throw new IllegalAccessException(
					"You must not call this method before calling parse()!");
		}
		return program;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getErrorText() {
		return errorText;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean parse(final String text) {
		boolean errorFlag = false;

		GeneratedAM1Parser parser = new GeneratedAM1Parser();
		AM1Scanner scanner = new AM1Scanner(new StringReader(text.trim()));

		errorText = "";
		program = null;

		try {
			program = (AM1Program) parser.parse(scanner);
		} catch (beaver.Parser.Exception e) {
			errorFlag = true;
		} catch (IOException e) {
			errorFlag = true;
		}

		ErrorEvents errors = parser.getErrorEvents();

		if (errors.hasErrors()) {
			errorFlag = true;
			errorText += errors.getErrorText();
		}

		if (!errorFlag) {
			errorText = null;
			return true;
		}

		return false;
	}
}
