package org.jalgo.module.hoare.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.gui.GuiControl;

public class ReInit extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GuiControl gui;

	public ReInit(GuiControl gui) {

		this.gui = gui;
		this.putValue(NAME, Messages.getString("hoare", "name.reinit"));
		// this.putValue(SHORT_DESCRIPTION, Messages.getString("hoare",
		// "ttt.reinit"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.reinit")));

	}

	public void actionPerformed(ActionEvent arg0) {
		gui.init();
	}

}
