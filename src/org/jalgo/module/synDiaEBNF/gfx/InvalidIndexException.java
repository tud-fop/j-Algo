/*
 * Created on 15.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gfx;

/**
 * This class provides a special exception which will only be thrown in case of an invalid index for
 * removeing or replaceing figures.
 * 
 * @author Marco Zimmerling
 */
public class InvalidIndexException extends SynDiaException {
	
	/**
	 * Creates an InvalidIndexException with the invalid index. An error message is automatically created.
	 * 
	 * @param index		invalid index in order to provide a detailed error description
	 */
	public InvalidIndexException(int index) {
		super("An invalid index of " + String.valueOf(index) + "has been passed."); //$NON-NLS-1$ //$NON-NLS-2$
	}
}
