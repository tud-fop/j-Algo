/*
 * Created on 15.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gfx;

import org.jalgo.main.InternalErrorException;

/**
 * Implement an exception class for exceptions which occure in connection with
 * syntactical diagrams. Mostly such an exception will be thrown, if someone tries
 * to remove or replace a figure and passes an invalid index or an invalid figure.
 * 
 * @author Marco Zimmerling
 */
public class SynDiaException extends InternalErrorException {
	
	private String errorMsg;
	
	/**
	 * Constructor that creates a SynDiaException with a short error description.
	 * 
	 * @param errorMsg		short message which describes the error 
	 */
	public SynDiaException(String errorMsg) {
		super(errorMsg);
	}

}
