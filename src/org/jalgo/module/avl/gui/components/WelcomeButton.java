/* Created on 19.04.2005 */
package org.jalgo.module.avl.gui.components;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

import org.jalgo.module.avl.gui.event.WelcomeScreenActionHandler;

/**
 * The class <code>WelcomeButton</code> represents a rollover-enabled graphical
 * button with a description, which can be displayed on mouse hovering.
 * 
 * @author Alexander Claus
 */
public class WelcomeButton
extends JToggleButton {

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
			WelcomeScreenActionHandler action) {
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