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

/* Created on 26.05.2005 */
package org.jalgo.module.avl.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.ModuleConnector;
import org.jalgo.module.avl.NoActionException;
import org.jalgo.module.avl.gui.GUIConstants;
import org.jalgo.module.avl.gui.GUIController;
import org.jalgo.module.avl.gui.components.RandomGenerationDialog;
import org.jalgo.module.avl.gui.graphics.RandomTreeAnimator;

/**
 * The class <code>RandomGenerationDialogActionHandler</code> represents an
 * event handler for the <code>RandomGenerationDialog</code> class. It handles
 * button clicks and key inputs.
 * 
 * @author Alexander Claus
 */
public class RandomGenerationDialogActionHandler
implements ActionListener, DocumentListener, GUIConstants {

	private Controller controller;
	private GUIController gui;
	private RandomGenerationDialog dialog;
	private ModuleConnector connector;

	/**
	 * Constructs a <code>RandomGenerationDialogActionHandler</code> object
	 * with the given references.
	 * 
	 * @param controller the <code>Controller</code> instance of the AVL
	 *            module
	 * @param gui the <code>GUIController</code> instance of the AVL module
	 * @param dialog the <code>RandomGenerationDialog</code> instance, for
	 *            which events are handled here
	 */
	public RandomGenerationDialogActionHandler(Controller controller,
		GUIController gui, RandomGenerationDialog dialog,
		ModuleConnector connector) {
		this.controller = controller;
		this.gui = gui;
		this.dialog = dialog;
		this.connector = connector;
	}

	/**
	 * Handles button clicks, starts the algorithm with choosen parameters.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("ok")) { //$NON-NLS-1$
			gui.setAVLMode(dialog.isAVLSelected(), false);
			controller.createRandomTree(dialog.getNodeCount());

			switch (dialog.getVisualizationMode()) {
				case NO_VISUALIZATION:
					try {
						controller.finish();
					}
					catch (NoActionException ex) {
						JAlgoGUIConnector.getInstance().showErrorMessage(
							ex.getMessage());
					}
					gui.installStandardLayout();
					connector.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
					// use alg.Finished() here, if stepwise backwards should be
					// enabled
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
	public void changedUpdate(DocumentEvent e) {
	// this method has no effect
	}
}