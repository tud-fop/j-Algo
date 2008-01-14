package org.jalgo.module.hoare.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.gui.GuiControl;

public class TreeEval extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GuiControl gui;

	public TreeEval(GuiControl gui) {

		this.gui = gui;
		this.putValue(NAME, Messages.getString("hoare", "name.evalTree"));
		// this.putValue(SHORT_DESCRIPTION, Messages.getString("hoare",
		// "ttt.evalTree"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.evalTree")));
	}

	public void actionPerformed(ActionEvent e) {
		gui.startTreeEval();
		gui.update(null, null);
	}

}
