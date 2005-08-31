/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and platform
 * independant. j-Algo is developed with the help of Dresden University of
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

/*
 * Created on Apr 17, 2004
 */

package org.jalgo.main.gui;

import java.util.LinkedList;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.jalgo.main.IModuleConnector;
import org.jalgo.main.JalgoMain;
import org.jalgo.main.gui.actions.AboutAction;
import org.jalgo.main.gui.actions.AboutModuleAction;
import org.jalgo.main.gui.actions.ExitAction;
import org.jalgo.main.gui.actions.HelpAction;
import org.jalgo.main.gui.actions.NewAction;
import org.jalgo.main.gui.actions.NewModuleAction;
import org.jalgo.main.gui.actions.OpenAction;
import org.jalgo.main.gui.actions.SaveAction;
import org.jalgo.main.gui.actions.SaveAsAction;
import org.jalgo.main.util.Messages;

/**
 * This class provides a basic GUI Window with ToolBars, MenuBars and a
 * StatusLine.
 * 
 * @author Christopher Friedrich
 * @author Cornelius Hald
 * @author Alexander Claus
 */
public class JalgoWindow
extends ApplicationWindow {

	private JalgoMain parent;

	private LinkedList<NewAction> newActions;

	private SaveAction saveAction;
	private SaveAsAction saveAsAction;
	private HelpAction helpAction;
	private AboutModuleAction aboutModuleAction;

	private CTabFolder ct;

	public JalgoWindow(JalgoMain parent) {

		super(null);

		this.parent = parent;

		newActions = new LinkedList<NewAction>();
		createNewActions();

		saveAction = new SaveAction(this);
		saveAction.setEnabled(false);

		saveAsAction = new SaveAsAction(this);
		saveAsAction.setEnabled(false);

		helpAction = new HelpAction(this);
		aboutModuleAction = new AboutModuleAction(this);
		aboutModuleAction.setEnabled(false);

		addMenuBar();
		addToolBar(SWT.WRAP | SWT.FLAT);
		addStatusLine();

	}

	/**
	 * Iterates over all open modules, calls <code>close()</code> on them.
	 * Closes the application only if all modules are ready to close, what
	 * means, that each call to <code>close()</code> has to return
	 * <code>true</code>.
	 */
	protected void handleShellCloseEvent() {
		while (getParent().getCurrentInstance() != null) {
			if (!getParent().getCurrentInstance().close() ||
				!showFinalSaveDialog(getParent().getCurrentInstance())) return;
			getParent().itemClosed(ct.getItem(ct.getSelectionIndex()));
		}
		super.handleShellCloseEvent();
	}

	/**
	 * If the module data of the current module are not saved, this method asks
	 * the user for saving his work, when the module / program is intended
	 * to be closed. This method returns <code>false</code>, if the user presses
	 * CANCEL during this process, <code>true</code> otherwise.
	 * 
	 * @return <code>true</code>, if module data are saved or if the user closes
	 * 			all dialogs normally, <code>false</code>, if the user presses
	 * 			CANCEL during saving
	 */
	private boolean showFinalSaveDialog(IModuleConnector moduleInstance) {
		if (moduleInstance.getSaveStatus() == IModuleConnector.NO_CHANGES ||
			moduleInstance.getSaveStatus() == IModuleConnector.NOTHING_TO_SAVE)
			return true;
		switch (showConfirmDialog(Messages.getString("main", "ui.Wish_to_save"), //$NON-NLS-1$ //$NON-NLS-2$
			DialogConstants.YES_NO_CANCEL_OPTION)) {
			case DialogConstants.YES_OPTION:
				return saveFile();
			case DialogConstants.NO_OPTION:
				return true;
			case DialogConstants.CANCEL_OPTION:
				return false;
			default:
				return false;
		}
	}

	protected Control createContents(Composite parent) {
		parent.getShell().setText(
			Messages.getString("main", "General.name") + //$NON-NLS-1$ //$NON-NLS-2$
			" - " + //$NON-NLS-1$
			Messages.getString("main", "General.version")); //$NON-NLS-1$ //$NON-NLS-2$
		parent.getShell().setSize(800, 600);
		parent.getShell().setImage(ImageDescriptor.createFromURL(
			Messages.getResourceURL("main", "ui.Logo")).createImage()); //$NON-NLS-1$ //$NON-NLS-2$

		final JalgoMain jalgo = this.parent;

		ct = new CTabFolder(parent, SWT.FLAT);
		ct.addCTabFolder2Listener(new CTabFolder2Adapter() {
			@SuppressWarnings("synthetic-access")
			public void close(CTabFolderEvent event) {
				// Ask user for saving
				IModuleConnector moduleInstance =
					jalgo.getModuleInstanceByTab((CTabItem)event.item); 
				if (!moduleInstance.close() ||
					!showFinalSaveDialog(moduleInstance)) event.doit = false;
				else jalgo.itemClosed((CTabItem)event.item);
			}
		});

		ct.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				jalgo.itemSelected((CTabItem)event.item);
			}
		});

		ct.setSimple(false);
		ct.setFocus();

		// no more need for the following lines when using the swt v 3.138
		// -alexander
		// parent.pack();
		// parent.getShell().setSize(800, 600);

		// start gui to choose module (by michi)
		/*CTabItem cti = requestNewCTabItem("Willkommen",
			ImageDescriptor.createFromURL(
				getClass().getResource("/main_pix/jalgo-file.png")).createImage());
		new ModuleChooser(jalgo, cti, (Composite)cti.getControl(), SWT.NONE);*/

		return ct;
	}

	/**
	 * Creates standard MenuBar
	 */
	protected MenuManager createMenuManager() {
		// ** new_menu (is in file_menu)**

		MenuManager new_menu = new MenuManager(
			Messages.getString("main", "ui.New")); //$NON-NLS-1$ //$NON-NLS-2$

		for (int i = 0; i < newActions.size(); i++) {
			new_menu.add(newActions.get(i));
		}

		// ** file_menu **

		MenuManager file_menu = new MenuManager(
			Messages.getString("main", "ui.File")); //$NON-NLS-1$ //$NON-NLS-2$
		file_menu.add(new_menu);
		file_menu.add(new Separator());
		file_menu.add(new OpenAction(this));
		file_menu.add(saveAction);
		file_menu.add(saveAsAction);
		file_menu.add(new Separator());
		file_menu.add(new ExitAction(this));

		// ** help_menu **

		MenuManager help_menu = new MenuManager(
			Messages.getString("main", "ui.Help"), //$NON-NLS-1$ //$NON-NLS-2$
			"help"); //$NON-NLS-1$
		help_menu.add(helpAction);
		help_menu.add(new Separator());
		help_menu.add(new AboutAction(this));
		help_menu.add(aboutModuleAction);

		// ** menubar **

		MenuManager menubar = new MenuManager(""); //$NON-NLS-1$
		menubar.add(file_menu);
		menubar.add(help_menu);

		return menubar;
	}

	/**
	 * Create standard ToolBar
	 */
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolbar = new ToolBarManager(style);

		toolbar.add(new NewModuleAction(this));
		toolbar.add(new OpenAction(this));
		toolbar.add(new Separator());
		toolbar.add(saveAction);
		toolbar.add(saveAsAction);
		toolbar.add(new Separator());

		return toolbar;
	}

	public CTabFolder getCTabFolder() {
		return ct;
	}

	public StatusLineManager getTheStatusLineManager() {
		return getStatusLineManager();
	}

	public JalgoMain getParent() {
		return parent;
	}

	/**
	 * Returns new CTabItem which should be used to put the module GUI into.
	 * 
	 * @param text The Title for the CTabItem
	 * @param img The Image for the CTabItem
	 * @return The Composite which should be used to put the module GUI into
	 */
	public CTabItem requestNewCTabItem(String text, Image img) {
		CTabItem cti = new CTabItem(this.ct, SWT.FLAT | SWT.CLOSE);
		cti.setText(text);
		cti.setImage(img);

		Composite comp = new Composite(ct, SWT.NONE);
		comp.setLayout(new FillLayout());

		cti.setControl(comp);

		return cti;
	}

	public void setTitle(String title) {
		getShell().setText(title);
	}

	public boolean saveFile() {
		return parent.saveFile();
	}

	public boolean saveFileAs(String filename) {
		return parent.saveFileAs(filename);
	}

	public boolean openFile(String filename) {
		return parent.openFile(filename);
	}

	public boolean openFile(String filename, boolean useCurrentInstance) {
		return parent.openFile(filename, useCurrentInstance);
	}

	public SaveAction getSaveAction() {
		return saveAction;
	}

	public SaveAsAction getSaveAsAction() {
		return saveAsAction;
	}

	public void updateSaveButtonEnableStatus(int status) {
		switch (status) {
			case IModuleConnector.NOTHING_TO_SAVE:
				saveAction.setEnabled(false);
				saveAsAction.setEnabled(false);
				break;
			case IModuleConnector.NO_CHANGES:
				saveAction.setEnabled(false);
				saveAsAction.setEnabled(true);
				break;
			case IModuleConnector.CHANGES_TO_SAVE:
				saveAction.setEnabled(true);
				saveAsAction.setEnabled(true);
				break;
			case IModuleConnector.SAVING_BLOCKED:
				saveAction.setEnabled(false);
				saveAsAction.setEnabled(false);
		}
	}

	private void createNewActions() {
		for (int i = 0; i < parent.getKnownModuleInfos().size(); i++) {
			newActions.add(new NewAction(parent, i));
		}
	}

	/**
	 * Opens a message box with an error message.
	 * 
	 * @param msg the error message to be displayed
	 */
	public void showErrorMessage(String msg) {
		MessageDialog.openError(getShell(),
			Messages.getString("main", "DialogConstants.Error"), msg); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Opens a message box with a warning message.
	 * 
	 * @param msg the warning message to be displayed
	 */
	public void showWarningMessage(String msg) {
		MessageDialog.openWarning(getShell(),
			Messages.getString("main", "DialogConstants.Warning"), msg); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Opens a confirm dialog with the given question.
	 * 
	 * @param question the question to be displayed
	 * @param optionType an int designating the options available on the dialog
	 *  
	 * @return an integer indicating the option selected by the user
	 */
	public int showConfirmDialog(String question, int optionType) {
		int result = new MessageDialog(getShell(),
			Messages.getString("main", "DialogConstants.Question"), null, //$NON-NLS-1$ //$NON-NLS-2$
			question, MessageDialog.QUESTION,
			DialogConstants.getOptionStrings(optionType), 0).open();
		if (optionType == DialogConstants.OK_CANCEL_OPTION &&
			result == DialogConstants.NO_OPTION)
			return DialogConstants.CANCEL_OPTION;
		return result;
	}

	/**
	 * Sets the enabled state of the 'About module' action to the given value.
	 * 
	 * @param b <code>true</code>, if the action should be enabled,
	 * 			<code>false</code> otherwise
	 */
	public void setAboutModuleActionEnabled(boolean b) {
		aboutModuleAction.setEnabled(b);
	}

	/**
	 * Opens a filechooser for opening files. The file selected by the user can
	 * be opened automatically as j-Algo file. When selected this option the
	 * second parameter specifies, if the selected file should be opened in the
	 * current instance of the module or if a new module instance should be
	 * opened.<br>
	 * If the file should not be opened automatically, e.g. for using the file
	 * in other ways, the file name is returned as string.
	 * 
	 * @param openAsJAlgoFile <code>true</code>, if the file should be opened as
	 * 			j-Algo file, <code>false</code> otherwise
	 * @param useCurrentModuleInstance <code>true</code>, if the file should be
	 * 			opened in current module instance, <code>false</code> otherwise
	 * 
	 * @return the file name of the selected file, if the first parameter is set
	 * 			to <code>false</code>
	 * 
	 */
	public String showOpenDialog(boolean openAsJAlgoFile,
		boolean useCurrentModuleInstance) {
		FileDialog fileChooser = new FileDialog(getShell(), SWT.OPEN);
		fileChooser.setText(Messages.getString("main", "ui.Open_file")); //$NON-NLS-1$
		fileChooser.setFilterPath(System.getProperty("user.dir")); //$NON-NLS-1$
		fileChooser.setFilterExtensions(new String[] { "*.jalgo", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
		fileChooser.setFilterNames(new String[] {
			Messages.getString("main", "OpenAction.jAlgo_files"), //$NON-NLS-1$ //$NON-NLS-2$
			Messages.getString("main", "OpenAction.All_files_8") }); //$NON-NLS-1$ //$NON-NLS-2$

		String filename = fileChooser.open();
		if (openAsJAlgoFile && filename != null)
			openFile(filename, useCurrentModuleInstance);
		return filename;
	}
}