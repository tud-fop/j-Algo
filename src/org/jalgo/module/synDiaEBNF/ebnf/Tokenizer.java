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
