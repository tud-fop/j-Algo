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
import org.jalgo.module.levenshtein.gui.components.CalculationPanel;
import org.jalgo.module.levenshtein.gui.components.GeneralFormulaPanel;
import org.jalgo.module.levenshtein.gui.components.SetUpPanel;
import org.jalgo.module.levenshtein.gui.components.WorkPanel;
import org.jalgo.module.levenshtein.model.Controller;

public class GuiController {

	private JComponent contentPane;
	private Controller controller;
	
	public GuiController(ModuleConnector connector, Controller controller) {
		this.controller = controller;
		contentPane =
				JAlgoGUIConnector.getInstance().getModuleComponent(connector);
//		SetUpPanel setUpPanel = new SetUpPanel();
//		setUpPanel.init();
//		contentPane.add(setUpPanel, BorderLayout.CENTER);

//		GeneralFormulaPanel panel = new GeneralFormulaPanel();
//		panel.init();
//		contentPane.add(panel, BorderLayout.CENTER);
		
//		controller.init("bürste", "schürze", 1, 1, 1, 0);
//		CalculationPanel panel = new CalculationPanel(controller);
//		panel.setFormula(2, 4);
//		contentPane.add(panel, BorderLayout.CENTER);
		
		WorkPanel workPanel = new WorkPanel();
		workPanel.init(controller, "bürste", "schürze");
		contentPane.add(workPanel, BorderLayout.CENTER);
		
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
	
	public static String getString(String key) {
		return Messages.getString("levenshtein", key);
	}
}
