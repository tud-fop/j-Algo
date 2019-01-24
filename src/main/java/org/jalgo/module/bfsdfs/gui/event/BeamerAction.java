package org.jalgo.module.bfsdfs.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.jalgo.module.bfsdfs.gui.ComponentUtility;
import org.jalgo.module.bfsdfs.gui.GUIController;

/**
 * This class represents an action to toggle the beamer mode.
 * 
 * @author Florian Dornbusch
 */
public class BeamerAction
extends AbstractAction {
	private static final long serialVersionUID = 2372809553068441995L;
	
	private GUIController gui;
	
	public BeamerAction(GUIController guiController) {
		this.gui = guiController;
	}
	
	public void actionPerformed(ActionEvent event) {
		ComponentUtility.BEAMER_MODE = !ComponentUtility.BEAMER_MODE;
		gui.toggleBeamerMode();
	}
}