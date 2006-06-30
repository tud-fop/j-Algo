package org.jalgo.module.kmp.gui.event;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.jalgo.main.util.Messages;
import org.jalgo.module.kmp.gui.GUIController;

/**
 * The class <code>ForwardAction</code> defines an <code>Action</code> object,
 * which can be added to toolbars and menus. Performing this action moves forward
 * in the currently running algorithm.
 * 
 * @author Danilo Lisske
 */
public class ForwardAction extends AbstractAction {
	private static final long serialVersionUID = 5491712020638562272L;
	
	private GUIController gui;

	/**
	 * Constructs an <code>ForwardAction</code> object with the given
	 * references.
	 * 
	 * @param gui the <code>GUIController</code> instance of the KMP module
	 */
	public ForwardAction(GUIController gui) {
		super();
		this.gui = gui;
		putValue(NAME, Messages.getString("kmp", "Forward"));
		putValue(SHORT_DESCRIPTION, Messages.getString("kmp", "Forward_tooltip"));
		putValue(SMALL_ICON, new ImageIcon(Messages.getResourceURL(
			"main", "Icon.Perform_step")));
	}

	/**
	 * Performs the action.
	 */
	public void actionPerformed(ActionEvent e) {
		gui.doNextStep();
	}
}