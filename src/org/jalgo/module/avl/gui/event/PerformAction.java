/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/* Created on 10.05.2005 */
package org.jalgo.module.avl.gui.event;

import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.NoActionException;
import org.jalgo.module.avl.gui.GUIController;

/**
 * The class <code>PerformAction</code> defines an <code>Action</code> object, which
 * can be added to toolbars and menus. Performing this action causes the currently
 * running algorithm to perform a single step.
 * 
 * @author Alexander Claus
 */
public class PerformAction
extends SwingSWTAction {
	
	private GUIController gui;
	private Controller controller;

	/**
	 * Constructs a <code>PerformAction</code> object with the given references.
	 * 
	 * @param gui the <code>GUIController</code> instance of the AVL module
	 * @param controller the <code>Controller</code> instance of the AVL module
	 */
	public PerformAction(GUIController gui, Controller controller) {
		super();
		this.gui = gui;
		this.controller = controller;
		setText("Nächster Schritt");
		setToolTipText("Führt den nächsten Schritt im aktuellen Algorithmus aus");
		setIconImage("avl_pix/perform.gif");
	}

	/**
	 * Performs the action.
	 */
	public void run() {
		//first perform after complete undo, like new start of algorithm
		if (!controller.algorithmHasPreviousStep()) gui.algorithmRestarted();

		try {controller.perform();}
		catch (NoActionException ex) {gui.showErrorMessage(ex.getMessage());}

		//for explanation of the following step, see DocuPane.update()
		gui.setStepDirection(true);
		if (!controller.algorithmHasNextStep()) gui.algorithmFinished();

		gui.update();
	}
}