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
 * Created on May 18, 2004
 */

package org.jalgo.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.action.SubToolBarManager;

/**
 * @author Michael Pradel
 * @author Christopher Friedrich
 * @author Stephan Creutz
 * @author Alexander Claus
 */
public interface IModuleConnector {

	/**
	 * After the module has been initialized using the constructor (and maybe
	 * some methods), use this method to start it. This method should return
	 * soon.
	 */
	void run();

	/**
	 * use this function after the user loaded a saved file for the module.
	 * 
	 * @param data the loaded file consists of the module header, which was
	 *            added by the main program before saving (e.g. including with
	 *            which module the file is associated) and the data for the
	 *            module; put the data in here
	 */
	void setDataFromFile(ByteArrayInputStream data);

	/**
	 * use this function, when the user wants to save the state of the module.
	 * 
	 * @return a stream with the data from the module, that has to be stored in
	 *         a file after the main program added the module header (e.g.
	 *         including with which module the file is associated) to it
	 */
	ByteArrayOutputStream getDataForFile();

	/**
	 * use this function, if the user clicked the print-button (or chose to
	 * print in any other way) the module will call a print dialog and manage
	 * the printing
	 */
	void print();

	/**
	 * Get the Menu from the module
	 */
	SubMenuManager getMenuManager();

	/**
	 * Get the ToolBar from the module
	 */
	SubToolBarManager getToolBarManager();

	/**
	 * Get a class with all module information (name, description, version, ...)
	 */
	IModuleInfo getModuleInfo();

	/**
	 * This method is invoked, when module or program are intended to be closed.
	 * Here the user can be asked for saving his work. If this method returns
	 * <code>false</code>, the closing of module / program is ignored.
	 * 
	 * @return <code>true</code>, if module is ready to be closed,
	 *         <code>false</code> otherwise
	 */
	boolean close();

	/** Indicates, that there is no file open or nothing to save */
	public static final int NOTHING_TO_SAVE = 0;
	/** Indicates, that the current file was saved recently */
	public static final int NO_CHANGES = 1;
	/** Indicates, that the current file is 'dirty' and has to be saved */
	public static final int CHANGES_TO_SAVE = 2;
	/**
	 * Indicates, that the current module has saving mechanism blocked, e.g. an
	 * algorithm is running, and the user musn't save during this.
	 */
	public static final int SAVING_BLOCKED = 4;

	/**
	 * This method retrieves the save status of the module. All modules have to
	 * implement this method for correctly working of the enabled button status
	 * of the save buttons and several features.
	 * 
	 * @return one of the following constants:
	 *         <ul>
	 *         <li><code>NOTHING_TO_SAVE</code></li> - if there is no file
	 *         open
	 *         <li><code>NO_CHANGES</code></li> - if the open file was saved
	 *         recently
	 *         <li><code>CHANGES_TO_SAVE</code></li> - if there are changes
	 *         to save
	 *         <li><code>SAVING_BLOCKED</code></li> - if save buttons should be
	 *         blocked
	 *         </ul>
	 */
	int getSaveStatus();

	/**
	 * This method is called from main program during a save action. Furthermore
	 * this method can be used by the module to set the save status centrally.
	 * 
	 * @param status the new save status
	 * 
	 * @see #getSaveStatus()
	 */
	void setSaveStatus(int status);

	/**
	 * Retrieves the file name of the currently opened file. This file name is
	 * shown on the title bar of the main program.
	 * 
	 * @return the file name of the currently opened file
	 */
	String getOpenFileName();

	/**
	 * Sets the file name of the currently opened file. This method is called by
	 * the main program, when a file is opened or when a file is saved with a
	 * new name (save as).
	 * 
	 * @param filename the file name of the currently opened file.
	 */
	void setOpenFileName(String filename);
}