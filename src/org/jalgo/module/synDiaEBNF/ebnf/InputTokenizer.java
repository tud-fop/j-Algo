/*
 * Created on 09.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.ebnf;

/**
 * @author Stephan Creutz
 */
public class InputTokenizer extends Tokenizer {
	private final String REGEX_COMMA = ","; //$NON-NLS-1$
	private final String REGEX_WHITESPACE = "\\s+"; //$NON-NLS-1$
	private final String REGEX_SYMBOL = "[^,\\s]+"; //$NON-NLS-1$
	private String[] checkorder =
		{ REGEX_COMMA, REGEX_WHITESPACE, REGEX_SYMBOL };
	private final Integer[] tokenNames = { new Integer(1), // COMMA
		new Integer(2), // WHITESPACE
		new Integer(3) // SYMBOL
	};

	public InputTokenizer(String str) {
		super(str);
		skip = REGEX_WHITESPACE;
		tokenize();
	}

	public void tokenize() {
		if (getStr().length() != 0) {
			substr = getStr();
			while (substr.length() != 0) {
				for (int index = 0; index < checkorder.length; index++) {
					if (match(checkorder[index], tokenNames[index]))
						break;
				}
			}
		}

	}
}
