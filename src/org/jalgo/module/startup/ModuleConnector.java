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
 * Created on Aug 15, 2004
 * $Id: ModuleConnector.java,v 1.3 2005/07/13 23:00:34 styjdt Exp $
 */
package org.jalgo.module.startup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubStatusLineManager;
import org.eclipse.jface.action.SubToolBarManager;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.main.IModuleConnector;
import org.jalgo.main.IModuleInfo;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.module.startup.gui.StartupComposite;

/**
 * 
 *  @author Frank Staudinger
 */
public class ModuleConnector implements IModuleConnector {

	private ModuleInfo moduleInfo;

	private ApplicationWindow appWin;

	private Composite comp;

	private SubMenuManager menuManager;

	private SubToolBarManager toolBarManager;

	private SubStatusLineManager statusLineManager;

	private StartupComposite m_cmpStartupComposite;

	/**
	 * @see IModuleConnector
	 */
	public ModuleConnector(ApplicationWindow appWin, Composite comp,
			SubMenuManager menu, SubToolBarManager tb, SubStatusLineManager sl) {

		moduleInfo = new ModuleInfo();
		
		this.appWin = appWin;
		this.comp = comp;
		this.menuManager = menu;
		this.toolBarManager = tb;
		this.statusLineManager = sl;
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.IModuleConnector#run()
	 */
	public void run() {
	    JalgoWindow mainWnd = ((JalgoWindow)appWin);
		CTabItem pTabItem = mainWnd.getCTabFolder().getItem(mainWnd.getCTabFolder().getItemCount() - 1);
		m_cmpStartupComposite = new StartupComposite(comp,SWT.NONE);
		
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.IModuleConnector#setDataFromFile(java.io.ByteArrayInputStream)
	 */
	public void setDataFromFile(ByteArrayInputStream data) {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.IModuleConnector#getDataForFile()
	 */
	public ByteArrayOutputStream getDataForFile() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jalgo.main.IModuleConnector#print()
	 */
	public void print() {

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
	 * @see org.jalgo.main.IModuleConnector#getStatusLineManager()
	 */
	public SubStatusLineManager getStatusLineManager() {
		return statusLineManager;
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

}