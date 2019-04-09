package org.jalgo.module.levenshtein.gui.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.levenshtein.gui.GuiController;
import org.jalgo.module.levenshtein.pattern.ToolbarObserver;

public class UndoStep extends AbstractAction implements ActionListener {

	private ToolbarObserver obs;
	
	public UndoStep(ToolbarObserver obs) {
		this.obs = obs;
		putValue(NAME, GuiController.getString("toolbar.undoKey"));
		putValue(SHORT_DESCRIPTION, GuiController.getString("toolbar.undo"));
		putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL("main", "Icon.Undo_step")));

	}
	
	public void actionPerformed(ActionEvent e) {
		obs.undoStep();
	}

}
