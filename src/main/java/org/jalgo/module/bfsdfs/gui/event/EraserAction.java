package org.jalgo.module.bfsdfs.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.gui.GUIController;
import org.jalgo.module.bfsdfs.gui.graphview.EditMode;

/**
 * This class represents an action to change to the eraser mode.
 * 
 * @author Florian Dornbusch
 */
public class EraserAction
extends AbstractAction {
	private static final long serialVersionUID = -6440908155733393663L;
	
	private GUIController gui;
	
	public EraserAction(GUIController guiController) {
		this.gui = guiController;
		setEnabled(false);
		putValue(NAME, Messages.getString("bfsdfs", "EraserAction.tooltip"));
		putValue(SHORT_DESCRIPTION, Messages.getString(
			"bfsdfs", "EraserAction.tooltip"));
		putValue(SMALL_ICON, new ImageIcon(
				Messages.getResourceURL("main", "Icon.Clear")));
	}
	
	public void actionPerformed(ActionEvent e) {
		gui.setGraphView(EditMode.ERASE);
	}
}