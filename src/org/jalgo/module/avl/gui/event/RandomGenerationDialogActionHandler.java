/* Created on 26.05.2005 */
package org.jalgo.module.avl.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.NoActionException;
import org.jalgo.module.avl.gui.GUIConstants;
import org.jalgo.module.avl.gui.GUIController;
import org.jalgo.module.avl.gui.components.RandomGenerationDialog;
import org.jalgo.module.avl.gui.graphics.RandomTreeAnimator;

/**
 * The class <code>RandomGenerationDialogActionHandler</code> represents an event
 * handler for the <code>RandomGenerationDialog</code> class. It handles button
 * clicks and key inputs.
 * 
 * @author Alexander Claus
 */
public class RandomGenerationDialogActionHandler
implements ActionListener, DocumentListener, GUIConstants {

	private Controller controller;
	private GUIController gui;
	private RandomGenerationDialog dialog;

	/**
	 * Constructs a <code>RandomGenerationDialogActionHandler</code> object with
	 * the given references.
	 * 
	 * @param controller the <code>Controller</code> instance of the AVL module
	 * @param gui the <code>GUIController</code> instance of the AVL module
	 * @param dialog the <code>RandomGenerationDialog</code> instance, for which
	 * 					events are handled here
	 */
	public RandomGenerationDialogActionHandler(Controller controller,
			GUIController gui, RandomGenerationDialog dialog) {
		this.controller = controller;
		this.gui = gui;
		this.dialog = dialog;
	}

	/**
	 * Handles button clicks, starts the algorithm with choosen parameters.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) {
			gui.setAVLMode(dialog.isAVLSelected(), false);
			controller.createRandomTree(dialog.getNodeCount());

			switch (dialog.getVisualizationMode()) {
			case NO_VISUALIZATION:
				try {controller.finish();}
				catch (NoActionException ex) {gui.showErrorMessage(ex.getMessage());}				
				gui.installStandardLayout();
				gui.setChangesToSave(true);
				//use alg.Finished() here, if stepwise backwards should be enabled
				gui.algorithmAborted();
				break;
			case STEPWISE:
				gui.installStandardLayout();
				gui.algorithmStarted();
				break;
			case AUTOMATICAL:
				gui.installStandardLayout();
				gui.randomAnimatorStarted();
				gui.setAnimator(new RandomTreeAnimator(gui, controller));
				gui.getAnimator().start();
			}
		}
		dialog.dispose();
	}

	/**
	 * Invoked if input textfield has been edited. Causes to validate the input.
	 */
	public void insertUpdate(DocumentEvent e) {
		dialog.validateInput();
	}

	/**
	 * Invoked if input textfield has been edited. Causes to validate the input.
	 */
	public void removeUpdate(DocumentEvent e) {
		dialog.validateInput();
	}

	/**
	 * This method has no effect.
	 */
	public void changedUpdate(DocumentEvent e) {}
}