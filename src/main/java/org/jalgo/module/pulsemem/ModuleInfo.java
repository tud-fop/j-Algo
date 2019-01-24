/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jalgo.module.pulsemem;

import java.net.URL;

import org.jalgo.main.IModuleInfo;
import org.jalgo.main.util.Messages;

public class ModuleInfo implements IModuleInfo {

    /** The singleton instance */
    private static IModuleInfo instance;

    /**
     * The only constructor is unusable from outside this class. This is part of
     * the singleton design pattern.
     */
    private ModuleInfo() {
        // unusable from outside
    }

    /**
     * Retrieves the singleton instance of <code>IModuleInfo</code>.
     *
     * @return the singleton instance
     */
    public static IModuleInfo getInstance() {
        if (instance == null)
            instance = new ModuleInfo();
        return instance;
    }

    public String getName() {
        return Messages.getString("pulsemem", "ModuleInfo.ModuleName"); //$NON-NLS-1$
    }

    public String getVersion() {
        return Messages.getString("pulsemem", "ModuleInfo.ModuleVersion"); //$NON-NLS-1$
    }

    public String getAuthor() {
        return Messages.getString("pulsemem", "ModuleInfo.Programmers"); //$NON-NLS-1$
    }

    public String getDescription() {
        return Admin.getLanguageString("Module.Description"); //$NON-NLS-1$
    }

    public URL getLogoURL() {
        return Messages.getResourceURL("pulsemem", "Main_Icon"); //$NON-NLS-1$ //$NON-NLS-2$
    }

    public String getLicense() {
        return Messages.getString("pulsemem", "ModuleInfo.LicenceType"); //$NON-NLS-1$
    }

    public URL getHelpSetURL() {
        return Admin.getResourceURL("HelpSet_Name"); //$NON-NLS-1$
    }
}