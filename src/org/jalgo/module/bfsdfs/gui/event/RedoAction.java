package org.jalgo.module.bfsdfs.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.gui.GUIController;

/**
 * This class represents an action to redo a design step (e.g. add a node).
 * 
 * @author Florian Dornbusch
 */
public class RedoAction
extends AbstractAction {
	private static final long serialVersionUID = 7006845531760056982L;
	
	private GUIController gui;
	
	public RedoAction(GUIController guiController) {
		this.gui = guiController;
		setEnabled(false);
		putValue(NAME, Messages.getString("bfsdfs", "RedoAction.tooltip"));
		putValue(SHORT_DESCRIPTION, Messages.getString(
			"bfsdfs", "RedoAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(
				Messages.getResourceURL("main", "Icon.Redo")));
	}
	
	public void actionPerformed(ActionEvent e) {
		gui.redo();
	}
}