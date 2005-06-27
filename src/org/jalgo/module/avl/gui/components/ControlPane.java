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

/* Created on 19.05.2005 */
package org.jalgo.module.avl.gui.components;

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

import org.jalgo.module.avl.Controller;
import org.jalgo.module.avl.gui.GUIConstants;
import org.jalgo.module.avl.gui.GUIController;
import org.jalgo.module.avl.gui.event.ControlActionHandler;

/**
 * The class <code>ControlPane</code> represents a panel with several control
 * elements for algorithms in the AVL module.
 * Here the user can type in a key, and start an algorithm by selecting one of
 * the buttons. Furthermore he can control the flow of the algorithm and set
 * some options e.g. animation speed.
 * Results of algorithms are displayed in a special field in this panel.
 * 
 * @author Alexander Claus
 */
public class ControlPane
extends JPanel
implements GUIConstants {
	
	//layout and control components
	private JLabel messageLabel;
	private JTextField keyTextField;
	private JButton randomKeyButton;
	private JButton startSearchButton;
	private JButton startInsertButton;
	private JButton startDeleteButton;
	private JButton startAVLTestButton;
	private JCheckBox avlMode;
	private JSlider animSpeed;

	//helper variables
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
		ControlActionHandler action = new ControlActionHandler(
			gui, this, controller);
		
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createTitledBorder("Kontroll-Bereich"));			

		// messagePane contains the error message
		JPanel messagePane = new JPanel();
		messagePane.setLayout(new BoxLayout(messagePane, BoxLayout.LINE_AXIS));
		messageLabel = new JLabel();
		errorIcon = new ImageIcon("pix/avl/msg_error.gif");
		infoIcon = new ImageIcon("pix/avl/msg_info.gif");
