package org.jalgo.module.c0h0.models;

/**
 * model interface to perform steps
 *
 */
public interface Performer {

	/**
	 * Return true if next step is possible, return false, if next step is impossible
	 * aka Model is fully exposed.
	 * @return if the end is reached
	 */
	public boolean isDone();

	/**
	 * Return true is previous step is possible, return false, if previous step is impossible
	 * aka Model is fully collapsed.
	 * @return if the performer is at the start
	 */
	public boolean isClear();

	/**
	 * Perform all Steps until end
	 */
	public void performAll();

	/**
	 * Performs a single Step
	 */
	public void performStep();

	/**
	 * Undo a single Step
	 */
	public void undoStep();

	/**
	 * Undo all Step until end
	 */
	public void undoAll();

	/**
	 * Clears the model
	 */
	public void clear();
	
	public void setActive(boolean a);
	
	public boolean isActive();
}
