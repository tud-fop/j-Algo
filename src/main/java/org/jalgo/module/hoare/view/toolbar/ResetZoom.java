package org.jalgo.module.hoare.view.toolbar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.view.View;

/**
 * The reset zoom to initial -action
 * 
 * @author: Tomas
 * 
 */
public class ResetZoom extends AbstractAction{

	private static final long serialVersionUID = 1L;
	private View gui;
	/**
	 * sets up the button
	 * @param gui
	 */	
	public ResetZoom(View gui){
		
		this.gui = gui;
		this.putValue(NAME, Messages.getString("hoare", "name.resetZoom"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.resetZoom")));
	}
	/**
	 * tells the view if button was pressed (resetZoom())
	 */
	public void actionPerformed(ActionEvent arg0) {
		gui.resetZoom();
	}
}