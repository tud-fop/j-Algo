package org.jalgo.module.ebnf.controller.wordalgorithm.exceptions;

/**
 * Exception thrown by the <code>WordAlgoAction</code> if there was a problem during
 * performing the step represented by the <code>WordAlgoAction</code>.
 * 
 * @author Claas Wilke
 */
@SuppressWarnings("serial")
public class CantPerformStepException extends Exception {

	/**
	 * Constructor without message
	 * 
	 * 
	 */
	public CantPerformStepException() {
		super();
	}

	/**
	 * Constructor with message
	 * 
	 * @param message
	 *            The message which should be thrown with the Exception.
	 */
	public CantPerformStepException(String message) {
		super(message);
	}

}
