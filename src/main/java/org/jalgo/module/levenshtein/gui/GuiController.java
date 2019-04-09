package org.jalgo.module.levenshtein.gui;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.main.util.Messages;
import org.jalgo.module.levenshtein.ModuleConnector;
import org.jalgo.module.levenshtein.gui.components.SetUpPanel;
import org.jalgo.module.levenshtein.gui.components.WorkPanel;
import org.jalgo.module.levenshtein.gui.events.Abort;
import org.jalgo.module.levenshtein.gui.events.AdaptCostFunction;
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
	private JMenu menu;

	public GuiController(ModuleConnector connector, Controller controller) {
		this.controller = controller;
		contentPane = JAlgoGUIConnector.getInstance().getModuleComponent(connector);

		// create the set up panel and show it
		setUpPanel = new SetUpPanel();
		setUpPanel.init(new Abort(connector), new StartAction(this));
		contentPane.add(setUpPanel, BorderLayout.CENTER);

//		JMenu menu = JAlgoGUIConnector.getInstance().getModuleMenu(connector);
//		JMenuItem item = new JMenuItem("a menu item");
//		menu.add(item);

		toolbar = JAlgoGUIConnector.getInstance().getModuleToolbar(connector);
		menu = JAlgoGUIConnector.getInstance().getModuleMenu(connector);
	}

	/**
	 * creates the four toolbar buttons to perform steps and undo steps
	 * 
	 * @param obs, an observer that wants to listen to actions
	 */
	private void createToolbar(ToolbarObserver obs) {
		// create the undoAll button
		JToolbarButton undoAll = new JToolbarButton(new ImageIcon(Messages.getResourceURL("main", "Icon.Undo_all")),
				getString("toolbar.undoall"), "");
		undoAll.addActionListener(new UndoAll(obs));
		toolbar.add(undoAll);

		// create the undo button
		JToolbarButton undo = new JToolbarButton(new ImageIcon(Messages.getResourceURL("main", "Icon.Undo_step")),
				getString("toolbar.undo"), "");
		undo.addActionListener(new UndoStep(obs));
		toolbar.add(undo);

		// create the perform a step button
		JToolbarButton performStep = new JToolbarButton(
				new ImageIcon(Messages.getResourceURL("main", "Icon.Perform_step")), getString("toolbar.performStep"),
				"");
		performStep.addActionListener(new PerformStep(obs));
		toolbar.add(performStep);

		// create the perform all steps button
		JToolbarButton performAll = new JToolbarButton(
				new ImageIcon(Messages.getResourceURL("main", "Icon.Perform_all")), getString("toolbar.performAll"),
				"");
		performAll.addActionListener(new PerformAllSteps(obs));
		toolbar.add(performAll);

		JToolbarButton adaptCostFunction = new JToolbarButton(
				new ImageIcon(Messages.getResourceURL("main", "ui.Prefs")), getString("toolbar.adaptCostFunction"), "");
		adaptCostFunction.addActionListener(new AdaptCostFunction(this));
		toolbar.add(adaptCostFunction);
	}

	private void installMenu(ToolbarObserver obs) {
		menu.add(new UndoAll(obs));
		menu.add(new UndoStep(obs));
		menu.add(new PerformStep(obs));
		menu.add(new PerformAllSteps(obs));
		menu.add(new AdaptCostFunction(this));
	}

	private void createShortcuts(ToolbarObserver obs) {
		// a for reset
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0),
				"undoAll");
		contentPane.getActionMap().put("undoAll", new UndoAll(obs));
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0),
				"undo");
		contentPane.getActionMap().put("undo", new UndoStep(obs));
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0),
				"step");
		contentPane.getActionMap().put("step", new PerformStep(obs));
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0),
				"doAll");
		contentPane.getActionMap().put("doAll", new PerformAllSteps(obs));

		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0),
				"back");
		contentPane.getActionMap().put("back", new AdaptCostFunction(this));

	}

	/**
	 * starts the levenshtein calculation and changes the view to the WorkPanel,
	 * also initilizes the controller
	 */
	public void startLevenshteinCalculation() {
		// create the WorkPanel
		WorkPanel workPanel = new WorkPanel();

		// initilize the controller
		controller.init(setUpPanel.getSource(), setUpPanel.getTarget(), setUpPanel.getDeletion(),
				setUpPanel.getInsertion(), setUpPanel.getSubstitution(), setUpPanel.getIdentity());

		// initilize the WorkPanel
		workPanel.init(controller, setUpPanel.getSource(), setUpPanel.getTarget());

		// create the toolbar. the workpanel knows the ToolbarObserver
		createToolbar(workPanel.getToolBarObserver());
		installMenu(workPanel.getToolBarObserver());
		createShortcuts(workPanel.getToolBarObserver());

		// remove other views and add the work panel to the contentPanel
		contentPane.removeAll();
		contentPane.add(workPanel, BorderLayout.CENTER);

		// redraw the panel
		contentPane.revalidate();
		contentPane.repaint();
	}

	public void adaptCostFunction() {
		toolbar.removeAll();
		toolbar.revalidate();
		toolbar.repaint();

		menu.removeAll();
		menu.revalidate();
		menu.repaint();
		
		contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).clear();
		contentPane.getActionMap().clear();

		contentPane.removeAll();
		contentPane.add(setUpPanel, BorderLayout.CENTER);

		// redraw the panel
		contentPane.revalidate();
		contentPane.repaint();
	}

	/**
	 * gets a string from the string resources
	 * 
	 * @param key, the key of the string
	 * @return the string
	 */
	public static String getString(String key) {
		return Messages.getString("levenshtein", key);
	}
}
