package org.jalgo.module.hoare.view.toolbar;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.view.View;

/**
 * Deletes a partial tree (the tree above the selectet cell)
 * 
 * @author:Tomas
 */
public class DelPart extends AbstractAction{

	private static final long serialVersionUID = 1L;

	private View gui;
/**
 * sets up the button
 * @param gui
 */
	public DelPart(View gui) {

		this.gui = gui;

		this.putValue(NAME, Messages.getString("hoare", "name.delNode"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.delNode")));

	}
/**
 * tells the view if button was pressed (deletePart())
 */
	public void actionPerformed(ActionEvent e) {
		gui.deletePart();

	}

}