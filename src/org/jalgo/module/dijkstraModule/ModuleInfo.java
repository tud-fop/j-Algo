/*
 * Created on Aug 15, 2004
 * $Id: ModuleInfo.java,v 1.1 2005/06/23 10:08:27 jalgosequoia Exp $
 */
package org.jalgo.module.dijkstraModule;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.IModuleInfo;

/**
 * @author Julian Stecklina
 */
public class ModuleInfo implements IModuleInfo {

    protected String m_strOpenFileName;
	public ModuleInfo()
	{
		super();
	}
    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getName()
     */
    public String getName() {
        return "Dijkstra";
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getVersion()
     */
    public String getVersion() {
        return "0.1";
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getAuthor()
     */
    public String getAuthor() {
        return "swt05-p2";
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getDescription()
     */
    public String getDescription() {
        return "Dijkstra Algorithmus";
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getLogo()
     */
    public ImageDescriptor getLogo() {
        return ImageDescriptor.createFromFile(null, "pix/dijkstra_icon.gif");
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getLicense()
     */
    public String getLicense() {
        return "GPL";
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getOpenFileName()
     */
    public String getOpenFileName() {
        return m_strOpenFileName;
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#setOpenFileName(java.lang.String)
     */
    public void setOpenFileName(String string) {
        m_strOpenFileName = string;
    }

}
