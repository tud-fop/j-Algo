/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
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
import java.awt.Panel;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.main.gui.actions.Messages;
import org.jalgo.main.gui.actions.SaveAction;
import org.jalgo.main.gui.actions.SaveAsAction;
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
import org.jalgo.module.avl.gui.event.HelpAction;
import org.jalgo.module.avl.gui.event.PerformAction;
import org.jalgo.module.avl.gui.event.PerformBlockStepAction;
import org.jalgo.module.avl.gui.event.SwingSWTAction;
import org.jalgo.module.avl.gui.event.ToggleDisplayModeAction;
import org.jalgo.module.avl.gui.event.UndoAction;
import org.jalgo.module.avl.gui.event.UndoBlockStepAction;
import org.jalgo.module.avl.gui.event.WelcomeAction;
import org.jalgo.module.avl.gui.graphics.Animator;
import org.jalgo.module.avl.gui.graphics.PaintArea;

/**
 * The class <code>GUIController</code> is the main class of the GUI of the
 * AVL module. It initializes the layout of the different parts, offers
 * methods to set the state of buttons etc., to show several dialogs and messages,
 * and causes GUI-components to update when necessary.
 * 
 * @author Alexander Claus
 */
public class GUIController implements GUIConstants {

	//general references
	private ModuleConnector connector;

	private Controller controller;

	private SearchTree tree;

	//components provided by the jAlgo-framework
	private ApplicationWindow appWin;

	private Composite comp;

	private SubMenuManager menuManager;

	private SubToolBarManager toolBarManager;

	private SubStatusLineManager statusLineManager;

	//actions for toolbar and menu (based on SWT)
	private SaveAction saveAction;

	private SaveAsAction saveAsAction;

	private WelcomeAction welcomeAction;

	private ClearTreeAction clearTreeAction;

	private SwingSWTAction abortAction;

	private SwingSWTAction undoBlockStepAction;

	private SwingSWTAction undoAction;

	private SwingSWTAction performAction;

	private SwingSWTAction performBlockStepAction;

	private SwingSWTAction finishAction;

	private HelpAction helpAction;

	//components (based on Swing)
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

	//layout stuff
	private JSplitPane standardLayoutSplitPane;

	private int graphicPaneInsetsX;

	private int graphicPaneInsetsY;

	private int northEastPaneWidth;

	private int northEastPaneHeight;

	private int logPaneHeight;

	//dummies
	private boolean saved;

	private boolean changesToSave;

	private boolean isDialogOpen;

	private Animator animator;

	/**
	 * Constructs the <code>GUIController</code> instance for the current AVL module
	 * instance.<br>
	 * Initializes all layout components, especially the toolbar and the menu.
	 * Installs an AWT-Frame on the panel provided by the main program,
	 * so that GUI of the AVL module can be created on Swing base.
	 * 
	 * @param connector the <code>ModuleConnector</code> of the AVL module
	 * @param appWin the current instance of <code>JalgoWindow</code>
	 * @param comp the parent component
	 * @param menu the menu manager
	 * @param tb the toolbar manager
	 * @param sl the status line manager
	 * @param controller the <code>Controller</code> instance of the AVL module
	 * @param st the <code>SearchTree</code> instance of the AVL module
	 */
	public GUIController(ModuleConnector connector, ApplicationWindow appWin, Composite comp, SubMenuManager menu,
			SubToolBarManager tb, SubStatusLineManager sl, Controller controller, SearchTree st) {

		this.connector = connector;
		this.appWin = appWin;
		this.comp = comp;
		this.menuManager = menu;
		this.toolBarManager = tb;
		this.statusLineManager = sl;

		this.controller = controller;
		this.tree = st;

		//install the main panel on swing base
		Composite locationComp = new Composite(comp, SWT.EMBEDDED);
		swt_awt_bridge = SWT_AWT.new_Frame(locationComp);

		contentPane = new JPanel();
		//here a awt panel is added to the bridge, because of the following bug:
		//if you add a lightweight component (such as JPanel) to the bridge, mouse
		//events are not received. so the cursor doesn't change correctly
		Panel root = new Panel(new BorderLayout());
		root.add(contentPane, BorderLayout.CENTER);
		swt_awt_bridge.add(root);
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			showErrorMessage("Fehler beim Setzen des nativen LAF:\r\n" + e.getMessage());
		} catch (InstantiationException e) {
			showErrorMessage("Fehler beim Setzen des nativen LAF:\r\n" + e.getMessage());
		} catch (IllegalAccessException e) {
			showErrorMessage("Fehler beim Setzen des nativen LAF:\r\n" + e.getMessage());
		} catch (UnsupportedLookAndFeelException e) {
			showErrorMessage("Fehler beim Setzen des nativen LAF:\r\n" + e.getMessage());
		}

