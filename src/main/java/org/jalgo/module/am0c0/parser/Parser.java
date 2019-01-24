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

/**
 * The {@code Parser} interface provides common methods for dealing
 * with the AM0 and C0 parsers.
 * 
 * @author Martin Morgenstern
 * @see org.jalgo.module.am0c0.parser.c0.C0Parser
 * @see org.jalgo.module.am0c0.parser.am0.AM0Parser
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
