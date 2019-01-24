package org.jalgo.module.c0h0.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.JMenu;

import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.main.util.Messages;
import org.jalgo.module.c0h0.controller.ButtonCommand;
import org.jalgo.module.c0h0.controller.Controller;
import org.jalgo.module.c0h0.controller.ViewManager;
import org.jalgo.module.c0h0.controller.ViewState;
import org.jalgo.module.c0h0.views.TerminalView;

/**
 * Takes Care of all ButtonActions, MouseEffects, Toolbar and Menu
 * 
 * @author Hendrik Sollich
 */
public class ButtonManager implements ActionListener {

	private JToolBar toolbar;
	public JLabel tipLine;
	private JMenu menu;
	private JToolbarButton runEditButton;
	private ArrayList<JToolbarButton> steps;

	private Controller controller;
	private ViewManager viewManager;

	private JToolbarButton c0h0Button, c0flowButton, flowh0Button, transviewOnButton, transviewOffButton;

	private JMenuItem newCode, saveC0, saveFC, saveH0, loadCode, exampleCode;

	private ImageIcon performAllIcon, undoAllIcon, performStepIcon, undoStepIcon, viewC0FlowIcon, viewFlowH0Icon,
			viewC0H0Icon, tvOnIcon, tvOffIcon, runIcon, editIcon;
	private String runString, editString;

	/**
	 * @param controller
	 * @param viewManager
	 * @param toolbar
	 * @param menu
	 */
	public ButtonManager(final Controller controller, ViewManager viewManager, JToolBar toolbar, JMenu menu) {
		this.toolbar = toolbar;
		this.controller = controller;
		this.menu = menu;
		this.viewManager = viewManager;

		// Separator
		toolbar.addSeparator();
		// TODO translate
		runEditButton = createToolbarButton(ButtonCommand.run, runIcon, runString, "Transformation starten", false);
		steps = new ArrayList<JToolbarButton>();
		// Menu aufbauen
		buildMenu();
		// Icons laden
		initIcons();
		// Toolbar aufbauen
		buildToolbar();
	}

	/**
	 * creates toolbar buttons
	 * 
	 * @param cmd
	 * @param icon
	 * @param tooltip
	 * @param enabled
	 * @return
	 */
	private JToolbarButton createToolbarButton(ButtonCommand cmd, ImageIcon icon, String tooltip, boolean enabled) {
		JToolbarButton ele = new JToolbarButton(icon, "", "");
		ele.setActionCommand(cmd.toString());
		ele.addActionListener(this);
		ele.setIcon(icon);
		ele.setToolTipText(tooltip);
		ele.setEnabled(enabled);
		return ele;
	}

	/**
	 * creates toolbar buttons
	 * 
	 * @param cmd
	 * @param icon
	 * @param text
	 * @param tooltip
	 * @param enabled
	 * @return
	 */
	private JToolbarButton createToolbarButton(ButtonCommand cmd, ImageIcon icon, String text, String tooltip,
			boolean enabled) {
		JToolbarButton button = createToolbarButton(cmd, icon, tooltip, enabled);
		button.setText(text);
		return button;
	}

