package org.jalgo.module.c0h0.models.ast;

/**
 * contains the error information
 *
 */
public class ErrorInformation {
	private String text;
	private Symbol errorNode;

	/**
	 * returns the error information
	 * 
	 * @return the error information
	 */
	public String getText() {
		return text;
	}

	/**
	 * returns the error node
	 * 
	 * @return the error node
	 */
	public Symbol getErrorNode() {
		return errorNode;
	}
}
