/*
 * Created on Apr 17, 2004
 */
 
package org.jalgo.main.gui;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Widget;
import org.jalgo.main.IModuleConnector;
import org.jalgo.main.JalgoMain;
import org.jalgo.main.gui.actions.AboutAction;
import org.jalgo.main.gui.actions.AboutModuleAction;
import org.jalgo.main.gui.actions.ExitAction;
import org.jalgo.main.gui.actions.NewAction;
import org.jalgo.main.gui.actions.OpenAction;
import org.jalgo.main.gui.actions.SaveAction;
import org.jalgo.main.gui.actions.SaveAsAction;
import org.jalgo.main.gui.widgets.CustomViewForm;
import org.jalgo.main.util.Storage;
import org.jalgo.module.synDiaEBNF.ModuleConnector;

/**
 * This class provides a basic GUI Window with ToolBars, MenuBars and a StatusLine.
 * 
 * @author Christopher Friedrich
 * @author Cornelius Hald
 */
public class JalgoWindow extends ApplicationWindow {

	private JalgoMain parent;
	private Collection knownModules;
	private IModuleConnector currentInstance;

	private SaveAction saveAction;
	private SaveAsAction saveAsAction;

	public JalgoWindow(
		JalgoMain parent,
		Collection knownModules,
		IModuleConnector currentInstance) {

		super(null);

		this.parent = parent;
		this.knownModules = knownModules;
		this.currentInstance = currentInstance;

		saveAction = new SaveAction(this);
		saveAction.setEnabled(false);

		saveAsAction = new SaveAsAction(this);
		saveAsAction.setEnabled(false);

		addMenuBar();
		addToolBar(SWT.WRAP | SWT.FLAT);
		addStatusLine();
	}

	private CTabFolder ct;

	private Collection tabWindow;

	private Widget widget;

	private AboutWindow aboutWindow;

	private Storage storage;

	private ModuleConnector moduleConnector;

	private Storage storage1;

	private AboutWindow aboutWindow1;

	private CustomViewForm form1, form2, form3;

	protected Control createContents(Composite parent) {

		parent.getShell().setText(Messages.getString("JalgoWindow.jAlgo_-_Version_X.XX_1")); //$NON-NLS-1$
		parent.getShell().setSize(800, 600);
		parent.getShell().setImage(
			new Image(parent.getDisplay(), "pix/jalgo.png")); //$NON-NLS-1$

		final JalgoMain jalgo = this.parent;

		ct = new CTabFolder(parent, SWT.FLAT);
		ct.addCTabFolderListener(new CTabFolderAdapter() {
			public void itemClosed(CTabFolderEvent event) {
				jalgo.itemClosed((CTabItem) event.item);
			}
		});
		ct.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				jalgo.itemSelected((CTabItem) event.item);
			}
		});

		ct.setFocus();

		return ct;
	}
	/**
	 * Creates standard MenuBar
	 */
	protected MenuManager createMenuManager() {

		// ** new_menu **

		NewAction na;

		MenuManager new_menu = new MenuManager(Messages.getString("JalgoWindow.&New_3")); //$NON-NLS-1$
		new_menu.add(new NewAction(this));
		new_menu.add(new Separator());

		Iterator it = knownModules.iterator();
		while (it.hasNext()) {
			na = new NewAction(this);
			na.setText((String) it.next());
			new_menu.add(na);
		}

		// ** file_menu **

		MenuManager file_menu = new MenuManager(Messages.getString("JalgoWindow.&File_4")); //$NON-NLS-1$
		file_menu.add(new_menu);
		file_menu.add(new Separator());
		file_menu.add(new OpenAction(this));
		file_menu.add(saveAction);
		file_menu.add(saveAsAction);
		file_menu.add(new Separator());
		file_menu.add(new ExitAction(this));

		// ** help_menu **

		MenuManager help_menu = new MenuManager(Messages.getString("JalgoWindow.&Help_5"), "help"); //$NON-NLS-1$
		help_menu.add(new AboutAction(this));
		help_menu.add(new AboutModuleAction(this));

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

		NewAction newact = new NewAction(this);
		//newact.xxx();
		//newact.setMenuCreator(new MenuCreator());
		toolbar.add(newact);
		toolbar.add(new OpenAction(this));
		toolbar.add(new Separator());
		toolbar.add(saveAction);
		toolbar.add(saveAsAction);
		toolbar.add(new Separator());

		return toolbar;
	}

	public CTabFolder getCTabFolder() {
		return this.ct;
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
	 * @param text	The Title for the CTabItem
	 * @param img	The Image for the CTabItem
	 * @return		The Composite which should be used to put the module GUI into
	 */
	public CTabItem requestNewCTabItem(String text, Image img) {
		CTabItem cti = new CTabItem(this.ct, SWT.FLAT);
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

	/* **  ** */

	public boolean openFile(String filename, boolean useCurrentInstance) {
		return parent.openFile(filename, useCurrentInstance);
	}

	public ModuleConnector getModuleConnector() {
		return moduleConnector;
	}

	public void setModuleConnector(ModuleConnector moduleConnector) {
		this.moduleConnector = moduleConnector;
	}

	public SaveAction getSaveAction() {
		return saveAction;
	}

	public SaveAsAction getSaveAsAction() {
		return saveAsAction;
	}

	public void setCurrentInstance(IModuleConnector ci) {
		this.currentInstance = ci;
	}

	public IModuleConnector getCurrentInstance() {
		return currentInstance;
	}

}
