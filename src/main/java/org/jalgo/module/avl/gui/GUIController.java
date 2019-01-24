/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/* Created on 26.04.2005 */
package org.jalgo.module.avl.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.ScrollPaneConstants;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.ModuleConnector;
import org.jalgo.module.avl.NoActionException;
import org.jalgo.module.avl.datastructure.SearchTree;
import org.jalgo.module.avl.gui.components.ControlPane;
import org.jalgo.module.avl.gui.components.DocuPane;
import org.jalgo.module.avl.gui.components.InfoPane;
import org.jalgo.module.avl.gui.components.LogPane;
import org.jalgo.module.avl.gui.components.Navigator;
import org.jalgo.module.avl.gui.components.RandomGenerationDialog;
import org.jalgo.module.avl.gui.components.WelcomeScreen;
import org.jalgo.module.avl.gui.event.AbortAction;
import org.jalgo.module.avl.gui.event.ClearTreeAction;
import org.jalgo.module.avl.gui.event.FinishAction;
import org.jalgo.module.avl.gui.event.PerformAction;
import org.jalgo.module.avl.gui.event.PerformBlockStepAction;
import org.jalgo.module.avl.gui.event.ToggleDisplayModeAction;
import org.jalgo.module.avl.gui.event.UndoAction;
import org.jalgo.module.avl.gui.event.UndoBlockStepAction;
import org.jalgo.module.avl.gui.event.WelcomeAction;
import org.jalgo.module.avl.gui.graphics.Animator;
import org.jalgo.module.avl.gui.graphics.PaintArea;

/**
 * The class <code>GUIController</code> is the main class of the GUI of the
 * AVL module. It initializes the layout of the different parts, offers methods
 * to set the state of buttons etc., to show several dialogs and messages, and
 * causes GUI-components to update when necessary.
 * 
 * @author Alexander Claus
 */
