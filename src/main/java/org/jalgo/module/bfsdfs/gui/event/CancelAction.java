package org.jalgo.module.bfsdfs.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.gui.GUIController;

/**
 * This class represents an action to cancel the animation to play the current
 * algorithm.
 * 
 * @author Florian Dornbusch
 */
public class CancelAction
extends AbstractAction {
	private static final long serialVersionUID = -4022448418439840518L;
	
	private GUIController gui;
	
	public CancelAction(GUIController guiController) {
		this.gui = guiController;
		setEnabled(false);
		putValue(NAME, Messages.getString("bfsdfs", "CancelAction.tooltip"));
		putValue(SHORT_DESCRIPTION, Messages.getString(
			"bfsdfs", "CancelAction.tooltip")); 
		putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"main", "Icon.Abort_algorithm"))); 
	}
	
	public void actionPerformed(ActionEvent e) {
		gui.cancel();
	}
}