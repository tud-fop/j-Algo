/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
 * Created on 13.04.2004
 */

package org.jalgo.main;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.main.gui.actions.SaveAsAction;
import org.jalgo.main.util.Storage;
import org.jalgo.module.synDiaEBNF.ModuleConnector;
import org.jalgo.module.synDiaEBNF.ModuleInfo;

/**
 * @author Christopher Friedrich, Michael Pradel
 */
public class JalgoMain {

	private JalgoWindow appWin;

	private LinkedList knownModules;
	// LinkedList of Class object with IModuleConnectors
	private LinkedList knownModuleInfos; // LinkedList of IModulesInfos
	private HashMap openInstances; // key = CTabItem, value = IModuleConnector
	private IModuleConnector currentInstance;

	public JalgoMain() {
		knownModules = new LinkedList();
		knownModuleInfos = new LinkedList();
		addKnownModules();
		openInstances = new HashMap();
	}

	public void createGUI() {
		appWin = new JalgoWindow(this);
		appWin.setBlockOnOpen(true);
		appWin.open();

		Display.getCurrent().dispose();
	}

	/**
	 * Is called, when the specified tab item is closed.
	 * @param cti The tab item, which is closed.
	 */
	public void itemClosed(CTabItem cti) {
		/* Delete menu and toolbar of CTab */
		if (currentInstance.getMenuManager() != null)
			currentInstance.getMenuManager().setVisible(false);
		if (currentInstance.getToolBarManager() != null)
			currentInstance.getToolBarManager().setVisible(false);
		appWin.getMenuBarManager().update(true);
		appWin.getToolBarManager().update(true);
		/* Remove CTab */
		openInstances.remove(cti);
		cti.dispose();
		if (openInstances.isEmpty()) {
			//closed tab was the last one
			currentInstance = null;
		} else {
			itemSelected(appWin.getCTabFolder().getSelection());
		}
	}

	/**
	 * Set currentInstance to corresponding CTabItem
	 * @param cti 
	 */
	public void itemSelected(CTabItem cti) {
		//happends only, when program is launched
		if (openInstances.isEmpty())
			return;

		//makes current Module-Tool/MenuBar invisible
		if (currentInstance != null) {
			if (currentInstance.getMenuManager() != null)
				currentInstance.getMenuManager().setVisible(false);
			if (currentInstance.getToolBarManager() != null)
				currentInstance.getToolBarManager().setVisible(false);
		}

		currentInstance = (IModuleConnector) openInstances.get(cti);

		//makes Module-Tool/MenuBar from new tab's module visible
		if (currentInstance == null) {
			throw new InternalErrorException("itemSelected() called, but new tab item's module is null");
		}
		if (currentInstance.getMenuManager() != null)
			currentInstance.getMenuManager().setVisible(true);
		if (currentInstance.getToolBarManager() != null)
			currentInstance.getToolBarManager().setVisible(true);

		appWin.getMenuBarManager().update(true);
		appWin.getToolBarManager().update(true);
	}

	/**
	 * Creates a new instance of the specified module.
	 * @param modNumber Number of new module in <code>knownModules</code> starting with 0.
	 * @return
	 */
	public IModuleConnector newInstance(int modNumber) {
		// Makes current Module-Tool/MenuBar invisible
		if (currentInstance != null) {
			if (currentInstance.getMenuManager() != null)
				currentInstance.getMenuManager().setVisible(false);
			if (currentInstance.getToolBarManager() != null)
				currentInstance.getToolBarManager().setVisible(false);
		}

		// Requests a fresh CTabItem from the appWin
		String ctiText =
			((IModuleInfo) knownModuleInfos.get(modNumber)).getName();
		CTabItem cti = appWin.requestNewCTabItem(ctiText, new Image(appWin.getShell().getDisplay(), "pix/jalgo-file.png")); //$NON-NLS-1$

		// Create a new instance of a module.
		Class constrArgs[] =
			new Class[] {
				ApplicationWindow.class,
				Composite.class,
				SubMenuManager.class,
				SubToolBarManager.class,
				SubStatusLineManager.class };
		Object[] args =
			new Object[] {
				appWin,
				(Composite) cti.getControl(),
				new SubMenuManager(appWin.getMenuBarManager()),
				new SubToolBarManager(appWin.getToolBarManager()),
				new SubStatusLineManager(appWin.getTheStatusLineManager())};
		try {
			Constructor constr =
				((Class) knownModules.get(modNumber)).getConstructor(
					constrArgs);
			currentInstance = (IModuleConnector) constr.newInstance(args);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Set CTabItem selected
		appWin.getCTabFolder().setSelection(
			appWin.getCTabFolder().getItemCount() - 1);

		// Activate the modules Menu
		currentInstance.getMenuManager().setVisible(true);
		appWin.getMenuBarManager().update(true);

		// Activate the modules ToolBar
		currentInstance.getToolBarManager().setVisible(true);
		appWin.getToolBarManager().update(true);

		// Add module to running instances
		openInstances.put(cti, currentInstance);

		currentInstance.run();

		return currentInstance;
	}

	public LinkedList getKnownModules() {
		return knownModules;
	}

	public LinkedList getKnownModuleInfos() {
		return knownModuleInfos;
	}

	public IModuleConnector getCurrentInstance() {
		return currentInstance;
	}

	/**
	 * Takes content from module and stores it in currently used file
	 * 
	 */
	public boolean saveFile() {
		if (currentInstance.getModuleInfo().getOpenFileName() == null) {
			SaveAsAction a = new SaveAsAction(appWin);
			a.run();
			return true;
		}
		return saveFileAs(currentInstance.getModuleInfo().getOpenFileName());
	}

	/**
	 * Takes content from module and stores it in file with given filename
	 * 
	 * @param filename
	 */
	public boolean saveFileAs(String filename) {
		return Storage.save(filename);
	}

	/**
	 * Opens file 
	 * 
	 * @param filename
	 */
	public boolean openFile(String filename) {

		return Storage.load(filename);
	}

	/**
	 * Opens file and gives content to Module
	 * 
	 * @param filename
	 */
	public boolean openFile(String filename, boolean useCurrentInstance) {
		if (useCurrentInstance) {
			return Storage.load(filename, currentInstance);
		}
		return Storage.load(filename);
	}
	
	/**
	 * Fills <code>knownModules</code> and <code>knownModuleInfos</code> with content.
	 * Module programmers have to alter this method and add their module here! 
	 */
	private void addKnownModules() {
		try {
			knownModules.add(ModuleConnector.class);
			knownModules.add(org.jalgo.module.testModule.ModuleConnector.class);
			//Add a new ModuleConnector here!!
		} catch (Exception e) {
			e.printStackTrace();
		}
		knownModuleInfos.add(new ModuleInfo());
		knownModuleInfos.add(new org.jalgo.module.testModule.ModuleInfo());
		//Add a new ModuleInfo here!!
	}
}
