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

/* Created on 14.05.2006 */
package org.jalgo.module.kmp.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.kmp.gui.GUIController;

/**
 * The class <code>FinishAction</code> defines an <code>Action</code>
 * object, which can be added to toolbars and menus. Performing this action
 * finishes the currently running algorithm.
 * 
 * @author Danilo Lisske
 */
public class FinishAction extends AbstractAction {
	private static final long serialVersionUID = 3302515416723636450L;
	
	private GUIController gui;

	/**
	 * Constructs a <code>FinishAction</code> object with the given
	 * references.
	 * 
	 * @param gui the <code>GUIController</code> instance of the KMP module
	 */
	public FinishAction(GUIController gui) {
		this.gui = gui;
		putValue(NAME, Messages.getString("kmp", "Finish"));
		putValue(SHORT_DESCRIPTION, Messages.getString("kmp", "Finish_tooltip"));
		putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL(
			"main", "Icon.Finish_algorithm")));
	}

	/**
	 * Performs the action.
	 */
	public void actionPerformed(ActionEvent e) {
		gui.doEndStep();
	}
}