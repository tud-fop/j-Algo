package org.jalgo.module.hoare.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.gui.GuiControl;

public class ToggleDescription extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GuiControl gui;

	public ToggleDescription(GuiControl g) {
		gui = g;

		this.putValue(NAME, Messages.getString("hoare", "name.toggleDesc"));
		// this.putValue(SHORT_DESCRIPTION, Messages.getString("hoare",
		// "ttt.toggleDesc"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.zoom")));

	}

	public void actionPerformed(ActionEvent arg0) {
		if (gui.getActiveNode() < 0)
			return;

		gui.getNode(gui.getActiveNode()).setShowLabel(
				!gui.getNode(gui.getActiveNode()).isShowLabel());

		gui.setActiveNode(gui.getActiveNode()); // setChanged, damit
												// notifyObservers funzt;
		gui.notifyObservers();
	}

}
