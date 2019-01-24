/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer science. It is written in Java and platform independent. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/* Created on 19.04.2005 */
package org.jalgo.module.kmp.gui.component;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import org.jalgo.module.kmp.gui.event.WelcomeScreenListener;

/**
 * The class <code>WelcomeButton</code> represents a rollover-enabled graphical
 * button with a description, which can be displayed on mouse hovering.
 * 
 * @author Danilo Lißke
 */
public class WelcomeButton extends JToggleButton {
	private static final long serialVersionUID = 284727572046563255L;
	
	private final ImageIcon desc;
	
	/**
	 * Constructs a <code>WelcomeButton</code> object with the given parameters.
	 * The description string is represented as a bitmap image, because of better
	 * layout on different platforms and the missing antialiasing of text on Swing
	 * components.
	 * 
	 * @param defaultIcon the icon, which is displayed, when button is not selected
	 * @param rolloverIcon the icon, which is displayed on mouse hovering
	 * @param description the description string as image
	 * @param actionCommand the action command
	 * @param action an event handler implementing <code>ActionListener</code>
	 * 		  and <code>MouseListener</code>
	 */
	public WelcomeButton(ImageIcon defaultIcon, ImageIcon rolloverIcon,
			ImageIcon description, String actionCommand,
			WelcomeScreenListener action) {
		setIcon(defaultIcon);
		setDisabledIcon(defaultIcon);
		//the standard rollover mechanism isn't used because of the following bug:
		//click on an button holds the rollover state so that 2nd opening
		//of welcome screen still highlights the button, even if mouse is not over
		setSelectedIcon(rolloverIcon);
		setPressedIcon(rolloverIcon);

		desc = description;

		setFocusPainted(false);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setBorderPainted(false);
		setMinimumSize(new Dimension(
			getIcon().getIconWidth(),
			getIcon().getIconHeight()));
		setMaximumSize(getMinimumSize());
		setPreferredSize(getMinimumSize());

		setActionCommand(actionCommand);
		addActionListener(action);
		addMouseListener(action);
	}

	/**
	 * Retrieves the description of this <code>WelcomeButton</code> as image.
	 * 
	 * @return the description string as image
	 */
	public ImageIcon getDescription() {
		return desc;
	}
}
