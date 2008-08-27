package org.jalgo.module.hoare.view.toolbar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.view.View;

/**
 * The undo-action
 * 
 * @author: Tomas
 */
public class Undo extends AbstractAction {

	private static final long serialVersionUID = 1L;

	private View gui;
	/**
	 * sets up the button
	 * @param gui
	 */
	public Undo(View g) {
		gui = g;
		this.putValue(NAME, Messages.getString("hoare", "name.undo"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.undo")));
	}
	/**
	 * tells the view if button was pressed (undo())
	 * and calls the update function on View with (null, null)
	 */
	public void actionPerformed(ActionEvent arg0) {
		gui.undo();
		gui.update(null, null);

	}
	/**
	 * replaces the button with a gray copy or reverse
	 * @param gray
	 */
	public void grayButton(boolean gray){
		if (gray){
			this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL("hoare", "icon.undo.gray")));
		} else {
			this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL("hoare", "icon.undo")));
		}
		
	}

}
