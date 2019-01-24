package org.jalgo.module.em.gui;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import org.jalgo.module.em.gui.StartScreen.StartScreenActionHandler;

/**
 * This class represents a button with a description and default/rollover icons,
 * which can be displayed on mouse hovering.
 * 
 * @author Tobias Nett
 *
 */
public class StartButton extends JToggleButton {

	private static final long serialVersionUID = -1924541401853204950L;
	private final String desc;
	
	/**
	 * Constructs a <code>StartButton</code> object with the given parameters.
	 * The description string is shown when the user moves the mouse over the 
	 * icon.
	 * 
	 * @param defaultIcon the icon, which is displayed, when button is not selected
	 * @param rolloverIcon the icon, which is displayed on mouse hovering
	 * @param description the action description string
	 * @param actionCommand the action command
	 * @param action an event handler implementing <code>ActionListener</code>
	 * 		  and <code>MouseListener</code>
	 */
	public StartButton(ImageIcon defaultIcon, ImageIcon rolloverIcon,
			String description, String actionCommand,
			StartScreenActionHandler action) {
		
		setIcon(defaultIcon);
		setDisabledIcon(defaultIcon);
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
	 * Returns the description of this buttons action as a string.
	 * 
	 * @return the description string
	 */
	public String getDescription() {
		return desc;
	}
}