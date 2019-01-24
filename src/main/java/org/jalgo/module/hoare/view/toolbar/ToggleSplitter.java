package org.jalgo.module.hoare.view.toolbar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.view.View;

public class ToggleSplitter extends AbstractAction{
	/**
	 * The toggle splitter-action
	 * 
	 * @author: Tomas
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private View gui;
	/**
	 * sets up the button
	 * @param gui
	 */	
	public ToggleSplitter(View gui){
		
		this.gui = gui;
		this.putValue(NAME, Messages.getString("hoare", "name.toggleSplitter"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.toggleSplitter")));
	}
	/**
	 * tells the view if button was pressed (toggleSplitter())
	 */
	public void actionPerformed(ActionEvent arg0) {
		gui.toggleSplitter();
	}
}