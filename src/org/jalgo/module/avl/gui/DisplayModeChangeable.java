/* Created on 18.06.2005 */
package org.jalgo.module.avl.gui;

/**
 * The interface <code>DisplayModeChangeable</code> has to be implemented by all
 * Components, whose display state should be dependent from a display mode setting.
 * Such setting could be a presentation mode for working on a beamer or a normal
 * display mode for working on a pc.<br>
 * Components, which implement this interface have to register themselves at the
 * instance, which controls the display mode setting, as observers.
 * 
 * @author Alexander Claus
 */
public interface DisplayModeChangeable {

	/**
	 * Invoked, if the display mode has changed.
	 */
	public void displayModeChanged();
}