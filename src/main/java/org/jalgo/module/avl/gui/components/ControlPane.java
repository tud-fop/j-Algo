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

/* Created on 19.05.2005 */
package org.jalgo.module.avl.gui.components;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.jalgo.main.util.Messages;
import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.gui.GUIConstants;
import org.jalgo.module.avl.gui.GUIController;
import org.jalgo.module.avl.gui.event.ControlActionHandler;

/**
 * The class <code>ControlPane</code> represents a panel with several control
 * elements for algorithms in the AVL module. Here the user can type in a key,
 * and start an algorithm by selecting one of the buttons. Furthermore he can
 * control the flow of the algorithm and set some options e.g. animation speed.
 * Results of algorithms are displayed in a special field in this panel.
 * 
 * @author Alexander Claus
 */
public class ControlPane
extends JPanel
implements GUIConstants {

	private static final long serialVersionUID = 5167913386704980330L;
	// layout and control components
	private JLabel messageLabel;
	private JTextField keyTextField;
	private JButton randomKeyButton;
	private JButton startSearchButton;
	private JButton startInsertButton;
	private JButton startDeleteButton;
	private JButton startAVLTestButton;
	private JCheckBox avlMode;
	private JSlider animSpeed;

	// helper variables
	private int currentKey;
	private ImageIcon errorIcon;
	private ImageIcon infoIcon;

	/**
	 * Constructs a <code>ControlPane</code> object with the given references.
	 * The layout of the pane is created here.
	 * 
	 * @param gui the module specific <code>GUIController</code>
	 * @param controller the module specific <code>Controller</code>
	 */
	public ControlPane(GUIController gui, Controller controller) {
		ControlActionHandler action = new ControlActionHandler(gui, this,
			controller);

		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createTitledBorder(Messages.getString(
			"avl", "ControlPane.Label"))); //$NON-NLS-1$ //$NON-NLS-2$

		// messagePane contains the error message
		JPanel messagePane = new JPanel();
		messagePane.setLayout(new BoxLayout(messagePane, BoxLayout.LINE_AXIS));
		messageLabel = new JLabel();
		errorIcon = new ImageIcon(Messages.getResourceURL(
			"main", "Icon.Msg_error")); //$NON-NLS-1$ //$NON-NLS-2$
		infoIcon = new ImageIcon(Messages.getResourceURL(
			"main", "Icon.Msg_info")); //$NON-NLS-1$ //$NON-NLS-2$
		messagePane.add(Box.createRigidArea(new Dimension(5,
			errorIcon.getIconHeight() + 4)));
		messagePane.add(messageLabel);
		setMessage(null, NO_MESSAGE);
		messagePane.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(messagePane);

		JPanel keyPane = new JPanel();
		keyPane.setLayout(new BoxLayout(keyPane, BoxLayout.LINE_AXIS));
		JLabel keyLabel = new JLabel(Messages.getString(
			"avl", "ControlPane.Key")); //$NON-NLS-1$ //$NON-NLS-2$
		keyLabel.setAlignmentY(Component.CENTER_ALIGNMENT);
		keyPane.add(keyLabel);
		keyTextField = new JTextField(2);
		keyTextField.setAlignmentY(Component.CENTER_ALIGNMENT);
		keyTextField.setMaximumSize(new Dimension(
			keyTextField.getMaximumSize().width,
			keyTextField.getMinimumSize().height));
		// watch key input, show error messages if necessary (key must be
		// integer between 0 and MAX_KEY)
		keyTextField.getDocument().addDocumentListener(action);
		keyTextField.setToolTipText(Messages.getString(
			"avl", "ControlPane.Insert_key")); //$NON-NLS-1$ //$NON-NLS-2$
		keyTextField.addMouseListener(action);
		keyTextField.addFocusListener(action);
		keyPane.add(keyTextField);

		randomKeyButton = new JButton(Messages.getString(
			"avl", "ControlPane.Random")); //$NON-NLS-1$ //$NON-NLS-2$
		randomKeyButton.setActionCommand("randomkey"); //$NON-NLS-1$
		randomKeyButton.setToolTipText(Messages.getString(
			"avl", "ControlPane.Random_tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		randomKeyButton.addActionListener(action);
		randomKeyButton.addMouseListener(action);
		randomKeyButton.setMnemonic('r');
		randomKeyButton.setAlignmentY(Component.CENTER_ALIGNMENT);
		keyPane.add(randomKeyButton);
		keyPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(keyPane);

		add(Box.createVerticalStrut(8));

		JLabel algSelectLabel = new JLabel(Messages.getString(
			"avl", "ControlPane.Choose_algorithm")); //$NON-NLS-1$ //$NON-NLS-2$
		algSelectLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(algSelectLabel);

		add(Box.createVerticalStrut(4));

		// buttonPane contains the buttons for selecting an algorithm
		JPanel buttonPane1 = new JPanel();
		buttonPane1.setLayout(new BoxLayout(buttonPane1, BoxLayout.LINE_AXIS));

		startSearchButton = new JButton(Messages.getString(
			"avl", "Alg_name.Search")); //$NON-NLS-1$ //$NON-NLS-2$
		startSearchButton.setActionCommand("search"); //$NON-NLS-1$
		startSearchButton.setToolTipText(Messages.getString(
			"avl", "ControlPane.Search_tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		startSearchButton.addActionListener(action);
		startSearchButton.addMouseListener(action);
		startSearchButton.setMnemonic('s');
		buttonPane1.add(startSearchButton);

		startInsertButton = new JButton(Messages.getString(
			"avl", "Alg_name.Insert")); //$NON-NLS-1$ //$NON-NLS-2$
		startInsertButton.setActionCommand("insert"); //$NON-NLS-1$
		startInsertButton.setToolTipText(Messages.getString(
			"avl", "ControlPane.Insert_tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		startInsertButton.addActionListener(action);
		startInsertButton.addMouseListener(action);
		startInsertButton.setMnemonic('i');
		buttonPane1.add(startInsertButton);

		startDeleteButton = new JButton(Messages.getString(
			"avl", "Alg_name.Remove")); //$NON-NLS-1$ //$NON-NLS-2$
		startDeleteButton.setActionCommand("delete"); //$NON-NLS-1$
		startDeleteButton.setToolTipText(Messages.getString(
			"avl", "ControlPane.Remove_tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		startDeleteButton.addActionListener(action);
		startDeleteButton.addMouseListener(action);

		startDeleteButton.setMnemonic('l');
		// currently not used
		startDeleteButton.setEnabled(false);
		buttonPane1.add(startDeleteButton);

		buttonPane1.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(buttonPane1);

		add(Box.createVerticalStrut(4));

		JPanel buttonPane2 = new JPanel();
		buttonPane2.setLayout(new BoxLayout(buttonPane2, BoxLayout.LINE_AXIS));

		avlMode = new JCheckBox(Messages.getString(
			"avl", "ControlPane.AVL_property")); //$NON-NLS-1$ //$NON-NLS-2$
		avlMode.setSelected(true);
		avlMode.setActionCommand("toggleavl"); //$NON-NLS-1$
		avlMode.addActionListener(action);
		buttonPane2.add(avlMode);
		buttonPane2.add(Box.createHorizontalGlue());

		startAVLTestButton = new JButton(Messages.getString(
			"avl", "Alg_name.AVL_test")); //$NON-NLS-1$ //$NON-NLS-2$
		startAVLTestButton.setActionCommand("avltest"); //$NON-NLS-1$
		startAVLTestButton.setToolTipText(Messages.getString(
			"avl", "ControlPane.AVL_test_tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		startAVLTestButton.addActionListener(action);
		startAVLTestButton.addMouseListener(action);
		startAVLTestButton.setMnemonic('t');
		buttonPane2.add(startAVLTestButton);

		buttonPane2.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(buttonPane2);

		setAlgorithmButtonsEnabled(false);

		add(Box.createVerticalStrut(8));

		JLabel flowControlLabel = new JLabel(Messages.getString(
			"avl", "ControlPane.Flow_control")); //$NON-NLS-1$ //$NON-NLS-2$
		flowControlLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(flowControlLabel);
		add(Box.createVerticalStrut(4));

		JToolBar flowControl = new JToolBar();
		flowControl.setFloatable(false);
		flowControl.setRollover(true);
		flowControl.setAlignmentX(Component.CENTER_ALIGNMENT);

		JButton[] flowControlButtons = gui.getFlowControlButtons();
		for (JButton button : flowControlButtons) {
			button.addMouseListener(action);
			flowControl.add(button);
		}

		JPanel flowControlPane = new JPanel();
		flowControlPane.setLayout(new BoxLayout(flowControlPane,
			BoxLayout.PAGE_AXIS));
		flowControlPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		flowControlPane.add(Box.createHorizontalGlue());
		flowControlPane.add(flowControl);
		flowControlPane.add(Box.createHorizontalGlue());
		add(flowControlPane);

		add(Box.createVerticalStrut(8));

		JLabel animSpeedLabel = new JLabel(Messages.getString(
			"avl", "ControlPane.Anim_speed")); //$NON-NLS-1$ //$NON-NLS-2$
		animSpeedLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
		add(animSpeedLabel);

		animSpeed = new JSlider(200, 2000, 1100);
		animSpeed.setInverted(true);
		animSpeed.setMinorTickSpacing(100);
		animSpeed.setPaintTicks(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(200, new JLabel(Messages.getString(
			"avl", "ControlPane.fast"))); //$NON-NLS-1$ //$NON-NLS-2$
		labelTable.put(2000, new JLabel(Messages.getString(
			"avl", "ControlPane.slow"))); //$NON-NLS-1$ //$NON-NLS-2$
		animSpeed.setLabelTable(labelTable);
		animSpeed.setPaintLabels(true);
		animSpeed.addChangeListener(action);
		animSpeed.setToolTipText(Messages.getString(
			"avl", "ControlPane.Anim_speed_tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		animSpeed.addMouseListener(action);
		animSpeed.setAlignmentX(Component.LEFT_ALIGNMENT);
		animSpeed.setEnabled(false);
		add(animSpeed);
	}

	/**
	 * Puts a random integer value in the acceptable range to the key textfield.
	 * This range is defined in <code>Constants</code>.
	 */
	public void setRandomKey() {
		keyTextField.setText(
			Integer.toString((int)(Math.random() * MAX_KEY + MIN_KEY)));
	}

	/**
	 * Checks the current input in the key text field, if a valid key is typed
	 * in. Returns an identifier, which determines the validity of the input.
	 * Furthermore an error message is displayed if the input is not a valid
	 * key. The constants used for this are implemented in
	 * <code>GUIConstants</code>.
	 * 
	 * @return <code>VALID_INPUT</code> - if the input is a valid key <br>
	 *         <code>INPUT_EMPTY</code> - if the input is empty <br>
	 *         <code>NO_INTEGER</code> - if the input contains nonnumber
	 *         characters <br>
	 *         <code>NOT_IN_RANGE</code> - if the input key is out of the
	 *         acceptable range
	 */
	public int validateKey() {
		int returnCode;
		try {
			currentKey = Integer.parseInt(keyTextField.getText());
			if (currentKey > MAX_KEY || currentKey < MIN_KEY)
				returnCode = NOT_IN_RANGE;
			else returnCode = VALID_INPUT;
		}
		catch (NumberFormatException ex) {
			if (keyTextField.getText().length() == 0) returnCode = INPUT_EMPTY;
			else returnCode = NO_INTEGER;
		}

		switch (returnCode) {
			case VALID_INPUT:
				setMessage(null, NO_MESSAGE);
				setAlgorithmButtonsEnabled(true);
				break;
			case NO_INTEGER:
				setMessage(Messages.getString(
					"avl", "Warning_only_integers"), //$NON-NLS-1$ //$NON-NLS-2$
					 ERROR_MESSAGE);
				setAlgorithmButtonsEnabled(false);
				break;
			case NOT_IN_RANGE:
				setMessage(
					Messages.getString(
						"avl", "Warning_only_values_from") + //$NON-NLS-1$ //$NON-NLS-2$
					MIN_KEY +
					Messages.getString(
						"avl", "Warning_to") + //$NON-NLS-1$ //$NON-NLS-2$
					MAX_KEY +
					Messages.getString(
						"avl", "Warning_valid"), //$NON-NLS-1$ //$NON-NLS-2$
					ERROR_MESSAGE);
				setAlgorithmButtonsEnabled(false);
				break;
			case INPUT_EMPTY:
				setMessage(null, NO_MESSAGE);
				setAlgorithmButtonsEnabled(false);
		}
		startAVLTestButton.setEnabled(!avlMode.isSelected());

		return returnCode;
	}

	/**
	 * Returns the value of the key textfield as an integer. Attention! Ensure
	 * to have <code>validateKey()</code> invoked before this method is
	 * called.
	 * 
	 * @return the current key input
	 */
	public int getCurrentKey() {
		return currentKey;
	}

	/**
	 * Displays an error message with an icon and the given string to the user.
	 * 
	 * @param msg the error message
	 * @param msgType one of the following integers defined in
	 *            <code>GUIConstants</code>:<br>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;<code>NO_MESSAGE</code><br>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;<code>INFORMATION_MESSAGE</code><br>
	 *            &nbsp;&nbsp;&nbsp;&nbsp;<code>ERROR_MESSAGE</code>
	 */
	public void setMessage(String msg, int msgType) {
		switch (msgType) {
			case NO_MESSAGE:
				messageLabel.setIcon(null);
				break;
			case INFORMATION_MESSAGE:
				messageLabel.setIcon(infoIcon);
				break;
			case ERROR_MESSAGE:
				messageLabel.setIcon(errorIcon);
				break;
		}
		messageLabel.setText(msg);
		validate();
	}

	/**
	 * Sets the enable status of the algorithm selection buttons in standard
	 * layout.
	 * 
	 * @param b <code>true</code>, if buttons should be enabled,
	 *            <code>false</code> otherwise
	 */
	public void setAlgorithmButtonsEnabled(boolean b) {
		startSearchButton.setEnabled(b);
		startInsertButton.setEnabled(b);
		startDeleteButton.setEnabled(b);
		startAVLTestButton.setEnabled(b && !avlMode.isSelected());
	}

	/**
	 * Sets the enable status of the key input area in standard layout.
	 * 
	 * @param b <code>true</code>, if input should be enabled,
	 *            <code>false</code> otherwise
	 */
	public void setKeyInputEnabled(boolean b) {
		keyTextField.setEnabled(b);
		randomKeyButton.setEnabled(b);
	}

	/**
	 * Sets the enable status of the AVL mode checkbox.
	 * 
	 * @param b <code>true</code>, if AVL mode checkbox should be enabled,
	 *            <code>false</code> otherwise
	 */
	public void setAVLToggleEnabled(boolean b) {
		avlMode.setEnabled(b);
	}

	/**
	 * Retrieves the enable status of the AVL mode checkbox.
	 * 
	 * @return <code>true</code>, if checkbox is enabled, <code>false</code>
	 *         otherwise
	 */
	public boolean isAVLToggleEnabled() {
		return avlMode.isEnabled();
	}

	/**
	 * Sets the selected state of the AVL mode checkbox.
	 * 
	 * @param b <code>true</code>, if checkbox should be selected,
	 *            <code>false</code> otherwise
	 */
	public void setAVLToggleSelected(boolean b) {
		avlMode.setSelected(b);
	}

	/**
	 * Sets the enable status of the AVL-Test button.
	 * 
	 * @param b <code>true</code>, if the button should be enabled,
	 *            <code>false</code> otherwise
	 */
	public void setAVLTestEnabled(boolean b) {
		startAVLTestButton.setEnabled(b);
	}

	/**
	 * Sets the enable status of the animation speed control.
	 * 
	 * @param b <code>true</code>, if the control should be enabled,
	 *            <code>false</code> otherwise
	 */
	public void setAnimSpeedEnabled(boolean b) {
		animSpeed.setEnabled(b);
	}

	/**
	 * Restores the initial state of the <code>ControlPane</code>.
	 */
	public void reset() {
		// Bugfix: the enable status of the input text field has to be changed
		// before setting text, otherwise the program sometimes hangs on
		// (type in a value, have a tree, press clear tree)
		setKeyInputEnabled(false);
		setKeyInputEnabled(true);

		keyTextField.setText(null);
		setAlgorithmButtonsEnabled(false);
		startAVLTestButton.setEnabled(!avlMode.isSelected());
		setMessage(null, NO_MESSAGE);
	}
}