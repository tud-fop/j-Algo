/*
 * Created on Aug 15, 2004
 */
package org.jalgo.module.testModule;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.IModuleInfo;

/**
 * @author Michael Pradel
 */
public class ModuleInfo implements IModuleInfo {

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getName()
     */
    public String getName() {
        return "testModule";
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
        return "Your Name";
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getDescription()
     */
    public String getDescription() {
        return "a module for testing purposes";
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getLogo()
     */
    public ImageDescriptor getLogo() {
        return ImageDescriptor.createFromFile(null, "pix/testModuleLogo.gif");
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
        return null;
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#setOpenFileName(java.lang.String)
     */
    public void setOpenFileName(String string) {

    }

}
