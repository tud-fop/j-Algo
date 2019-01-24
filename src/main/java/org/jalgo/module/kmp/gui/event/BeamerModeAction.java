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

/* Created on 12.06.2005 */
package org.jalgo.module.kmp.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;

import org.jalgo.main.util.Messages;
import org.jalgo.module.kmp.gui.GUIController;

/**
 * The class <code>BeamerModeAction</code> defines a checkbox menuitem for
 * switching between beamer mode and pc mode.<br>
 * Here the Singleton design pattern is implemented, in order that this setting
 * takes global effect for all open instances of the KMP module.<br>
 * 
 * @author Danilo Lisske
 */
public class BeamerModeAction extends JCheckBoxMenuItem implements ActionListener {
	private static final long serialVersionUID = 5523818288828234644L;
	private static BeamerModeAction instance;
	private static GUIController guicontroller;

	/**
	 * Constructs the singleton instance of <code>BeamerModeAction</code>.
	 */
	private BeamerModeAction() {
		super(Messages.getString("kmp", "Beamer_mode"),
			new ImageIcon(Messages.getResourceURL("main", "Icon.Beamer_mode")), false);
		addActionListener(this);
	}

	/**
	 * Retrieves and, if necessary, initializes the singleton instance of
	 * <code>BeamerModeAction</code>.
	 * 
	 * @param gc the <code>GUIController</code>
	 * 
	 * @return the singleton instance
	 */
	public static BeamerModeAction getInstance(GUIController gc) {
		guicontroller = gc;
		if (instance == null) instance = new BeamerModeAction();
		return instance;
	}

	/**
	 * Performs the action.
	 */
	public void actionPerformed(ActionEvent e) {
		guicontroller.doBeamerMode(isSelected());
	}
}