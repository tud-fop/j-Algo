package org.jalgo.module.unifikation.algo.controller;

import org.jalgo.module.unifikation.IAppView;

/**
 * Interface for button actions
 * @author Alex
 *
 */
public abstract class IButtonAction {
	
	public IButtonAction(IAppView appView){
	}
	
	/**
	 * Called when the button is clicked
	 */
	public abstract void onClick();
	
	/**
	 * Gets the text, that should be displayed when button is hovered
	 * @return Text, that should be displayed when button is hovered
	 */
	public String getHoverText(){
		return null;
	}
}
