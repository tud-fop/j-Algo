/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 */

/*
 * Created on 30.05.2004
 */

package org.jalgo.module.synDiaEBNF.ebnf;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Stephan Creutz
 */
public class EbnfTokenizer extends Tokenizer implements IEbnfTokenConstants {
	private final String REGEX_WHITESPACE = "\\s+"; //$NON-NLS-1$

	private final String REGEX_LEFT_CURLY_BRACKET = "\\\\\\^\\{", //$NON-NLS-1$
			REGEX_RIGHT_CURLY_BRACKET = "\\\\\\^\\}"; //$NON-NLS-1$

	private final String REGEX_LEFT_SQUARED_BRACKET = "\\\\\\^\\[", //$NON-NLS-1$
			REGEX_RIGHT_SQUARED_BRACKET = "\\\\\\^\\]"; //$NON-NLS-1$

	private final String REGEX_LEFT_BRACKET = "\\\\\\^\\(", //$NON-NLS-1$
			REGEX_RIGHT_BRACKET = "\\\\\\^\\)"; //$NON-NLS-1$

	private final String REGEX_PIPE = "\\\\\\^\\|"; //$NON-NLS-1$

	private final String REGEX_DOT = "\\."; //$NON-NLS-1$

	private final String REGEX_REST = "."; //$NON-NLS-1$

	private List<String> checkorder;

	private Integer[] tokenNames = { WHITESPACE, PIPE, LEFT_CURLY_BRACKET, RIGHT_CURLY_BRACKET, LEFT_SQUARED_BRACKET,
			RIGHT_SQUARED_BRACKET, LEFT_BRACKET, RIGHT_BRACKET, DOT };

	/**
	 * Constructor for the EBNF tokenizer
	 * 
	 * @param term
	 *                       an EBNF term to tokenize
	 * @param symbols
	 *                       the defined symbols (V union Sigma)
	 */
	public EbnfTokenizer(String term, Set<String> symbols) {
		super(term);
		checkorder = new LinkedList<String>();
		checkorder.add(REGEX_WHITESPACE);
		checkorder.add(REGEX_PIPE);
		checkorder.add(REGEX_LEFT_CURLY_BRACKET);
		checkorder.add(REGEX_RIGHT_CURLY_BRACKET);
		checkorder.add(REGEX_LEFT_SQUARED_BRACKET);
		checkorder.add(REGEX_RIGHT_SQUARED_BRACKET);
		checkorder.add(REGEX_LEFT_BRACKET);
		checkorder.add(REGEX_RIGHT_BRACKET);
		checkorder.add(REGEX_DOT);
		skip = REGEX_WHITESPACE;
		LinkedList<String> unquotedSymbolList = new LinkedList<String>(symbols);
		Collections.sort(unquotedSymbolList, new StringLengthComparator());
		LinkedList<String> quotedSymbolList = new LinkedList<String>();
		for (String unquotedSymbol : unquotedSymbolList) {
			quotedSymbolList.add(quoteRegex(unquotedSymbol));
		}
		checkorder.addAll(quotedSymbolList);
		checkorder.add(REGEX_REST);
		tokenize();
	}

	/**
	 * tokenize the term
	 */
	public void tokenize() {
		if (getStr().length() != 0) {
			substr = getStr();
			while (substr.length() != 0) {
				for (int index = 0; index < checkorder.size(); index++) {
					Integer tokenName = null;
					if (index < tokenNames.length) {
						tokenName = tokenNames[index];
					} else {
						tokenName = SYMBOL;
					}
					if (match(checkorder.get(index), tokenName))
						break;
				}
			}
		}
	}

	/**
	 * special characters must be quoted for use in regular expressions
	 * 
	 * @param unquotedStr
	 *                       the unquoted <code>String</code>
	 * @return a quoted <code>String</code>
	 */
	private String quoteRegex(String unquotedStr) {
		int position = 0;
		String regexMetaSymbol = "\\^|\\(|\\)|\\[|\\]|\\{|\\}|\\||\\\\|\\$|\\&|\\?|\\*|\\+"; //$NON-NLS-1$
		StringBuffer quotedStr = new StringBuffer(""); //$NON-NLS-1$
		Matcher matcher = Pattern.compile(regexMetaSymbol).matcher(unquotedStr);
		while (matcher.find()) {
			quotedStr.append(unquotedStr.substring(position, matcher.start()));
			quotedStr.append("\\" + matcher.group()); //$NON-NLS-1$
			position = matcher.end();
		}
		quotedStr.append(unquotedStr.substring(position));
		return quotedStr.toString();
	}

	private class StringLengthComparator implements Comparator<String> {
		public int compare(String str1, String str2) {
			int str1Length = str1.length();
			int str2Length = str2.length();
			if (str1Length < str2Length) {
				return 1;
			} else if (str1Length > str2Length) {
				return -1;
			} else {
				return 0;
			}
		}
	}
}