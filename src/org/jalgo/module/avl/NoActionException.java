/*
 * Created on 18.05.2005
 * 
 */
package org.jalgo.module.avl;

/**
 * 
 * @author Matthias Schmidt
 *
 *	The class <code>NoActionException</code> is thrown if something is wrong with the
 *	connection between the GUI and the algorithm. <br>
 *  It is the super class for some special Exceptions.
 */
public class NoActionException extends Exception {

	public NoActionException() {}
	public NoActionException(String error) {
		super(error);
	}
}
