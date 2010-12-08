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
package org.jalgo.module.am0c0.parser.c0;

import java.io.IOException;
import java.io.StringReader;

import org.jalgo.module.am0c0.model.c0.C0Program;
import org.jalgo.module.am0c0.model.c0.ast.Program;
import org.jalgo.module.am0c0.parser.ErrorEvents;
import org.jalgo.module.am0c0.parser.Parser;

/**
 * Provides an abstraction to the generated C0 parser/lexer as well as an
 * implementation of the {@code Parser} interface.
 * 
 * @author Martin Morgenstern
 * @see Parser
 */
public class C0Parser implements Parser {
	/**
	 * The resulting C0 program.
	 */
	private C0Program program;

	/**
	 * Contains the error message in case of an parser error.
	 */
	private String errorText;

	/**
	 * Gets the resulting generic program containing the abstract syntax tree
	 * part {@code Program}. Returns {@code null} if the parser encountered an
	 * error.
	 * 
	 * <p>
	 * <strong>Note:</strong> You must first parse the source code via
	 * {@link #parse(String)} and handle any errors that may occur while
	 * parsing.
	 * 
	 * @return a C0Program containing the abstract representation of the parsed
	 *         program
	 */
	public C0Program getProgram() {
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
		if (null == text) {
			throw new NullPointerException("text must not be null");
		}

		boolean errorFlag = false;

		GeneratedC0Parser parser = new GeneratedC0Parser();
		C0Scanner scanner = new C0Scanner(new StringReader(text));

		errorText = "";
		program = null;

		try {
			Program prog = (Program) parser.parse(scanner); 
			program = new C0Program();
			program.add(prog);
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
