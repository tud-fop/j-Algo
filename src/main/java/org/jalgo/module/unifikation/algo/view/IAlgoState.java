package org.jalgo.module.unifikation.algo.view;

/**
 * Represents the state the Algo is currently in
 * @author Alex
 *
 */
public interface IAlgoState {
	
	/**
	 * Updates the view (Buttons, text...)
	 */
	public void updateView();
	/**
	 * Switches to next state
	 * @return True if it was possible
	 */
	public boolean next();
	/**
	 * Switches to previous state
	 * @return True if it was possible
	 */
	public boolean prev();

}
