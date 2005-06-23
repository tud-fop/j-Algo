/*
 * Created on 20.05.2005
 * 
 */
package org.jalgo.module.avl;

public class NoPerformException extends NoActionException {

/**
 * 
 * @author Matthias Schmidt
 *
 *	The class <code>NoPerformException</code> is thrown if it is not possible to
 *  calculate a <code>perform()</code> by the active algorithm.
 *
 */
	public NoPerformException(){
		super("Working Algorithm Has No Next Step!");
	}
	public NoPerformException(String error) {
		super(error);
	}
}