	/**
	 * Assigns menu items to "C0H0"-menu and connects them to Actions
	 */
	private void buildMenu() {
		JCheckBoxMenuItem beamerModeSelector = new JCheckBoxMenuItem();
		beamerModeSelector.setText("Beamermode");
		beamerModeSelector.setActionCommand(ButtonCommand.toggleBeamerMode.toString());
		beamerModeSelector.addActionListener(this);

		newCode = new JMenuItem("Neuen c0-Code erstellen");
		newCode.setActionCommand(ButtonCommand.newCode.toString());
		newCode.addActionListener(this);

		saveC0 = new JMenuItem("Datei speichern (.c0)");
		saveC0.setActionCommand(ButtonCommand.saveC0.toString());
		saveC0.addActionListener(this);

		saveFC = new JMenuItem("Flowchart speichern (.png)");
		saveFC.setActionCommand(ButtonCommand.saveFC.toString());
		saveFC.addActionListener(this);
		saveFC.setEnabled(false);

		saveH0 = new JMenuItem("Datei speichern (.h0)");
		saveH0.setActionCommand(ButtonCommand.saveH0.toString());
		saveH0.addActionListener(this);
		saveH0.setEnabled(false);

		loadCode = new JMenuItem("Datei öffnen...");
		loadCode.setActionCommand(ButtonCommand.loadCode.toString());
		loadCode.addActionListener(this);

		exampleCode = new JMenuItem("Beispiel öffnen...");
		exampleCode.setActionCommand(ButtonCommand.exampleCode.toString());
		exampleCode.addActionListener(this);

		menu.add(newCode);
		menu.add(loadCode);
		menu.add(exampleCode);
		menu.add(saveC0);
		menu.add(saveFC);
		menu.add(saveH0);
		menu.add(beamerModeSelector);
	}

	private void initIcons() {
		// TODO translate this; upper case
		runString = "Transfomieren"; // TODO: Wir simulieren doch garnicht!?
		editString = "Bearbeiten";

		runIcon = new ImageIcon(Messages.getResourceURL("main", "Icon.Finish_algorithm"));
		editIcon = new ImageIcon(Messages.getResourceURL("main", "Icon.Abort_algorithm"));
		performAllIcon = new ImageIcon(Messages.getResourceURL("main", "Icon.Perform_all"));
		undoAllIcon = new ImageIcon(Messages.getResourceURL("main", "Icon.Undo_all"));
		performStepIcon = new ImageIcon(Messages.getResourceURL("main", "Icon.Perform_step"));
		undoStepIcon = new ImageIcon(Messages.getResourceURL("main", "Icon.Undo_step"));
		viewC0FlowIcon = new ImageIcon(Messages.getResourceURL("c0h0", "viewC0FlowIcon"));
		viewFlowH0Icon = new ImageIcon(Messages.getResourceURL("c0h0", "viewFlowH0Icon"));
		viewC0H0Icon = new ImageIcon(Messages.getResourceURL("c0h0", "viewC0H0Icon"));
		tvOnIcon = new ImageIcon(Messages.getResourceURL("c0h0", "transViewOnIcon"));
		tvOffIcon = new ImageIcon(Messages.getResourceURL("c0h0", "transViewOffIcon"));
	}

	/**
	 * Assigns buttons to toolbar and connects them to Actions
	 */
	private void buildToolbar() {
		// TODO translate
		JToolbarButton undoAll = createToolbarButton(ButtonCommand.undoAll, undoAllIcon,
				"Vollständigen Transformationsschritt rückgängig machen", false);
		JToolbarButton undoSingleStep = createToolbarButton(ButtonCommand.undoStep, undoStepIcon,
				"Einzelschritt rückgängig machen", false);
		JToolbarButton performSingleStep = createToolbarButton(ButtonCommand.performStep, performStepIcon,
				"Einzelschritt vorwärts durchführen", false);
		JToolbarButton performAll = createToolbarButton(ButtonCommand.performAll, performAllIcon,
				"Vollständigen Transformationsschritt abschließen", false);

		// Viewstates
		c0h0Button = createToolbarButton(ButtonCommand.viewC0H0, viewC0H0Icon, "C0-Code und H0-Code", false);
		c0flowButton = createToolbarButton(ButtonCommand.viewC0Flow, viewC0FlowIcon, "C0-Code und Flowchart", false);
		flowh0Button = createToolbarButton(ButtonCommand.viewFlowH0, viewFlowH0Icon, "Flowchart und H0-Code", false);

		// Buttons einfügen
		steps.add(undoAll); // 1
		steps.add(undoSingleStep);

		steps.add(performSingleStep);
		steps.add(performAll);

		for (JToolbarButton step : steps)
			toolbar.add(step);

		// Separator
		toolbar.addSeparator();
		toolbar.add(runEditButton, 0);
		runEditButton.setText(runString);
		runEditButton.setIcon(runIcon);

		// Separator
		toolbar.addSeparator();

		// View States
		toolbar.add(c0flowButton);
		toolbar.add(flowh0Button);
		toolbar.add(c0h0Button);
		// Buttons beim konkreten EventListener registrieren
		// TODO translate this
		transviewOnButton = createToolbarButton(ButtonCommand.transviewOn, tvOnIcon, "Regeln anzeigen", true);
		transviewOffButton = createToolbarButton(ButtonCommand.transviewOff, tvOffIcon, "Regeln ausblenden", true);
		transviewOnButton.setEnabled(false);
		transviewOffButton.setEnabled(false);

		toolbar.addSeparator();
		toolbar.add(transviewOnButton);
		toolbar.add(transviewOffButton);

		// Nur für die Optik: keine Vergrößerung bei Buttonchange
		toolbar.updateUI();
	}

