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

/* Created on 17.05.2005 */
package org.jalgo.module.avl.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.gui.GUIConstants;
import org.jalgo.module.avl.gui.GUIController;
import org.jalgo.module.avl.gui.Settings;
import org.jalgo.module.avl.gui.components.ControlPane;

/**
 * The class <code>ControlActionHandler</code> represents an event handler for
 * the <code>ControlPane</code> class. It handles button clicks, input events
 * on textfields, mouse events for displaying status messages, focus events for
 * marking the content of a textfield and change events for option controls.
 * 
 * @author Alexander Claus
 */
public class ControlActionHandler
implements ActionListener, DocumentListener, MouseListener, GUIConstants,
	ChangeListener, FocusListener {

	private GUIController gui;
	private ControlPane controlPane;
	private Controller controller;

	/**
	 * Contsructs a <code>ControlActionHandler</code> object with the given
	 * references.
	 * 
	 * @param gui the <code>GUIController</code> instance
	 * @param controlPane the <code>ControlPane</code> instance, for which
	 *            events are handled here
	 * @param controller the <code>Controller</code> instance
	 */
	public ControlActionHandler(GUIController gui, ControlPane controlPane,
		Controller controller) {
		this.gui = gui;
		this.controlPane = controlPane;
		this.controller = controller;
	}

	/**
	 * Handles button clicks, starts algorithms.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("randomkey")) //$NON-NLS-1$
			controlPane.setRandomKey();
		else if (e.getActionCommand().equals("search")) { //$NON-NLS-1$
			controller.startSearch(controlPane.getCurrentKey());
			gui.algorithmStarted();
		}
		else if (e.getActionCommand().equals("insert")) { //$NON-NLS-1$
			controller.startInsert(controlPane.getCurrentKey());
			gui.algorithmStarted();
		}
		else if (e.getActionCommand().equals("delete")) { //$NON-NLS-1$
			controller.startRemove(controlPane.getCurrentKey());
			gui.algorithmStarted();
		}
		else if (e.getActionCommand().equals("avltest")) { //$NON-NLS-1$
			gui.algorithmUndone();
			controlPane.validateKey();
			controller.startAVLTest();
			gui.showAVLTestDialog();
		}
		else if (e.getActionCommand().equals("toggleavl")) { //$NON-NLS-1$
			boolean avlMode = ((JCheckBox)e.getSource()).isSelected();
			gui.algorithmAborted();
			controlPane.validateKey();
			controller.putLogDescription(
				Messages.getString("avl", "AVL_mode") + //$NON-NLS-1$ //$NON-NLS-2$
				(avlMode ? Messages.getString("avl", "Switched_on") : //$NON-NLS-1$ //$NON-NLS-2$
					Messages.getString("avl", "Switched_off"))); //$NON-NLS-1$ //$NON-NLS-2$
			gui.setAVLMode(avlMode, true);
		}
	}

	/**
	 * Invoked if input textfield has been edited. Causes to validate the input.
	 */
	public void insertUpdate(DocumentEvent e) {
		controlPane.validateKey();
	}

	/**
	 * Invoked if input textfield has been edited. Causes to validate the input.
	 */
	public void removeUpdate(DocumentEvent e) {
		controlPane.validateKey();
	}

	/**
	 * This method has no effect.
	 */
	public void changedUpdate(DocumentEvent e) {
	// this method has no effect
	}

	/**
	 * This method has no effect.
	 */
	public void mouseClicked(MouseEvent e) {
	// this method has no effect
	}

	/**
	 * This method has no effect.
	 */
	public void mousePressed(MouseEvent e) {
	// this method has no effect
	}

	/**
	 * This method has no effect.
	 */
	public void mouseReleased(MouseEvent e) {
	// this method has no effect
	}

	/**
	 * Causes to display the tooltip text of the event source in the status
	 * line.
	 */
	public void mouseEntered(MouseEvent e) {
		JAlgoGUIConnector.getInstance().setStatusMessage(
			((JComponent)e.getSource()).getToolTipText());
	}

	/**
	 * Causes to remove the message from the status line.
	 */
	public void mouseExited(MouseEvent e) {
		JAlgoGUIConnector.getInstance().setStatusMessage(null);
	}

	/**
	 * Invoked if animation speed control has been changed.
	 */
	public void stateChanged(ChangeEvent e) {
		// TODO: check, what happens if more than one AVL modules are open
		Settings.setStepDelay(((JSlider)e.getSource()).getValue());
	}

	/**
	 * Invoked if textfield is selected. Causes to select the whole input.
	 */
	public void focusGained(FocusEvent e) {
		if (e.getSource() instanceof JTextField)
			((JTextField)e.getSource()).selectAll();
	}

	/**
	 * This method has no effect.
	 */
	public void focusLost(FocusEvent e) {
	// this method has no effect
	}
}