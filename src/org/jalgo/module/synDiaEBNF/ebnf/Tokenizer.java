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
 * Created on Apr 30, 2004
 */

package org.jalgo.module.synDiaEBNF.ebnf;

import java.lang.String;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Stephan Creutz
 */
public abstract class Tokenizer {
	private String str;
	protected String substr;
	protected List tokens;
	protected String skip;
	protected int position = 0;
	private int index = 0;

	/**
	 * 
	 * @return true if there is one more token else false
	 */
	public boolean hasNextToken() {
		return (index < tokens.size());
	}

	/**
	 * returns the next token
	 * @return token object
	 */
	public Token getNextToken() {
		return (Token) tokens.get(index++);
	}

	/**
	 * Is a lookahead of k tokens possible?
	 * @param k number of tokens
	 * @return true if it is possible else false if not
	 */
	public boolean hasLookahead(int k) {
		return (index + k < tokens.size());
	}

	/**
	 * performs a lookahead to the next k tokens
	 * @param k number of tokens
	 * @return token object
	 */
	public Token lookahead(int k) {
		if ((k >= 0) && hasLookahead(k)) {
			return (Token) tokens.get(index + k);
		}
		return null;
	}

	/**
	 * consumes the actual token
	 */
	public void consume() {
		index++;
	}

	/**
	 * reset the tokenizer
	 */
	public void reset() {
		index = 0;
	}

	public String getStr() {
		return str;
	}

	public void setStr(String str) {
		this.str = str;
	}

	/**
	 * @param regex
	 * @param tokenName
	 * @return true, if the Regexpression matched, otherwise false
	 * 
	 * This function takes a regular expression and a String and tries to match it. If it matched, a Token
	 * object will be created and added to the tokens list.
	 */
	protected boolean match(String regex, Integer tokenName) {
		Matcher matcher = Pattern.compile("^" + regex).matcher(substr); //$NON-NLS-1$
		boolean matched = matcher.find();
		if (matched) {
			if (!regex.equals(skip))
				tokens.add(new Token(tokenName, matcher.group(), position));
			position = matcher.end();
			substr = substr.substring(matcher.end(), substr.length());
		}
		return matched;
	}

	/**
	 * common  interface to tokenizers
	 */
	public abstract void tokenize();

	/**
	 * Constructor for a Tokenizer object.
	 * @param String to tokenize
	 */
	Tokenizer(String str) {
		this.str = str;
		this.substr = str;
		this.tokens = new LinkedList();
	}

}
