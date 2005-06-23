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
		ApplicationWindow appWin,
		Composite comp,
		SubMenuManager menu,
		SubToolBarManager tb,
		SubStatusLineManager sl) {
		moduleInfo = new ModuleInfo();
		controller = new ModuleController((ModuleInfo)moduleInfo, appWin, comp, menu, tb, sl);
	}

	/**
	 * @see IModuleConnector#run()
	 */
	public void run() {
		controller.run();
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

	public boolean close() {
		return true;
	}
}