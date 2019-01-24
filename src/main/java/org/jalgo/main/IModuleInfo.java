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

/*
 * Created on 24.06.2004
 */

package org.jalgo.main;

import java.net.URL;

/**
 * The interface <code>IModuleInfo</code> contains methods to get some
 * information about a module, which is shown on a module chooser dialog or in
 * an about frame.<br>
 * The interface <code>IModuleInfo</code> has to be implemented by each module.
 * As convention the implementing classes have to be named 
 * <code>org.jalgo.module.&lt;moduleName&gt;.ModuleInfo</code>. The main program
 * will search for that class when loading the module.
 * For better performance and for the reason, that the interesting information
 * are unique for one module, the implementing classes of
 * <code>IModuleInfo</code> have to be modelled as singleton. The main program
 * will call the the method <code>getInstance()</code>, which has to retrieve
 * the singleton instance of <code>IModuleInfo</code>
 *   
 * @author Alexander Claus, Stephan Creutz
 */
public interface IModuleInfo {

	/**
	 * Retrieves the name of the module, which is shown on a module chooser
	 * dialog, on the tab of the module and as the menu-name of the module menu.
	 * 
	 * @return the name of the module
	 */
	public String getName();

	/**
	 * Retrieves the current build version number of the module as string.
	 * 
	 * @return the version number of the module
	 */ 
	public String getVersion();

	/**
	 * Retrieves a comma-separated list of the authors of the module.
	 * 
	 * @return tha author(s) of the module
	 */
	public String getAuthor();

	/**
	 * Retrieves a (more or less) short description about the module. It is
	 * shown on the module chooser inter alia. So it should give a short, but
	 * detailled information about the module.
	 * 
	 * @return a string representing a description of the module
	 */
	public String getDescription();

	/**
	 * Retrieves an <code>URL</code> object pointing to the logo image of the
	 * module. The image has to be sized to 16 x 16 pixels. It is shown on the
	 * tab of the module and in the new-Menu. Also this logo image is shown on
	 * the module chooser dialog, but later versions may provide two icons, one
	 * small icon for menu and tab and a big icon for the module chooser dialog. 
	 * 
	 * @return an <code>URL</code> to the logo of the module
	 */
	public URL getLogoURL();

	/**
	 * Retrieves a string describing the license, the module is distributed with.
	 * 
	 * @return the license
	 */
	public String getLicense();
	
	/**
	 * Retrieves an <code>URL</code> object pointing to the HelpSet file of the module.  
	 * The HelpSet file is the major metafile of the JavaHelp system.
	 * The URL must point to a *.hs-file.
	 * 
	 * @return the HelpSet filename
	 */
	public URL getHelpSetURL();
}