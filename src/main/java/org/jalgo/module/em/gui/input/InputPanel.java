package org.jalgo.module.em.gui.input;

import javax.swing.JButton;

/**
 * Interface for all input panels in j-Algo's em-module. Offers access to two
 * buttons (back/forward).
 *
 * @author Tobias Nett
 *
 */
public interface InputPanel {

	/**
	 * Returns the button that should lead to the next view.
	 * No ActionListener is registered to this button.
	 * 
	 * @return forward button - should lead to the next view. Returns
	 *         <b>null</b> if there is no button.
	 */
	public JButton getForwardButton();

	/**
	 * Returns the button that should lead to the previous view. No
	 * ActionListener is registered to this button.
	 * 
	 * @return back button - should lead to the previous view. Returns
	 *         <b>null</b> if there is no button.
	 */
	public JButton getBackButton();

}
