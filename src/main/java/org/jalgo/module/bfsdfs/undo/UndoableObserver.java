/**
 * interface: 		UndoableObserver - defines methods for classes which have to be able to observe 
 * 						the "Algo" or the "GraphController" for undo and redo being enabled 
 * 						and/or disabled.
 * creation date: 	08.05.09
 * completion date:	20.05.09
 * author: 			Ephraim Zimmer
 */

package org.jalgo.module.bfsdfs.undo;

public interface UndoableObserver {
	
	/**
	 * This method is called, if the revocation of a step is allowed.
	 */
	public void onUndoEnabled();
	
	/**
	 * This method is called, if there is no step which could be revoked.
	 */
	public void onUndoDisabled();
	
	/**
	 * This method is called, if the redo of a revoked step is allowed.
	 */
	public void onRedoEnabled();
	
	/**
	 * This method is called, if there is no step which could be redone.
	 */
	public void onRedoDisabled();
}