package org.jalgo.module.hoare.view.toolbar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.view.View;

/**
 * The set too normal mouse
 * 
 * @author: Tomas
 */
public class NormalMouse extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private View gui;
	/**
	 * sets up the button
	 * @param gui
	 */
	public NormalMouse(View gui){
		
		this.gui = gui;
		this.putValue(NAME, Messages.getString("hoare", "name.normalMouse"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.normalMouse")));
	}
	/**
	 * tells the view if button was pressed (setZoomMode(View.ZoomMode.NO))
	 */
	public void actionPerformed(ActionEvent arg0) {
		gui.setZoomMode(View.ZoomMode.NO);
	}
}