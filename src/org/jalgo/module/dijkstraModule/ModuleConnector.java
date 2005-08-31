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
 * Created on Aug 15, 2004 $Id: ModuleConnector.java,v 1.3 2005/07/13 23:01:10
 * styjdt Exp $
 */
package org.jalgo.module.dijkstraModule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.main.IModuleConnector;
import org.jalgo.main.IModuleInfo;
import org.jalgo.main.JAlgoGUIConnector;
import org.jalgo.module.dijkstraModule.gui.Controller;

/**
 * @see IModuleConnector
 * 
 * @author Julian Stecklina
 */
public class ModuleConnector
implements IModuleConnector {

	private ModuleInfo moduleInfo;
	private ApplicationWindow appWin;
	private Composite comp;
	private SubMenuManager menuManager;
	private SubToolBarManager toolBarManager;
	private Controller controller;
	private int saveStatus;

	/**
	 * @see IModuleConnector
	 */
	public ModuleConnector(ApplicationWindow appWin, Composite comp,
		SubMenuManager menu, SubToolBarManager tb) {

		moduleInfo = new ModuleInfo();

		this.appWin = appWin;
		this.comp = comp;
		this.menuManager = menu;
		this.toolBarManager = tb;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.IModuleConnector#run()
	 */
	public void run() {
		controller = new Controller(this, comp, toolBarManager, appWin);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.IModuleConnector#setDataFromFile(java.io.ByteArrayInputStream)
	 */
	public void setDataFromFile(ByteArrayInputStream data) {
		controller.setSerializedData(data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.IModuleConnector#getDataForFile()
	 */
	public ByteArrayOutputStream getDataForFile() {
		return controller.getSerializedData();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.IModuleConnector#print()
	 */
	public void print() {
	// printing is currently not supported
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.IModuleConnector#getMenuManager()
	 */
	public SubMenuManager getMenuManager() {
		return menuManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.IModuleConnector#getToolBarManager()
	 */
	public SubToolBarManager getToolBarManager() {
		return toolBarManager;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.IModuleConnector#getModuleInfo()
	 */
	public IModuleInfo getModuleInfo() {
		return moduleInfo;
	}

	public boolean close() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.IModuleConnector#getSaveStatus()
	 */
	public int getSaveStatus() {
		return saveStatus;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.IModuleConnector#setSaveStatus(int)
	 */
	public void setSaveStatus(int status) {
		this.saveStatus = status;
		JAlgoGUIConnector.getInstance().saveStatusChanged(this);
	}
}