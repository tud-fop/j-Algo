/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
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
 * Created on 25.06.2004
 */

package org.jalgo.module.testModule;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.action.SubToolBarManager;
import org.jalgo.main.IModuleConnector;
import org.jalgo.main.IModuleInfo;

/**
 * @author Christopher Friedrich
 * @author Stephan Creutz
 * 
 * This is an example module. I use this code for documentation. (Stephan)
 */
public class ModuleConnector implements IModuleConnector {

	public ModuleConnector() {

	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleConnector#getDataForFile()
	 */
	public ByteArrayOutputStream getDataForFile() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleConnector#getMenuManager()
	 */
	public SubMenuManager getMenuManager() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleConnector#getModuleInfo()
	 */
	public IModuleInfo getModuleInfo() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleConnector#getStatusLineManager()
	 */
	public SubStatusLineManager getStatusLineManager() {
		return null;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleConnector#getToolBarManager()
	 */
	public SubToolBarManager getToolBarManager() {
		return null;
	}

	public void print() {

	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleConnector#setDataFromFile(java.io.ByteArrayInputStream)
	 */
	public void setDataFromFile(ByteArrayInputStream data) {

	}

	public void run() {
	}

}