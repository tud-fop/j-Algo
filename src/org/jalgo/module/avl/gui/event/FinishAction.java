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
		setToolTipText("Durchl�uft den Algorithmus bis zum Ende");
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