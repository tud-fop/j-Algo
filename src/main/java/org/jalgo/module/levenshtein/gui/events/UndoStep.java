package org.jalgo.module.levenshtein.gui.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jalgo.module.levenshtein.pattern.ToolbarObserver;

public class UndoStep implements ActionListener {

	private ToolbarObserver obs;
	
	public UndoStep(ToolbarObserver obs) {
		this.obs = obs;
	}
	
	public void actionPerformed(ActionEvent e) {
		obs.undoStep();
	}

}
