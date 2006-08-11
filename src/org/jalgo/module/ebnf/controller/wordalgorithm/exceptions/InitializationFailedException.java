package org.jalgo.module.ebnf.controller.wordalgorithm.exceptions;

/**
 * Exception thrown by the method <code>initialize</code> of the <Code>WordAlgorithmController</code>.
 * 
 * @author Claas Wilke
 */
@SuppressWarnings("serial")
public class InitializationFailedException extends Exception {

	/**
	 * Constructor without message
	 * 
	 * 
	 */
	public InitializationFailedException() {
		super();
	}

	/**
	 * Constructor with message
	 * 
	 * @param message
	 *            The message which should be thrown with the Exception.
	 */
	public InitializationFailedException(String message) {
		super(message);
	}
}
