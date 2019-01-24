package org.jalgo.module.ebnf.gui.syndia;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;

/**
 * ActionListener for "redo-button" in the j-algo toolbar.
 * 
 * @author Michael Thiele
 * 
 */
@SuppressWarnings("serial")
public class RedoAction extends AbstractAction {

	private GuiController guiController;

	/**
	 * Initializes the button.
	 * 
	 * @param guiController
	 *            the guiController - needed in <code>actionPerformed()</code>
	 */
	public RedoAction(GuiController guiController) {
		super();
		this.guiController = guiController;
		putValue(NAME, Messages.getString("ebnf", "SynDiaEditor.Redo"));
		putValue(SHORT_DESCRIPTION, Messages.getString("ebnf",
				"SynDiaEditor.Redo"));
		putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL("main",
				"Icon.Redo")));
	}

	/**
	 * Redoes an action.
	 */
	public void actionPerformed(ActionEvent arg0) {
		guiController.getSynDiaController().redo();
	}

}
