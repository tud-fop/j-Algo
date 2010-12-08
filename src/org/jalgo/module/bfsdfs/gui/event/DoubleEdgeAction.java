package org.jalgo.module.bfsdfs.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.gui.GUIController;
import org.jalgo.module.bfsdfs.gui.graphview.EditMode;

/**
 * This class represents an action to change to the double-edge mode.
 * 
 * @author Florian Dornbusch
 */
public class DoubleEdgeAction
extends AbstractAction {
	private static final long serialVersionUID = -5920832848188996554L;
	
	private GUIController gui;
	
	public DoubleEdgeAction(GUIController guiController) {
		this.gui = guiController;
		setEnabled(false);
		putValue(NAME, Messages.getString("bfsdfs", "DoubleEdgeAction.tooltip"));
		putValue(SHORT_DESCRIPTION, Messages.getString(
			"bfsdfs", "DoubleEdgeAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(
				Messages.getResourceURL("bfsdfs", "icon_doubleedges")));
	}
	
	public void actionPerformed(ActionEvent e) {
		gui.setGraphView(EditMode.START_DOUBLE_EDGE);
	}
}