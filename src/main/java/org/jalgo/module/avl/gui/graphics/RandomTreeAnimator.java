/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/* Created on 21.05.2005 */
package org.jalgo.module.avl.gui.graphics;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.NoActionException;
import org.jalgo.module.avl.gui.GUIController;
import org.jalgo.module.avl.gui.Settings;

/**
 * Class <code>RandomTreeAnimator</code> provides an animation thread for
 * automatical creation of a <code>SearchTree</code>.
 * 
 * @author Alexander Claus
 */
public class RandomTreeAnimator
extends Animator {

	private GUIController gui;
	private Controller controller;

	/**
	 * Constructs a <code>RandomTreeAnimator</code> object with the given
	 * references.
	 * 
	 * @param gui the <code>GUIController</code> of the current AVL module
	 *            instance
	 * @param controller the <code>Controller</code> of the current AVL module
	 */
	public RandomTreeAnimator(GUIController gui, Controller controller) {
		this.gui = gui;
		this.controller = controller;
	}

	/**
	 * The "program code" of the animator thread. Goes stepwise through the
	 * current algorithm until finished.
	 */
	public void run() {
		while (controller.algorithmHasNextStep() && !isStopRequested()) {
			try {
				controller.perform();
				gui.update();
				sleep(Settings.getStepDelay());
			}
			catch (InterruptedException ex) {
				JAlgoGUIConnector.getInstance().showErrorMessage(
					Messages.getString("avl", "Hard_exception") //$NON-NLS-1$ //$NON-NLS-2$
					+ System.getProperty("line.separator") + ex.getMessage()); //$NON-NLS-1$
				return;
			}
			catch (NoActionException ex) {
				JAlgoGUIConnector.getInstance().showErrorMessage(
					ex.getMessage());
				return;
			}
		}
		if (!isStopRequested()) gui.algorithmFinished();
		animStopped();
	}
}