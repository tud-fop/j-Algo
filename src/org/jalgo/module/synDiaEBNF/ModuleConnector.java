/*
 * Created on 13.04.2004
 */
 
package org.jalgo.module.synDiaEBNF;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.main.IModuleConnector;
import org.jalgo.main.IModuleInfo;

/**
 * @author Michael Pradel
 * @author Benjamin Scholz
 * @author Marco Zimmerling
  */
public class ModuleConnector implements IModuleConnector {

	private IModuleInfo moduleInfo;
	private ModuleController controller;

	/**
	 * @see IModuleConnector
	 */
	public ModuleConnector(
		boolean startWizard,
		ApplicationWindow appWin,
		Composite comp,
		SubMenuManager menu,
		SubToolBarManager tb,
		SubStatusLineManager sl) {
		moduleInfo = new ModuleInfo();
		controller = new ModuleController(startWizard, (ModuleInfo)moduleInfo, appWin, comp, menu, tb, sl);
	}

	/**
	 * @see IModuleConnector#setDataFromFile(ByteArrayInputStream)
	 */
	public void setDataFromFile(ByteArrayInputStream data) {
		controller.setSerializedData(data);
	}

	/**
	 * @see IModuleConnector#getDataForFile()
	 */
	public ByteArrayOutputStream getDataForFile() {
		return controller.getSerializedData();
	}

	/**
	 * This method is not used in this module.
	 */
	public void print() {
	}

	/**
	 * Returns the <code>ModuleController</code>, which is controlling this module.
	 * @return the <code>ModuleController</code>
	 * @see ModuleController
	 */
	public ModuleController getController() {
		return controller;
	}

	public SubMenuManager getMenuManager() {
		return controller.getMenuManager();
	}

	/**
	 * @see IModuleConnector#getToolBarManager()
	 */	
	public SubToolBarManager getToolBarManager() {
		return controller.getToolBarManager();
	}

	/**
	 * @see IModuleConnector#getStatusLineManager()
	 */
	public SubStatusLineManager getStatusLineManager() {
		return controller.getStatusLineManager();
	}

	/**
	 * @see IModuleConnector#getModuleInfo()
	 */
	public IModuleInfo getModuleInfo() {
		return moduleInfo; 
	}

}
