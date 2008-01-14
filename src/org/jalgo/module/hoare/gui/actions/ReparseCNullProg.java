package org.jalgo.module.hoare.gui.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JTextPane;

import org.jalgo.main.util.Messages;
import org.jalgo.module.hoare.gui.GuiControl;

public class ReparseCNullProg extends AbstractAction {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GuiControl gui;

	private JTextPane source;

	public ReparseCNullProg(GuiControl gui, JTextPane source) {

		this.gui = gui;
		this.source = source;
		this.putValue(NAME, Messages.getString("hoare", "name.reparse"));
		this.putValue(SHORT_DESCRIPTION, Messages.getString("hoare",
				"ttt.reparse"));
		this.putValue(AbstractAction.SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"hoare", "icon.openC0Program")));
	}

	public void actionPerformed(ActionEvent arg0) {
		gui.init(source.getText());

	}

}
