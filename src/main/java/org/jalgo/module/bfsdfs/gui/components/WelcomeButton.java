package org.jalgo.module.bfsdfs.gui.components;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import org.jalgo.module.bfsdfs.gui.event.WelcomeScreenActionHandler;

/**
 * This class represents one of three buttons in {@linkplain WelcomeScreen}.
 * It is a roll over enabled graphical button.
 * 
 * @author Florian Dornbusch
 */
public class WelcomeButton
extends JToggleButton {
	private static final long serialVersionUID = -1924541401853204950L;
	
	private final ImageIcon desc;
	
	public WelcomeButton(ImageIcon defaultIcon, ImageIcon rolloverIcon,
			ImageIcon description, String actionCommand,
			WelcomeScreenActionHandler action) {
		
		desc = description;
		this.setFocusable(false);
		
		// set the icons
		setIcon(defaultIcon);
		setDisabledIcon(defaultIcon);
		setSelectedIcon(rolloverIcon);
		setPressedIcon(rolloverIcon);
		
		// set the size of the button to the size of the icon
		setMinimumSize(new Dimension(
				defaultIcon.getIconWidth(),
				defaultIcon.getIconHeight()));
		setMaximumSize(getMinimumSize());
		setPreferredSize(getMinimumSize());
		
		// set up the action command and the listeners
		setActionCommand(actionCommand);
		addActionListener(action);
		addMouseListener(action);
	}

	/**
	 * Returns the description of this button.
	 * @author Florian Dornbusch
	 */
	public ImageIcon getDescription() {
		return desc;
	}
}