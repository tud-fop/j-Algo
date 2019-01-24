package org.jalgo.module.bfsdfs.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.gui.GUIController;

/**
 * This class represents an action to undo a design step (e.g. add a node).
 * 
 * @author Florian Dornbusch
 */
public class UndoAction
extends AbstractAction {
	private static final long serialVersionUID = -4212789064786233606L;
	
	private GUIController gui;
	
	public UndoAction(GUIController guiController) {
		this.gui = guiController;
		setEnabled(false);
		putValue(NAME, Messages.getString("bfsdfs", "UndoAction.tooltip"));
		putValue(SHORT_DESCRIPTION, Messages.getString(
			"bfsdfs", "UndoAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(
				Messages.getResourceURL("main", "Icon.Undo")));
	}
	
	public void actionPerformed(ActionEvent e) {
		gui.undo();
	}
}