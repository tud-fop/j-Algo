package org.jalgo.module.ebnf.util;

/**
 * IAction is single step during a algorithm or input. Every IAction can be made
 * undone by the user. Ervery IAction has to implement the two methods perform()
 * and undo().
 * 
 * Can throw any Exception during perform() or undo().
 * 
 * @author Claas Wilke
 * 
 */

public interface IAction {

	/**
	 * Perform is the the method represented by the IAction.
	 * 
	 * 
	 */
	public abstract void perform() throws Exception;

	/**
	 * Undo is the method the user can use to have the represented Action
	 * undone.
	 */
	public abstract void undo() throws Exception;
}
