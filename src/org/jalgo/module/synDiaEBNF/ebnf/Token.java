/*
 * Created on May 3, 2004
 */
 
package org.jalgo.module.synDiaEBNF.ebnf;

/**
 * every recognized token is represented by a <code>Token</code> object
 * 
 * @author Stephan Creutz 
 */
public class Token {
	private Integer tokenName;
	private String tokenValue;
	private int position;

	/**
	 * constructor for the <code>Token</code> object
	 * @param tokenName
	 * @param tokenValue
	 * @param position
	 */
	public Token (Integer tokenName, String tokenValue, int position) {
		this.tokenName = tokenName;
		this.tokenValue = tokenValue;
		this.position = position;
	}
	
	/**
	 * 
	 * @return the token name
	 */
	public Integer getTokenName() {
		return tokenName;
	}

	/**
	 * 
	 * @param tokenName
	 */
	public void setTokenName(Integer tokenName) {
		this.tokenName = tokenName;
	}

	/**
	 * @return token value
	 */
	public String getTokenValue() {
		return tokenValue;
	}

	/**
	 * @param string token value
	 */
	public void setTokenValue(String string) {
		tokenValue = string;
	}

	/**
	 * @return the position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * @param position
	 */
	public void setPosition(int position) {
		this.position = position;
	}

}
