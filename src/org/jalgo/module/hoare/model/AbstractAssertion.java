package org.jalgo.module.hoare.model;

import java.io.Serializable;

/**
 * Represents an abstract class of an assertion
 *
 * @author Thomas
 */

abstract class AbstractAssertion implements Serializable {

	/**
	 * To get the full C0 compatible String of this Assertion
	 * 
	 * @see java.lang.Object#toString()
	 */
	public abstract String toString();
	
	/** 
	 * return the String with the correct Unicode Symbols of an 
	 * either short or long Form of this Assertion
	 * 
	 * @param full 
	 *    true if you want to get a long Form of this Assertion
	 * @return 
	 *    the String with the correct Unicode Symbols
	 */	
	public abstract String toText(boolean full);
	
	/**
	 * return the original String.<br>
	 * Note: You can insert an original String
	 * into every {@link VarAssertion} by calling
	 * {@link VarAssertion#setOriginal}. 
	 * 
	 * @return the original you set in a VarAssertion or
	 *         the String you get by calling <code>toString</code> 
	 */
	public String getOrginal(){
		return toString();
	}
	
	/**
	 * return true if this assertiontree includes only filled VarAssertions
	 * and normal assertions
	 * 
	 * @return
	 *    the possibility to verify this Assertion 
	 */
	public abstract boolean verifiable();
	
	/**
	 * Checks if the assertion is just an variable
	 * 
	 * @return true if the Assertion is an VarAssertion
	 */
	public boolean isVariable()	{
		return false;
	}
	
}
