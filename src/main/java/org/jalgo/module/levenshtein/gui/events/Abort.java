package org.jalgo.module.levenshtein.gui.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jalgo.module.levenshtein.ModuleConnector;
import org.jalgo.module.levenshtein.gui.GuiController;

public class Abort implements ActionListener {

	private ModuleConnector connector;
	
	public Abort(ModuleConnector connector) {
		this.connector = connector;
	}
	
	public void actionPerformed(ActionEvent e) {
		connector.close();
	}

}
