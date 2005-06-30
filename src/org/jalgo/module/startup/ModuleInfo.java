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
 * $Id: ModuleInfo.java,v 1.2 2005/06/27 21:03:26 mischi Exp $
 */
package org.jalgo.module.startup;

import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.IModuleInfo;

/**
 * @author Julian Stecklina
 */
public class ModuleInfo implements IModuleInfo {

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getName()
     */
    public String getName() {
        return "Start";
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
        return "";
    }

    /* (non-Javadoc)
     * @see org.jalgo.main.IModuleInfo#getLogo()
     */
    public ImageDescriptor getLogo() {
        return null;
        //return ImageDescriptor.createFromFile(null, "pix/testModuleLogo.gif");
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