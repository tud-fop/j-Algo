/* Created on 21.05.2005 */
package org.jalgo.module.avl.gui.graphics;

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
	 * Constructs a <code>RandomTreeAnimator</code> object with the given references.
	 * 
	 * @param gui the <code>GUIController</code> of the current AVL module instance
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
				gui.showErrorMessage("Schwerer Ausnahmefehler:\r\n"+ex.getMessage());
				return;
			}
			catch (NoActionException ex) {
				gui.showErrorMessage(ex.getMessage());
				return;
			}
		}
		if (!isStopRequested()) gui.algorithmFinished();
		animStopped();
	}
}