		//setup the status line
		statusLineManager.setVisible(true);

		//initialization of the following components is called before
		//installToolbar() because of needed references
		paintArea = new PaintArea(this, tree, controller);
		logPane = new LogPane(this, controller);
		docuPane = new DocuPane(this, controller);
		installToolbar();
		installMenu();
		//init components
		rgd = new RandomGenerationDialog(this, swt_awt_bridge, controller);
		welcomeScreen = new WelcomeScreen(this);
		controlPane = new ControlPane(this, controller);
		infoPane = new InfoPane(this, tree);
		graphicPane = new JScrollPane(paintArea);
		graphicPane.setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, new Navigator(this, paintArea));

		//store current insets for later layout validations (efficiency)
		graphicPaneInsetsX = graphicPane.getInsets().left + graphicPane.getInsets().right;
		graphicPaneInsetsY = graphicPane.getInsets().top + graphicPane.getInsets().bottom;
		//force paint area to resize, if necessary (e.g. splitpane divider moved)
		graphicPane.addComponentListener(new ComponentAdapter() {
			public void componentResized(ComponentEvent e) {
				doLayout();
			}
		});
		//on north pane are the paint area, the infobox and the control center
		JPanel northPane = new JPanel(new BorderLayout());
		northPane.add(graphicPane, BorderLayout.CENTER);
		JPanel northEastPane = new JPanel(new BorderLayout());
		northEastPane.add(infoPane, BorderLayout.NORTH);
		northEastPane.add(controlPane, BorderLayout.CENTER);
		northPane.add(northEastPane, BorderLayout.EAST);
		//on south pane are the textareas: docupane and logpane
		JPanel southPane = new JPanel(new BorderLayout());
		southPane.add(docuPane, BorderLayout.CENTER);
		southPane.add(logPane, BorderLayout.EAST);

		//determine the main layout values: on basis of width and height of the
		//northeast pane the size of the graphic-, log- and docupane are calculated
		northEastPaneWidth = northEastPane.getMinimumSize().width;
		northEastPaneHeight = northEastPane.getMinimumSize().height;
		logPaneHeight = (logPane.getFontMetrics(logPane.getFont()).getHeight() + 2) * 8 + logPane.getInsets().top
				+ logPane.getInsets().bottom;
		logPane.setMinimumSize(new Dimension(northEastPaneWidth, logPaneHeight));
		logPane.setPreferredSize(new Dimension(northEastPaneWidth, logPaneHeight));
		graphicPane.setMinimumSize(new Dimension(0, northEastPaneHeight));

		//splitpane to adjust the height of the docu- and logpane
		standardLayoutSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, northPane, southPane);
		standardLayoutSplitPane.setOneTouchExpandable(true);
		standardLayoutSplitPane.setResizeWeight(0.9);
		standardLayoutSplitPane.setDividerLocation(northEastPaneHeight);

		setChangesToSave(false);
		//restore the non-module buttons, when reactivating module
		final CTabItem thisItem = ((JalgoWindow) appWin).getCTabFolder().getItem(
				((JalgoWindow) appWin).getCTabFolder().getItemCount() - 1);
		((JalgoWindow) appWin).getCTabFolder().addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent arg0) {
				if (arg0.item == thisItem)
					setChangesToSave(areChangesToSave());
			}
		});
	}

	/**
	 * Sets up the toolbar.
	 */
	private void installToolbar() {
		saveAction = ((JalgoWindow) appWin).getSaveAction();
		saveAsAction = ((JalgoWindow) appWin).getSaveAsAction();

		welcomeAction = new WelcomeAction((JalgoWindow) appWin, connector, this, tree);
		toolBarManager.add(welcomeAction);
		clearTreeAction = new ClearTreeAction(this, comp, tree);
		toolBarManager.add(clearTreeAction);
		toolBarManager.add(new Separator());
		abortAction = new AbortAction(this, controller);
		toolBarManager.add(abortAction);
		undoBlockStepAction = new UndoBlockStepAction(this, controller);
		toolBarManager.add(undoBlockStepAction);
		undoAction = new UndoAction(this, controller);
		toolBarManager.add(undoAction);
		performAction = new PerformAction(this, controller);
		toolBarManager.add(performAction);
		performBlockStepAction = new PerformBlockStepAction(this, controller);
		toolBarManager.add(performBlockStepAction);
		finishAction = new FinishAction(this, controller);
		toolBarManager.add(finishAction);

		helpAction = new HelpAction(this);
		toolBarManager.add(new Separator());
		toolBarManager.add(helpAction);
	}

	/**
	 * Sets up the menu.
	 */
	private void installMenu() {
		MenuManager subMenu = new MenuManager("AVL-Baeume");
		subMenu.add(welcomeAction);
		subMenu.add(clearTreeAction);
		subMenu.add(new Separator());
		subMenu.add(abortAction);
		subMenu.add(undoBlockStepAction);
		subMenu.add(undoAction);
		subMenu.add(performAction);
		subMenu.add(performBlockStepAction);
		subMenu.add(finishAction);
		subMenu.add(new Separator());
		subMenu.add(ToggleDisplayModeAction.getInstance());
		ToggleDisplayModeAction.registerTarget(paintArea);
		ToggleDisplayModeAction.registerTarget(logPane);
		ToggleDisplayModeAction.registerTarget(docuPane);
		menuManager.insertBefore("help", subMenu);

		menuManager.findMenuUsingPath("help").add(helpAction);
	}

	/**
	 * Installs the standard layout of the AVL module. This means the composition of
	 * the graphical visualisation of the tree with control components and
	 * information/documentation panels.
	 */
	public void installStandardLayout() {
		setChangesToSave(false);
		contentPane.removeAll();
		contentPane.setBackground(STANDARD_BACKGROUND);
		contentPane.setLayout(new BorderLayout());
		contentPane.add(standardLayoutSplitPane, BorderLayout.CENTER);
		standardLayoutSplitPane.setDividerLocation(standardLayoutSplitPane.getSize().height
				- standardLayoutSplitPane.getInsets().bottom - standardLayoutSplitPane.getDividerSize()
				- logPaneHeight);

		welcomeAction.setEnabled(true);
		if (tree.getHeight() > 0)
			clearTreeAction.setEnabled(true);
		else
			clearTreeAction.setEnabled(false);

		//reset components to clear input fields etc.
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

		setToolbarButtonsDisabled();
		contentPane.updateUI();
		contentPane.validate();
	}

	/**
	 * This method should be invoked, if something in visualization or tree state
	 * changes. It causes the appropriate components to update their contents.
	 */
	public void update() {
		doLayout();
		paintArea.update();
		infoPane.update();
		docuPane.update();
		logPane.update();
	}

	/**
	 * Causes the paint area to resize if necessary. This method should be invoked
	 * if the state of the tree has changed.
	 */
	public void doLayout() {
		contentPane.validate();
		paintArea.setPreferredSize(new Dimension(graphicPane.getWidth() - graphicPaneInsetsX, graphicPane.getHeight()
				- graphicPaneInsetsY));
		paintArea.revalidate();
		paintArea.repaint();
	}

	/**
	 * Opens a filechooser dialog for opening files of the AVL module.
	 */
	public void showOpenDialog() {
		appWin.getShell().getDisplay().syncExec(new Runnable() {
			public void run() {
				FileDialog fileChooser = new FileDialog(appWin.getShell(), SWT.OPEN);
				fileChooser.setText(Messages.getString("ui.Open_file"));
				fileChooser.setFilterPath(System.getProperty("user.dir"));
				fileChooser.setFilterExtensions(new String[] { "*.jalgo", "*.*" });
				fileChooser.setFilterNames(new String[] {
						Messages.getString("OpenAction.jAlgo_files_(*.jalgo)_7"),
						Messages.getString("OpenAction.All_files_8") });
				String filename = fileChooser.open();
				if (filename != null)
					((JalgoWindow) appWin).openFile(filename, true);
			}
		});
	}

	/**
	 * Opens a question dialog to aks the user for saving the current instance of
	 * <code>SearchTree</code>. If the user wants to save, a filechooser dialog
	 * is shown for saving the file.
	 * 
	 * @return <code>true</code>, if the user don't want to save or saves correctly,
	 * 			<code>false</code>, if the user abort one of the dialogs.
	 */
	public boolean showSaveDialog() {
		//ensure that this method is called only from an swt thread!!
		switch (new MessageDialog(appWin.getShell(), "Beenden", null, "Möchten Sie Ihre Arbeit speichern?",
				MessageDialog.QUESTION, new String[] { "Ja", "Nein", "Abbrechen" }, 0).open()) {
		case 0:
			appWin.getShell().getDisplay().syncExec(new Runnable() {
				public void run() {
					FileDialog fileChooser = new FileDialog(appWin.getShell(), SWT.SAVE);
					fileChooser.setText(Messages.getString("ui.Open_file"));
					fileChooser.setFilterPath(System.getProperty("user.dir"));
					fileChooser.setFilterExtensions(new String[] { "*.jalgo", "*.*" });
					fileChooser.setFilterNames(new String[] {
							Messages.getString("OpenAction.jAlgo_files_(*.jalgo)_7"),
							Messages.getString("OpenAction.All_files_8") });
					String filename = fileChooser.open();
					if (filename != null)
						saved = ((JalgoWindow) appWin).saveFileAs(filename);
					else
						saved = false;
				}
			});
			return saved;
		case 1:
			return true;
		case 2:
			return false;
		default:
			return false;
		}
	}

	/**
	 * Shows an error message with the given message string.
	 * @param msg the error message 
	 */
	public void showErrorMessage(final String msg) {
		appWin.getShell().getDisplay().syncExec(new Runnable() {
			public void run() {
				MessageDialog.openError(appWin.getShell(), "Fehler", msg);
			}
		});
		//here no Swing dialog is used because of thread problems, when calling
		//this method from SWT
		//JOptionPane.showMessageDialog(contentPane, msg, "Fehler", JOptionPane.ERROR_MESSAGE);
		//FIXME: wenn harte exception geworfen, muss ausser message noch fehlerbehandlung erfolgen
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
	 * is asked for switching to AVL mode. Otherwise, a dialog is shown, indicating
	 * this result to the user. While the dialog is open, the balances of the tree
	 * are shown, so that the user can see, why the result is so or so.
	 * Closing this dialog without switching to AVL mode causes to hide the
	 * balances.
	 */
	public void showAVLTestDialog() {
		setSaveButtonsEnabled(false);
		clearTreeAction.setEnabled(false);
		welcomeAction.setEnabled(false);
		//only for visualisation of balances
		controller.setAVLMode(true);
		update();
		isDialogOpen = true;
		if (controller.getAVLTestResult()) {
			if (JOptionPane.showConfirmDialog(controlPane, "Der aktuelle Baum hat die AVL - Eigenschaft!\r\n"
					+ "Möchten Sie jetzt in den AVL - Modus wechseln?", "Modus wechseln?",
					JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				controller.putLogDescription("AVL-Modus angeschalten");
				setAVLMode(true, true);
				setChangesToSave(tree.getHeight() > 0);
			} else {
				//balance visualisation off
				controller.setAVLMode(false);
				//normalize visualisation
				try {
					controller.perform();
				} catch (NoActionException ex) {
					showErrorMessage(ex.getMessage());
				}
				update();
				setChangesToSave(areChangesToSave());
			}
			isDialogOpen = false;
			if (tree.getHeight() > 0)
				clearTreeAction.setEnabled(true);
			welcomeAction.setEnabled(true);
		} else {
			//use this approach to show nonmodal dialog, which is always on top
			//for navigating through highlighted balances
			//store old state
			final boolean avlEnabled = controlPane.isAVLToggleEnabled();
			final boolean buttonsEnabled = (controlPane.validateKey() == VALID_INPUT);
			controlPane.setKeyInputEnabled(false);
			controlPane.setAVLToggleEnabled(false);
			controlPane.setAlgorithmButtonsEnabled(false);

			final Dialog d = new JOptionPane("Der aktuelle Baum ist kein AVL - Baum!",
					JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION).createDialog(controlPane,
					"Nachricht");
			d.setAlwaysOnTop(true);
			d.setModal(false);
			d.addComponentListener(new ComponentAdapter() {
				public void componentHidden(ComponentEvent e) {
					//balance visualisation off
					controller.setAVLMode(false);
					//normalize visualisation
					try {
						controller.perform();
					} catch (NoActionException ex) {
						showErrorMessage(ex.getMessage());
					}
					//restore state
					controlPane.setKeyInputEnabled(true);
					controlPane.setAVLToggleEnabled(avlEnabled);
					controlPane.setAlgorithmButtonsEnabled(buttonsEnabled);
					controlPane.setAVLTestEnabled(true);

					update();
					d.dispose();
					isDialogOpen = false;
					if (tree.getHeight() > 0)
						clearTreeAction.setEnabled(true);
					welcomeAction.setEnabled(true);
					setChangesToSave(areChangesToSave());
				}
			});
			d.setVisible(true);
		}
	}

	/**
	 * Checks, if there is any dialog open. This method is necessary for avoiding
	 * conflicts, when closing the module and having dialogs open.
	 * 
	 * @return <code>true</code>, if a dialog is open, <code>false</code> otherwise
	 */
	public boolean isDialogOpen() {
		return isDialogOpen;
	}

	/**
	 * Displays the given message string to the status line.
	 * 
	 * @param msg the message to be displayed
	 */
	public void setStatusMessage(final String msg) {
		//the following approach is necessary, because of getting an
		//invalid thread access, when calling SubStatusLineManager.setMessage()
		//directly
		appWin.getShell().getDisplay().syncExec(new Runnable() {
			public void run() {
				statusLineManager.setMessage(msg);
			}
		});
	}

	/**
	 * Disables the toolbar buttons and the save buttons.
	 */
	public void setToolbarButtonsDisabled() {
		setPerformButtonsEnabled(false);
		setUndoButtonsEnabled(false);
		setSaveButtonsEnabled(false);
		welcomeAction.setEnabled(false);
		clearTreeAction.setEnabled(false);
	}

	/**
	 * Sets the enable status of the buttons perform, perform-blockstep,
	 * finish and abort on toolbar.
	 * 
	 * @param b true, if buttons should be enabled, false otherwise
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
	 * @param b true, if buttons should be enabled, false otherwise
	 */
	private void setUndoButtonsEnabled(boolean b) {
		undoAction.setEnabled(b);
		undoBlockStepAction.setEnabled(b);
	}

	/**
	 * Sets the enable status of the save buttons on toolbar.
	 * 
	 * @param b true, if save buttons should be enabled, false otherwise
	 */
	private void setSaveButtonsEnabled(boolean b) {
		saveAction.setEnabled(b);
		saveAsAction.setEnabled(b);
		//TODO: wenn mehrere module offen sind, und eines geschlossen wird, so
		//werden die save-buttons deaktiviert, obwohl sie im aktuellen modul
		//aktiviert sein sollten
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
	 * Sets the enabled state of the buttons for running an algorithm.
	 * This method is called, when an algorithm is selected.
	 */
	public void algorithmStarted() {
		setChangesToSave(true);
		clearTreeAction.setEnabled(false);
		setUndoButtonsEnabled(false);
		if (controller.algorithmHasNextStep()) {
			setPerformButtonsEnabled(true);
			controlPane.setAVLToggleEnabled(false);
			controlPane.setAlgorithmButtonsEnabled(false);
			controlPane.setKeyInputEnabled(false);
			setSaveButtonsEnabled(false);
			controlPane.setMessage(null, NO_MESSAGE);
		} else
			controlPane.setMessage(controller.getResult(), INFORMATION_MESSAGE);
		//for explanation of the following step, see DocuPane.update()
		setStepDirection(true);
		docuPane.algorithmStarted();
		update();
	}

	/**
	 * Sets the enable state of the buttons when an algorithm is finished.
	 */
	public void algorithmFinished() {
		if (controller.isAVLMode())
			controlPane.setAVLToggleEnabled(true);
		setPerformButtonsEnabled(false);
		setUndoButtonsEnabled(true);
		if (tree.getHeight() > 0)
			clearTreeAction.setEnabled(true);
		controlPane.setKeyInputEnabled(true);
		controlPane.validateKey();
		controlPane.setMessage(controller.getResult(), INFORMATION_MESSAGE);
		controlPane.setAnimSpeedEnabled(false);
		//enable save buttons
		setChangesToSave(areChangesToSave());
		//for explanation of the following step, see DocuPane.update()
		setStepDirection(false);
	}

	/**
	 * Sets the enable state of the buttons when an algorithm is aborted.
	 */
	public void algorithmAborted() {
		if (getAnimator() != null && getAnimator().isRunning()) {
			getAnimator().stopAnim();
			controlPane.setAnimSpeedEnabled(false);
			//busy waiting for animator has being stopped to avoid race conditions
			while (getAnimator().isRunning())
				;
		}
		if (controller.isAVLMode() || tree.getHeight() == 0)
			controlPane.setAVLToggleEnabled(true);
		if (tree.getHeight() > 0)
			clearTreeAction.setEnabled(true);
		setPerformButtonsEnabled(false);
		setUndoButtonsEnabled(false);
		controlPane.setKeyInputEnabled(true);
		controlPane.validateKey();
		docuPane.reset();
		//enable save buttons
		setChangesToSave(areChangesToSave());
		update();
	}

	/**
	 * Sets the enable state of the buttons for running an algorithm.
	 * This method is called, when an algorithm was completely undone and now
	 * perform or perform blockstep is pressed.
	 */
	public void algorithmRestarted() {
		//TODO: currently this method is ivoked at regular algorithm start too
		//so it isn't bad, but not efficient
		setUndoButtonsEnabled(true);
		if (controller.algorithmHasNextStep()) {
			setPerformButtonsEnabled(true);
			controlPane.setAlgorithmButtonsEnabled(false);
			controlPane.setKeyInputEnabled(false);
			setSaveButtonsEnabled(false);
			//for explanation of the following step, see DocuPane.update()
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
	 * Sets the enable state of the buttons for running an algorithm.
	 * This method is called, when algorithm was finished and undo is pressed.
	 */
	public void algorithmRestartedFromUndo() {
		setPerformButtonsEnabled(true);
		setSaveButtonsEnabled(false);
		clearTreeAction.setEnabled(false);
		controlPane.setAVLToggleEnabled(false);
		controlPane.setAlgorithmButtonsEnabled(false);
		controlPane.setKeyInputEnabled(false);
		controlPane.setMessage(null, NO_MESSAGE);
		//for explanation of the following step, see DocuPane.update()
		setStepDirection(true);
	}

	/**
	 * Sets the enable state of the buttons for running the animation of generation
	 * of a random tree. Loads the algorithm text to the <code>DocuPane</code>.<br>
	 * This method is called by <code>RandomGenerationDialogActionHandler</code>.
	 */
	public void randomAnimatorStarted() {
		setChangesToSave(true);
		setSaveButtonsEnabled(false);
		controlPane.setAnimSpeedEnabled(true);
		controlPane.setAlgorithmButtonsEnabled(false);
		controlPane.setKeyInputEnabled(false);
		abortAction.setEnabled(true);
		//for explanation of the following step, see DocuPane.update()
		setStepDirection(true);
		docuPane.algorithmStarted();
	}

	/**
	 * Sets the current <code>Animator</code>. Currently this is only used for the
	 * <code>RandomTreeAnimator</code>. Other animations may be provided in later
	 * versions.
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
	 * @param toggleEnabled <code>true</code>, if the AVL-mode-checkbox should be
	 * 											enabled,
	 * 						<code>false</code> otherwise
	 */
	public void setAVLMode(boolean avlMode, boolean toggleEnabled) {
		controlPane.setAVLToggleSelected(avlMode);
		controlPane.setAVLTestEnabled(!avlMode);
		controller.setAVLMode(avlMode);
		controlPane.setAVLToggleEnabled(toggleEnabled);
		update();
	}

	/**
	 * Retrieves the algorithm flow control actions as <code>JButton</code> array.
	 * This buttons can be added e.g. to a <code>JToolBar</code>.
	 * 
	 * @return an array of buttons
	 */
	public JButton[] getFlowControlButtons() {
		JButton[] buttons = new JButton[6];
		buttons[0] = abortAction.createToolbarButton();
		buttons[1] = undoBlockStepAction.createToolbarButton();
		buttons[2] = undoAction.createToolbarButton();
		buttons[3] = performAction.createToolbarButton();
		buttons[4] = performBlockStepAction.createToolbarButton();
		buttons[5] = finishAction.createToolbarButton();
		return buttons;
	}

	/**
	 * Retrieves the to-save state of this AVL module instance.
	 * 
	 * @return <code>true</code>, if there are changes to be saved,
	 * 			<code>false</code> otherwise
	 */
	public boolean areChangesToSave() {
		return changesToSave;
	}

	/**
	 * Sets the to-save status in this instance of the AVL module. If the status
	 * is set to true, the save buttons are enabled. Otherwise the save buttons
	 * are enabled in the following way:<br>
	 * The save button is disabled. The save-as button is enabled, if there is a
	 * tree to be saved, which means, that the tree has more than 0 nodes.
	 * 
	 * @param b <code>true</code>, if there are changes to be saved,
	 * 			<code>false</code> otherwise
	 */
	public void setChangesToSave(boolean b) {
		changesToSave = b;
		saveAction.setEnabled(b);
		if (changesToSave)
			saveAsAction.setEnabled(true);
		else
			saveAsAction.setEnabled(tree.getHeight() != 0);
		//TODO: if changesToSave is set to true, notify appWin to add '*' to title 
	}

	/**
	 * A helper method for easy use of <code>GridBagLayout</code>
	 * @param comp the component to lay out
	 * @param gbl the instance of the <code>GridBagLayout</code>
	 * @param x the x coordinate in the grid
	 * @param y the y coordinate in the grid
	 * @param width the width in grid fields
	 * @param height the height in grid fields
	 * @param anchor the anchor position in the field, when component does not fill the field
	 * @param fill the mode how to fill the field
	 */
	public static void setGBC(Component comp, GridBagLayout gbl, int x, int y, int width, int height, int anchor, int fill) {
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
	 *  @param perform <code>true</code>, if the current step is a perform step,
	 *  				<code>false</code>, if the current step is an undo step
	 *  
	 *  @see DocuPane#update()
	 */
	public void setStepDirection(boolean perform) {
		performDirection = perform;
	}

	/**
	 * Retrieves, if the current step is a perform step.
	 * 
	 * @return <code>true</code>, if the current step is a perform step,
	 *  		<code>false</code>, if the current step is an undo step
	 *  
	 *  @see #setStepDirection(boolean)
	 *  @see DocuPane#update()
	 */
	public boolean isPerformStep() {
		return performDirection;
	}
}