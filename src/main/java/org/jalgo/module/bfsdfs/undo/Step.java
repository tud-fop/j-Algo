/**
 * interface: 		Step - declares methods to represent a step within the algorithm for 
 * 						some kind of a history (undo-redo)
 * creation date: 	08.05.09
 * completion date:	20.05.09
 * author: 			Ephraim Zimmer
 */

package org.jalgo.module.bfsdfs.undo;

public interface Step {
	
	/**
	 * Executes the step represented by the implementation of this interface
	 */
	public void execute();
	
	/**
	 * Revokes the step represented by the implementation of this interface with the 
	 * possibility to execute the revoked step again
	 */
	public void undo();
}
