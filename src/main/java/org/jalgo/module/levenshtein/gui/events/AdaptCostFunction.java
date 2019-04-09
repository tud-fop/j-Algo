package org.jalgo.module.levenshtein.gui.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.levenshtein.gui.GuiController;

public class AdaptCostFunction extends AbstractAction implements ActionListener {

	private GuiController guiController;
	
	public AdaptCostFunction(GuiController guiController) {
		this.guiController = guiController;
		putValue(NAME, GuiController.getString("toolbar.adaptCostFunctionKey"));
		putValue(SHORT_DESCRIPTION, GuiController.getString("toolbar.adaptCostFunction"));
		putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL("main", "ui.Prefs")));

	}
	
	public void actionPerformed(ActionEvent e) {
		guiController.adaptCostFunction();
	}

}
