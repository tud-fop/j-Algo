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

package org.jalgo.module.kmp.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.gui.DialogConstants;
import org.jalgo.main.util.Messages;
import org.jalgo.module.kmp.gui.GUIController;

import org.jalgo.main.gui.JAlgoGUIConnector;

/**
 * The class <code>WelcomeAction</code> defines an <code>Action</code>
 * object, which can be added to toolbars and menus. Performing this action asks
 * the user for discarding his changes, and if so, switches the layout of the
 * current KMP module instance to the welcome screen and clears the tree. If the
 * user doesn't want to discard his changes, a new instance of the KMP module is
 * opened. The question dialog can be cancelled too.
 * 
 * @author Danilo Lisske
 */
public class WelcomeAction extends AbstractAction {
	private static final long serialVersionUID = -4393334839778312693L;
	
	private GUIController gui;

	/**
	 * Constructs a <code>WelcomeAction</code> object with the given
	 * references.
	 * 
	 * @param gui the <code>GUIController</code> instance of the KMP module
	 */
	public WelcomeAction(GUIController gui) {
		this.gui = gui;
		putValue(NAME, Messages.getString("kmp", "Show_welcome_screen"));
		putValue(SHORT_DESCRIPTION, Messages.getString(
			"kmp", "Show_welcome_screen_tooltip"));
		putValue(SMALL_ICON, new ImageIcon(
			Messages.getResourceURL("kmp", "Module_logo")));
	}

	/**
	 * Performs the action.
	 */
	public void actionPerformed(ActionEvent e) {
		switch (JAlgoGUIConnector.getInstance().showConfirmDialog(
			Messages.getString("kmp", "Wish_to_discard"),
			DialogConstants.YES_NO_CANCEL_OPTION)) {
			case DialogConstants.YES_OPTION:
				gui.clearValues();
				gui.installWelcomeScreen();
				break;
			case DialogConstants.NO_OPTION:
				JAlgoGUIConnector.getInstance().newModuleInstanceByName(
					Messages.getString("kmp", "Module_name"));
				break;
			case DialogConstants.CANCEL_OPTION:
				return;
		}
	}
}