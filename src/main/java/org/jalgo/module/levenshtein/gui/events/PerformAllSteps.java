package org.jalgo.module.levenshtein.gui.events;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.levenshtein.gui.GuiController;
import org.jalgo.module.levenshtein.pattern.ToolbarObserver;

public class PerformAllSteps extends AbstractAction implements ActionListener {

	private ToolbarObserver obs;
	
	public PerformAllSteps(ToolbarObserver obs) {
		this.obs = obs;
		putValue(NAME, GuiController.getString("toolbar.performAllKey"));
		putValue(SHORT_DESCRIPTION, GuiController.getString("toolbar.performAll"));
		putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL("main", "Icon.Perform_all")));
	}
	
	public void actionPerformed(ActionEvent e) {
		obs.performAllSteps();
	}

}
