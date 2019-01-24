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

/* Created on 12.04.2005 */
package org.jalgo.module.kmp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.kmp.Controller;
import org.jalgo.module.kmp.gui.GUIController;
import org.jalgo.module.kmp.algorithm.History;
import org.jalgo.module.kmp.algorithm.Pattern;

/**
 * This class provides the bridge between the main program and the current
 * instance of the KMP module. It handles the references of several objects and
 * provides some methods used in the main program.
 * 
 * @author Danilo Lisske
 */
public class ModuleConnector
extends AbstractModuleConnector {
	private Controller controller;
	private GUIController gui;
	
	public void init() {
		controller = new Controller(new Pattern(), new String(), new History());
		gui = new GUIController(this, controller);
	}

	public void run() {
		gui.installWelcomeScreen();
	}
	
	public void setDataFromFile(ByteArrayInputStream data) {
		try {
			ObjectInputStream in = new ObjectInputStream(data);
			String pattern = (String)in.readObject();
			String searchtext = (String)in.readObject();
			History history = (History)in.readObject();
			int phase = controller.loadData(pattern, searchtext, history);
			if (phase == 1) {
				gui.installPhaseOneScreen();
				gui.restorePhaseOne();
			}
			else if (phase == 2) {
				gui.installPhaseTwoScreen();
				gui.restorePhaseTwo();
			}
			else gui.installWelcomeScreen();
		}
		catch (IOException ex) {
			JAlgoGUIConnector.getInstance().showErrorMessage(Messages.getString(
				"kmp", "ModuleConnector.No_valid_KMP_file"));
		}
		catch (ClassNotFoundException ex) {
			JAlgoGUIConnector.getInstance().showErrorMessage(Messages.getString(
				"kmp", "ModuleConnector.Loading_error") +
				System.getProperty("line.separator") +
				Messages.getString("kmp", "ModuleConnector.File_damaged"));
		}
	}

	public ByteArrayOutputStream getDataForFile() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		controller.saveData(out);
		return out;
	}

	public void print() {
//	printing is currently not supported
	}
}