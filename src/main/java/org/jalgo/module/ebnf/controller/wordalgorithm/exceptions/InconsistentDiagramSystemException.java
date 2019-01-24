package org.jalgo.module.ebnf.controller.wordalgorithm.exceptions;

/**
 * Exception thrown by the method <code>generateAdresses</code> of the <Code>WordAlgorithmController</code>.
 * 
 * @author Claas Wilke
 */
@SuppressWarnings("serial")
public class InconsistentDiagramSystemException extends Exception {

	/**
	 * Constructor without message
	 * 
	 * 
	 */
	public InconsistentDiagramSystemException() {
		super();
	}

	/**
	 * Constructor with message
	 * 
	 * @param message
	 *            The message which should be thrown with the Exception.
	 */
	public InconsistentDiagramSystemException(String message) {
		super(message);
	}
}
