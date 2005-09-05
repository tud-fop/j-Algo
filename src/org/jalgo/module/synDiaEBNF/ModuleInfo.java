/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and platform
 * independant. j-Algo is developed with the help of Dresden University of
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

/*
 * Created on 13.04.2004
 */
package org.jalgo.module.synDiaEBNF;

import java.net.URL;

import org.jalgo.main.IModuleInfo;
import org.jalgo.main.util.Messages;

/**
 * Gives information about the module.
 * 
 * @author Michael Pradel
 * @author Benjamin Scholz
 * @author Marco Zimmerling
 * @author Babett Schaliz
 * @author Stephan Creutz
 */
public class ModuleInfo
implements IModuleInfo {

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
		if (instance == null) instance = new ModuleInfo();
		return instance;
	}

	/**
	 * @see IModuleInfo#getName()
	 */
	public String getName() {
		return Messages.getString("synDiaEBNF",
			"ModuleInfo.EBNF_und_Syntaxdiagramme_1"); //$NON-NLS-1$
	}

	/**
	 * @see IModuleInfo#getVersion()
	 */
	public String getVersion() {
		return "0.42"; //$NON-NLS-1$
	}

	/**
	 * @see IModuleInfo#getAuthor()
	 */
	public String getAuthor() {
		return Messages.getString("synDiaEBNF", "ModuleInfo.Authors_3"); //$NON-NLS-1$
	}

	/**
	 * @see IModuleInfo#getDescription()
	 */
	public String getDescription() {
		return Messages.getString("synDiaEBNF", "ModuleInfo.Description_1_4") //$NON-NLS-1$
			+ Messages.getString("synDiaEBNF", "ModuleInfo.Description_2_5") //$NON-NLS-1$
			+ Messages.getString("synDiaEBNF", "ModuleInfo.Description_3_6") //$NON-NLS-1$
			+ Messages.getString("synDiaEBNF", "ModuleInfo.Description_4_7") //$NON-NLS-1$
			+ Messages.getString("synDiaEBNF", "ModuleInfo.Description_5_8") //$NON-NLS-1$
			+ Messages.getString("synDiaEBNF", "ModuleInfo.Description_6_9"); //$NON-NLS-1$
	}

	/**
	 * @see IModuleInfo#getLogoURL()
	 */
	public URL getLogoURL() {
		// TODO: design logo and provide it here as an java.net.URL
		return getClass().getResource("/ebnf_pix/logo.gif");
	}

	/**
	 * @see IModuleInfo#getLicense()
	 */
	public String getLicense() {
		return Messages.getString("synDiaEBNF",
			"ModuleInfo.GNU_General_Public_License_10"); //$NON-NLS-1$
	}
}
