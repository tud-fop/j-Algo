package org.jalgo.module.kmp.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.kmp.gui.GUIController;

/**
 * The class <code>BackwardAction</code> defines an <code>Action</code> object,
 * which can be added to toolbars and menus. Performing this action moves backward
 * in the currently running algorithm.
 * 
 * @author Danilo Lisske
 */
public class BackwardAction extends AbstractAction {
	private static final long serialVersionUID = -2182592701926208241L;
	
	private GUIController gui;

	/**
	 * Constructs an <code>BackwardAction</code> object with the given
	 * references.
	 * 
	 * @param gui the <code>GUIController</code> instance of the KMP module
	 */
	public BackwardAction(GUIController gui) {
		super();
		this.gui = gui;
		putValue(NAME, Messages.getString("kmp", "Back"));
		putValue(SHORT_DESCRIPTION, Messages.getString("kmp", "Back_tooltip"));
		putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL(
			"main", "Icon.Undo_step")));
	}

	/**
	 * Performs the action.
	 */
	public void actionPerformed(ActionEvent e) {
		gui.doPreviousStep();
	}
}