package org.jalgo.module.levenshtein.gui.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jalgo.module.levenshtein.gui.GuiController;

public class AdaptCostFunction implements ActionListener {

	private GuiController guiController;
	
	public AdaptCostFunction(GuiController guiController) {
		this.guiController = guiController;
	}
	
	public void actionPerformed(ActionEvent e) {
		guiController.adaptCostFunction();
	}

}
