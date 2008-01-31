/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and
 * platform independent. j-Algo is developed with the help of Dresden
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
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;
import java.util.List;
import java.util.MissingResourceException;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.gui.JAlgoWindow;
import org.jalgo.main.util.ErrorLog;
import org.jalgo.main.util.Messages;
import org.jalgo.main.util.Settings;
import org.jalgo.main.util.Storage;

/**
 * The class <code>JAlgoMain</code> is the entry point for the application. This
 * class implements the Singleton design pattern. It handles the management of
 * open module instances and provides useful methods for file handling.
 * 
 * @author Alexander Claus, Christopher Friedrich, Michael Pradel
 */
public class JAlgoMain {

	/** The singleton instance of <code>JAlgoMain</code> */
	private static JAlgoMain instance;
	/** The singleton instance of <code>JAlgoWindow</code> */ 
	private JAlgoWindow appWin;

	private List<Class<AbstractModuleConnector>> knownModules;
	private List<IModuleInfo> knownModuleInfos;
	private AbstractModuleConnector currentInstance;

	/**
	 * Constructs an object of <code>JalgoMain</code>. This constructor is
	 * declared as private to avoid access from outside this class. This
	 * mechanism is part of the Singleton design pattern.
	 */
	private JAlgoMain() {
		addKnownModules();
	}

	/**
	 * Retrieves the singleton instance of this class. This method is part of
	 * the Singleton design pattern.
	 * 
	 * @return the singleton instance of <code>JalgoMain</code>
	 */
	public static JAlgoMain getInstance() {
		// here no test to null is necessary, because instance was created in main()
		return instance;
	}

	/**
	 * Initializes the GUI of j-Algo. Opens the main frame.
	 */
	public void createGUI() {
		// init main frame
		appWin = new JAlgoWindow();
		// init gui connector
		JAlgoGUIConnector.initInstance(appWin);
	}

	/**
	 * Creates a new instance of the module with the given name.
	 * Returns <code>null</code>, if the given name does not match to any known
	 * module name.
	 * 
	 * @param moduleName the name of the module to be created
	 * 
	 * @return the <code>AbstractModuleConnector</code> instance of the module,
	 * 			if it is created, <code>null</code> otherwise
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
	 * @param modNumber number of new module in <code>knownModules</code>
	 *                  starting with 0.
	 * @return the new instance of <code>AbstractModuleConnector</code>
	 */
	public AbstractModuleConnector newInstance(int modNumber) {
		// hides current modules toolbar and menubar
		if (currentInstance != null)
			appWin.setCurrentInstanceVisible(false);
		currentInstance = null;

		try {
			currentInstance = knownModules.get(modNumber).newInstance();
		}
		catch (Exception ex) {ex.printStackTrace();}

		appWin.createNewModuleGUIComponents();
		currentInstance.init();
		appWin.activateNewInstance();

		currentInstance.run();
		appWin.updateTitle();

		return currentInstance;
	}

	/**
	 * Retrieves a list of the <code>IModuleInfo</code>s of the registered
	 * modules. This list ist used for module choosing mechanisms and for
	 * opening j-Algo files.
	 *  
	 * @return a list of all registered <code>IModuleInfo</code>s
	 */
	public List<IModuleInfo> getKnownModuleInfos() {
		return knownModuleInfos;
	}

	/**
	 * Retrieves the <code>AbstractModuleConnector</code> of the currently
	 * active module instance.
	 *  
	 * @return the currently active module instance
	 */
	public AbstractModuleConnector getCurrentInstance() {
		return currentInstance;
	}

	/**
	 * Takes content from module and stores it in currently used file. If no
	 * file is currently used, a filechooser dialog is opened to select a file
	 * to be saved.
	 */
	public boolean saveFile() {
	    if (currentInstance == null) return false;
		if (currentInstance.getOpenFileName() == null ||
			currentInstance.getOpenFileName().length() == 0) {
			//if no name was given to current module content, open a filechooser
			return (appWin.showSaveDialog(true) != null);
		}
		return saveFileAs(currentInstance.getOpenFileName());
	}

	/**
	 * Takes content from module and stores it in a file with the given filename.
	 * 
	 * @param filename the path to the file to be saved
	 */
	public boolean saveFileAs(String filename) {
		currentInstance.setOpenFileName(filename);
		currentInstance.setSaveStatus(SaveStatus.NO_CHANGES);
		return Storage.save(filename);
	}

	/**
	 * Opens file and gives content to module instance.
	 * 
	 * @param filename the path to the file to be opened
	 * @param useCurrentInstance <code>true</code>, if file content should be
	 * 			delegated to the currently active module instance,
	 * 			<code>false</code>, if a new module instance should be opened
	 */
	public boolean openFile(String filename, boolean useCurrentInstance) {
		if ((useCurrentInstance && Storage.load(filename, currentInstance)) ||
			!useCurrentInstance && Storage.load(filename, null)) {
			currentInstance.setOpenFileName(filename);
			currentInstance.setSaveStatus(SaveStatus.NO_CHANGES);
			return true;
		}
		return false;
	}

	/**
	 * Searches the folder <i>runtime/modules</i> for jar files with modules.
	 * Fills <code>knownModules</code> and <code>knownModuleInfos</code>
	 * with the found modules and registers resource and settings bundles, if
	 * they are supported by the modules.<br>
	 * For detailled description of writing and registering a module, read the
	 * developers manual available as pdf.
	 */
	@SuppressWarnings("unchecked")
	private void addKnownModules() {
		knownModules = new LinkedList<Class<AbstractModuleConnector>>();
		knownModuleInfos = new LinkedList<IModuleInfo>();

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
						"org.jalgo.module."+moduleName);
					Settings.registerSettingsBundle(moduleName,
						"/"+moduleName+".prefs");
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
	 * Checks if a given class implements a specific interface.
	 * 
	 * @param classObj <code>Class</code> object which should checked
	 * @param interfaceName the name of the interface the class should implement 
	 *
	 * @return <code>true</code> if the interface is implemented,
	 * 			<code>false</code> otherwise
	 */
	private boolean implementsInterface(Class classObj, String interfaceName) {
		Class [] interfaces = classObj.getInterfaces();
		for (int i = 0; i < interfaces.length; i++) {
			if (interfaces[i].getName().equals(interfaceName)) return true;
		}
		return false;
	}

	/**
	 * The entry point of the j-Algo main program.<br>
	 * When releasing the product, start the program with the flag "errorLogOn".
	 * So an error log file could be created for easy debugging.
	 * 
	 * @param args the program arguments
	 * 
	 * @see ErrorLog
	 */
	public static void main(String[] args) {
		//saves exceptions to file
/*		ErrorLog errorLog = null;
		if (args.length > 0 && args[0].equalsIgnoreCase("errorlogon"))
			errorLog = new ErrorLog();*/

		JAlgoWindow.createSplashScreen();	
		instance = new JAlgoMain();
		instance.createGUI();

//		if (errorLog != null) errorLog.close();
	}

	/**
	 * Sets the currently active module instance to the given instance of
	 * <code>AbstractModuleConnector</code>. This is only for intern management
	 * and does not influence the GUI!<br>
	 * Selecting another module is handled by <code>JalgoWindow</code>.
	 * 
	 * @param instance the new module instance
	 */
	public void setCurrentInstance(AbstractModuleConnector instance) {
		currentInstance = instance;
	}
}