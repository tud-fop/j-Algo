package org.jalgo.module.kmp.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.kmp.gui.GUIController;

/**
 * The class <code>FastBackwardAction</code> defines an <code>Action</code> object,
 * which can be added to toolbars and menus. Performing this action moves fast backward
 * in the currently running algorithm.
 * 
 * @author Danilo Lisske
 */
public class FastBackwardAction extends AbstractAction {
	private static final long serialVersionUID = 2252270830798753137L;
	
	private GUIController gui;

	/**
	 * Constructs an <code>FastBackwardAction</code> object with the given
	 * references.
	 * 
	 * @param gui the <code>GUIController</code> instance of the KMP module
	 */
	public FastBackwardAction(GUIController gui) {
		super();
		this.gui = gui;
		putValue(NAME, Messages.getString("kmp", "FastBack"));
		putValue(SHORT_DESCRIPTION, Messages.getString("kmp", "FastBack_tooltip"));
		putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL(
			"main", "Icon.Undo_blockstep")));
	}

	/**
	 * Performs the action.
	 */
	public void actionPerformed(ActionEvent e) {
		gui.doPreviousBigStep();
	}
}