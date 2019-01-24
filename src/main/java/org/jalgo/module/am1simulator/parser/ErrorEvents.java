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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;
import beaver.Scanner;
import beaver.Symbol;
import beaver.Parser.Events;
import org.jalgo.module.am1simulator.parser.am1.GeneratedAM1Parser;

/**
 * Provides a replacement for the standard error reporting gateway of the Beaver
 * parser generator. This class is not intended to be instantiated directly
 * (since it is abstract), use {@link #forAm1()} to get instances of this class
 * that are specialized for AM1 error reporting, respectively.
 * 
 * @author Martin Morgenstern
 * @author Max Leuth&auml;user
 */
public abstract class ErrorEvents extends Events {
	/**
	 * CSS style for highlighted user input (tokens) in error messages.
	 */
	private final static String STYLE_TOKEN = "background-color:#fff3af;font-size:100%;font-weight:bold;";

	/**
	 * CSS style for words indicating an error.
	 */
	private final static String STYLE_ERROR = "color: #ff0000;";
	
	/**
	 * Collects all syntax errors.
	 */
	private List<String> syntaxErrors = new ArrayList<String>();

	/**
	 * Collects all lexer errors.
	 */
	private List<String> lexicalErrors = new ArrayList<String>();

	/**
	 * Caches forbidden input sequences.
	 */
	private StringBuilder forbiddenInput = new StringBuilder();

	/**
	 * Marker for the last line where an unmatched character was found (i.e. the
	 * "catch-all" rule of the lexer matched) and a lexer error occured.
	 */
	private int lastScannerErrorLine = 1;

	/**
	 * Handles scanner errors. Unmatched characters are collected per line and
	 * printed together.
	 */
	@Override
	public void scannerError(Scanner.Exception e) {
		if (e.line != lastScannerErrorLine) {
			finishScannerErrorLine();
		}

		forbiddenInput.append(e.getMessage());
		lastScannerErrorLine = e.line;
	}

	/**
	 * Add an error message with the cached characters in {@code forbiddenInput}
	 * to the lexer error list and clear the cache. If the cache is empty, do
	 * nothing.
	 */
	private void finishScannerErrorLine() {
		if (forbiddenInput.length() > 0) {
			final String error = String
					.format("Can't process input <code style=\"%s\">\"%s\"</code> on line %d.",
							STYLE_TOKEN, escapeHtml(forbiddenInput.toString()),
							lastScannerErrorLine);
			lexicalErrors.add(error);
			forbiddenInput = new StringBuilder();
		}
	}

	/**
	 * Handle syntax errors.
	 */
	@Override
	public void syntaxError(Symbol token) {
		final String tokenValue = (token.value != null) ? token.value
				.toString() : "#" + token.getId();

		int line = Symbol.getLine(token.getStart());
		int col = Symbol.getColumn(token.getStart());

		String error;

		if (ParserConstants.EOF.equals(tokenValue)) {
			error = String.format(
					"Unexpected end-of-file at line %d, column %d.", line, col);
		} else if (ParserConstants.EOL.equals(tokenValue)) {
			error = String.format(
					"Unexpected end-of-line at line %d, column %d.", line, col);
		} else {
			error = String
					.format("Unexpected token <code style=\"%s\">\"%s\"</code> at line %d, column %d.",
							STYLE_TOKEN, escapeHtml(tokenValue), line, col);
		}

		syntaxErrors.add(error);
	}

	/**
	 * Handle the deletion of unexpected tokens. Here an error message is
	 * appended to the last error message to give the user a hint.
	 */
	@Override
	public void unexpectedTokenRemoved(Symbol token) {
		extendLastErrorMessage("Delete it to correct this error.");
	}

	/**
	 * Helper method that appends a string to the last element of the syntax
	 * error list.
	 * 
	 * @param extension
	 *            the string to append
	 */
	protected void extendLastErrorMessage(String extension) {
		int replacePosition = syntaxErrors.size() - 1;

		if (replacePosition >= 0) {
			String error = syntaxErrors.get(replacePosition) + " " + extension;
			syntaxErrors.set(replacePosition, error);
		}
	}

