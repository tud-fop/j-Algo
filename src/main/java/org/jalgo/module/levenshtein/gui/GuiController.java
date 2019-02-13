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
import org.jalgo.module.levenshtein.gui.components.WorkPanel;
import org.jalgo.module.levenshtein.gui.events.Abort;
import org.jalgo.module.levenshtein.gui.events.PerformAllSteps;
import org.jalgo.module.levenshtein.gui.events.PerformStep;
import org.jalgo.module.levenshtein.gui.events.StartAction;
import org.jalgo.module.levenshtein.gui.events.UndoAll;
import org.jalgo.module.levenshtein.gui.events.UndoStep;
import org.jalgo.module.levenshtein.model.Controller;
import org.jalgo.module.levenshtein.pattern.ToolbarObserver;

public class GuiController {

	private JComponent contentPane;
	private Controller controller;
	
	private SetUpPanel setUpPanel;
	
	private JToolBar toolbar;
	
	public GuiController(ModuleConnector connector, Controller controller) {
		this.controller = controller;
		contentPane =
				JAlgoGUIConnector.getInstance().getModuleComponent(connector);
		setUpPanel = new SetUpPanel();
		setUpPanel.init(new Abort(connector), new StartAction(this));
		contentPane.add(setUpPanel, BorderLayout.CENTER);

//		GeneralFormulaPanel panel = new GeneralFormulaPanel();
//		panel.init();
//		contentPane.add(panel, BorderLayout.CENTER);
		
//		controller.init("bürste", "schürze", 1, 1, 1, 0);
//		CalculationPanel panel = new CalculationPanel(controller);
//		panel.setFormula(2, 4);
//		contentPane.add(panel, BorderLayout.CENTER);
		
		
		
		JMenu menu = JAlgoGUIConnector.getInstance().getModuleMenu(connector);
		JMenuItem item = new JMenuItem("a menu item");
		menu.add(item);

		toolbar = JAlgoGUIConnector.getInstance().getModuleToolbar(connector);
		
//		startLevenshteinCalculation();
	}
	
	public void createToolbar(ToolbarObserver obs) {
		JToolbarButton undoAll = new JToolbarButton(
				new ImageIcon(Messages.getResourceURL("main", "Icon.Undo_all")),
				getString("toolbar.undoall"),
				"");
		undoAll.addActionListener(new UndoAll(obs));
		toolbar.add(undoAll);
		
		JToolbarButton undo = new JToolbarButton(
				new ImageIcon(Messages.getResourceURL("main", "Icon.Undo_step")),
				getString("toolbar.undo"),
				"");
		undo.addActionListener(new UndoStep(obs));
		toolbar.add(undo);
			
		JToolbarButton redo = new JToolbarButton(
				new ImageIcon(Messages.getResourceURL("main", "Icon.Perform_step")),
				getString("toolbar.performStep"),
				"");
		redo.addActionListener(new PerformStep(obs));
		toolbar.add(redo);
		
		JToolbarButton redoAll = new JToolbarButton(
				new ImageIcon(Messages.getResourceURL("main", "Icon.Perform_all")),
				getString("toolbar.performAll"),
				"");
		redoAll.addActionListener(new PerformAllSteps(obs));
		toolbar.add(redoAll);	
	}
	
	public void startLevenshteinCalculation() {
		WorkPanel workPanel = new WorkPanel();
		
		controller.init(
				setUpPanel.getSource(), 
				setUpPanel.getTarget(), 
				setUpPanel.getDeletion(), 
				setUpPanel.getInsertion(), 
				setUpPanel.getSubstitution(), 
				setUpPanel.getIdentity());
		
		workPanel.init(controller, setUpPanel.getSource(), setUpPanel.getTarget());
		
		createToolbar(workPanel.getToolBarObserver());
		
		contentPane.removeAll();
		contentPane.add(workPanel, BorderLayout.CENTER);

		contentPane.revalidate();
		contentPane.repaint();
	}
	
	public static String getString(String key) {
		return Messages.getString("levenshtein", key);
	}
}
