/*
 * Created on 30.05.2004
 */

package org.jalgo.module.synDiaEBNF.ebnf;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
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
	private List checkorder;
	private Integer[] tokenNames =
		{
			WHITESPACE,
			PIPE,
			LEFT_CURLY_BRACKET,
			RIGHT_CURLY_BRACKET,
			LEFT_SQUARED_BRACKET,
			RIGHT_SQUARED_BRACKET,
			LEFT_BRACKET,
			RIGHT_BRACKET,
			DOT };

	/**
	 * Constructor for the EBNF tokenizer
	 * @param term an EBNF term to tokenize
	 * @param symbols the defined symbols (V union Sigma)
	 */
	public EbnfTokenizer(String term, Set symbols) {
		super(term);
		checkorder = new LinkedList();
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
		LinkedList list = new LinkedList(symbols);
		Collections.sort(list, new StringLengthComparator());
		for (Iterator it = list.iterator(); it.hasNext();) {
			quoteRegex((String) it.next());
		}
		checkorder.addAll(list);
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
					if (match((String) checkorder.get(index), tokenName))
						break;
				}
			}
		}
	}

	/**
	 * special characters must be quoted for use in regular expressions
	 * @param unquotedStr the unquoted <code>String</code>
	 * @return a quoted <code>String</code>
	 */
	private String quoteRegex(String unquotedStr) {
		int position = 0;
		String regexMetaSymbol =
			"\\^|\\(|\\)|\\[|\\]|\\{|\\}|\\||\\\\|\\$|\\&|\\?|\\*|\\+"; //$NON-NLS-1$
		StringBuffer quotedStr = new StringBuffer(""); //$NON-NLS-1$
		Matcher matcher = Pattern.compile(regexMetaSymbol).matcher(unquotedStr);
		while (matcher.find()) {
			quotedStr.append(unquotedStr.substring(position, matcher.start()));
			quotedStr.append("\\" + matcher.group()); //$NON-NLS-1$
			position = matcher.end();
		}
		return quotedStr.toString();
	}

	private class StringLengthComparator implements Comparator {
		public int compare(Object o1, Object o2) {
			if (o1 instanceof String && o2 instanceof String) {
				int o1Length = ((String) o1).length();
				int o2Length = ((String) o2).length();
				if (o1Length < o2Length) {
					return 1;
				} else if (o1Length > o2Length) {
					return -1;
				} else {
					return 0;
				}
			}
			return 0;
		}
	}
}