	/**
	 * Specific implementation of AM1 error events.
	 */
	private static class AM1ErrorEvents extends ErrorEvents {
		/**
		 * Contains a mapping of hints for terminal symbols that should be
		 * inserted by the user.
		 */
		private static final Map<Short, String> hints;

		static {
			final Map<Short, String> m = new HashMap<Short, String>();

			m.put(GeneratedAM1Parser.Terminals.NUMBER,
					"Insert a number to correct this error.");
			m.put(GeneratedAM1Parser.Terminals.SEMICOLON,
					"Insert <code>\";\"</code> to complete this AM1 statement.");
			m.put(GeneratedAM1Parser.Terminals.COMMA,
					"Insert <code>\",\"</code> to correct this error.");
			m.put(GeneratedAM1Parser.Terminals.NUMBER,
					"Insert a number to complete this AM1 statement.");
			hints = Collections.unmodifiableMap(m);
		}

		@Override
		public void missingTokenInserted(Symbol token) {
			final Short id = token.getId();
			if (hints.containsKey(id)) {
				extendLastErrorMessage(hints.get(id));
			}
		}
	}

	/**
	 * Indicates that an error can be recovered if {@code token} is inserted at
	 * the position of the syntax error.
	 */
	@Override
	public abstract void missingTokenInserted(Symbol token);

	/**
	 * Indicates that an error can be recovered if the error token is replaced
	 * by {@code token}.
	 */
	@Override
	public void misspelledTokenReplaced(Symbol token) {
	}

	@Override
	public void errorPhraseRemoved(Symbol error) {
	}

	/**
	 * Used to collect string to int conversion errors because of too large
	 * integers.
	 * 
	 * @param number
	 *            the string containing the number that was too large to convert
	 * @param line
	 *            the line on which the error occured
	 * @param col
	 *            the column in which the error occured
	 */
	public void conversionError(final String number, final int line,
			final int col) {
		lexicalErrors
				.add(String
						.format("The number <code style=\"%s\">%s</code> at line %d, column %d is too large: the maximum possible value is <code>%d</code>.",
								STYLE_TOKEN, escapeHtml(number), line, col,
								Integer.MAX_VALUE));
	}

	/**
	 * Get the total amount of errors collected.
	 * 
	 * @return total amount of errors
	 */
	private int getErrorCount() {
		return lexicalErrors.size() + syntaxErrors.size();
	}

	/**
	 * Check wether an error occured during parsing.
	 * 
	 * @return {@code true}, if there was at least one error, otherwise
	 *         {@code false}
	 */
	public boolean hasErrors() {
		// update internal state
		finishScannerErrorLine();
		return getErrorCount() > 0;
	}

	/**
	 * Get a summarizing error text (HTML formatted) for all the errors that
	 * occured during parsing or {@code null}, if no error occured. Use
	 * {@link #hasErrors()} to check if errors occured.
	 * 
	 * @return summarizing error text or {@code null}, if no error occured
	 */
	public String getErrorText() {
		if (!hasErrors()) {
			return null;
		}

		StringBuilder result = new StringBuilder("<p>");

		if (getErrorCount() > 1) {
			result.append(String
					.format("The following <strong style=\"%s\">errors</strong> occured during validation:",
							STYLE_ERROR));
		} else {
			result.append(String
					.format("The following <strong style=\"%s\">error</strong> occured during validation:",
							STYLE_ERROR));
		}

		result.append("<br />");
		final String itemFormat = "&nbsp;- <em>%s</em>: %s<br />";

		for (String error : lexicalErrors) {
			result.append(String.format(itemFormat, "Lexical Analysis", error));
		}

		for (String error : syntaxErrors) {
			result.append(String
					.format(itemFormat, "Syntactic Analysis", error));
		}

		result.append("</p>");

		return result.toString();
	}

	/**
	 * Get a fresh {@link ErrorEvents} instance specialized for AM0 parsers.
	 * 
	 * @return a new {@link ErrorEvents} instance
	 */
	public static ErrorEvents forAm1() {
		return new AM1ErrorEvents();
	}
}
