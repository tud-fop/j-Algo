/*
 * Created on 08.06.2004
 */
package org.jalgo.module.synDiaEBNF.ebnf;

/**
 * this is a new <code>Exception</code> for the EBNF parser
 * 
 * @author Stephan Creutz
 */
public class EbnfParseException extends Exception {

	public EbnfParseException(String message) {
		super(message);
	}

	public EbnfParseException(String message, int position) {
		super(message + Messages.getString("EbnfParseException.._char___1") + position); //$NON-NLS-1$
	}

}
