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

}