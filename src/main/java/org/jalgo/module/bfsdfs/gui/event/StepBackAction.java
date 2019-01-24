package org.jalgo.module.bfsdfs.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.gui.GUIController;

/**
 * This class represents an action to do a step in the current algorithm
 * in backwards direction.
 * 
 * @author Florian Dornbusch
 */
public class StepBackAction
extends AbstractAction {
	private static final long serialVersionUID = -8007372505949383154L;
	
	private GUIController gui;
	
	public StepBackAction(GUIController guiController) {
		this.gui = guiController;
		setEnabled(false);
		putValue(NAME, Messages.getString("bfsdfs", "StepBackAction.tooltip"));
		putValue(SHORT_DESCRIPTION, Messages.getString(
			"bfsdfs", "StepBackAction.tooltip"));
		putValue(SMALL_ICON,
				new ImageIcon(Messages.getResourceURL("main", "Icon.Undo_step")));
	}
	
	public void actionPerformed(ActionEvent e) {
		gui.previousAlgoStep();
	}
}