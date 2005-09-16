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
import java.util.MissingResourceException;

import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.main.gui.actions.SaveAsAction;
import org.jalgo.main.util.Messages;
import org.jalgo.main.util.Storage;

/**
 * @author Alexander Claus, Christopher Friedrich, Michael Pradel
 */
public class JalgoMain {

	private JalgoWindow appWin;

	private LinkedList<Class<AbstractModuleConnector>> knownModules;
	private LinkedList<IModuleInfo> knownModuleInfos;
	private HashMap<CTabItem, AbstractModuleConnector> openInstances;
	
	private AbstractModuleConnector currentInstance;

	public JalgoMain() {
		knownModules = new LinkedList<Class<AbstractModuleConnector>>();
		knownModuleInfos = new LinkedList<IModuleInfo>();
		addKnownModules();
		openInstances = new HashMap<CTabItem, AbstractModuleConnector>();
	}

	public void createGUI() {
		appWin = new JalgoWindow(this);
		JAlgoGUIConnector.initInstance(appWin);
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
		/* Remove CTab */
		openInstances.remove(cti);
		cti.dispose();
		//closed tab was the last one
		if (openInstances.isEmpty()) {
			currentInstance = null;
			appWin.updateSaveButtonEnableStatus(
				AbstractModuleConnector.NOTHING_TO_SAVE);
			appWin.updateTitle(null);
			appWin.setAboutModuleActionEnabled(false);
		}
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
		appWin.updateSaveButtonEnableStatus(currentInstance.getSaveStatus());
		appWin.updateTitle(currentInstance);
	}

	/**
	 * Creates a new instance of the module with the given name.
	 * Returns <code>null</code>, if the given name does not match to any known
	 * module name.
	 * 
	 * @param moduleName the name of the module to be created
	 * 
	 * @return the <code>AbstractModuleConnector</code> instance of the module, if it is
	 * 			created, <code>null</code> otherwise
	 */
	public AbstractModuleConnector newInstanceByName(String moduleName) {
		for (int i=0; i<knownModuleInfos.size(); i++) {
			if ((knownModuleInfos.get(i)).getName().equals(moduleName))
				return newInstance(i);
		}
		return null;
	}

	/**
	 * Creates a new instance of the specified module.
	 * 
	 * @param modNumber Number of new module in <code>knownModules</code>
	 *                  starting with 0.
	 * @return the new instance of <code>AbstractModuleConnector</code>
	 */
	public AbstractModuleConnector newInstance(int modNumber) {
		// Makes current Module-Tool/MenuBar invisible
		if (currentInstance != null) {
			if (currentInstance.getMenuManager() != null)
				currentInstance.getMenuManager().setVisible(
						false);
			if (currentInstance.getToolBarManager() != null)
				currentInstance.getToolBarManager().setVisible(
						false);
		}

		// Disable save buttons
		appWin.updateSaveButtonEnableStatus(AbstractModuleConnector.NOTHING_TO_SAVE);

		// Requests a fresh CTabItem from the appWin
		IModuleInfo module = knownModuleInfos.get(modNumber);
		String ctiText = module.getName();
		ImageDescriptor imageDescriptor = ImageDescriptor.createFromURL(
			module.getLogoURL());
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
		Class constrArgs[] = new Class[] {
				Composite.class, SubMenuManager.class,
				SubToolBarManager.class };
		Object[] args = new Object[] {
				(Composite) cti.getControl(),
				new SubMenuManager(appWin.getMenuBarManager()),
				new SubToolBarManager(appWin.getToolBarManager()) };
		try {
			Constructor constr =
				((Class)knownModules.get(modNumber)).getConstructor(constrArgs);
			currentInstance = (AbstractModuleConnector) constr
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

		// Enable 'About module'
		appWin.setAboutModuleActionEnabled(true);

		currentInstance.run();
		appWin.updateTitle(currentInstance);

		return currentInstance;
	}

	public LinkedList<Class<AbstractModuleConnector>> getKnownModules() {
		return knownModules;
	}

	public LinkedList<IModuleInfo> getKnownModuleInfos() {
		return knownModuleInfos;
	}

	public AbstractModuleConnector getCurrentInstance() {
		return currentInstance;
	}

	/**
	 * Takes content from module and stores it in currently used file
	 */
	public boolean saveFile() {
	    if (currentInstance == null) return false;
		if (currentInstance.getOpenFileName() == null ||
			currentInstance.getOpenFileName().length() == 0) {
			SaveAsAction a = new SaveAsAction(appWin);
			a.run();
			return a.wasSuccessful();
		}
		return saveFileAs(currentInstance.getOpenFileName());
	}

	/**
	 * Takes content from module and stores it in file with given filename
	 * 
	 * @param filename
	 */
	public boolean saveFileAs(String filename) {
		currentInstance.setOpenFileName(filename);
		currentInstance.setSaveStatus(AbstractModuleConnector.NO_CHANGES);
		return Storage.save(filename);
	}

	/**
	 * Opens file and gives content to Module
	 * 
	 * @param filename
	 */
	public boolean openFile(String filename, boolean useCurrentInstance) {
		if ((useCurrentInstance && Storage.load(filename, currentInstance)) ||
			!useCurrentInstance && Storage.load(filename, null)) {
			currentInstance.setOpenFileName(filename);
			currentInstance.setSaveStatus(AbstractModuleConnector.NO_CHANGES);
			return true;
		}
		return false;
	}

	/**
	 * Fills <code>knownModules</code> and <code>knownModuleInfos</code>
	 * with content. Module programmers have to alter this method and add
	 * their module here!
	 */
	@SuppressWarnings("unchecked")
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
					Class moduleConnector = Class.forName("org.jalgo.module." +
							moduleName + ".ModuleConnector");
					Class moduleInfo = Class.forName("org.jalgo.module." +
							moduleName + ".ModuleInfo");
					
					if (moduleConnector.getSuperclass().equals(
						AbstractModuleConnector.class) &&
						implementsInterface(moduleInfo,
							"org.jalgo.main.IModuleInfo")) {
						knownModules.add(moduleConnector);
						knownModuleInfos.add((IModuleInfo)
							moduleInfo.getMethod("getInstance", new Class[] {}).
							invoke(null, new Object[] {}));
					}

					Messages.registerResourceBundle(moduleName,
						"org.jalgo.module."+moduleName+".de");
				}
				catch (ClassNotFoundException ex) {ex.printStackTrace();}
				catch (IllegalAccessException ex) {ex.printStackTrace();}
				catch (MissingResourceException ex) {
					//do nothing, that means only, that the current module has
					//no strings externalized
				}
				catch (IllegalArgumentException ex) {ex.printStackTrace();}
				catch (SecurityException ex) {ex.printStackTrace();}
				catch (InvocationTargetException ex) {ex.printStackTrace();}
				catch (NoSuchMethodException ex) {ex.printStackTrace();}
			}
		}
	}
	
	/**
	 * checks if a given class implements a specific interface
	 * @param classObj class object which should checked
	 * @param interfaceName interface name the class should be implemented 
	 * @return returns true if the "classObj" implements "interfaceName" otherwise false
	 */
	private boolean implementsInterface(Class classObj, String interfaceName) {
		Class [] interfaces = classObj.getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			if (interfaces[i].getName().equals(interfaceName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Retrieves the module instance belonging to the given tab item.
	 * 
	 * @param item the interesting tab item
	 * 
	 * @return the module instance belonging to the given tab item
	 */
	public AbstractModuleConnector getModuleInstanceByTab(CTabItem item) {
		return openInstances.get(item);
	}
}