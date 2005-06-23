/*
 * Created on 20.05.2005
 * 
 */
package org.jalgo.module.avl;

public class NoUndoException extends NoActionException {

/**
 * 
 * @author Matthias Schmidt
 *
 *	The class <code>NoUndoException</code> is thrown if it is not possible to
 *  calculate an <code>undo()</code> by the active algorithm.
 *
 */
	public NoUndoException() {
		super("Working Algorithm Has No Previous Step!");
	}
	public NoUndoException(String error) {
		super(error);
	}
	
}
