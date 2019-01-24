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
package org.jalgo.module.testModule;

import java.awt.BorderLayout;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;

import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.gui.components.JToolbarButton;
import org.jalgo.main.util.Messages;


public class ModuleConnector
extends AbstractModuleConnector {

	@Override
	public void init() {
		JComponent contentPane =
			JAlgoGUIConnector.getInstance().getModuleComponent(this);
		JLabel helloJAlgo = new JLabel(
			"Hello j-Algo!",
			new ImageIcon(Messages.getResourceURL("main", "ui.Logo")),
			SwingConstants.CENTER);
		contentPane.add(helloJAlgo, BorderLayout.CENTER);

		JMenu menu = JAlgoGUIConnector.getInstance().getModuleMenu(this);
		JMenuItem item = new JMenuItem("a menu item");
		menu.add(item);

		JToolBar toolbar =
			JAlgoGUIConnector.getInstance().getModuleToolbar(this);
		JToolbarButton button = new JToolbarButton(
			new ImageIcon(Messages.getResourceURL("main", "ui.Logo_small")),
			"a tooltip",
			"");
		toolbar.add(button);
	}

	@Override
	public void run() {
		System.out.println("testModule is running");
	}

	@Override
	public void setDataFromFile(ByteArrayInputStream data) {
	// here is no action performed in test module
	}

	@Override
	public ByteArrayOutputStream getDataForFile() {
		return null;
	}

	@Override
	public void print() {
	// here is no action performed in test module
	}
}