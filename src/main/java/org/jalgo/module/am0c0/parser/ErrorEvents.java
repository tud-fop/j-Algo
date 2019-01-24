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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.parser.am0.GeneratedAM0Parser;
import org.jalgo.module.am0c0.parser.c0.GeneratedC0Parser;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

import beaver.Scanner;
import beaver.Symbol;
import beaver.Parser.Events;

/**
 * Provides a replacement for the standard error reporting gateway of the Beaver
 * parser generator. This class is not intended to be instantiated directly
 * (since it is abstract), use {@link #forAm0()} or {@link #forC0()} to get
 * instances of this class that are specialized for AM0 and C0 error reporting,
 * respectively.
 * 
 * @author Martin Morgenstern
 */
public abstract class ErrorEvents extends Events {
	/**
	 * CSS style for highlighted user input (tokens) in error messages.
	 */
	private final static String STYLE_TOKEN = "background-color:#fff3af;font-size:100%;font-weight:bold;"; //$NON-NLS-1$

	/**
	 * CSS style for words indicating an error.
	 */
	private final static String STYLE_ERROR = "color: #ff0000;"; //$NON-NLS-1$

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
			final String error = String.format(
					translate("DontKnowWhatToDo"), //$NON-NLS-1$
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
				.toString() : "#" + token.getId(); //$NON-NLS-1$

		int line = Symbol.getLine(token.getStart());
		int col = Symbol.getColumn(token.getStart());

		String error;

		if (ParserConstants.EOF.equals(tokenValue)) {
			error = String.format(translate("UnexpectedEOF"), line, col); //$NON-NLS-1$
		} else if (ParserConstants.EOL.equals(tokenValue)) {
			error = String.format(translate("UnexpectedEOL"), line, col); //$NON-NLS-1$
		} else {
			error = String.format(translate("UnexpectedTokenAt"), STYLE_TOKEN, //$NON-NLS-1$
					escapeHtml(tokenValue), line, col);
		}

		syntaxErrors.add(error);
	}

	/**
	 * Handle the deletion of unexpected tokens. Here an error message is
	 * appended to the last error message to give the user a hint.
	 */
	@Override
	public void unexpectedTokenRemoved(Symbol token) {
		extendLastErrorMessage(translate("RemoveThisToken")); //$NON-NLS-1$
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
			String error = syntaxErrors.get(replacePosition) + " " + extension; //$NON-NLS-1$
			syntaxErrors.set(replacePosition, error);
		}
	}

	/**
	 * Specific implementation of C0 error events to give the user better hints
	 * how to recover from a syntax error.
	 */
	private static class C0ErrorEvents extends ErrorEvents {
		/**
		 * Contains a mapping of hints for terminal symbols that should be
		 * inserted by the user.
		 */
		private static final Map<Short, String> hints;

		static {
			final Map<Short, String> m = new HashMap<Short, String>();

			final String insertRelation = translate("InsertRelation"); //$NON-NLS-1$
			m.put(GeneratedC0Parser.Terminals.EQ, insertRelation);
			m.put(GeneratedC0Parser.Terminals.NE, insertRelation);
			m.put(GeneratedC0Parser.Terminals.LE, insertRelation);
			m.put(GeneratedC0Parser.Terminals.GE, insertRelation);
			m.put(GeneratedC0Parser.Terminals.LT, insertRelation);
			m.put(GeneratedC0Parser.Terminals.GT, insertRelation);

			m.put(GeneratedC0Parser.Terminals.AMP, translate("InsertAmp")); //$NON-NLS-1$
			m.put(GeneratedC0Parser.Terminals.COMMA, translate("InsertComma")); //$NON-NLS-1$
			m.put(GeneratedC0Parser.Terminals.ASSIGN, translate("InsertAssign")); //$NON-NLS-1$
			m.put(GeneratedC0Parser.Terminals.SEMICOLON,
					translate("InsertSemicolon")); //$NON-NLS-1$

			m.put(GeneratedC0Parser.Terminals.IDENT, translate("InsertIdent")); //$NON-NLS-1$
			m.put(GeneratedC0Parser.Terminals.NUMBER, translate("InsertNumber")); //$NON-NLS-1$

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
	 * Specific implementation of AM0 error events.
	 */
	private static class AM0ErrorEvents extends ErrorEvents {
		/**
		 * Contains a mapping of hints for terminal symbols that should be
		 * inserted by the user.
		 */
		private static final Map<Short, String> hints;

		static {
			final Map<Short, String> m = new HashMap<Short, String>();

			m.put(GeneratedAM0Parser.Terminals.NUMBER,
					translate("InsertNumber_AM0")); //$NON-NLS-1$
			m.put(GeneratedAM0Parser.Terminals.SEMICOLON,
					translate("InsertSemicolon_AM0")); //$NON-NLS-1$

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
		lexicalErrors.add(String.format(translate("NumberTooLarge"),
				STYLE_TOKEN, escapeHtml(number), line, col, Integer.MAX_VALUE));
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

		StringBuilder result = new StringBuilder("<p>"); // $NON-NLS-1$

		if (getErrorCount() > 1) {
			result.append(String.format(translate("FollowingErrors"),
					STYLE_ERROR)); // $NON-NLS-1$
		} else {
			result.append(String.format(translate("FollowingError"),
					STYLE_ERROR)); // $NON-NLS-1$
		}

		result.append("<br />"); // $NON-NLS-1$
		final String itemFormat = "&nbsp;- <em>%s</em>: %s<br />"; //$NON-NLS-1$

		for (String error : lexicalErrors) {
			result.append(String.format(itemFormat,
					translate("LexicalAnalysis"), error)); //$NON-NLS-1$
		}

		for (String error : syntaxErrors) {
			result.append(String.format(itemFormat,
					translate("SyntacticAnalysis"), error)); //$NON-NLS-1$
		}

		result.append("</p>"); //$NON-NLS-1$

		return result.toString();
	}

	/**
	 * Get a fresh {@link ErrorEvents} instance specialized for AM0 parsers.
	 * 
	 * @return a new {@link ErrorEvents} instance
	 */
	public static ErrorEvents forAm0() {
		return new AM0ErrorEvents();
	}

	/**
	 * Get a fresh {@link ErrorEvents} instance specialized for C0 parsers.
	 * 
	 * @return a new {@link ErrorEvents} instance
	 */
	public static ErrorEvents forC0() {
		return new C0ErrorEvents();
	}

	/**
	 * Convenience method to get translated strings for this class.
	 * 
	 * @see Messages
	 * @param index
	 *            the index name of the externalized string
	 * @return the translation
	 */
	protected static String translate(String index) {
		return Messages.getString("am0c0", "ErrorEvents." + index); //$NON-NLS-1$ $NON-NLS-2$
	}
}
