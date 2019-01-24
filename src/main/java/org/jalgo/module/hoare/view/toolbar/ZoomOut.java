package org.jalgo.module.hoare.view.toolbar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.view.View;

/**
 * the zoom Out button
 * @author: Tomas
 */
public class ZoomOut extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private View gui;
	/**
	 * sets up the button
	 * @param gui
	 */	
	public ZoomOut(View gui){
		
		this.gui = gui;
		this.putValue(NAME, Messages.getString("hoare", "name.zoomOut"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.zoomOut")));
	}
	/**
	 * tells the view if button was pressed (setZoomMode(View.ZoomMode.OUT))
	 */
	public void actionPerformed(ActionEvent arg0) {
		gui.setZoomMode(View.ZoomMode.OUT);
	}
}
