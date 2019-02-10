package org.jalgo.module.levenshtein.gui;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.main.util.Messages;
import org.jalgo.module.levenshtein.ModuleConnector;
import org.jalgo.module.levenshtein.gui.components.SetUpPanel;
import org.jalgo.module.levenshtein.model.Controller;

public class GuiController {

	private JComponent contentPane;
	private Controller controller;
	
	public GuiController(ModuleConnector connector, Controller controller) {
		this.controller = controller;
		contentPane =
				JAlgoGUIConnector.getInstance().getModuleComponent(connector);
		SetUpPanel setUpPanel = new SetUpPanel();
		setUpPanel.init();
		contentPane.add(setUpPanel, BorderLayout.CENTER);

		JMenu menu = JAlgoGUIConnector.getInstance().getModuleMenu(connector);
		JMenuItem item = new JMenuItem("a menu item");
		menu.add(item);

		JToolBar toolbar =
			JAlgoGUIConnector.getInstance().getModuleToolbar(connector);
		JToolbarButton button = new JToolbarButton(
			new ImageIcon(Messages.getResourceURL("main", "ui.Logo_small")),
			"a tooltip",
			"");
		toolbar.add(button);
	}
	
	public void startLevenshteinCalculation() {
		
	}
	
}
