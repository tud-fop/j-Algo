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

/* Created on 20.05.2005 */
package org.jalgo.module.avl.gui.components;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.ModuleConnector;
import org.jalgo.module.avl.gui.GUIConstants;
import org.jalgo.module.avl.gui.GUIController;
import org.jalgo.module.avl.gui.event.RandomGenerationDialogActionHandler;

/**
 * The class <code>RandomGenerationDialog</code> represents a dialog for
 * creating a random tree. It provides the setting of the number of nodes, the
 * AVL property and the type of visualization. The three available visualization
 * types are defined as constants in <code>GUIConstants</code>.<br>
 * The input of the number of nodes is validated on the fly and appropriate
 * messages are displayed to the user, if the input is not valid.<br>
 * To show the dialog, call <code>open()</code>, all settings are reset
 * before.
 * 
 * @author Alexander Claus
 */
public class RandomGenerationDialog
extends JDialog
implements GUIConstants {

	private static final long serialVersionUID = 4752493355110066765L;
	private Frame parent;
	private RandomGenerationDialogActionHandler action;

	private JLabel errorIcon;
	private JLabel errorMessage;
	private JTextField textField;
	private JCheckBox avlProperty;
	private JRadioButton noVisualisation;
	private JRadioButton stepWise;
	private JRadioButton automatical;
	private JButton ok;
	private JButton cancel;
	private int currentInput;

	/**
	 * Constructs a <code>RandomGenerationDialog</code> object with the given
	 * references. The dialog is only created here, to bring it to the screen,
	 * call <code>open()</code>.
	 * 
	 * @param gui the <code>GUIController</code> instance of the AVL module
	 * @param parent the parent component to locate correctly on the screen
	 * @param controller the <code>Controller</code> instance of the AVL
	 *            module
	 */
	public RandomGenerationDialog(GUIController gui, Frame parent,
		Controller controller, ModuleConnector connector) {
		super(parent, Messages.getString(
			"avl", "RGD.Generate_random_tree"), true); //$NON-NLS-1$ //$NON-NLS-2$

		this.parent = parent;
		action = new RandomGenerationDialogActionHandler(controller, gui, this,
			connector);

		GridBagLayout gbl = new GridBagLayout();
		setLayout(gbl);

		// messagePane contains the error message
		JPanel messagePane = new JPanel(new BorderLayout(4, 4));
		errorIcon = new JLabel(new ImageIcon(Messages.getResourceURL(
			"main", "Icon.Msg_error"))); //$NON-NLS-1$ //$NON-NLS-2$
		errorMessage = new JLabel();
		messagePane.add(errorIcon, BorderLayout.WEST);
		messagePane.add(errorMessage, BorderLayout.CENTER);
		messagePane.add(Box.createVerticalStrut(30), BorderLayout.EAST);
		setErrorMessage(null);
		GUIController.setGBC(messagePane, gbl, 0, 0, 3, 1,
			GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL);
		add(messagePane);

		JLabel countLabel = new JLabel(Messages.getString(
			"avl", "RGD.Node_count")); //$NON-NLS-1$ //$NON-NLS-2$
		GUIController.setGBC(countLabel, gbl, 0, 1, 1, 1,
			GridBagConstraints.WEST, GridBagConstraints.NONE);
		add(countLabel);
		textField = new JTextField();
		textField.getDocument().addDocumentListener(action);

		GUIController.setGBC(textField, gbl, 2, 1, 1, 1,
			GridBagConstraints.EAST, GridBagConstraints.HORIZONTAL);
		add(textField);
		JLabel avlLabel = new JLabel(Messages.getString(
			"avl", "RGD.AVL_property")); //$NON-NLS-1$ //$NON-NLS-2$
		GUIController.setGBC(avlLabel, gbl, 0, 2, 1, 1,
			GridBagConstraints.WEST, GridBagConstraints.NONE);
		add(avlLabel);
		avlProperty = new JCheckBox();
		GUIController.setGBC(avlProperty, gbl, 2, 2, 1, 1,
			GridBagConstraints.WEST, GridBagConstraints.NONE);
		add(avlProperty);

		JLabel visualizeLabel = new JLabel(Messages.getString(
			"avl", "RGD.Visualization")); //$NON-NLS-1$ //$NON-NLS-2$
		GUIController.setGBC(visualizeLabel, gbl, 0, 3, 1, 1,
			GridBagConstraints.WEST, GridBagConstraints.NONE);
		add(visualizeLabel);
		ButtonGroup visualization = new ButtonGroup();
		noVisualisation = new JRadioButton(Messages.getString(
			"avl", "RGD.No_visualization")); //$NON-NLS-1$ //$NON-NLS-2$
		visualization.add(noVisualisation);
		GUIController.setGBC(noVisualisation, gbl, 2, 3, 1, 1,
			GridBagConstraints.WEST, GridBagConstraints.NONE);
		add(noVisualisation);
		stepWise = new JRadioButton(Messages.getString(
			"avl", "RGD.Stepwise")); //$NON-NLS-1$ //$NON-NLS-2$
		visualization.add(stepWise);
		GUIController.setGBC(stepWise, gbl, 2, 4, 1, 1,
			GridBagConstraints.WEST, GridBagConstraints.NONE);
		add(stepWise);
		automatical = new JRadioButton(Messages.getString(
			"avl", "RGD.Automatical")); //$NON-NLS-1$ //$NON-NLS-2$
		visualization.add(automatical);
		GUIController.setGBC(automatical, gbl, 2, 5, 1, 1,
			GridBagConstraints.WEST, GridBagConstraints.NONE);
		add(automatical);

		ok = new JButton(Messages.getString(
			"avl", "RGD.Button_ok")); //$NON-NLS-1$ //$NON-NLS-2$
		ok.setActionCommand("ok"); //$NON-NLS-1$
		ok.addActionListener(action);
		ok.setDefaultCapable(true);
		GUIController.setGBC(ok, gbl, 0, 6, 2, 1, GridBagConstraints.EAST,
			GridBagConstraints.NONE);
		add(ok);

		cancel = new JButton(Messages.getString(
			"avl", "RGD.Button_cancel")); //$NON-NLS-1$ //$NON-NLS-2$
		cancel.setActionCommand("cancel"); //$NON-NLS-1$
		cancel.addActionListener(action);
		GUIController.setGBC(cancel, gbl, 2, 6, 1, 1,
			GridBagConstraints.CENTER, GridBagConstraints.NONE);
		add(cancel);

		pack();
	}

	/**
	 * Retrieves the selection status of the AVL-mode-checkbox.
	 * 
	 * @return <code>true</code>, if AVL mode is selected, <code>false</code>
	 *         otherwise
	 */
	public boolean isAVLSelected() {
		return avlProperty.isSelected();
	}

	/**
	 * Sets the enabled status of the OK button.
	 * 
	 * @param b <code>true</code>, if the button should be enabled,
	 *            <code>false</code> otherwise
	 */
	public void setOKEnabled(boolean b) {
		ok.setEnabled(b);
	}

	/**
	 * Validates the input string to be a valid integer in the acceptable range
	 * defined in <code>Constants</code>. Returns an identifier, which
	 * determines the validity of the input. Furthermore an error message is
	 * displayed if the input is not a valid key. The constants used for this
	 * are implemented in <code>GUIConstants</code>.
	 * 
	 * @return <code>VALID_INPUT</code> - if the input is a valid key <br>
	 *         <code>INPUT_EMPTY</code> - if the input is empty <br>
	 *         <code>NO_INTEGER</code> - if the input contains nonnumber
	 *         characters <br>
	 *         <code>NOT_IN_RANGE</code> - if the input key is out of the
	 *         acceptable range
	 */
	public int validateInput() {
		int returnCode;
		try {
			currentInput = Integer.parseInt(textField.getText());
			if (currentInput > MAX_KEY || currentInput < MIN_KEY)
				returnCode = NOT_IN_RANGE;
			else returnCode = VALID_INPUT;
		}
		catch (NumberFormatException ex) {
			if (textField.getText().length() == 0) returnCode = INPUT_EMPTY;
			else returnCode = NO_INTEGER;
		}
		switch (returnCode) {
			case VALID_INPUT:
				setErrorMessage(null);
				setOKEnabled(true);
				break;
			case NO_INTEGER:
				setErrorMessage(Messages.getString(
					"avl", "Warning_only_integers")); //$NON-NLS-1$ //$NON-NLS-2$
				setOKEnabled(false);
				break;
			case NOT_IN_RANGE:
				setErrorMessage(Messages.getString(
					"avl", "Warning_only_values_from") + //$NON-NLS-1$ //$NON-NLS-2$
					MIN_KEY +
					Messages.getString("avl", "Warning_to") + //$NON-NLS-1$ //$NON-NLS-2$
					MAX_KEY +
					Messages.getString("avl", "Warning_valid")); //$NON-NLS-1$ //$NON-NLS-2$
				setOKEnabled(false);
				break;
			case INPUT_EMPTY:
				setErrorMessage(null);
				setOKEnabled(false);
		}
		return returnCode;
	}

	/**
	 * Displays an error message with an icon and the given string to the user.
	 * 
	 * @param msg the error message
	 */
	public void setErrorMessage(String msg) {
		if (msg == null) {
			errorIcon.setVisible(false);
			errorMessage.setText(""); //$NON-NLS-1$
		}
		else {
			errorIcon.setVisible(true);
			errorMessage.setText(msg);
		}
		validate();
	}

	/**
	 * Resets the settings to the initial state.
	 */
	private void reset() {
		currentInput = 10;
		textField.setText(String.valueOf(currentInput));
		avlProperty.setSelected(false);
		noVisualisation.setSelected(true);
	}

	/**
	 * Brings the dialog to the screen. First <code>reset()</code> is called.
	 */
	public void open() {
		reset();
		setLocationRelativeTo(parent);
		getRootPane().setDefaultButton(ok);
		setVisible(true);
	}

	/**
	 * Retrieves the number of nodes the tree should consist of.
	 * 
	 * @return the number of nodes
	 */
	public int getNodeCount() {
		return currentInput;
	}

	/**
	 * Retrieves the selected visualization mode. The constants for this are
	 * defined in <code>GUIConstants</code>.
	 * 
	 * @return <ul>
	 * <li><code>NO_VISUALIZATION</code> - if the tree should be generated in
	 * background</li>
	 * <li><code>STEPWISE</code> - if the generation should be confirmed
	 * stepwise by the user</li>
	 * <li><code>AUTOMATICAL</code> - if the generation of the tree should
	 * run as an animation</li>
	 */
	public int getVisualizationMode() {
		if (noVisualisation.isSelected()) return GUIConstants.NO_VISUALIZATION;
		if (stepWise.isSelected()) return GUIConstants.STEPWISE;
		return GUIConstants.AUTOMATICAL;
	}
}