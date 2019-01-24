package org.jalgo.module.hoare.view.toolbar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.view.View;

/**
 * loads C0-Program from .c-file
 *
 * @author:Tomas
 */
public class LoadCNullProg extends AbstractAction {
	
	private static final long serialVersionUID = 1L;

	private View gui;
	/**
	 * sets up the button
	 * @param gui
	 */
	public LoadCNullProg(View gui) {
		this.gui = gui;

		this.putValue(NAME, Messages.getString("hoare", "name.openC0Program"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.openC0Program")));
	}
	/**
	 * tells the view if button was pressed (loadSourceCode())
	 */
	public void actionPerformed(ActionEvent arg0) {
		gui.loadSourceCode();
	}

}
