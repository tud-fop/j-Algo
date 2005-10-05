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

import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubToolBarManager;
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
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.IModuleInfo;
import org.jalgo.main.InternalErrorException;
import org.jalgo.main.JalgoMain;
import org.jalgo.main.AbstractModuleConnector.SaveStatus;
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
 * The class <code>JalgoWindow</code> represents the main window of the j-Algo
 * main program. It provides a basic GUI with toolbars, menubars and a status
 * line. The content area in the middle of the window is realized as a tabbed
 * pane for easyly switching between open module instances.<br>
 * This class provides several methods for displaying message boxes and dialogs.
 * Also this class controlls the gui components for the opened module instances.
 * <br>
 * This class realizes a mutation of the singleton pattern. There should be only
 * one instance of <code>JalgoWindow</code>, but: This instance should not be
 * accessed by classes from outside org.jalgo.main.*, furthermore the
 * instantiator (<code>JalgoMain</code>) is in another package than this class.
 * because of this, the classic implementation of the singleton design pattern
 * cannot be used.
 * 
 * @author Alexander Claus, Christopher Friedrich, Cornelius Hald
 */
public class JalgoWindow
extends ApplicationWindow {

	/** The singleton instance of <code>JalgoWindow</code> */
	private static JalgoWindow instance;

	private LinkedList<NewAction> newActions;
	private HashMap<CTabItem, AbstractModuleConnector> openInstances;
	//management of module gui compontents
	private HashMap<AbstractModuleConnector, Composite> moduleComponents;
	private HashMap<AbstractModuleConnector, SubMenuManager> moduleMenus;
	private HashMap<AbstractModuleConnector, SubToolBarManager> moduleToolbars;

	private SaveAction saveAction;
	private SaveAsAction saveAsAction;
	private AboutModuleAction aboutModuleAction;

	private CTabFolder ct;

	/**
	 * Constructs an object of <code>JalgoWindow</code>. This constructor has
	 * the ability to stop the application, when called a second time. This
	 * guarantees, that only a singleton instance of this class exists.
	 */
	public JalgoWindow() {
		super(null);

		if (instance != null) {
			System.err.println(Messages.getString(
				"main", "JalgoWindow.No_second_instance")); //$NON-NLS-1$ //$NON-NLS-2$
			System.err.println(Messages.getString(
				"main", "JalgoWindow.Application_terminates")); //$NON-NLS-1$ //$NON-NLS-2$
			System.exit(1);
		}

		setDefaultImage(ImageDescriptor.createFromURL(
			Messages.getResourceURL("main", "ui.Logo")).createImage()); //$NON-NLS-1$ //$NON-NLS-2$

		openInstances = new HashMap<CTabItem, AbstractModuleConnector>();
		moduleComponents = new HashMap<AbstractModuleConnector, Composite>();
		moduleMenus = new HashMap<AbstractModuleConnector, SubMenuManager>();
		moduleToolbars = new HashMap<AbstractModuleConnector, SubToolBarManager>();

		createNewActions();

		saveAction = new SaveAction();
		saveAction.setEnabled(false);

		saveAsAction = new SaveAsAction();
		saveAsAction.setEnabled(false);

		aboutModuleAction = new AboutModuleAction(this);
		aboutModuleAction.setEnabled(false);

		addMenuBar();
		addToolBar(SWT.WRAP | SWT.FLAT);
		addStatusLine();

		instance = this;
	}

	/**
	 * Iterates over all open modules, calls <code>close()</code> on them.
	 * Closes the application only if all modules are ready to close, what
	 * means, that each call to <code>close()</code> has to return
	 * <code>true</code>.
	 */
	protected void handleShellCloseEvent() {
		while (JalgoMain.getInstance().getCurrentInstance() != null) {
			if (!JalgoMain.getInstance().getCurrentInstance().close() ||
				!showFinalSaveDialog(JalgoMain.getInstance().getCurrentInstance()))
				return;
			itemClosed(ct.getItem(ct.getSelectionIndex()));
		}
		super.handleShellCloseEvent();
	}

	/**
	 * If the module data of the current module are not saved, this method asks
	 * the user for saving his work, when the module / program is intended
	 * to be closed. This method returns <code>false</code>, if the user presses
	 * <code>CANCEL</code> during this process, <code>true</code> otherwise.
	 * 
	 * @return <code>true</code>, if module data are saved or if the user closes
	 * 			all dialogs normally, <code>false</code>, if the user presses
	 * 			<code>CANCEL</code> during saving
	 */
	private boolean showFinalSaveDialog(AbstractModuleConnector moduleInstance) {
		if (moduleInstance.getSaveStatus() == SaveStatus.NO_CHANGES ||
			moduleInstance.getSaveStatus() == SaveStatus.NOTHING_TO_SAVE)
			return true;
		switch (showConfirmDialog(Messages.getString("main", "ui.Wish_to_save"), //$NON-NLS-1$ //$NON-NLS-2$
			DialogConstants.YES_NO_CANCEL_OPTION)) {
			case DialogConstants.YES_OPTION:
				return JalgoMain.getInstance().saveFile();
			case DialogConstants.NO_OPTION:
				return true;
			case DialogConstants.CANCEL_OPTION:
				return false;
			default:
				return false;
		}
	}

	/**
	 * Creates the main component; the tabbed pane. Registers listeners on it to
	 * handle selection and closing events.
	 * Sets also title and size of the main window.<br>
	 * This method is called by the SWT framework.
	 * 
	 * @param parent the parent component of the content pane
	 */
	protected Control createContents(Composite parent) {
		updateTitle();
		parent.getShell().setSize(800, 600);

		ct = new CTabFolder(parent, SWT.FLAT);
		ct.addCTabFolder2Listener(new CTabFolder2Adapter() {
			@SuppressWarnings("synthetic-access")
			public void close(CTabFolderEvent event) {
				// Ask user for saving
				AbstractModuleConnector moduleInstance =
					openInstances.get(event.item); 
				if (!moduleInstance.close() ||
					!showFinalSaveDialog(moduleInstance)) event.doit = false;
				else itemClosed((CTabItem)event.item);
			}
		});

		ct.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("synthetic-access")
			public void widgetSelected(SelectionEvent event) {
				itemSelected((CTabItem)event.item);
			}
		});

		ct.setSimple(false);
		ct.setFocus();

		// no more need for the following lines when using the swt v 3.138
		// -alexander
		// main.pack();
		// main.getShell().setSize(800, 600);

		return ct;
	}

	/**
	 * Creates the standard menu with file and help menu. This method is called
	 * by the SWT framework.
	 */
	protected MenuManager createMenuManager() {
		// new menu (is in file_menu)
		MenuManager new_menu = new MenuManager(
			Messages.getString("main", "ui.New")); //$NON-NLS-1$ //$NON-NLS-2$
		for (int i = 0; i < newActions.size(); i++)
			new_menu.add(newActions.get(i));

		// file_menu
		MenuManager file_menu = new MenuManager(
			Messages.getString("main", "ui.File")); //$NON-NLS-1$ //$NON-NLS-2$
		file_menu.add(new_menu);
		file_menu.add(new Separator());
		file_menu.add(new OpenAction(this));
		file_menu.add(saveAction);
		file_menu.add(saveAsAction);
		file_menu.add(new Separator());
		file_menu.add(new ExitAction(this));

		// help_menu
		MenuManager help_menu = new MenuManager(
			Messages.getString("main", "ui.Help"), //$NON-NLS-1$ //$NON-NLS-2$
			"help"); //$NON-NLS-1$
		help_menu.add(new HelpAction(this));
		help_menu.add(new Separator());
		help_menu.add(new AboutAction(this));
		help_menu.add(aboutModuleAction);

		// menubar
		MenuManager menubar = new MenuManager(""); //$NON-NLS-1$
		menubar.add(file_menu);
		menubar.add(help_menu);

		return menubar;
	}

	/**
	 * Creates the standard ToolBar. This method is called by the SWT framework.
	 * 
	 * @param style the style of the toolbar, see the SWT documentation
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

	/**
	 * Returns a new <code>CTabItem</code> which should be used to put the
	 * module GUI into. The informations about title text and image are taken
	 * from the parameter <code>module</code>, which is an instance of the
	 * <code>IModuleInfo</code> of the interesting module.
	 * 
	 * @param module the instance of <code>IModuleInfo</code> of the module
	 *
	 * @return the composite which should be used to put the module GUI into
	 */
	public CTabItem requestNewCTabItem(IModuleInfo module) {
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(
			module.getLogoURL());
		if (imageDescriptor == null) // create default "jAlgo File" icon.
			imageDescriptor = ImageDescriptor.createFromURL(
				getClass().getResource("/main_pix/jalgo-file.png"));

		CTabItem cti = new CTabItem(ct, SWT.FLAT | SWT.CLOSE);
		cti.setText(module.getName());
		cti.setImage(imageDescriptor.createImage());

		Composite comp = new Composite(ct, SWT.NONE);
		comp.setLayout(new FillLayout());

		cti.setControl(comp);

		return cti;
	}

	/**
	 * This method is called, when the specified tab item is closed.
	 * 
	 * @param cti the tab item, which is closed
	 */
	private void itemClosed(CTabItem cti) {
		/* Delete menu and toolbar of CTab */
		setCurrentInstanceVisible(false);
		/* Remove CTab */
		openInstances.remove(cti);
		cti.dispose();
		//closed tab was the last one
		if (openInstances.isEmpty()) {
			JalgoMain.getInstance().setCurrentInstance(null);
			updateSaveButtonEnableStatus();
			updateTitle();
			setAboutModuleActionEnabled(false);
		}
		else itemSelected(ct.getSelection());
	}

	/**
	 * This method is called, when a tab is selected. Sets the currently active
	 * module instance to the corresponding <code>CTabItem</code> and updates
	 * the GUI.
	 * 
	 * @param cti the <code>CTabItem</code> which was selected
	 */
	private void itemSelected(CTabItem cti) {
		//happends only, when program is launched
		if (openInstances.isEmpty()) return;

		//makes current Module-Tool/MenuBar invisible
		if (JalgoMain.getInstance().getCurrentInstance() != null)
			setCurrentInstanceVisible(false);

		JalgoMain.getInstance().setCurrentInstance(openInstances.get(cti));

		//makes Module-Tool/MenuBar from new tab's module visible
		if (JalgoMain.getInstance().getCurrentInstance() == null) {
			throw new InternalErrorException(
					"itemSelected() called, but new tab item's module is null");
		}
		setCurrentInstanceVisible(true);

		updateTitle();
	}

	/**
	 * Sets the title of the main window to the given string. This method should
	 * never be called directly, but <code>updateTitle</code> should be called.<br>
	 * This action is encapsulated within a new <code>Runnable</code> object,
	 * which is executed, because of interacting of Swing and SWT. Otherwise
	 * there would be thread problems.
	 * 
	 * @param title the new title of the window
	 * 
	 * @see #updateTitle()
	 */
	public void setTitle(final String title) {
		getShell().getDisplay().syncExec(new Runnable() {
			public void run() {
				getShell().setText(title);
			}
		});
	}

	/**
	 * Updates the title string of the application window. Takes file name and
	 * save status from the currently opened module, and composes the title
	 * string from it.<br>
	 * Note: If a module's open file name is <code>null</code>, there is no file
	 * name displayed. If the module's open file name is an empty string (""),
	 * the displayed file name is "untitled".
	 */
	public void updateTitle() {
		AbstractModuleConnector currentInstance =
			JalgoMain.getInstance().getCurrentInstance();
		StringBuffer title = new StringBuffer();
		if (currentInstance != null) {
			if (currentInstance.getOpenFileName() != null) {
				if (currentInstance.getOpenFileName().length() == 0)
					title.append(Messages.getString("main", "ui.Untitled"));
				else title.append(currentInstance.getOpenFileName());
				if (currentInstance.getSaveStatus() ==
					SaveStatus.CHANGES_TO_SAVE) title.append("*  -  ");
				else title.append("  -  ");
			}
			title.append(currentInstance.getModuleInfo().getName());
			title.append("  -  ");
		}
		title.append(Messages.getString("main", "General.name")); //$NON-NLS-1$ //$NON-NLS-2$
		setTitle(title.toString());
	}

	/**
	 * Updates the enabled status of the buttons "Save" and "Save as" to provide
	 * correct semantics. The necessary information to do so is taken from the
	 * currently opened module instance.
	 */
	public void updateSaveButtonEnableStatus() {
		AbstractModuleConnector currentInstance =
			JalgoMain.getInstance().getCurrentInstance();
		if (currentInstance == null ||
			currentInstance.isSavingBlocked()) {
			saveAction.setEnabled(false);
			saveAsAction.setEnabled(false);
		}
		else switch (currentInstance.getSaveStatus()) {
			case NOTHING_TO_SAVE:
				saveAction.setEnabled(false);
				saveAsAction.setEnabled(false);
				break;
			case NO_CHANGES:
				saveAction.setEnabled(false);
				saveAsAction.setEnabled(true);
				break;
			case CHANGES_TO_SAVE:
				saveAction.setEnabled(true);
				saveAsAction.setEnabled(true);
				break;
		}
	}

	/**
	 * Creates the <code>Action</code> objects for each registered module. This
	 * objects are later displayed as menu items in the "New" menu.
	 */
	private void createNewActions() {
		newActions = new LinkedList<NewAction>();
		for (int i=0; i<JalgoMain.getInstance().getKnownModuleInfos().size(); i++)
			newActions.add(new NewAction(JalgoMain.getInstance(), i));
	}

	/**
	 * Sets the message at the status line to the given message string.<br>
	 * This action is encapsulated within a new <code>Runnable</code> object,
	 * which is executed, because of interacting of Swing and SWT. Otherwise
	 * there would be thread problems.
	 * 
	 * @param msg the message string
	 */
	public void setStatusMessage(final String msg) {
		getShell().getDisplay().syncExec(new Runnable() {
			public void run() {
				setStatus(msg);
			}
		});
	}

	/**
	 * Opens a message box with an error message.<br>
	 * The dialog is encapsulated within a new <code>Runnable</code> object,
	 * which is executed, because of interacting of Swing and SWT. Otherwise
	 * there would be thread problems.
	 * 
	 * @param msg the error message to be displayed
	 */
	public void showErrorMessage(final String msg) {
		getShell().getDisplay().syncExec(new Runnable() {
			public void run() {
				MessageDialog.openError(getShell(), Messages.getString(
					"main", "DialogConstants.Error"), msg); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});
		
	}

	/**
	 * Opens a message box with a warning message.<br>
	 * The dialog is encapsulated within a new <code>Runnable</code> object,
	 * which is executed, because of interacting of Swing and SWT. Otherwise
	 * there would be thread problems.
	 * 
	 * @param msg the warning message to be displayed
	 */
	public void showWarningMessage(final String msg) {
		getShell().getDisplay().syncExec(new Runnable() {
			public void run() {
				MessageDialog.openWarning(getShell(), Messages.getString(
					"main", "DialogConstants.Warning"), msg); //$NON-NLS-1$ //$NON-NLS-2$
			}
		});
	}

	/**
	 * Opens a message box with an info message and standard title.
	 * 
	 * @param msg the info message to be displayed
	 */
	public void showInfoMessage(String msg) {
		showInfoMessage(Messages.getString(
			"main", "DialogConstants.Info"), msg); //$NON-NLS-1$ //$NON-NLS-2$
	}

	/**
	 * Opens a message box with an info message and the given title.<br>
	 * The dialog is encapsulated within a new <code>Runnable</code> object,
	 * which is executed, because of interacting of Swing and SWT. Otherwise
	 * there would be thread problems.
	 * 
	 * @param msg the info message to be displayed
	 */
	public void showInfoMessage(final String title, final String msg) {
		getShell().getDisplay().syncExec(new Runnable() {
			public void run() {
				MessageDialog.openInformation(getShell(), title, msg);
			}
		});		
	}

	/* the following variable is necessary for ecapsulate the dialog within a
	   Runnable object*/
	private int _result;
	/**
	 * Opens a confirm dialog with the given question.<br>
	 * The dialog is encapsulated within a new <code>Runnable</code> object,
	 * which is executed, because of interacting of Swing and SWT. Otherwise
	 * there would be thread problems.
	 * 
	 * @param question the question to be displayed
	 * @param optionType an int designating the options available on the dialog
	 *  
	 * @return an integer indicating the option selected by the user
	 */
	public int showConfirmDialog(final String question, final int optionType) {
		getShell().getDisplay().syncExec(new Runnable() {
			@SuppressWarnings("synthetic-access")
			public void run() {
				_result = new MessageDialog(getShell(), Messages.getString(
					"main", "DialogConstants.Question"), null, //$NON-NLS-1$ //$NON-NLS-2$
					question, MessageDialog.QUESTION,
					DialogConstants.getOptionStrings(optionType), 0).open();
				if (optionType == DialogConstants.OK_CANCEL_OPTION &&
					_result == DialogConstants.NO_OPTION)
					_result = DialogConstants.CANCEL_OPTION;
			}
		});
		return _result;
	}

	/**
	 * Sets the enabled state of the 'About module' action to the given value.
	 * 
	 * @param b <code>true</code>, if the action should be enabled,
	 * 			<code>false</code> otherwise
	 */
	private void setAboutModuleActionEnabled(boolean b) {
		aboutModuleAction.setEnabled(b);
	}

	/* the following variable is necessary for ecapsulate the dialog within a
	   Runnable object*/
	private String _filename;

	/**
	 * Opens a filechooser for opening files. The file selected by the user can
	 * be opened automatically as j-Algo file. When selected this option the
	 * second parameter specifies, if the selected file should be opened in the
	 * current instance of the module or if a new module instance should be
	 * opened.<br>
	 * If the file should not be opened automatically, e.g. for using the file
	 * in other ways, the file name is returned as string.<br>
	 * The dialog is encapsulated within a new <code>Runnable</code> object,
	 * which is executed, because of interacting of Swing and SWT. Otherwise
	 * there would be thread problems.
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
	public String showOpenDialog(final boolean openAsJAlgoFile,
		final boolean useCurrentModuleInstance) {
		getShell().getDisplay().syncExec(new Runnable() {
			@SuppressWarnings("synthetic-access")
			public void run() {
				FileDialog fileChooser = new FileDialog(getShell(), SWT.OPEN);
				fileChooser.setText(Messages.getString("main", "ui.Open_file")); //$NON-NLS-1$
				fileChooser.setFilterPath(System.getProperty("user.dir")); //$NON-NLS-1$
				fileChooser.setFilterExtensions(new String[] { "*.jalgo", "*.*" }); //$NON-NLS-1$ //$NON-NLS-2$
				fileChooser.setFilterNames(new String[] {
					Messages.getString("main", "OpenAction.jAlgo_files"), //$NON-NLS-1$ //$NON-NLS-2$
					Messages.getString("main", "OpenAction.All_files_8") }); //$NON-NLS-1$ //$NON-NLS-2$

				_filename = fileChooser.open();
				if (openAsJAlgoFile && _filename != null)
					JalgoMain.getInstance().openFile(
						_filename, useCurrentModuleInstance);
			}
		});
		return _filename;
	}

	/**
	 * Sets the visibility status of the currently opened module instance to the
	 * given value. Setting (in)visible a module instance means to show/hide the
	 * module's menu and toolbar and to update the enabled status of the save
	 * buttons according to the save status of the module instance.
	 * 
	 * @param visible <code>true</code>, if the module instance should be shown,
	 * 				<code>false</code> otherwise
	 */
	public void setCurrentInstanceVisible(boolean visible) {
		AbstractModuleConnector currentInstance =
			JalgoMain.getInstance().getCurrentInstance();
		if (getModuleMenu(currentInstance) != null)
			getModuleMenu(currentInstance).setVisible(visible);
		if (getModuleToolbar(currentInstance) != null)
			getModuleToolbar(currentInstance).setVisible(visible);		
		getMenuBarManager().update(true);
		getToolBarManager().update(true);
		updateSaveButtonEnableStatus();
	}

	/**
	 * This method is invoked during initialization of a new module instance. It
	 * activates the corresponding tab in the tabbed pane, activates the menu
	 * and the toolbar of the module instance and guarantees, that the menu item
	 * 'About module' is enabled.
	 */
	public void activateNewInstance() {
		// Set CTabItem selected
		ct.setSelection(ct.getItemCount() - 1);
		// Set module visible
		setCurrentInstanceVisible(true);
		// Enable 'About module'
		setAboutModuleActionEnabled(true);
	}

	/*--------------Management of module's GUI components---------------*/

	/**
	 * Retrieves the main GUI component of the module instance, having the given
	 * <code>AbstractModuleConnector</code>.
	 * 
	 * @param module the <code>AbstractModuleConnector</code> instance of module
	 * 
	 * @return the main GUI component of the module instance
	 */
	protected Composite getModuleComponent(AbstractModuleConnector module) {
		return moduleComponents.get(module);
	}

	/**
	 * Sets the main GUI component of the module instance, having the given
	 * <code>AbstractModuleConnector</code>.
	 * 
	 * @param module the <code>AbstractModuleConnector</code> of the module
	 * @param comp the main GUI component of the module instance
	 */
	protected void setModuleComponent(AbstractModuleConnector module,
		Composite comp) {
		moduleComponents.put(module, comp);
	}

	/**
	 * Retrieves the menu of the module instance, having the given
	 * <code>AbstractModuleConnector</code>.
	 * 
	 * @param module the <code>AbstractModuleConnector</code> instance of module
	 * 
	 * @return the menu of the module instance
	 */
	protected SubMenuManager getModuleMenu(AbstractModuleConnector module) {
		return moduleMenus.get(module);
	}

	/**
	 * Sets the menu of the module instance, having the given
	 * <code>AbstractModuleConnector</code>.
	 * 
	 * @param module the <code>AbstractModuleConnector</code> of the module
	 * @param menu the menu of the module instance
	 */
	protected void setModuleMenu(AbstractModuleConnector module,
		SubMenuManager menu) {
		moduleMenus.put(module, menu);
	}

	/**
	 * Retrieves the toolbar of the module instance, having the given
	 * <code>AbstractModuleConnector</code>.
	 * 
	 * @param module the <code>AbstractModuleConnector</code> instance of module
	 * 
	 * @return the toolbar of the module instance
	 */
	protected SubToolBarManager getModuleToolbar(AbstractModuleConnector module) {
		return moduleToolbars.get(module);
	}

	/**
	 * Sets the toolbar of the module instance, having the given
	 * <code>AbstractModuleConnector</code>.
	 * 
	 * @param module the <code>AbstractModuleConnector</code> of the module
	 * @param toolbar the toolbar of the module instance
	 */
	protected void setModuleToolbar(AbstractModuleConnector module,
		SubToolBarManager toolbar) {
		moduleToolbars.put(module, toolbar);
	}

	/**
	 * Creates new GUI components for the recently instanciated module instance
	 * and makes them available for getter- and setter-methods.
	 */
	public void createNewModuleGUIComponents() {
		AbstractModuleConnector module =
			JalgoMain.getInstance().getCurrentInstance();
		CTabItem cti = requestNewCTabItem(module.getModuleInfo());
		moduleComponents.put(module, (Composite)cti.getControl());
		moduleMenus.put(module, new SubMenuManager(getMenuBarManager()));
		moduleToolbars.put(module, new SubToolBarManager(getToolBarManager()));
		// Add module to running instances
		openInstances.put(cti, JalgoMain.getInstance().getCurrentInstance());
	}
}