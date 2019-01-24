package org.jalgo.module.bfsdfs.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.gui.GUIController;

/**
 * This class represents an action to reset the current algorithm.
 * 
 * @author Florian Dornbusch
 */
public class ResetAction
extends AbstractAction {
	private static final long serialVersionUID = -8467509629339786748L;
	
	private GUIController gui;
	
	public ResetAction(GUIController guiController) {
		this.gui = guiController;
		setEnabled(false);
		putValue(NAME, Messages.getString("bfsdfs", "ResetAction.tooltip"));
		putValue(SHORT_DESCRIPTION, Messages.getString(
			"bfsdfs", "ResetAction.tooltip")); 
		putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"main", "Icon.Undo_all"))); 
	}
	
	public void actionPerformed(ActionEvent e) {
		gui.resetAlgo();
	}
}