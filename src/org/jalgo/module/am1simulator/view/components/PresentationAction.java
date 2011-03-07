/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2010 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
package org.jalgo.module.am1simulator.view.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am1simulator.presenter.Simulator;

/**
 * Handle the presentation mode.
 * 
 * @author Max Leuth&auml;user
 */
public class PresentationAction extends JCheckBoxMenuItem implements
		ActionListener {
	private static final long serialVersionUID = 1L;
	private Simulator simulator;

	public PresentationAction(Simulator simulator) {
		super("Toggle presentation mode", new ImageIcon(Messages
				.getResourceURL("main", "Icon.Beamer_mode")), false);
		this.simulator = simulator;
		addActionListener(this);
	}

	/**
	 * Toggles the presentation mode.
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		simulator.setPresentationMode(isSelected());
	}
}
