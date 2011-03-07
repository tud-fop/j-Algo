/**
 * AM1 Simulator - simulating am1 code in an abstract machine based on the
 * definitions of the lectures 'Programmierung' at TU Dresden.
 * Copyright (C) 2010 Max Leuthäuser
 * Contact: s7060241@mail.zih.tu-dresden.de
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.jalgo.module.am1simulator.view.components;

import java.awt.event.ActionListener;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToolBar;

import org.jalgo.main.util.Messages;

/**
 * A toolbar which is used to handle the user input.
 * 
 * @author Max Leuth&auml;user
 */
public class Toolbar extends JToolBar {
	private static final long serialVersionUID = 1L;

	/**
	 * Enumeration that holds all kinds of buttons which are used here.
	 * 
	 * @author Max Leuth&auml;user
	 */
	public static enum ButtonType {
		RUN, TOP, BOTTOM, INITIAL, CLEAR, BACK, FORWARD, ONESTEP, OPEN, SAVE;

		@Override
		public String toString() {
			return name().toLowerCase();
		}

		public String iconName() {
			return toString() + "_Icon";
		}

		/**
		 * Check if a String equals the string representation of this specific
		 * button. (use getActionCommand())
		 * 
		 * @param o
		 * @return true of the String equals this specific button, false
		 *         otherwise.
		 */
		public boolean isButtonEvent(String o) {
			return o != null && o.equals(toString());
		}
	}

	private JButton run, top, bottom, initial, clear, back, forward, onestep,
			open, save;

	public Toolbar() {
		run = makeButton(ButtonType.RUN,
				"Parse the code and run the simulation.", "Parse and run", true);
		top = makeButton(ButtonType.TOP,
				"Scroll to the top of the simulation table.",
				"Scroll to the top", false);
		bottom = makeButton(ButtonType.BOTTOM,
				"Scroll to the bottom of the simulation table.",
				"Scroll to the bottom", false);
		initial = makeButton(ButtonType.INITIAL,
				"Enter an initial configuration for the AM1.", "Configuration",
				false);
		clear = makeButton(ButtonType.CLEAR, "Delete the current simulation.",
				"Clear", false);
		back = makeButton(ButtonType.BACK, "Do one step back.", "Back", false);
		forward = makeButton(ButtonType.FORWARD, "Do on step forward.",
				"Forward", false);
		onestep = makeButton(ButtonType.ONESTEP,
				"Run the whole simulation in one step.", "One step", false);
		open = makeButton(ButtonType.OPEN,
				"Open a file and load it to the editor.", "Open", true);
		save = makeButton(ButtonType.SAVE, "Save the source code to a file.",
				"Save", true);

		add(open);
		add(save);
		addSeparator();
		add(run);
		addSeparator();
		add(initial);
		add(clear);
		add(back);
		add(forward);
		add(onestep);
		add(Box.createHorizontalGlue());
		add(top);
		add(bottom);
	}

	/**
	 * Change the image and text of the run button to show the user could stop
	 * the simulation here.
	 */
	public void setRunButtonToStop() {
		run.setIcon(new ImageIcon(Messages.getResourceURL("am1simulator",
				"stop_Icon")));
		run.setText("Stop");
		run.setToolTipText("<html>Stop the current simulation.<br /><b>This will delete your current simulation!</b></html>");
	}

	/**
	 * Change the image and text of the run button to show the user could run
	 * the simulation here.
	 */
	public void setRunButtonToRun() {
		run.setIcon(new ImageIcon(Messages.getResourceURL("am1simulator",
				"run_Icon")));
		run.setText("Parse and run");
		run.setToolTipText("Parse the code and run the simulation.");
	}

	/**
	 * Create a new {@link JButton} with the given attributes.
	 * 
	 * @param actionCommand
	 * @param toolTipText
	 * @param altText
	 * @param enabled
	 * @return a new {@link JButton} with the given attributes.
	 */
	private JButton makeButton(ButtonType actionCommand, String toolTipText,
			String altText, boolean enabled) {
		JButton button = new JButton(altText);
		button.setActionCommand(actionCommand.toString());
		button.setToolTipText(toolTipText);
		button.setIcon(new ImageIcon(Messages.getResourceURL("am1simulator",
				actionCommand.iconName())));
		button.setVerticalTextPosition(AbstractButton.BOTTOM);
		button.setHorizontalTextPosition(AbstractButton.CENTER);
		button.setEnabled(enabled);
		return button;
	}

	/**
	 * Add an {@link ActionListener} to all buttons to handle user input
	 * correctly.
	 * 
	 * @param a
	 */
	public void addUserInputListener(ActionListener a) {
		run.addActionListener(a);
		top.addActionListener(a);
		bottom.addActionListener(a);
		initial.addActionListener(a);
		clear.addActionListener(a);
		back.addActionListener(a);
		forward.addActionListener(a);
		onestep.addActionListener(a);
		open.addActionListener(a);
		save.addActionListener(a);
	}

	/**
	 * Method which set all important buttons to the initial state after a
	 * simulation error was found.
	 */
	public void setButtonsDisabledAfterError() {
		setButtonStatus(ButtonType.BACK, false);
		setButtonStatus(ButtonType.FORWARD, false);
		setButtonStatus(ButtonType.ONESTEP, false);
		setButtonStatus(ButtonType.CLEAR, true);
		setButtonStatus(ButtonType.INITIAL, false);
	}

	/**
	 * Set the specific button disabled/enabled.
	 * 
	 * @param button
	 * @param enabled
	 */
	public void setButtonStatus(ButtonType button, boolean enabled) {
		switch (button) {
		case BACK:
			back.setEnabled(enabled);
			break;
		case RUN:
			run.setEnabled(enabled);
			break;
		case TOP:
			top.setEnabled(enabled);
			break;
		case BOTTOM:
			bottom.setEnabled(enabled);
			break;
		case ONESTEP:
			onestep.setEnabled(enabled);
			break;
		case OPEN:
			open.setEnabled(enabled);
			break;
		case SAVE:
			save.setEnabled(enabled);
			break;
		case FORWARD:
			forward.setEnabled(enabled);
			break;
		case CLEAR:
			clear.setEnabled(enabled);
			break;
		case INITIAL:
			initial.setEnabled(enabled);
			break;
		default:
			throw new AssertionError("Unkown button type: " + button);
		}
	}
}
