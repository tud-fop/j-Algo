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
 * Created on May 18, 2004
 */
package org.jalgo.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import org.jalgo.main.gui.JAlgoGUIConnector;

/**
 * The class <code>AbstractModuleConnector</code> defines the module side
 * interface between the j-Algo main program and a module to be implemented.<br>
 * This class contains methods for cooperation between the main program and the
 * module. Some methods the module programmer has to override, so
 * {@link #init()}, {@link #run()},
 * {@link #setDataFromFile(ByteArrayInputStream)}, {@link #getDataForFile()}
 * and {@link #print()}, because they are module specific. Other methods the
 * module programmer can implement, when there is the need for higher
 * sophisticated semantics, such as {@link #close()}. Finally some methods must
 * have well defined semantics and are therefore implemented in this class and
 * declared with <code>final</code>.<br>
 * Because the constructor of a module instance is invoked automatically and in
 * interaction with creation of module's GUI components by the main program,
 * subclasses doesn't have to declare a constructor. Initialization of the GUI
 * of the module has to be placed in the <code>init</code> method. Furthermore
 * the GUI components for the module aren't available yet at the time of the
 * constructor call.
 * 
 * @author Alexander Claus, Michael Pradel, Christopher Friedrich, Stephan
 *         Creutz
 */
public abstract class AbstractModuleConnector {

	private SaveStatus saveStatus;
	private boolean savingBlocked;
	private String openFileName;

	/**
	 * Constructs an <code>AbstractModuleConnector</code>.
	 */
	public AbstractModuleConnector() {
		saveStatus = SaveStatus.NOTHING_TO_SAVE;
	}

	/**
	 * Modules have to override this method to initialize their GUI.
	 */
	public abstract void init();

	/**
	 * After the module has been initialized using the constructor (and maybe
	 * some methods), this method will be invoked by the main program. It should
	 * contain the code to start the module. This method should return soon.
	 */
	public abstract void run();

	/**
	 * If the user opens a file saved with the specific module, this method is
	 * called with an input stream backed by the file. So the module programmer
	 * has to care, how to read from the file.
	 * 
	 * @param data an input stream with the file content in the format saved by
	 *            the module
	 */
	public abstract void setDataFromFile(ByteArrayInputStream data);

	/**
	 * If user wants to save the current module data, this method is called. The
	 * module programmer has to prepare the interesting data and to provide it
	 * as output stream in a format, which can be deserialized too!
	 * 
	 * @return an output stream with the interesting data of the module
	 */
	public abstract ByteArrayOutputStream getDataForFile();

	/**
	 * Printing is currently not supported, this method is only present for
	 * compatibility.
	 */
	public abstract void print();

	/**
	 * This method is called, when module or program are intended to be closed.
	 * Here final operations can be performed, before closing the module, or, if
	 * the module is currently not ready to be closed, this method can return
	 * <code>false</code>. When this method returns <code>true</code>, the
	 * user will be asked for saving his work (if there is something to save).
	 * Otherwise the closing of the module / program is ignored.
	 * 
	 * @return <code>true</code>, if module is ready to be closed,
	 *         <code>false</code> otherwise
	 */
	public boolean close() {
		return true;
	}

	/**
	 * Retrieves the singleton instance of the <code>IModuleInfo</code>
	 * corresponding to this module.
	 * 
	 * @return the corresponding <code>IModuleInfo</code>
	 * 
	 * @see IModuleInfo
	 */
	public final IModuleInfo getModuleInfo() {
		try {
			return (IModuleInfo)Class.forName(
				getClass().getPackage().getName() + ".ModuleInfo").getMethod(
				"getInstance", new Class[] {}).invoke(null, new Object[] {});
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * The enum type <code>SaveStatus</code> is used by modules to indicate if
	 * module content has changed or not. This status is evaluated to set the
	 * enabled status of the save buttons in the main program.
	 */
	public enum SaveStatus {
		/** Indicates, that there is no file open or nothing to save */
		NOTHING_TO_SAVE,
		/** Indicates, that the current file was saved recently */
		NO_CHANGES,
		/** Indicates, that the current file is 'dirty' and has to be saved */
		CHANGES_TO_SAVE
	}

	/**
	 * This method retrieves the save status of the module. This method is
	 * necessary for correctly working of the enabled button status of the save
	 * buttons and several features.
	 * 
	 * @return one of the {@link SaveStatus} enum constants:
	 *         <ul>
	 *         <li><code>NOTHING_TO_SAVE</code></li> - if there is no file
	 *         open
	 *         <li><code>NO_CHANGES</code></li> - if the open file was saved
	 *         recently
	 *         <li><code>CHANGES_TO_SAVE</code></li> - if there are changes
	 *         to save
	 *         </ul>
	 */
	public final SaveStatus getSaveStatus() {
		return saveStatus;
	}

	/**
	 * This method is called from main program during a save action. Furthermore
	 * this method can be used by the module to set the save status centrally.
	 * 
	 * @param status the new save status
	 * 
	 * @see #getSaveStatus()
	 * @see SaveStatus
	 */
	public final void setSaveStatus(SaveStatus status) {
		saveStatus = status;
		if (status == SaveStatus.NOTHING_TO_SAVE) setOpenFileName(null);
		else if (status == SaveStatus.CHANGES_TO_SAVE && openFileName == null) setOpenFileName("");
		JAlgoGUIConnector.getInstance().saveStatusChanged(this);
	}

	/**
	 * This method can be called, if the save buttons should be disabled for a
	 * while, e.g. during a running algorithm. So can be guaranteed, that only
	 * consistent states of module contents can be saved. Ensure to deblock the
	 * save buttons !!!
	 * 
	 * @param blocked <code>true</code>, if the save buttons should be
	 *            blocked, <code>false</code> otherwise
	 * 
	 * @see #isSavingBlocked()
	 */
	public final void setSavingBlocked(boolean blocked) {
		savingBlocked = blocked;
		JAlgoGUIConnector.getInstance().saveStatusChanged(this);
	}

	/**
	 * Retrieves, if saving mechanism is blocked or not, e.g. during a running
	 * algorithm.
	 * 
	 * @return <code>true</code> if saving mechanism is blocked,
	 *         <code>false</code> otherwise
	 */
	public final boolean isSavingBlocked() {
		return savingBlocked;
	}

	/**
	 * Retrieves the file name of the currently opened file. This file name is
	 * shown on the title bar of the main program.
	 * 
	 * @return the file name of the currently opened file
	 */
	public final String getOpenFileName() {
		return openFileName;
	}

	/**
	 * Sets the file name of the currently opened file. This method is called by
	 * the main program, when a file is opened or when a file is saved with a
	 * new name (save as).
	 * 
	 * @param filename the file name of the currently opened file.
	 */
	public final void setOpenFileName(String filename) {
		openFileName = filename;
	}
}