public class GUIController
implements GUIConstants {

	// general references
	private ModuleConnector connector;
	private Controller controller;
	private SearchTree tree;

	// components (based on SWT)
	private WelcomeAction welcomeAction;
	private ClearTreeAction clearTreeAction;
	private AbstractAction abortAction;
	private AbstractAction undoBlockStepAction;
	private AbstractAction undoAction;
	private AbstractAction performAction;
	private AbstractAction performBlockStepAction;
	private AbstractAction finishAction;

	// components (based on Swing)
	private Frame swt_awt_bridge;
	private JPanel contentPane;
	private WelcomeScreen welcomeScreen;
	private RandomGenerationDialog rgd;
	private ControlPane controlPane;
	private InfoPane infoPane;
	private DocuPane docuPane;
	private LogPane logPane;
	private PaintArea paintArea;
	private JScrollPane graphicPane;

	// layout stuff
	private JSplitPane standardLayoutSplitPane;
	private int graphicPaneInsetsX;
	private int graphicPaneInsetsY;
	private int northEastPaneWidth;
	private int northEastPaneHeight;
	private int logPaneHeight;

	private boolean isDialogOpen;
	private Animator animator;

	private static final String lineSep = System.getProperty("line.separator"); //$NON-NLS-1$

	/**
	 * Constructs the <code>GUIController</code> instance for the current AVL
	 * module instance.<br>
	 * Initializes all layout components, especially the toolbar and the menu.
	 * Installs an AWT-Frame on the panel provided by the main program, so that
	 * GUI of the AVL module can be created on Swing base.
	 * 
	 * @param connector the <code>ModuleConnector</code> of the AVL module
	 * @param controller the <code>Controller</code> instance of the AVL
	 *            module
	 * @param st the <code>SearchTree</code> instance of the AVL module
	 */
	public GUIController(ModuleConnector connector, Controller controller,
		SearchTree st) {

		this.connector = connector;
		this.controller = controller;
		this.tree = st;

		// install the main panel
		JComponent rootPane =
			JAlgoGUIConnector.getInstance().getModuleComponent(connector);
		rootPane.setLayout(new BorderLayout());
		contentPane = new JPanel();
		rootPane.add(contentPane, BorderLayout.CENTER);

		// initialization of the following components is called before
		// installToolbar() because of needed references
		paintArea = new PaintArea(this, tree, controller);
		logPane = new LogPane(controller);
		docuPane = new DocuPane(this, controller);
		installToolbar();
		installMenu();
		// init components
		rgd = new RandomGenerationDialog(this, swt_awt_bridge, controller,
			connector);
		welcomeScreen = new WelcomeScreen(this);
		controlPane = new ControlPane(this, controller);
		infoPane = new InfoPane(tree);
		graphicPane = new JScrollPane(paintArea);
		graphicPane.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER,
			new Navigator(paintArea));

		// store current insets for later layout validations (efficiency)
		graphicPaneInsetsX = graphicPane.getInsets().left
			+ graphicPane.getInsets().right;
		graphicPaneInsetsY = graphicPane.getInsets().top
			+ graphicPane.getInsets().bottom;
		// force paint area to resize, if necessary (e.g. splitpane divider
		// moved)
		graphicPane.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				doLayout();
			}
		});
		// on north pane are the paint area, the infobox and the control center
		JPanel northPane = new JPanel(new BorderLayout());
		northPane.add(graphicPane, BorderLayout.CENTER);
		JPanel northEastPane = new JPanel(new BorderLayout());
		northEastPane.add(infoPane, BorderLayout.NORTH);
		northEastPane.add(controlPane, BorderLayout.CENTER);
		northPane.add(northEastPane, BorderLayout.EAST);
		// on south pane are the textareas: docupane and logpane
		JPanel southPane = new JPanel(new BorderLayout());
		southPane.add(docuPane, BorderLayout.CENTER);
		southPane.add(logPane, BorderLayout.EAST);

		// determine the main layout values: on basis of width and height of the
		// northeast pane the size of the graphic-, log- and docupane are
		// calculated
		northEastPaneWidth = northEastPane.getMinimumSize().width;
		northEastPaneHeight = northEastPane.getMinimumSize().height;
		logPaneHeight = (logPane.getFontMetrics(logPane.getFont()).getHeight() + 2)
			* 8 + logPane.getInsets().top + logPane.getInsets().bottom;
		logPane.setMinimumSize(
			new Dimension(northEastPaneWidth, logPaneHeight));
		logPane.setPreferredSize(new Dimension(northEastPaneWidth,
			logPaneHeight));
		graphicPane.setMinimumSize(new Dimension(0, northEastPaneHeight));

		// splitpane to adjust the height of the docu- and logpane
		standardLayoutSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
			false, northPane, southPane);
		standardLayoutSplitPane.setOneTouchExpandable(true);
		standardLayoutSplitPane.setResizeWeight(0.9);
		standardLayoutSplitPane.setDividerLocation(northEastPaneHeight);
	}

	/**
	 * Sets up the toolbar.
	 */
	private void installToolbar() {
		JToolBar toolBar =
			JAlgoGUIConnector.getInstance().getModuleToolbar(connector);
		welcomeAction = new WelcomeAction(this, tree);
		toolBar.add(createToolbarButton(welcomeAction));
		clearTreeAction = new ClearTreeAction(this, tree);
		toolBar.add(createToolbarButton(clearTreeAction));
		toolBar.addSeparator();
		abortAction = new AbortAction(this, controller);
		toolBar.add(createToolbarButton(abortAction));
		undoBlockStepAction = new UndoBlockStepAction(this, controller);
		toolBar.add(createToolbarButton(undoBlockStepAction));
		undoAction = new UndoAction(this, controller);
		toolBar.add(createToolbarButton(undoAction));
		performAction = new PerformAction(this, controller);
		toolBar.add(createToolbarButton(performAction));
		performBlockStepAction = new PerformBlockStepAction(this, controller);
		toolBar.add(createToolbarButton(performBlockStepAction));
		finishAction = new FinishAction(this, controller);
		toolBar.add(createToolbarButton(finishAction));
	}

	/**
	 * Sets up the menu.
	 */
	private void installMenu() {
		JMenu menu = JAlgoGUIConnector.getInstance().getModuleMenu(connector);
		menu.add(welcomeAction);
		menu.add(clearTreeAction);
		menu.addSeparator();
		menu.add(abortAction);
		menu.add(undoBlockStepAction);
		menu.add(undoAction);
		menu.add(performAction);
		menu.add(performBlockStepAction);
		menu.add(finishAction);
		menu.addSeparator();
		menu.add(ToggleDisplayModeAction.getInstance());
		ToggleDisplayModeAction.registerTarget(paintArea);
		ToggleDisplayModeAction.registerTarget(logPane);
		ToggleDisplayModeAction.registerTarget(docuPane);
	}

	/**
	 * Installs the standard layout of the AVL module. This means the
	 * composition of the graphical visualisation of the tree with control
	 * components and information/documentation panels.
	 */
	public void installStandardLayout() {
		connector.setSaveStatus(tree.getHeight() != 0 ?
			SaveStatus.NO_CHANGES : SaveStatus.NOTHING_TO_SAVE);
		contentPane.removeAll();
		contentPane.setBackground(STANDARD_BACKGROUND);
		contentPane.setLayout(new BorderLayout());
		contentPane.add(standardLayoutSplitPane, BorderLayout.CENTER);
		standardLayoutSplitPane.setDividerLocation(
			standardLayoutSplitPane.getSize().height
			- standardLayoutSplitPane.getInsets().bottom
			- standardLayoutSplitPane.getDividerSize() - logPaneHeight);

		welcomeAction.setEnabled(true);
		if (tree.getHeight() > 0) clearTreeAction.setEnabled(true);
		else clearTreeAction.setEnabled(false);

		// reset components to clear input fields etc.
		controlPane.reset();
		logPane.reset();
		docuPane.reset();

		update();
	}

	/**
	 * Switches the layout to the welcome screen, where user can select tasks.
	 */
	public void installWelcomeScreen() {
		contentPane.removeAll();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(welcomeScreen, BorderLayout.CENTER);

		connector.setSaveStatus(SaveStatus.NOTHING_TO_SAVE);
		setToolbarButtonsDisabled();
		contentPane.updateUI();
		contentPane.validate();
	}

	/**
	 * This method should be invoked, if something in visualization or tree
	 * state changes. It causes the appropriate components to update their
	 * contents.
	 */
	public void update() {
		doLayout();
		paintArea.update();
		infoPane.update();
		docuPane.update();
		logPane.update();
	}

	/**
	 * Causes the paint area to resize if necessary. This method should be
	 * invoked if the state of the tree has changed.
	 */
	public void doLayout() {
		contentPane.validate();
		paintArea.setPreferredSize(new Dimension(
			graphicPane.getWidth() - graphicPaneInsetsX,
			graphicPane.getHeight() - graphicPaneInsetsY));
		paintArea.revalidate();
		paintArea.repaint();
	}

	/**
	 * Shows a dialog, where user can create a random tree.
	 */
	public void showRandomGenerationDialog() {
		rgd.open();
	}

	/**
	 * Checks the result of the AVL-Test algorithm and displays an appropriate
	 * dialog. If the test result is positive, a dialog is shown, where the user
	 * is asked for switching to AVL mode. Otherwise, a dialog is shown,
	 * indicating this result to the user. While the dialog is open, the
	 * balances of the tree are shown, so that the user can see, why the result
	 * is so or so. Closing this dialog without switching to AVL mode causes to
	 * hide the balances.
	 */
	public void showAVLTestDialog() {
		connector.setSavingBlocked(true);
		clearTreeAction.setEnabled(false);
		welcomeAction.setEnabled(false);
		// only for visualisation of balances
		controller.setAVLMode(true);
		update();
		isDialogOpen = true;
		if (controller.getAVLTestResult()) {
			if (JOptionPane.showConfirmDialog(controlPane,
				Messages.getString("avl", "GUIController.Is_AVL_tree") + //$NON-NLS-1$ //$NON-NLS-2$
				lineSep +
				Messages.getString("avl", "GUIController.Wish_to_change_to_AVL_mode"), //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("main", "DialogConstants.Question"), //$NON-NLS-1$ //$NON-NLS-2$
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { //$NON-NLS-1$
				controller.putLogDescription(
					Messages.getString("avl", "GUIController.Changed_to_AVL_mode")); //$NON-NLS-1$ //$NON-NLS-2$
				setAVLMode(true, true);
				connector.setSaveStatus(tree.getHeight() > 0 ?
					SaveStatus.CHANGES_TO_SAVE : SaveStatus.NOTHING_TO_SAVE);
				connector.setSavingBlocked(false);
			}
			else {
				// balance visualisation off
				controller.setAVLMode(false);
				// normalize visualisation
				try {
					controller.perform();
				}
				catch (NoActionException ex) {
					JAlgoGUIConnector.getInstance().showErrorMessage(
						ex.getMessage());
				}
				update();
				connector.setSavingBlocked(false);
			}
			isDialogOpen = false;
			if (tree.getHeight() > 0) clearTreeAction.setEnabled(true);
			welcomeAction.setEnabled(true);
		}
		else {
			// use this approach to show nonmodal dialog, which is always on top
			// for navigating through highlighted balances
			// store old state
			final boolean avlEnabled = controlPane.isAVLToggleEnabled();
			final boolean buttonsEnabled = (controlPane.validateKey() == VALID_INPUT);
			controlPane.setKeyInputEnabled(false);
			controlPane.setAVLToggleEnabled(false);
			controlPane.setAlgorithmButtonsEnabled(false);

			final Dialog d = new JOptionPane(
				Messages.getString("avl", "GUIController.Is_no_AVL_tree"), //$NON-NLS-1$ //$NON-NLS-2$
				JOptionPane.INFORMATION_MESSAGE,
				JOptionPane.DEFAULT_OPTION).createDialog(controlPane,
					Messages.getString("main", "DialogConstants.Info")); //$NON-NLS-1$ //$NON-NLS-2$
			d.setAlwaysOnTop(true);
			d.setModal(false);
			d.addComponentListener(new ComponentAdapter() {
				@SuppressWarnings("synthetic-access")
				public void componentHidden(ComponentEvent e) {
					// balance visualisation off
					controller.setAVLMode(false);
					// normalize visualisation
					try {
						controller.perform();
					}
					catch (NoActionException ex) {
						JAlgoGUIConnector.getInstance().showErrorMessage(
							ex.getMessage());
					}
					// restore state
					controlPane.setKeyInputEnabled(true);
					controlPane.setAVLToggleEnabled(avlEnabled);
					controlPane.setAlgorithmButtonsEnabled(buttonsEnabled);
					controlPane.setAVLTestEnabled(true);

					update();
					d.dispose();
					isDialogOpen = false;
					if (tree.getHeight() > 0) clearTreeAction.setEnabled(true);
					welcomeAction.setEnabled(true);
					connector.setSavingBlocked(false);
				}
			});
			d.setVisible(true);
		}
	}

	/**
	 * Checks, if there is any dialog open. This method is necessary for
	 * avoiding conflicts, when closing the module and having dialogs open.
	 * 
	 * @return <code>true</code>, if a dialog is open, <code>false</code>
	 *         otherwise
	 */
	public boolean isDialogOpen() {
		return isDialogOpen;
	}

	/**
	 * Disables the toolbar buttons and the save buttons.
	 */
	public void setToolbarButtonsDisabled() {
		setPerformButtonsEnabled(false);
		setUndoButtonsEnabled(false);
		welcomeAction.setEnabled(false);
		clearTreeAction.setEnabled(false);
	}

	/**
	 * Sets the enable status of the buttons perform, perform-blockstep, finish
	 * and abort on toolbar.
	 * 
	 * @param b <code>true</code>, if buttons should be enabled,
	 * 			<code>false</code> otherwise
	 */
	private void setPerformButtonsEnabled(boolean b) {
		performAction.setEnabled(b);
		performBlockStepAction.setEnabled(b);
		finishAction.setEnabled(b);
		abortAction.setEnabled(b);
	}

	/**
	 * Sets the enable status of the buttons undo and undo-blockstep on toolbar.
	 * 
	 * @param b <code>true</code>, if buttons should be enabled,
	 * 			<code>false</code> otherwise
	 */
	private void setUndoButtonsEnabled(boolean b) {
		undoAction.setEnabled(b);
		undoBlockStepAction.setEnabled(b);
	}

	/**
	 * Returns the documentation panel. This method is currently necessary for
	 * delegating this reference to the action handler.
	 * 
	 * @return the documentation panel
	 */
	public DocuPane getDocuPane() {
		return docuPane;
	}

	/**
	 * Sets the enabled state of the buttons for running an algorithm. This
	 * method is called, when an algorithm is selected.
	 */
	public void algorithmStarted() {
		connector.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
		clearTreeAction.setEnabled(false);
		setUndoButtonsEnabled(false);
		if (controller.algorithmHasNextStep()) {
			setPerformButtonsEnabled(true);
			controlPane.setAVLToggleEnabled(false);
			controlPane.setAlgorithmButtonsEnabled(false);
			controlPane.setKeyInputEnabled(false);
			connector.setSavingBlocked(true);
			controlPane.setMessage(null, NO_MESSAGE);
		}
		else controlPane.setMessage(controller.getResult(),
			INFORMATION_MESSAGE);
		// for explanation of the following step, see DocuPane.update()
		setStepDirection(true);
		docuPane.algorithmStarted();
		update();
	}

	/**
	 * Sets the enable state of the buttons when an algorithm is finished.
	 */
	public void algorithmFinished() {
		if (controller.isAVLMode()) controlPane.setAVLToggleEnabled(true);
		setPerformButtonsEnabled(false);
		setUndoButtonsEnabled(true);
		if (tree.getHeight() > 0) clearTreeAction.setEnabled(true);
		controlPane.setKeyInputEnabled(true);
		controlPane.validateKey();
		controlPane.setMessage(controller.getResult(), INFORMATION_MESSAGE);
		controlPane.setAnimSpeedEnabled(false);
		// enable save buttons
		connector.setSavingBlocked(false);
		// for explanation of the following step, see DocuPane.update()
		setStepDirection(false);
	}

	/**
	 * Sets the enable state of the buttons when an algorithm is aborted.
	 */
	public void algorithmAborted() {
		if (getAnimator() != null && getAnimator().isRunning()) {
			getAnimator().stopAnim();
			controlPane.setAnimSpeedEnabled(false);
			// busy waiting for animator has being stopped to avoid race
			// conditions
			while (getAnimator().isRunning()) {/*busy waiting*/}
		}
		if (controller.isAVLMode() || tree.getHeight() == 0)
			controlPane.setAVLToggleEnabled(true);
		if (tree.getHeight() > 0) clearTreeAction.setEnabled(true);
		setPerformButtonsEnabled(false);
		setUndoButtonsEnabled(false);
		controlPane.setKeyInputEnabled(true);
		controlPane.validateKey();
		docuPane.reset();
		// enable save buttons
		connector.setSavingBlocked(false);
		update();
	}

	/**
	 * Sets the enable state of the buttons for running an algorithm. This
	 * method is called, when an algorithm was completely undone and now perform
	 * or perform blockstep is pressed.
	 */
	public void algorithmRestarted() {
		// TODO: currently this method is invoked at regular algorithm start too
		// so it isn't bad, but not efficient
		setUndoButtonsEnabled(true);
		if (controller.algorithmHasNextStep()) {
			setPerformButtonsEnabled(true);
			controlPane.setAlgorithmButtonsEnabled(false);
			controlPane.setKeyInputEnabled(false);
			connector.setSavingBlocked(true);
			// for explanation of the following step, see DocuPane.update()
			setStepDirection(true);
		}
	}

	/**
	 * Sets the enable state of the buttons when algorithm is completely undone.
	 */
	public void algorithmUndone() {
		setUndoButtonsEnabled(false);
	}

	/**
	 * Sets the enable state of the buttons for running an algorithm. This
	 * method is called, when algorithm was finished and undo is pressed.
	 */
	public void algorithmRestartedFromUndo() {
		setPerformButtonsEnabled(true);
		connector.setSavingBlocked(true);
		clearTreeAction.setEnabled(false);
		controlPane.setAVLToggleEnabled(false);
		controlPane.setAlgorithmButtonsEnabled(false);
		controlPane.setKeyInputEnabled(false);
		controlPane.setMessage(null, NO_MESSAGE);
		// for explanation of the following step, see DocuPane.update()
		setStepDirection(true);
	}

	/**
	 * Sets the enable state of the buttons for running the animation of
	 * generation of a random tree. Loads the algorithm text to the
	 * <code>DocuPane</code>.<br>
	 * This method is called by <code>RandomGenerationDialogActionHandler</code>.
	 */
	public void randomAnimatorStarted() {
		connector.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
		connector.setSavingBlocked(true);
		controlPane.setAnimSpeedEnabled(true);
		controlPane.setAlgorithmButtonsEnabled(false);
		controlPane.setKeyInputEnabled(false);
		abortAction.setEnabled(true);
		// for explanation of the following step, see DocuPane.update()
		setStepDirection(true);
		docuPane.algorithmStarted();
	}

	/**
	 * Sets the current <code>Animator</code>. Currently this is only used
	 * for the <code>RandomTreeAnimator</code>. Other animations may be
	 * provided in later versions.
	 * 
	 * @param animator the new <code>Animator</code>
	 * 
	 * @see #getAnimator()
	 */
	public void setAnimator(Animator animator) {
		this.animator = animator;
	}

	/**
	 * Retrieves the current <code>Animator</code>.
	 * 
	 * @return the current <code>Animator</code>
	 * 
	 * @see #setAnimator(Animator)
	 */
	public Animator getAnimator() {
		return animator;
	}

	/**
	 * Sets the AVL mode.
	 * 
	 * @param avlMode the AVL mode
	 * @param toggleEnabled <code>true</code>, if the AVL-mode-checkbox
	 *            should be enabled, <code>false</code> otherwise
	 */
	public void setAVLMode(boolean avlMode, boolean toggleEnabled) {
		controlPane.setAVLToggleSelected(avlMode);
		controlPane.setAVLTestEnabled(!avlMode);
		controller.setAVLMode(avlMode);
		controlPane.setAVLToggleEnabled(toggleEnabled);
		update();
	}

	/**
	 * Retrieves the algorithm flow control actions as <code>JButton</code>
	 * array. This buttons can be added e.g. to a <code>JToolBar</code>.
	 * 
	 * @return an array of buttons
	 */
	public JButton[] getFlowControlButtons() {
		JButton[] buttons = new JButton[6];
		buttons[0] = createToolbarButton(abortAction);
		buttons[1] = createToolbarButton(undoBlockStepAction);
		buttons[2] = createToolbarButton(undoAction);
		buttons[3] = createToolbarButton(performAction);
		buttons[4] = createToolbarButton(performBlockStepAction);
		buttons[5] = createToolbarButton(finishAction);
		return buttons;
	}

	/**
	 * Creates a <code>JButton</code> object without border and text, which
	 * can be used in <code>JToolBar</code>s
	 * 
	 * @return a <code>JButton</code> instance with the given <code>Action</code>
	 */
	public JButton createToolbarButton(Action a) {
		JToolbarButton button = new JToolbarButton(
			(Icon)a.getValue(Action.SMALL_ICON),
			null, null);
		button.setAction(a);
		button.setText("");
		return button;
	}

	/**
	 * A helper method for easy use of <code>GridBagLayout</code>
	 * 
	 * @param comp the component to lay out
	 * @param gbl the instance of the <code>GridBagLayout</code>
	 * @param x the x coordinate in the grid
	 * @param y the y coordinate in the grid
	 * @param width the width in grid fields
	 * @param height the height in grid fields
	 * @param anchor the anchor position in the field, when component does not
	 *            fill the field
	 * @param fill the mode how to fill the field
	 */
	public static void setGBC(Component comp, GridBagLayout gbl, int x, int y,
		int width, int height, int anchor, int fill) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = x;
		gbc.gridy = y;
		gbc.gridwidth = width;
		gbc.gridheight = height;
		gbc.anchor = anchor;
		gbc.fill = fill;
		gbl.setConstraints(comp, gbc);
	}

	/** Determines the direction of the current step. */
	private boolean performDirection;

	/**
	 * Sets the direction of the current step. This approach is a necessary
	 * trick for correctly scrolling in <code>DocuPane</code>.
	 * 
	 * @param perform <code>true</code>, if the current step is a perform
	 *            step, <code>false</code>, if the current step is an undo
	 *            step
	 * 
	 * @see DocuPane#update()
	 */
	public void setStepDirection(boolean perform) {
		performDirection = perform;
	}

	/**
	 * Retrieves, if the current step is a perform step.
	 * 
	 * @return <code>true</code>, if the current step is a perform step,
	 *         <code>false</code>, if the current step is an undo step
	 * 
	 * @see #setStepDirection(boolean)
	 * @see DocuPane#update()
	 */
	public boolean isPerformStep() {
		return performDirection;
	}
}