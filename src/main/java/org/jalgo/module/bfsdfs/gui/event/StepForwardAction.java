package org.jalgo.module.bfsdfs.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.gui.GUIController;

/**
 * This class represents an action to do a step in the current algorithm 
 * in forward direction.
 * 
 * @author Florian Dornbusch
 */
public class StepForwardAction
extends AbstractAction {
	private static final long serialVersionUID = -106333707156892106L;
	
	private GUIController gui;
	
	public StepForwardAction(GUIController guiController) {
		this.gui = guiController;
		setEnabled(false);
		putValue(NAME, Messages.getString("bfsdfs", "StepForwardAction.tooltip"));
		putValue(SHORT_DESCRIPTION, Messages.getString(
			"bfsdfs", "StepForwardAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL(
				"main", "Icon.Perform_step")));
	}
	
	public void actionPerformed(ActionEvent e) {
		gui.nextAlgoStep();
	}
}