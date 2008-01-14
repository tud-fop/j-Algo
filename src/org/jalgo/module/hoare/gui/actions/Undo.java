package org.jalgo.module.hoare.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.gui.GuiControl;

public class Undo extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GuiControl gui;

	public Undo(GuiControl g) {
		gui = g;
		this.putValue(NAME, Messages.getString("hoare", "name.undo"));
		// this.putValue(SHORT_DESCRIPTION, Messages.getString("hoare",
		// "ttt.undo"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.undo")));
	}

	public void actionPerformed(ActionEvent arg0) {
		gui.undo();
		gui.update(null, null);

	}

}
