/* Created on 10.05.2005 */
package org.jalgo.module.avl.gui.event;

import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.NoActionException;
import org.jalgo.module.avl.gui.GUIController;

/**
 * The class <code>PerformBlockStepAction</code> defines an <code>Action</code>
 * object, which can be added to toolbars and menus. Performing this action causes
 * the currently running algorithm to perform a block step.
 * 
 * @author Alexander Claus
 */
public class PerformBlockStepAction
extends SwingSWTAction {
	
	private GUIController gui;
	private Controller controller;
	
	/**
	 * Constructs a <code>PerformBlockStepAction</code> object with the given
	 * references.
	 * 
	 * @param gui the <code>GUIController</code> instance of the AVL module
	 * @param controller the <code>Controller</code> instance of the AVL module
	 */
	public PerformBlockStepAction(GUIController gui, Controller controller) {
		this.gui = gui;
		this.controller = controller;
		setText("Nächster Blockschritt");
		setToolTipText(
			"Führt den nächsten grösseren Schritt im aktuellen Algorithmus aus");
		setIconImage("pix/avl/performBlockStep.gif");
	}

	/**
	 * Performs the action.
	 */
	public void run() {
		//first perform after complete undo, like new start of algorithm
		if (!controller.algorithmHasPreviousStep()) gui.algorithmRestarted();

		try {controller.performBlockStep();}
		catch (NoActionException ex) {gui.showErrorMessage(ex.getMessage());}

		//for explanation of the following step, see DocuPane.update()
		gui.setStepDirection(true);
		if (!controller.algorithmHasNextStep()) gui.algorithmFinished();

		gui.update();
	}
}