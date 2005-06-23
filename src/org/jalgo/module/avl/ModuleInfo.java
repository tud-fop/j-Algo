/* Created on 12.04.2005 */
package org.jalgo.module.avl;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.IModuleInfo;

/**
 * This class represents some information about the AVL module.
 * 
 * @author Alexander Claus
 */
public class ModuleInfo
implements IModuleInfo {

	private String openFileName;
	private static final String lineSep = System.getProperty("line.separator");
	
	/* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getName()
     */
    public String getName() {
        return "AVL - Bäume";
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getVersion()
     */
    public String getVersion() {
        return "1.0";
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getAuthor()
     */
    public String getAuthor() {
        return "Alexander Claus, Ulrike Fischer, Jean Christoph Jung, Sebastian Pape, Matthias Schmidt";
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getDescription()
     */
	public String getDescription() {
		return "Dieses Modul behandelt binäre Suchbäume mit und ohne die "+
			"AVL-Eigenschaft. "+lineSep+
			"Es werden die Algorithmen Suchen, Einfügen und Löschen visualisiert. "+
			"Dabei können die Algorithmen interaktiv gesteuert werden";
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getLogo()
     */
    public ImageDescriptor getLogo() {
		return ImageDescriptor.createFromFile(null, "pix/avl/logo.gif");
//TODO: enable this, when switching to plugin structure
//		return ImageDescriptor.createFromURL(
//			getClass().getResource("/pix/avl/logo.gif"));
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getLicense()
     */
    public String getLicense() {
        return "GNU General Public License";
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getOpenFileName()
     */
	public String getOpenFileName() {
        return openFileName;
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#setOpenFileName(java.lang.String)
     */
    public void setOpenFileName(String fileName) {
		openFileName = fileName;
    }
}