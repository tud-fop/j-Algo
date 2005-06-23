/*
 * Created on Aug 15, 2004
 * $Id: ModuleConnector.java,v 1.1 2005/06/23 10:08:27 jalgosequoia Exp $
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
		// TODO Auto-generated method stub
		return true;
	}

}