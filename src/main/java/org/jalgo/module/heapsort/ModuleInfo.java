/*
 * j-Algo - a visualization tool for algorithm runs, especially useful for
 * students and lecturers of computer science. j-Algo is written in Java and
 * thus platform independent. Development is supported by Technische Universität
 * Dresden.
 *
 * Copyright (C) 2004-2008 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
package org.jalgo.module.heapsort;

import java.net.URL;

import org.jalgo.main.IModuleInfo;

/**
 * As demanded by j-Algo specification.
 * 
 * @author mbue
 *
 */
public final class ModuleInfo implements IModuleInfo {
	
	private static IModuleInfo instance = null;
	
	private ModuleInfo() {
		super();
	}
	
	public static synchronized IModuleInfo getInstance() {
		if (instance == null)
			instance = new ModuleInfo();
		return instance;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleInfo#getAuthor()
	 */
	public String getAuthor() {
		return Util.getString("Module_authors");
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleInfo#getDescription()
	 */
	public String getDescription() {
		return Util.getString("Module_description");
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleInfo#getHelpSetURL()
	 */
	public URL getHelpSetURL() {
		return Util.getResourceURL("HelpSet_Name");
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleInfo#getLicense()
	 */
	public String getLicense() {
		return Util.getString("Module_license");
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleInfo#getLogoURL()
	 */
	public URL getLogoURL() {
		return Util.getResourceURL("Module_logo");
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleInfo#getName()
	 */
	public String getName() {
		return Util.getString("Module_name");
	}

	/* (non-Javadoc)
	 * @see org.jalgo.main.IModuleInfo#getVersion()
	 */
	public String getVersion() {
		return Util.getString("Module_version");
	}

}
