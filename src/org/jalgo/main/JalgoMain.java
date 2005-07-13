/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
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
 * Created on 13.04.2004
 */

package org.jalgo.main;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.LinkedList;

import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.main.gui.actions.SaveAsAction;
import org.jalgo.main.util.Storage;

/**
 * @author Christopher Friedrich, Michael Pradel
 */
public class JalgoMain {

	private JalgoWindow appWin;

	private LinkedList<Class<IModuleConnector>> knownModules;
	private LinkedList<IModuleInfo> knownModuleInfos;
	private HashMap<CTabItem, IModuleConnector> openInstances;
	
	private IModuleConnector currentInstance;

	public JalgoMain() {
		knownModules = new LinkedList<Class<IModuleConnector>>();
		knownModuleInfos = new LinkedList<IModuleInfo>();
		addKnownModules();
		openInstances = new HashMap<CTabItem, IModuleConnector>();
	}

	public void createGUI() {
		appWin = new JalgoWindow(this);
		appWin.setBlockOnOpen(true);
		appWin.open();

		Display.getCurrent().dispose();	
	}

	/**
	 * Is called, when the specified tab item is closed.
	 * 
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
		appWin.getSaveAction().setEnabled(false);
		appWin.getSaveAsAction().setEnabled(false);
		/* Remove CTab */
		openInstances.remove(cti);
		cti.dispose();
		//closed tab was the last one
		if (openInstances.isEmpty()) currentInstance = null;
		else itemSelected(appWin.getCTabFolder().getSelection());
	}

	/**
	 * Set currentInstance to corresponding CTabItem
	 * 
	 * @param cti
	 */
	public void itemSelected(CTabItem cti) {
		//happends only, when program is launched
		if (openInstances.isEmpty()) return;

		//makes current Module-Tool/MenuBar invisible
		if (currentInstance != null) {
			if (currentInstance.getMenuManager() != null)
				currentInstance.getMenuManager().setVisible(
						false);
			if (currentInstance.getToolBarManager() != null)
				currentInstance.getToolBarManager().setVisible(
						false);
		}

		currentInstance = openInstances.get(cti);

		//makes Module-Tool/MenuBar from new tab's module visible
		if (currentInstance == null) {
			throw new InternalErrorException(
					"itemSelected() called, but new tab item's module is null");
		}
		if (currentInstance.getMenuManager() != null)
			currentInstance.getMenuManager().setVisible(true);
		if (currentInstance.getToolBarManager() != null)
			currentInstance.getToolBarManager().setVisible(true);

		appWin.getMenuBarManager().update(true);
		appWin.getToolBarManager().update(true);
	}

	/**
	 * Creates a new instance of the module with the given name.
	 * Returns <code>null</code>, if the given name does not match to any known
	 * module name.
	 * 
	 * @param moduleName the name of the module to be created
	 * 
	 * @return the <code>IModuleConnector</code> instance of the module, if it is
	 * 			created, <code>null</code> otherwise
	 */
	public IModuleConnector newInstanceByName(String moduleName) {
		for (int i=0; i<knownModuleInfos.size(); i++) {
			if ((knownModuleInfos.get(i)).getName().equals(moduleName))
				return newInstance(i);
		}
		return null;
	}

	/**
	 * Creates a new instance of the specified module.
	 * 
	 * @param modNumber
	 *                       Number of new module in <code>knownModules</code>
	 *                       starting with 0.
	 * @return
	 */
	public IModuleConnector newInstance(int modNumber) {
		// Makes current Module-Tool/MenuBar invisible
		if (currentInstance != null) {
			if (currentInstance.getMenuManager() != null)
				currentInstance.getMenuManager().setVisible(
						false);
			if (currentInstance.getToolBarManager() != null)
				currentInstance.getToolBarManager().setVisible(
						false);
		}
		
		// Requests a fresh CTabItem from the appWin
		IModuleInfo module = knownModuleInfos.get(modNumber);
		String ctiText = module.getName();
		ImageDescriptor imageDescriptor = module.getLogo();
		Image image;
		if (imageDescriptor == null) {
			// Default "jAlgo File" icon.
			image = ImageDescriptor.createFromURL(
				getClass().getResource("/main_pix/jalgo-file.png")).createImage();
		} else {
			// Custom icon as defined in the module info.
			image = imageDescriptor.createImage(appWin.getShell().getDisplay());
		}
		CTabItem cti = appWin.requestNewCTabItem(ctiText, image);
		
		// Create a new instance of a module.
		Class constrArgs[] = new Class[] { ApplicationWindow.class,
				Composite.class, SubMenuManager.class,
				SubToolBarManager.class,
				SubStatusLineManager.class };
		Object[] args = new Object[] {
				appWin,
				(Composite) cti.getControl(),
				new SubMenuManager(appWin.getMenuBarManager()),
				new SubToolBarManager(appWin
						.getToolBarManager()),
				new SubStatusLineManager(appWin
						.getTheStatusLineManager()) };
		try {
			Constructor constr = ((Class) knownModules
					.get(modNumber))
					.getConstructor(constrArgs);
			currentInstance = (IModuleConnector) constr
					.newInstance(args);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
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

	public LinkedList<Class<IModuleConnector>> getKnownModules() {
		return knownModules;
	}

	public LinkedList<IModuleInfo> getKnownModuleInfos() {
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
	    //FSt
	    if(this.currentInstance == null)
	        return false;
	    //~FSt
		if (currentInstance.getModuleInfo().getOpenFileName() == null) {
			SaveAsAction a = new SaveAsAction(appWin);
			a.run();
			
			return true;
		}
		return saveFileAs(currentInstance.getModuleInfo()
				.getOpenFileName());
	}

	/**
	 * Takes content from module and stores it in file with given filename
	 * 
	 * @param filename
	 */
	public boolean saveFileAs(String filename) {
		currentInstance.getModuleInfo().setOpenFileName(filename);
		return Storage.save(filename);
	}

	/**
	 * Opens file
	 * 
	 * @param filename
	 */
	public boolean openFile(String filename) {
		if( Storage.load(filename) == true)
		{
		    this.currentInstance.getModuleInfo().setOpenFileName(filename);
		    return true;
		}
		return false;
	}

	/**
	 * Opens file and gives content to Module
	 * 
	 * @param filename
	 */
	public boolean openFile(String filename, boolean useCurrentInstance) {
		if (useCurrentInstance) {
		   
			if( Storage.load(filename, currentInstance) == true)
			{
			    currentInstance.getModuleInfo().setOpenFileName(filename);
			    return true;
			}
			return false;
		}
		return openFile( filename);
	}

	/**
	 * Fills <code>knownModules</code> and <code>knownModuleInfos</code>
	 * with content. Module programmers have to alter this method and add
	 * their module here!
	 */
	private void addKnownModules() {
		String jarFileName, moduleName;
		String fileSep = System.getProperty("file.separator");
		for (File file : new File(System.getProperty("user.dir") + fileSep +
				"runtime" + fileSep + "modules").listFiles()) {
			jarFileName = file.getName();
			if (file.isFile() &&
				jarFileName.endsWith(".jar") &&
				!jarFileName.equals("jalgo.jar")) {
				moduleName = jarFileName.substring(0, jarFileName.length()-4);
				try {
					knownModules.add((Class<IModuleConnector>)Class.forName("org.jalgo.module."+
						moduleName+".ModuleConnector"));
					knownModuleInfos.add((IModuleInfo)Class.forName("org.jalgo.module."+
						moduleName+".ModuleInfo").newInstance());
				}
				catch (ClassNotFoundException e) {e.printStackTrace();}
				catch (InstantiationException e) {e.printStackTrace();}
				catch (IllegalAccessException e) {e.printStackTrace();}
			}
		}

//		knownModules.add(org.jalgo.module.avl.ModuleConnector.class);
//		knownModules.add(org.jalgo.module.dijkstraModule.ModuleConnector.class);
//		knownModules.add(org.jalgo.module.synDiaEBNF.ModuleConnector.class);
//		knownModules.add(org.jalgo.module.testModule.ModuleConnector.class);
		//Add a new ModuleConnector here!!

//		knownModuleInfos.add(new org.jalgo.module.avl.ModuleInfo());
//		knownModuleInfos.add(new org.jalgo.module.dijkstraModule.ModuleInfo());
//		knownModuleInfos.add(new org.jalgo.module.synDiaEBNF.ModuleInfo());
//		knownModuleInfos.add(new org.jalgo.module.testModule.ModuleInfo());
		//Add a new ModuleInfo here!! 
	}

	/**
	 * Retrieves the module instance belonging to the given tab item.
	 * 
	 * @param item the interesting tab item
	 * 
	 * @return the module instance belonging to the given tab item
	 */
	public IModuleConnector getModuleInstanceByTab(CTabItem item) {
		return openInstances.get(item);
	}
}