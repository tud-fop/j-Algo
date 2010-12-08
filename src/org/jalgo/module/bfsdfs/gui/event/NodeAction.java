package org.jalgo.module.bfsdfs.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.gui.GUIController;
import org.jalgo.module.bfsdfs.gui.graphview.EditMode;

/**
 * This class represents an action to change to the node mode.
 * 
 * @author Florian Dornbusch
 */
public class NodeAction
extends AbstractAction {
	private static final long serialVersionUID = -6899288668914760000L;
	
	private GUIController gui;
	
	public NodeAction(GUIController guiController) {
		this.gui = guiController;
		setEnabled(false);
		putValue(NAME, Messages.getString("bfsdfs", "NodeAction.tooltip"));
		putValue(SHORT_DESCRIPTION, Messages.getString(
			"bfsdfs", "NodeAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(
				Messages.getResourceURL("bfsdfs", "icon_nodeEdit")));
	}
	
	public void actionPerformed(ActionEvent e) {
		gui.setGraphView(EditMode.PUT_NODE);
	}
}