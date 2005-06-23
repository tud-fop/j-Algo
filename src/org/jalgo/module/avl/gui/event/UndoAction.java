/* Created on 10.05.2005 */
package org.jalgo.module.avl.gui.event;

import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.NoActionException;
import org.jalgo.module.avl.gui.GUIController;

/**
 * The class <code>UndoAction</code> defines an <code>Action</code> object, which
 * can be added to toolbars or menus. Performing this action causes the currently
 * running algorithm to undo a single step.
 * 
 * @author Alexander Claus
 */
public class UndoAction
extends SwingSWTAction {

	private GUIController gui;
	private Controller controller;

	/**
	 * Constructs an <code>UndoAction</code> object with the given references.
	 * 
	 * @param gui the <code>GUIController</code> instance of the AVL module
	 * @param controller the <code>Controller</code> instance of the AVL module
	 */
	public UndoAction(GUIController gui, Controller controller) {
		this.gui = gui;
		this.controller = controller;
		setText("Schritt zurück");
		setToolTipText("Macht den letzten Schritt im aktuellen Algorithmus rückgängig");
		setIconImage("pix/avl/undo.gif");
	}

	/**
	 * Performs the action.
	 */
	public void run() {
		//for explanation of the following step, see DocuPane.update()
		gui.setStepDirection(false);
		//first undo after completion of algorithm
		if (!controller.algorithmHasNextStep()) gui.algorithmRestartedFromUndo();
		
		try {controller.undo();}
		catch (NoActionException ex) {gui.showErrorMessage(ex.getMessage());}

		if (!controller.algorithmHasPreviousStep()) gui.algorithmUndone();

		gui.update();
	}
}