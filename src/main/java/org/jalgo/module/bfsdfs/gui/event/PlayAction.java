package org.jalgo.module.bfsdfs.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.gui.GUIController;

/**
 * This class represents an action to play the current algorithm.
 * 
 * @author Florian Dornbusch
 */
public class PlayAction
extends AbstractAction {
	private static final long serialVersionUID = -7156200255850126725L;
	
	private GUIController gui;
	
	public PlayAction(GUIController guiController) {
		this.gui = guiController;
		setEnabled(false);
		putValue(NAME, Messages.getString("bfsdfs", "PlayAction.tooltip"));
		putValue(SHORT_DESCRIPTION, Messages.getString(
			"bfsdfs", "PlayAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(
				Messages.getResourceURL("main", "Icon.Perform_all")));
	}
	
	public void actionPerformed(ActionEvent e) {
		gui.playAlgo();
	}
}