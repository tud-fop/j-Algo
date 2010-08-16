package org.jalgo.module.unifikation;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

/**
 * defines the button for the welcome screen
 */

public class WelcomeScreenButton extends JToggleButton{
	
	private static final long serialVersionUID = -1771155701778223947L;
	private String descText;


	public WelcomeScreenButton(ImageIcon defaultIcon, ImageIcon rolloverIcon, String descText) {

		this.descText = descText;

		setIcon(defaultIcon);

		setDisabledIcon(defaultIcon);

		

		setSelectedIcon(rolloverIcon);

		setPressedIcon(rolloverIcon);

		

		setFocusPainted(false);

		setBorder(new EmptyBorder(0, 0, 0, 0));

		setBorderPainted(false);

		setMinimumSize(new Dimension(80, 80));

		setMaximumSize(getMinimumSize());

		setPreferredSize(getMinimumSize());

	}

	

	public String getDescText() {

		return descText;

	}

}