	/**
	 * Compares aa ActionCommand to BtnCmd-enum
	 * 
	 * @param e
	 * @param b
	 * @return
	 */
	private boolean isCmd(ActionEvent e, ButtonCommand b) {
		return (b.toString().equals(e.getActionCommand()));
	}

	/**
	 * toggles undo buttons
	 * 
	 * @param b
	 */
	public void setUndoStepEnabled(boolean b) {
		steps.get(1).setEnabled(b);
	}

	/**
	 * toggles undo buttons
	 * 
	 * @param b
	 */
	public void setUndoAllEnabled(boolean b) {
		steps.get(0).setEnabled(b);
	}

	/**
	 * toggles perform buttons
	 * 
	 * @param b
	 */
	public void setPerformStepEnabled(boolean b) {
		steps.get(2).setEnabled(b);
	}

	/**
	 * toggles perform buttons
	 * 
	 * @param b
	 */
	public void setPerformAllEnabled(boolean b) {
		steps.get(3).setEnabled(b);
	}

	/**
	 * toggles trans view visibility
	 * 
	 * @param on
	 */
	public void toggleTransView(boolean on) {
		viewManager.getContainer().toggleBottomPane(on);
		int position = 12;

		if (on) {
			toolbar.remove(transviewOnButton);
			toolbar.add(transviewOffButton, position);
		} else {
			toolbar.remove(transviewOffButton);
			toolbar.add(transviewOnButton, position);
		}
		toolbar.updateUI();
	}

	/**
	 * toggles toolbar steps
	 */
	public void toggleToolbarSteps() {
		for (java.awt.Component b : steps) {
			b.setEnabled(!b.isEnabled());
		}
	}

