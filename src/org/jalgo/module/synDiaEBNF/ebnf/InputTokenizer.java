/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on 09.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.ebnf;

/**
 * @author Stephan Creutz
 */
public class InputTokenizer extends Tokenizer {
	private final static String REGEX_COMMA = ","; //$NON-NLS-1$
	private final static String REGEX_WHITESPACE = "\\s+"; //$NON-NLS-1$
	private final static String REGEX_SYMBOL = "[^,\\s]+"; //$NON-NLS-1$
	private static String[] checkorder =
		{ REGEX_COMMA, REGEX_WHITESPACE, REGEX_SYMBOL };
	private final static Integer[] tokenNames = { new Integer(1), // COMMA
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
