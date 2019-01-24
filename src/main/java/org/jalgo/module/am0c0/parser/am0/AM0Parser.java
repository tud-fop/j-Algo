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
package org.jalgo.module.am0c0.parser.am0;

import java.io.IOException;
import java.io.StringReader;

import org.jalgo.module.am0c0.model.am0.AM0Program;
import org.jalgo.module.am0c0.parser.ErrorEvents;
import org.jalgo.module.am0c0.parser.Parser;

/**
 * Provides an abstraction to the generated AM0 lexer and parser as well as an
 * implementation of the {@code Parser} interface.
 * 
 * @author Martin Morgenstern
 * @see Parser
 */
public class AM0Parser implements Parser {
	/**
	 * The resulting AM0 program.
	 */
	private AM0Program program;

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
	 * do error handling as needed, otherwise you will get unexpected results.
	 * 
	 * @return an AM0Program containing the abstract representation of the
	 *         parsed program
	 */
	public AM0Program getProgram() {
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

		GeneratedAM0Parser parser = new GeneratedAM0Parser();
		AM0Scanner scanner = new AM0Scanner(new StringReader(text.trim()));

		errorText = "";
		program = null;

		try {
			program = (AM0Program) parser.parse(scanner);
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
