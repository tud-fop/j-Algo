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

/*
 * Created on Apr 17, 2004
 */

package org.jalgo.main.gui;

import java.util.LinkedList;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderAdapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.jalgo.main.JalgoMain;
import org.jalgo.main.gui.actions.AboutAction;
import org.jalgo.main.gui.actions.AboutModuleAction;
import org.jalgo.main.gui.actions.ExitAction;
import org.jalgo.main.gui.actions.NewAction;
import org.jalgo.main.gui.actions.NewModuleAction;
import org.jalgo.main.gui.actions.OpenAction;
import org.jalgo.main.gui.actions.SaveAction;
import org.jalgo.main.gui.actions.SaveAsAction;

/**
 * This class provides a basic GUI Window with ToolBars, MenuBars and a
 * StatusLine.
 * 
 * @author Christopher Friedrich
 * @author Cornelius Hald
 * @author Alexander Claus
 */
public class JalgoWindow extends ApplicationWindow {

	private JalgoMain parent;
	private LinkedList newActions; // LinkedList of NewActions (one for each
								   // module)
	private SaveAction saveAction;
	private SaveAsAction saveAsAction;
	private CTabFolder ct;

	public JalgoWindow(JalgoMain parent) {

		super(null);

		this.parent = parent;

		newActions = new LinkedList();
		createNewActions();

		saveAction = new SaveAction(this);
		saveAction.setEnabled(false);

		saveAsAction = new SaveAsAction(this);
		saveAsAction.setEnabled(false);

		addMenuBar();
		addToolBar(SWT.WRAP | SWT.FLAT);
		addStatusLine();
	
	}

	/**
	 * Iterates over all open modules, calls <code>close()</code> on them.
	 * Closes the application only if all modules are ready to close, what means,
	 * that each call to <code>close()</code> has to return <code>true</code>.
	 */
	protected void handleShellCloseEvent() {
		while (getParent().getCurrentInstance() != null) {
			if (!getParent().getCurrentInstance().close()) return;
			getParent().itemClosed(ct.getItem(ct.getSelectionIndex()));
		}
		super.handleShellCloseEvent();
	}
	
	protected Control createContents(Composite parent) {

		parent
				.getShell()
				.setText(
						Messages.getString("General.name") + " - " +
						Messages.getString("General.version")); //$NON-NLS-1$
		parent.getShell().setSize(800, 600);
		parent.getShell().setImage(
				new Image(parent.getDisplay(), "pix/jalgo.png")); //$NON-NLS-1$

		final JalgoMain jalgo = this.parent;
		
		// code used in swt 2.1 and deprecated when using swt 3.0
		ct = new CTabFolder(parent, SWT.FLAT);
		ct.addCTabFolderListener(new CTabFolderAdapter() {
			public void itemClosed(CTabFolderEvent event) {
				// Ask user for savinge
				if (!getParent().getCurrentInstance().close()) event.doit = false;
				else jalgo.itemClosed((CTabItem) event.item);
			}
		});

		// replacement for deprecated code above (to use with swt 3.0)
		/*ct = new CTabFolder(parent, SWT.FLAT);
		ct.addCTabFolder2Listener(new CTabFolder2Adapter() {
			public void itemClosed(CTabFolderEvent event) {
				// Ask user for saving
				if (!getParent().getCurrentInstance().close()) event.doit = false;
				else jalgo.itemClosed((CTabItem) event.item);
			}
		});*/
		
		ct.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				jalgo.itemSelected((CTabItem) event.item);
			}
		});
//		ct.setSimple(false);
		ct.setFocus();

		//enable the following for release version -alexander
//		jalgo.newInstance(0);
		//no more need for the following lines when using the swt v 3.138 -alexander
//		parent.pack();
//		parent.getShell().setSize(800, 600);
		
		return ct;
	}

	/**
	 * Creates standard MenuBar
	 */
	protected MenuManager createMenuManager() {

		// ** new_menu (is in file_menu)**

		MenuManager new_menu = new MenuManager(Messages
				.getString("ui.New")); //$NON-NLS-1$

		for (int i = 0; i < newActions.size(); i++) {
			new_menu.add((NewAction) newActions.get(i));
		}

		// ** file_menu **

		MenuManager file_menu = new MenuManager(Messages
				.getString("ui.File")); //$NON-NLS-1$
		file_menu.add(new_menu);
		file_menu.add(new Separator());
		file_menu.add(new OpenAction(this));
		file_menu.add(saveAction);
		file_menu.add(saveAsAction);
		file_menu.add(new Separator());
		file_menu.add(new ExitAction(this));

		// ** help_menu **

		MenuManager help_menu = new MenuManager(Messages
				.getString("ui.Help"), "help"); //$NON-NLS-1$
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

		toolbar.add(new NewModuleAction(parent));
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
	 * @param text
	 *            The Title for the CTabItem
	 * @param img
	 *            The Image for the CTabItem
	 * @return The Composite which should be used to put the module GUI into
	 */
	public CTabItem requestNewCTabItem(String text, Image img) {
		CTabItem cti = new CTabItem(this.ct, SWT.FLAT|SWT.CLOSE);
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

	private void createNewActions() {
		for (int i = 0; i < parent.getKnownModuleInfos().size(); i++) {
			newActions.add(new NewAction(parent, i));
		}
	}

}