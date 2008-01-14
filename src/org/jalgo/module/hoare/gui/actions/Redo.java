package org.jalgo.module.hoare.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.gui.GuiControl;

public class Redo extends AbstractAction {

	/**
	 * The redo-action
	 * 
	 * @author: Peter
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GuiControl gui;

	public Redo(GuiControl g) {
		gui = g;
		this.putValue(NAME, Messages.getString("hoare", "name.redo"));
		// this.putValue(SHORT_DESCRIPTION, Messages.getString("hoare",
		// "ttt.redo"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.redo")));

	}

	public void actionPerformed(ActionEvent arg0) {
		gui.redo();
		gui.update(null, null);
	}

}
