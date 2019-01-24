package org.jalgo.module.app.controller.undoRedo;

/**
 * You can undo or redo an undoable action. 
 */
public interface UndoableAction {

	/**
	 * Performs the steps to undo a happened action.
	 */
	public void undo();
	
	/**
	 * Performs the steps to redo a undone action. 
	 */
	public void redo();
	
}

