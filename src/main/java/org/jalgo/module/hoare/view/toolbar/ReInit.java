package org.jalgo.module.hoare.view.toolbar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.view.View;

/**
 * The reinit-action
 * 
 * @author: Tomas
 * 
 */
public class ReInit extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private View gui;
	/**
	 * sets up the button
	 * @param gui
	 */
	public ReInit(View gui) {

		this.gui = gui;
		this.putValue(NAME, Messages.getString("hoare", "name.reinit"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.reinit")));

	}
	/**
	 * tells the view if button was pressed (reinit())
	 */
	public void actionPerformed(ActionEvent arg0) {
		gui.reinit();
	}

}
