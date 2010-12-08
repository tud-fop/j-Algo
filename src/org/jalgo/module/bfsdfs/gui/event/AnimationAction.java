package org.jalgo.module.bfsdfs.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.gui.GUIController;

/**
 * This class represents an action to turn the animations on and off.
 * 
 * @author Ephraim Zimmer
 */
public class AnimationAction
extends AbstractAction {
	private static final long serialVersionUID = -5649219591514335869L;
	
	private GUIController gui;
	
	public AnimationAction(GUIController gui) {
		this.gui = gui;
		setEnabled(false);
		putValue(SHORT_DESCRIPTION, Messages.getString(
			"bfsdfs", "AnimationAction.tooltip"));
	}
	
	public void actionPerformed(ActionEvent e) {
		gui.toggleAnimation();
	}
}