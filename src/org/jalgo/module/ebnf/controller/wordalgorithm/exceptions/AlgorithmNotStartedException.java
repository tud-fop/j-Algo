package org.jalgo.module.ebnf.controller.wordalgorithm.exceptions;

/**
 * Exception thrown by the <code>WordAlgoAction</code> if the algorithm was not started
 * before performing the <code>WordAlgoAction</code>.
 * 
 * @author Claas Wilke
 */
@SuppressWarnings("serial")
public class AlgorithmNotStartedException extends Exception {

	/**
	 * Constructor without message
	 * 
	 * 
	 */
	public AlgorithmNotStartedException() {
		super();
	}

	/**
	 * Constructor with message
	 * 
	 * @param message
	 *            The message which should be thrown with the Exception.
	 */
	public AlgorithmNotStartedException(String message) {
		super(message);
	}

}
