package org.jalgo.module.ebnf.controller.wordalgorithm.exceptions;

/**
 * Exception thrown by the <code>AlgorithmStartAction</code> if the algorithm was already started
 * before performing the <code>AlgorithmStartAction</code>.
 * 
 * @author Claas Wilke
 */
@SuppressWarnings("serial")
public class AlgorithmAlreadyStartedException extends Exception {

	/**
	 * Constructor without message
	 * 
	 * 
	 */
	public AlgorithmAlreadyStartedException() {
		super();
	}

	/**
	 * Constructor with message
	 * 
	 * @param message
	 *            The message which should be thrown with the Exception.
	 */
	public AlgorithmAlreadyStartedException(String message) {
		super(message);
	}


}
