/* Created on 10.05.2005 */
package org.jalgo.module.avl.gui.event;

import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.NoActionException;
import org.jalgo.module.avl.gui.GUIController;

/**
 * The class <code>AbortAction</code> defines an <code>Action</code> object, which
 * can be added to toolbars and menus. Performing this action aborts the currently
 * running algorithm.
 * 
 * @author Alexander Claus
 */
public class AbortAction
extends SwingSWTAction {

	private GUIController gui;
	private Controller controller;
	
	/**
	 * Constructs an <code>AbortAction</code> object with the given references.
	 * 
	 * @param gui the <code>GUIController</code> instance of the AVL module
	 * @param controller the <code>Controller</code> instance of the AVL module
	 */
	public AbortAction(GUIController gui, Controller controller) {
		this.gui = gui;
		this.controller = controller;
		setText("Algorithmus abbrechen");
		setToolTipText("Bricht den laufenden Algorithmus ab");
		setIconImage("pix/avl/abort.gif");
	}

	/**
	 * Performs the action.
	 */
	public void run() {
		try {controller.abort();}
		catch (NoActionException ex) {gui.showErrorMessage(ex.getMessage());}

		gui.algorithmAborted();
	}
}