//TODO: enable this, when switching to plugin structure
//		errorIcon = new ImageIcon(getClass().getResource("/pix/avl/msg_error.gif"));
//		infoIcon = new ImageIcon(getClass().getResource("/pix/avl/msg_info.gif"));
		messagePane.add(Box.createRigidArea(new Dimension(
			5, errorIcon.getIconHeight()+4)));
		messagePane.add(messageLabel);
		setMessage(null, NO_MESSAGE);
		messagePane.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		add(messagePane);
		
		JPanel keyPane = new JPanel();
		keyPane.setLayout(new BoxLayout(keyPane, BoxLayout.LINE_AXIS));
		JLabel keyLabel = new JLabel("Schlüssel:");
		keyLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		keyPane.add(keyLabel);
		keyTextField = new JTextField(2);
		keyTextField.setAlignmentY(JTextField.CENTER_ALIGNMENT);
		keyTextField.setMaximumSize(new Dimension(
			keyTextField.getMaximumSize().width,
			keyTextField.getMinimumSize().height));
		// watch key input, show error messages if necessary (key must be
		// integer between 0 and MAX_KEY)
		keyTextField.getDocument().addDocumentListener(action);
		keyTextField.setToolTipText("Geben Sie hier den Schlüssel ein");
		keyTextField.addMouseListener(action);
		keyTextField.addFocusListener(action);
		keyPane.add(keyTextField);

		randomKeyButton = new JButton("Zufallswert");
		randomKeyButton.setActionCommand("randomkey");
		randomKeyButton.setToolTipText("Erzeugt einen Zufallswert");
		randomKeyButton.addActionListener(action);
		randomKeyButton.addMouseListener(action);
		randomKeyButton.setMnemonic('r');
		randomKeyButton.setAlignmentY(JButton.CENTER_ALIGNMENT);
		keyPane.add(randomKeyButton);
		keyPane.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		add(keyPane);

		add(Box.createVerticalStrut(8));

		JLabel algSelectLabel = new JLabel("Auswahl eines Algorithmus:");
		algSelectLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		add(algSelectLabel);

		add(Box.createVerticalStrut(4));

		// buttonPane contains the buttons for selecting an algorithm
		JPanel buttonPane1 = new JPanel();
		buttonPane1.setLayout(new BoxLayout(buttonPane1, BoxLayout.LINE_AXIS));

		startSearchButton = new JButton("Suchen");
		startSearchButton.setActionCommand("search");
		startSearchButton.setToolTipText(
			"Startet den Suchen-Algorithmus mit dem gegebenen Schlüssel");
		startSearchButton.addActionListener(action);
		startSearchButton.addMouseListener(action);
		startSearchButton.setMnemonic('s');
		buttonPane1.add(startSearchButton);

		startInsertButton = new JButton("Einfügen");
		startInsertButton.setActionCommand("insert");
		startInsertButton.setToolTipText(
			"Startet den Einfüge-Algorithmus mit dem gegebenen Schlüssel");
		startInsertButton.addActionListener(action);
		startInsertButton.addMouseListener(action);
		startInsertButton.setMnemonic('i');
		buttonPane1.add(startInsertButton);

		startDeleteButton = new JButton("Löschen");
		startDeleteButton.setActionCommand("delete");
		startDeleteButton.setToolTipText(
			"Startet den Löschen-Algorithmus mit dem gegebenen Schlüssel");
		startDeleteButton.addActionListener(action);
		startDeleteButton.addMouseListener(action);
		
		startDeleteButton.setMnemonic('l');
		//currently not used
		startDeleteButton.setEnabled(false);
		buttonPane1.add(startDeleteButton);

		buttonPane1.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		add(buttonPane1);

		add(Box.createVerticalStrut(4));

		JPanel buttonPane2 = new JPanel();
		buttonPane2.setLayout(new BoxLayout(buttonPane2, BoxLayout.LINE_AXIS));

		avlMode = new JCheckBox("AVL - Modus");
		avlMode.setSelected(true);
		avlMode.setActionCommand("toggleavl");
		avlMode.addActionListener(action);
		buttonPane2.add(avlMode);
		buttonPane2.add(Box.createHorizontalGlue());

		startAVLTestButton = new JButton("AVL-Test");
		startAVLTestButton.setActionCommand("avltest");
		startAVLTestButton.setToolTipText(
			"Testet den aktuellen Baum auf die AVL-Eigenschaft");
		startAVLTestButton.addActionListener(action);
		startAVLTestButton.addMouseListener(action);
		startAVLTestButton.setMnemonic('t');
		buttonPane2.add(startAVLTestButton);

		buttonPane2.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		add(buttonPane2);

		setAlgorithmButtonsEnabled(false);

		add(Box.createVerticalStrut(8));

		JLabel flowControlLabel = new JLabel("Algorithmussteuerung:");
		flowControlLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		add(flowControlLabel);
		add(Box.createVerticalStrut(4));

		JToolBar flowControl = new JToolBar();
		flowControl.setFloatable(false);
		flowControl.setRollover(true);
		flowControl.setAlignmentX(JToolBar.CENTER_ALIGNMENT);

		JButton[] flowControlButtons  = gui.getFlowControlButtons();
		for (JButton button : flowControlButtons) {
			button.addMouseListener(action);
			flowControl.add(button);
		}

		JPanel flowControlPane = new JPanel();
		flowControlPane.setLayout(
			new BoxLayout(flowControlPane, BoxLayout.PAGE_AXIS));
		flowControlPane.setAlignmentX(JPanel.LEFT_ALIGNMENT);
		flowControlPane.add(Box.createHorizontalGlue());
		flowControlPane.add(flowControl);
		flowControlPane.add(Box.createHorizontalGlue());
		add(flowControlPane);

		add(Box.createVerticalStrut(8));

		JLabel animSpeedLabel = new JLabel("Animationsgeschwindigkeit:");
		animSpeedLabel.setAlignmentX(JLabel.LEFT_ALIGNMENT);
		add(animSpeedLabel);

		animSpeed = new JSlider(200, 1400, 800);
		animSpeed.setInverted(true);
		animSpeed.setMinorTickSpacing(100);
		animSpeed.setPaintTicks(true);
		Hashtable<Integer, JLabel> labelTable = new Hashtable<Integer, JLabel>();
		labelTable.put(200, new JLabel("schnell"));
		labelTable.put(1400, new JLabel("langsam"));
		animSpeed.setLabelTable(labelTable);
		animSpeed.setPaintLabels(true);
		animSpeed.addChangeListener(action);
		animSpeed.setToolTipText("Stellt die Geschwindigkeit der Animation ein");
		animSpeed.addMouseListener(action);
		animSpeed.setAlignmentX(JSlider.LEFT_ALIGNMENT);
		animSpeed.setEnabled(false);
		add(animSpeed);
	}

	/**
	 * Puts a random integer value in the acceptable range to the key textfield.
	 * This range is defined in <code>Constants</code>.
	 */
	public void setRandomKey() {
		keyTextField.setText(
			Integer.toString((int)(Math.random()*MAX_KEY+MIN_KEY)));
	}

	/**
	 * Checks the current input in the key text field, if a valid key is typed in.
	 * Returns an identifier, which determines the validity of the input.
	 * Furthermore an error message is displayed if the input is not a valid key.
	 * The constants used for this are implemented in <code>GUIConstants</code>.
	 * 
	 * @return <code>VALID_INPUT</code> - if the input is a valid key <br>
	 *         <code>INPUT_EMPTY</code> - if the input is empty <br>
	 *         <code>NO_INTEGER</code> - if the input contains nonnumber
	 *         								characters <br>
	 *         <code>NOT_IN_RANGE</code> - if the input key is out of the
	 *         								acceptable range
	 */
	public int validateKey() {
		int returnCode;
		try {
			currentKey = Integer.parseInt(keyTextField.getText());
			if (currentKey > MAX_KEY ||
				currentKey < MIN_KEY) returnCode = NOT_IN_RANGE;
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
			setMessage("Nur ganze Zahlen als Schlüssel gültig!", ERROR_MESSAGE);
			setAlgorithmButtonsEnabled(false);
			break;
		case NOT_IN_RANGE:
			setMessage("Nur Schlüssel von "+MIN_KEY+" bis "+MAX_KEY+" gültig!",
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
	 * Returns the value of the key textfield as an integer.
	 * Attention! Ensure to have <code>validateKey()</code> invoked
	 * before this method is called.
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
	 * 					<code>GUIConstants</code>:<br>
	 * 					&nbsp;&nbsp;&nbsp;&nbsp;<code>NO_MESSAGE</code><br>
	 * 					&nbsp;&nbsp;&nbsp;&nbsp;<code>INFORMATION_MESSAGE</code><br>
	 * 					&nbsp;&nbsp;&nbsp;&nbsp;<code>ERROR_MESSAGE</code>
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
	 * Sets the enable status of the algorithm selection buttons in standard layout.
	 * 
	 * @param b <code>true</code>, if buttons should be enabled,
	 * 			<code>false</code> otherwise
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
	 * 			<code>false</code> otherwise
	 */
	public void setKeyInputEnabled(boolean b) {
		keyTextField.setEnabled(b);
		randomKeyButton.setEnabled(b);
	}	

	/**
	 * Sets the enable status of the AVL mode checkbox.
	 * 
	 * @param b <code>true</code>, if AVL mode checkbox should be enabled,
	 * 			<code>false</code> otherwise
	 */
	public void setAVLToggleEnabled(boolean b) {
		avlMode.setEnabled(b);
	}

	/**
	 * Retrieves the enable status of the AVL mode checkbox.
	 * 
	 * @return <code>true</code>, if checkbox is enabled,
	 * 			<code>false</code> otherwise
	 */
	public boolean isAVLToggleEnabled() {
		return avlMode.isEnabled();
	}

	/**
	 * Sets the selected state of the AVL mode checkbox.
	 * 
	 * @param b <code>true</code>, if checkbox should be selected,
	 * 			<code>false</code> otherwise
	 */
	public void setAVLToggleSelected(boolean b) {
		avlMode.setSelected(b);
	}

	/**
	 * Sets the enable status of the AVL-Test button.
	 * 
	 * @param b <code>true</code>, if the button should be enabled,
	 * 			<code>false</code> otherwise
	 */
	public void setAVLTestEnabled(boolean b) {
		startAVLTestButton.setEnabled(b);
	}

	/**
	 * Sets the enable status of the animation speed control.
	 * 
	 * @param b <code>true</code>, if the control should be enabled,
	 * 			<code>false</code> otherwise
	 */
	public void setAnimSpeedEnabled(boolean b) {
		animSpeed.setEnabled(b);
	}

	/**
	 * Restores the initial state of the <code>ControlPane</code>.
	 */
	public void reset() {
		//Bugfix: the enable status of the input text field has to be changed
		//before setting text, otherwise the program sometimes hangs on
		//(type in a value, have a tree, press clear tree)
		setKeyInputEnabled(false);
		setKeyInputEnabled(true);

		keyTextField.setText(null);
		setAlgorithmButtonsEnabled(false);
		startAVLTestButton.setEnabled(!avlMode.isSelected());
		setMessage(null, NO_MESSAGE);
	}
}