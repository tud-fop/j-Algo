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

/* Created on 25.05.2005 */
package org.jalgo.module.ebnf.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.jalgo.main.util.Messages;
import org.jalgo.main.util.Settings;
import org.jalgo.module.ebnf.MainController;

/**
 * Class <code>StartScreen</code> represents a screen with three buttons,
 * where the user can choose what to do. The buttons are rollover enabled and a
 * description of the selected task is displayed.
 * 
 * @author Tom Kazimiers
 */
public class StartScreen extends JPanel implements GUIConstants {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9085830968372916356L;

	private StartScreenActionHandler action;

	// components
	private StartButton loadButton;
	private StartButton ebnfButton;
	private StartButton syndiaButton;
	private JLabel descriptionLabel;
	/**
	 * Constructs a <code>StartScreen</code> object with the given
	 * reference.
	 * 
	 * @param gui the <code>GUIController</code> instance
	 */
	public StartScreen(MainController gui) {
		action = new StartScreenActionHandler(gui, this);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		String lang = Settings.getString("main", "Language"); 
		if (!lang.equals("de")) lang = "en";
		loadButton = new StartButton(
			new ImageIcon(
				Messages.getResourceURL("ebnf", "Start_load")),
			new ImageIcon(
				Messages.getResourceURL("ebnf", "Start_load_rollover")),
			new ImageIcon(
				Messages.getResourceURL("ebnf", "Start_load_description_"+lang)),
			"load", action); 
		ebnfButton = new StartButton(
			new ImageIcon(
				Messages.getResourceURL("ebnf", "Start_ebnf")),
			new ImageIcon(
				Messages.getResourceURL("ebnf", "Start_ebnf_rollover")),
			new ImageIcon(
				Messages.getResourceURL("ebnf", "Start_ebnf_description_"+lang)),
			"startEbnfInput", action); 
		syndiaButton = new StartButton(
			new ImageIcon(
				Messages.getResourceURL("ebnf", "Start_syndia")),
			new ImageIcon(
				Messages.getResourceURL("ebnf", "Start_syndia_rollover")),
			new ImageIcon(
				Messages.getResourceURL("ebnf", "Start_syndia_description_"+lang)),
			"startSynDiaInput", action); 

		descriptionLabel = new JLabel();
		descriptionLabel.setSize(399,40);
		descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
		

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(START_SCREEN_BACKGROUND);
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(loadButton);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(ebnfButton);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(syndiaButton);
		
		

		add(Box.createVerticalStrut(150));
		add(buttonPane);
		add(Box.createVerticalStrut(50));
		add(descriptionLabel);
	}

	/**
	 * Displays the given description-image on the screen.
	 * 
	 * @param desc the task description string
	 */
	public void setDescription(Icon desc) {
		descriptionLabel.setIcon(desc);
		descriptionLabel.updateUI();
	}

	/**
	 * Sets the enabled status of the buttons. If the given value is
	 * <code>false</code>, also the description string is removed from the
	 * screen.
	 * 
	 * @param b <code>true</code>, if the buttons should be enabled,
	 *            <code>false</code> otherwise
	 */
	public void setButtonsEnabled(boolean b) {
		loadButton.setEnabled(b);
		ebnfButton.setEnabled(b);
		syndiaButton.setEnabled(b);
		if (!b) setDescription(null);
	}

	/**
	 * Draws the background with a beautyful color ;o). Normally there would be
	 * the background color set with <code>setBackground(..)</code>, but, under
	 * linux (GTK) this has no effect. So this is a workaround...
	 */
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(START_SCREEN_BACKGROUND);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}