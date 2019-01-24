package org.jalgo.module.lambda.view;

import java.awt.Dimension;
import javax.swing.ImageIcon;
import javax.swing.JToggleButton;
import javax.swing.border.EmptyBorder;

public class WelcomeScreenButton extends JToggleButton {
	private static final long serialVersionUID = 1L;
	
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
