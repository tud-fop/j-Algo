package org.jalgo.module.c0h0.views;

import javax.swing.ImageIcon;
import javax.swing.JToggleButton;

import org.jalgo.module.c0h0.controller.ButtonCommand;

/**
 * Button for welcomeView
 * 
 * @author hendrik
 */
public class WelcomeViewButton extends JToggleButton{

	private static final long serialVersionUID = 1L;
	private String tip;
	
	/**
	 * @param icon
	 * @param tip
	 * @param cmd
	 * @param toolTip
	 * @param welcomeView
	 */
	public WelcomeViewButton(ImageIcon icon, String tip, ButtonCommand cmd, String toolTip, WelcomeView welcomeView){
		setIcon(icon);
		this.tip = tip;
		this.setActionCommand(cmd.toString());
		this.addActionListener(welcomeView);
		this.addMouseListener(welcomeView);
		this.setToolTipText(toolTip);
	}
	
	/**
	 * returns the tip
	 * @return the tip
	 */
	public String getTip(){
		return this.tip;
	}
}