	/**
	 * sets the state
	 * 
	 * @param state
	 */
	public void setState(ViewState state) {
		switch (state) {
		case C0FLOW:
			TerminalView.println("views: C0FLOW");
			c0flowButton.setEnabled(false);
			flowh0Button.setEnabled(true);
			c0h0Button.setEnabled(true);
			break;

		case FLOWH0:
			TerminalView.println("views: FLOWH0");
			c0flowButton.setEnabled(true);
			flowh0Button.setEnabled(false);
			c0h0Button.setEnabled(true);
			break;

		case C0H0:
			TerminalView.println("views: C0H0");
			c0flowButton.setEnabled(true);
			flowh0Button.setEnabled(true);
			c0h0Button.setEnabled(false);
			break;

		case RUN:
			for (JToolbarButton b : steps)
				b.setEnabled(true);

			runEditButton.setIcon(editIcon);
			runEditButton.setText(editString);

			if (runEditButton.getActionListeners() != null)
				runEditButton.removeActionListener(runEditButton.getActionListeners()[0]);
			runEditButton.addActionListener(this);
			runEditButton.setActionCommand(ButtonCommand.edit.toString());
			saveH0.setEnabled(true);
			saveFC.setEnabled(true);

			c0flowButton.setEnabled(false);
			flowh0Button.setEnabled(true);
			c0h0Button.setEnabled(true);
			break;

		case EDIT:
			TerminalView.println("editView");
			for (JToolbarButton b : steps)
				b.setEnabled(false);
			runEditButton.setEnabled(true);
			runEditButton.setIcon(runIcon);
			runEditButton.setText(runString);
			transviewOnButton.setEnabled(true);
			transviewOffButton.setEnabled(true);

			if (runEditButton.getActionListeners() != null)
				runEditButton.removeActionListener(runEditButton.getActionListeners()[0]);
			runEditButton.addActionListener(this);
			runEditButton.setActionCommand(ButtonCommand.run.toString());
			saveH0.setEnabled(false);
			saveFC.setEnabled(false);

			c0flowButton.setEnabled(false);
			flowh0Button.setEnabled(false);
			c0h0Button.setEnabled(false);
			break;
		}
		toolbar.updateUI();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (isCmd(e, ButtonCommand.run)) {
			runAction();
		} else if (isCmd(e, ButtonCommand.edit)) {
			editAction();
		} else if (isCmd(e, ButtonCommand.undoAll)) {
			controller.undoAll();
		} else if (isCmd(e, ButtonCommand.undoStep)) {
			controller.undoStep();
		} else if (isCmd(e, ButtonCommand.performAll)) {
			controller.performAll();
		} else if (isCmd(e, ButtonCommand.performStep)) {
			controller.performStep();
		} else if (isCmd(e, ButtonCommand.toggleBeamerMode)) {
			controller.toggleBeamerMode();
		} else if (isCmd(e, ButtonCommand.newCode)) {
			newButtonAction();
		} else if (isCmd(e, ButtonCommand.saveC0)) {
			saveC0ButtonAction();
		} else if (isCmd(e, ButtonCommand.saveFC)) {
			saveFCButtonAction();
		} else if (isCmd(e, ButtonCommand.saveH0)) {
			saveH0ButtonAction();
		} else if (isCmd(e, ButtonCommand.loadCode)) {
			loadButtonAction();
		} else if (isCmd(e, ButtonCommand.exampleCode)) {
			exampleButtonAction();
		} else if (isCmd(e, ButtonCommand.viewC0Flow)) {
			controller.setState(ViewState.C0FLOW);
		} else if (isCmd(e, ButtonCommand.viewC0H0)) {
			controller.setState(ViewState.C0H0);
		} else if (isCmd(e, ButtonCommand.viewFlowH0)) {
			controller.setState(ViewState.FLOWH0);
		} else if (isCmd(e, ButtonCommand.transviewOn)) {
			toggleTransView(true);
		} else if (isCmd(e, ButtonCommand.transviewOff)) {
			toggleTransView(false);
		}
	}

	/**
	 * Action for editButton
	 */
	private void editAction() {
		// Edit Button pressed
		// viewModeBox.setSelectedIndex(0);
		controller.returnToEditMode();
	}

	/**
	 * Action for runButton
	 */
	private void runAction() {
		// Run Button pressed
		controller.runTransformation();
	}

	/**
	 * Action for newButton
	 */
	private void newButtonAction() {
		controller.newCode();
	}

	/**
	 * Action for saveButton C0
	 */
	private void saveC0ButtonAction() {
		controller.saveFileC0();
	}

	/**
	 * Action for saveButton H0
	 */
	private void saveH0ButtonAction() {
		controller.saveFileH0();
	}

	/**
	 * Action for saveButton H0
	 */
	private void saveFCButtonAction() {
		controller.saveFileFlowchart();
	}

	/**
	 * Action for loadButton
	 */
	private void loadButtonAction() {
		controller.openFile();
	}

	/**
	 * Action for exampleButton
	 */
	private void exampleButtonAction() {
		controller.openExample();
	}
}
