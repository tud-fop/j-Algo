package org.jalgo.module.bfsdfs.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.gui.GUIController;
import org.jalgo.module.bfsdfs.gui.graphview.EditMode;

/**
 * This class represents an action to change to the edge mode.
 * 
 * @author Florian Dornbusch
 */
public class EdgeAction
extends AbstractAction {
	private static final long serialVersionUID = -8933041027480952255L;
	
	private GUIController gui;
	
	public EdgeAction(GUIController guiController) {
		this.gui = guiController;
		setEnabled(false);
		putValue(NAME, Messages.getString("bfsdfs", "EdgeAction.tooltip"));
		putValue(SHORT_DESCRIPTION, Messages.getString(
			"bfsdfs", "EdgeAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(
				Messages.getResourceURL("bfsdfs", "icon_edges")));
	}
	
	public void actionPerformed(ActionEvent e) {
		gui.setGraphView(EditMode.START_EDGE);
	}
}