/* Created on 10.05.2005 */
package org.jalgo.module.avl.gui.event;

import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.NoActionException;
import org.jalgo.module.avl.gui.GUIController;

/**
 * The class <code>FinishAction</code> defines an <code>Action</code> object, which
 * can be added to toolbars and menus. Performing this action finishes the currently
 * running algorithm.
 * 
 * @author Alexander Claus
 */
public class FinishAction
extends SwingSWTAction {

	private GUIController gui;
	private Controller controller;

	/**
	 * Constructs a <code>FinishAction</code> object with the given references.
	 * 
	 * @param gui the <code>GUIController</code> instance of the AVL module
	 * @param controller the <code>Controller</code> instance of the AVL module
	 */
	public FinishAction(GUIController gui, Controller controller) {
		this.gui = gui;
		this.controller = controller;
		setText("Algorithmus beenden");
		setToolTipText("Durchläuft den Algorithmus bis zum Ende");
		setIconImage("pix/avl/finish.gif");
	}

	/**
	 * Performs the action.
	 */
	public void run() {
		try {controller.finish();}
		catch (NoActionException ex) {gui.showErrorMessage(ex.getMessage());}

		gui.algorithmFinished();
		gui.update();
	}
}