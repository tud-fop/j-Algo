package org.jalgo.module.hoare.view;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

/**
 * This is a JToggleButton to use on the welcomescreen.
 * 
 * @author Tomas
 *
 */
public class WelcomeScreenButton extends JToggleButton {
	private static final long serialVersionUID = 1L;

	private final ImageIcon desc;
	
	/**
	 * @param ImageIcon defaulIcon
	 * 		Standard Icon
	 * @param ImageIcon rolloverIcon
	 * 		rollover Itcon
	 * @param ImageIcon description
	 * 		Description
	 * @param String actionCommand
	 *		ActionComand
	 * @param ActionListener action
	 * 		ActionListener
	 * @param MouseListener mouse
	 * 		MouseListner
	 */
	public WelcomeScreenButton(ImageIcon defaultIcon, ImageIcon rolloverIcon,
			ImageIcon description, String actionCommand,
			ActionListener action, MouseListener mouse) {
		setIcon(defaultIcon);
		setDisabledIcon(defaultIcon);
		
		setSelectedIcon(rolloverIcon);
		setPressedIcon(rolloverIcon);

		desc = description;

		setFocusPainted(false);
		setBorder(new EmptyBorder(0, 0, 0, 0));
		setBorderPainted(false);
		setMinimumSize(new Dimension(getIcon().getIconWidth(), getIcon()
				.getIconHeight()));
		setMaximumSize(getMinimumSize());
		setPreferredSize(getMinimumSize());

		setActionCommand(actionCommand);
		addActionListener(action);
		addMouseListener(mouse);
	}
	
	/**
	 * returning the Icon
	 * 
	 * @return ImageIcon
	 */
	
	public ImageIcon getDescription() {
		return desc;
	}
}
