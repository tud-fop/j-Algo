/*
 * Created on Aug 15, 2004
 */
package org.jalgo.module.testModule;

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
 */
public class ModuleConnector implements IModuleConnector {

    
    private ModuleInfo moduleInfo;

    private ApplicationWindow appWin;
    private Composite comp;
    private SubMenuManager menuManager;
    private SubToolBarManager toolBarManager;
    private SubStatusLineManager statusLineManager;
    
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
	    
	    this.appWin = appWin;
	    this.comp = comp;
	    this.menuManager = menu;
	    this.toolBarManager = tb;
	    this.statusLineManager = sl;
	}
    
    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleConnector#run()
     */
    public void run() {
        System.err.println("testModule is running");
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleConnector#setDataFromFile(java.io.ByteArrayInputStream)
     */
    public void setDataFromFile(ByteArrayInputStream data) {
        
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleConnector#getDataForFile()
     */
    public ByteArrayOutputStream getDataForFile() {
        return null;
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleConnector#print()
     */
    public void print() {

    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleConnector#getMenuManager()
     */
    public SubMenuManager getMenuManager() {
        return menuManager;
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleConnector#getToolBarManager()
     */
    public SubToolBarManager getToolBarManager() {
        return toolBarManager;
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleConnector#getStatusLineManager()
     */
    public SubStatusLineManager getStatusLineManager() {
        return statusLineManager;
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleConnector#getModuleInfo()
     */
    public IModuleInfo getModuleInfo() {
        return moduleInfo;
    }

}
