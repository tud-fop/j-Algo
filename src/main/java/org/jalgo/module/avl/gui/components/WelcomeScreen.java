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
package org.jalgo.module.avl.gui.components;

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
import org.jalgo.module.avl.gui.GUIConstants;
import org.jalgo.module.avl.gui.GUIController;
import org.jalgo.module.avl.gui.event.WelcomeScreenActionHandler;

/**
 * Class <code>WelcomeScreen</code> represents a screen with three buttons,
 * where the user can choose what to do. The buttons are rollover enabled and a
 * description of the selected task is displayed.
 * 
 * @author Alexander Claus
 */
public class WelcomeScreen
extends JPanel
implements GUIConstants {

	private static final long serialVersionUID = -2019743374918523370L;

	private WelcomeScreenActionHandler action;

	// components
	private WelcomeButton loadButton;
	private WelcomeButton manualButton;
	private WelcomeButton randomButton;
	private JLabel descriptionLabel;

	/**
	 * Constructs a <code>WelcomeScreen</code> object with the given
	 * reference.
	 * 
	 * @param gui the <code>GUIController</code> instance
	 */
	public WelcomeScreen(GUIController gui) {
		action = new WelcomeScreenActionHandler(gui, this);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		String lang = Settings.getString("main", "Language"); //$NON-NLS-1$
		if (!lang.equals("de")) lang = "en"; //$NON-NLS-1$ //$NON-NLS-2$
		loadButton = new WelcomeButton(
			new ImageIcon(
				Messages.getResourceURL("avl", "Welcome_load")), //$NON-NLS-1$ //$NON-NLS-2$
			new ImageIcon(
				Messages.getResourceURL("avl", "Welcome_load_rollover")), //$NON-NLS-1$ //$NON-NLS-2$
			new ImageIcon(
				Messages.getResourceURL("avl", "Welcome_load_description_"+lang)), //$NON-NLS-1$ //$NON-NLS-2$
			"load", action); //$NON-NLS-1$
		manualButton = new WelcomeButton(
			new ImageIcon(
				Messages.getResourceURL("avl", "Welcome_manual")), //$NON-NLS-1$ //$NON-NLS-2$
			new ImageIcon(
				Messages.getResourceURL("avl", "Welcome_manual_rollover")), //$NON-NLS-1$ //$NON-NLS-2$
			new ImageIcon(
				Messages.getResourceURL("avl", "Welcome_manual_description_"+lang)), //$NON-NLS-1$ //$NON-NLS-2$
			"createManually", action); //$NON-NLS-1$
		randomButton = new WelcomeButton(
			new ImageIcon(
				Messages.getResourceURL("avl", "Welcome_random")), //$NON-NLS-1$ //$NON-NLS-2$
			new ImageIcon(
				Messages.getResourceURL("avl", "Welcome_random_rollover")), //$NON-NLS-1$ //$NON-NLS-2$
			new ImageIcon(
				Messages.getResourceURL("avl", "Welcome_random_description_"+lang)), //$NON-NLS-1$ //$NON-NLS-2$
			"createRandomly", action); //$NON-NLS-1$

		descriptionLabel = new JLabel();
		descriptionLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(WELCOME_SCREEN_BACKGROUND);
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(loadButton);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(manualButton);
		buttonPane.add(Box.createRigidArea(new Dimension(20, 0)));
		buttonPane.add(randomButton);

		add(Box.createVerticalStrut(150));
		add(buttonPane);
		add(Box.createVerticalStrut(50));
		add(descriptionLabel);
	}

	/**
	 * Displays the given string description on the screen.
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
		manualButton.setEnabled(b);
		randomButton.setEnabled(b);
		if (!b) setDescription(null);
	}

	/**
	 * Draws the background with a beautyful color ;o). Normally there would be
	 * the background color set with <code>setBackground(..)</code>, but, under
	 * linux (GTK) this has no effect. So this is a workaround...
	 */
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(WELCOME_SCREEN_BACKGROUND);
		g.fillRect(0, 0, getWidth(), getHeight());
	}
}