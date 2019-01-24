package org.jalgo.module.ebnf.gui.syndia;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * ActionListener for the "OK-Button" that appears when editing terminal
 * symbols, variables and syntay diagram names.
 * 
 * @author Michael Thiele
 * 
 */
public class OkActionListener implements ActionListener {

	private GuiController guiController;

	/**
	 * Initializes the ActionListener.
	 * 
	 * @param guiController
	 *            the guiController for this listener
	 */
	public OkActionListener(GuiController guiController) {
		this.guiController = guiController;
	}

	/**
	 * Calls <code>guiController.changeLabelOfElement()</code>.
	 */
	public void actionPerformed(ActionEvent e) {
		guiController.changeLabelOfElement();
	}

}
