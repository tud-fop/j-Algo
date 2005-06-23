/*
 * Created on 20.05.2005
 * 
 */
package org.jalgo.module.avl;

/**
 * 
 * @author Matthias Schmidt
 * 
 *  The class <code>NoCommandException</code> is thrown if there is no activ algorithm.
 */
public class NoCommandException extends NoActionException {
	
	public NoCommandException() {
		super("No Algorithm Working!");
	}
	public NoCommandException(String error) {
		super(error);
	}
}
