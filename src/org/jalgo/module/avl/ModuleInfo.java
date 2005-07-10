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
		return ImageDescriptor.createFromURL(
			getClass().getResource("/avl_pix/logo.gif"));
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