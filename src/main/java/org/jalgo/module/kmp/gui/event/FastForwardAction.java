package org.jalgo.module.kmp.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.kmp.gui.GUIController;

/**
 * The class <code>FastForwardAction</code> defines an <code>Action</code> object,
 * which can be added to toolbars and menus. Performing this action moves fast forward
 * in the currently running algorithm.
 * 
 * @author Danilo Lisske
 */
public class FastForwardAction extends AbstractAction {
	private static final long serialVersionUID = -803759150116357558L;
	
	private GUIController gui;

	/**
	 * Constructs a <code>FastForwardAction</code> object with the given
	 * references.
	 * 
	 * @param gui the <code>GUIController</code> instance of the KMP module
	 */
	public FastForwardAction(GUIController gui) {
		super();
		this.gui = gui;
		putValue(NAME, Messages.getString("kmp", "FastForward"));
		putValue(SHORT_DESCRIPTION, Messages.getString("kmp", "FastForward_tooltip"));
		putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL(
			"main", "Icon.Perform_blockstep")));
	}

	/**
	 * Performs the action.
	 */
	public void actionPerformed(ActionEvent e) {
		gui.doNextBigStep();
	}
}