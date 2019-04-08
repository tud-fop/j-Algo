package org.jalgo.module.levenshtein.gui.events;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.jalgo.module.levenshtein.gui.GuiController;

public class StartAction extends AbstractAction {
	private static final long serialVersionUID = -2735790526306954726L;

	private GuiController controller;
	
	public StartAction(GuiController controller) {
		this.controller = controller;
	}
	
	public void actionPerformed(ActionEvent e) {
		controller.startLevenshteinCalculation();
	}



}
