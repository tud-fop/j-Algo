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

package org.jalgo.module.ebnf.gui;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.DialogConstants;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.ebnf.MainController;
import org.jalgo.module.ebnf.ModuleConnector;

/**
 * The class <code>StartAction</code> defines an <code>Action</code> which could
 * be added to ToolBar- and Menu-Buttons. This action asks the user to discard his/her
 * changes to switch to the StartScreen. If "No" is chosen nothing will happen, if
 * "yes" he/she is taken to the StartScreen.
 * 
 * @author Tom Kazimiers
 */
public class StartAction
extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1675559376688011664L;
	private MainController maincontroller;
	private ModuleConnector connector;

	/**
	 * Constructs a <code>StartAction</code> object with the given
	 * references.
	 * 
	 * @param controller the <code>GUIController</code> instance of the EBNF module
	 */
	public StartAction(MainController controller, ModuleConnector connector) {
		this.maincontroller = controller;
		this.connector = connector;
		putValue(NAME, Messages.getString("ebnf", "Show_start_screen"));
		putValue(SHORT_DESCRIPTION, Messages.getString(
			"ebnf", "Show_start_screen_tooltip"));
		putValue(SMALL_ICON, new ImageIcon(
			Messages.getResourceURL("ebnf", "Module_logo")));
	}

	/**
	 * Performs the action.
	 */
	public void actionPerformed(ActionEvent e) {
		if (connector.getSaveStatus() == SaveStatus.CHANGES_TO_SAVE) {
			switch (JAlgoGUIConnector.getInstance().showConfirmDialog(
				Messages.getString("ebnf", "Wish_to_discard"),
				DialogConstants.YES_NO_CANCEL_OPTION)) {
				case DialogConstants.YES_OPTION:
					maincontroller.installStartScreen();
					break;
				case DialogConstants.NO_OPTION:
					JAlgoGUIConnector.getInstance().newModuleInstanceByName(
						Messages.getString("ebnf", "Module_name"));
					break;
				case DialogConstants.CANCEL_OPTION:
					return;
			}
		} else {
			switch (JAlgoGUIConnector.getInstance().showConfirmDialog(
				Messages.getString("ebnf", "Wish_to_show_Startscreen"),
				DialogConstants.YES_NO_OPTION)) {
				case DialogConstants.YES_OPTION:
					maincontroller.installStartScreen();
					break;
				case DialogConstants.NO_OPTION:
					return;
			}
		}
	}
}