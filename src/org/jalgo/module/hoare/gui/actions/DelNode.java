package org.jalgo.module.hoare.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.control.ProgramControl.Edit;
import org.jalgo.module.hoare.gui.GuiControl;

public class DelNode extends AbstractAction {

	/**
	 * Deletes a partial tree
	 * 
	 * @author:Markus
	 */
	private static final long serialVersionUID = 1L;

	private GuiControl gui;

	public DelNode(GuiControl gui) {

		this.gui = gui;

		this.putValue(NAME, Messages.getString("hoare", "name.delNode"));
		// this.putValue(SHORT_DESCRIPTION, Messages.getString("hoare",
		// "ttt.delNode"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.delNode")));

	}

	public void actionPerformed(ActionEvent e) {
		gui.applyTreeEdit(Edit.DeleteNode, gui.getActiveNode());

	}

}