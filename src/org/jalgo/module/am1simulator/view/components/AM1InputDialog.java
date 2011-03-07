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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;

import org.jalgo.module.am1simulator.view.GuiUtilities;
import org.jalgo.module.am1simulator.model.SimulationSet;
import org.jalgo.module.am1simulator.model.am1.machine.MachineConfiguration;
import org.jalgo.module.am1simulator.model.am1.machine.RuntimeStack;
import org.jalgo.module.am1simulator.model.am1.machine.RuntimeStackEntry;
import org.jalgo.module.am1simulator.model.am1.machine.Stream;

/**
 * Modal dialog to allow the user to type in a new AM1 configuration. Returns a
 * {@link SimulationSet}
 * 
 * @see AM1InputDialog#showAndWaitForReturn()
 * 
 * @author Max Leuth&auml;user
 */
public class AM1InputDialog extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static MachineConfiguration machineConfiguration = null;
	private JButton ok, cancel, rsAdd, rsDel;
	private JLabel pcDesc, stackDesc, rsDesc, refDesc, inDesc, outDesc, rsOut;
	private JTextField pcInput, stackInput, inInput, outInput, rsInput,
			refInput;
	private JPanel inputs, buttons;
	private JDialog dialog;
	private static JSlider steps;

	private String EMPTY = "Ɛ";
	private String INTEGER = "(0|-?[1-9]{1}[0-9]*)";
	static final int STEPS_MIN = 1000;
	/**
	 * Constant value to stop the presentation after the given number of steps
	 * and ask the user if he wants to go on.
	 */
	static final int STEPS_MAX = 2000;
	static final int STEPS_INIT = 1000;

	/**
	 * Constant value which represents the maximum string length where all GUI
	 * relevant strings should be shortened.
	 */
	static final int MAX_STRING_LENGTH = 15;

	private LinkedList<RuntimeStackEntry> runStack = new LinkedList<RuntimeStackEntry>();

	private AM1InputDialog() {
		dialog = new JDialog();
		dialog.setTitle("AM1 - Initial configuration");
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.setLayout(new BorderLayout());
		((JPanel) dialog.getContentPane()).setBorder(BorderFactory
				.createEmptyBorder(10, 10, 10, 10));
		createBaseLayout();
		createInputLayout();

		dialog.pack();
		int posX = GuiUtilities.getJAlgoWindow().getWidth() / 2
				- (dialog.getSize().width / 2);
		int posY = GuiUtilities.getJAlgoWindow().getHeight() / 2
				- (dialog.getSize().height / 2);
		dialog.setLocation(posX, posY);
		dialog.setVisible(true);
	}

	/**
	 * Create the input layout including text fields and description to enter a
	 * new AM0 configuration.
	 */
	private void createInputLayout() {
		inputs = new JPanel();
		inputs.setLayout(new BoxLayout(inputs, BoxLayout.Y_AXIS));
		pcDesc = new JLabel("Program counter");
		pcDesc.setFont(new Font(pcDesc.getFont().getFontName(), Font.BOLD,
				pcDesc.getFont().getSize()));
		stackDesc = new JLabel("Data stack (values separated whith ':')");
		stackDesc.setFont(new Font(stackDesc.getFont().getFontName(),
				Font.BOLD, stackDesc.getFont().getSize()));
		rsDesc = new JLabel("Runtime stack");
		rsDesc.setFont(new Font(rsDesc.getFont().getFontName(), Font.BOLD,
				rsDesc.getFont().getSize()));
		refDesc = new JLabel("Reference counter");
		refDesc.setFont(new Font(refDesc.getFont().getFontName(), Font.BOLD,
				refDesc.getFont().getSize()));
		inDesc = new JLabel("Input (values separated whith ',')");
		inDesc.setFont(new Font(inDesc.getFont().getFontName(), Font.BOLD,
				inDesc.getFont().getSize()));
		outDesc = new JLabel("Output (values separated whith ',')");
		outDesc.setFont(new Font(outDesc.getFont().getFontName(), Font.BOLD,
				outDesc.getFont().getSize()));

		pcInput = new JTextField();
		pcInput.setHorizontalAlignment(JTextField.CENTER);
		pcInput.setText("  1  ");
		pcInput.setCaretPosition(3);
		stackInput = new JTextField();
		stackInput.setText("Ɛ");
		rsOut = new JLabel("Ɛ");
		refInput = new JTextField();
		refInput.setText("  0  ");
		pcInput.setCaretPosition(3);
		pcInput.setHorizontalAlignment(JTextField.CENTER);
		inInput = new JTextField();
		inInput.setText("Ɛ");
		outInput = new JTextField();
		outInput.setText("Ɛ");

		rsInput = new JTextField();

		rsAdd = new JButton("+");
		rsAdd.addActionListener(this);
		rsDel = new JButton("-");
		rsDel.addActionListener(this);

		JPanel pc = new JPanel();
		pc.setLayout(new BorderLayout(5, 5));
		pc.add(pcDesc, BorderLayout.NORTH);
		pc.add(pcInput, BorderLayout.WEST);

		JPanel stack = new JPanel();
		stack.setLayout(new BorderLayout(5, 5));
		stack.add(stackDesc, BorderLayout.NORTH);
		stack.add(stackInput, BorderLayout.CENTER);

		JPanel rs = new JPanel();
		JPanel holdA = new JPanel();
		holdA.setLayout(new GridLayout(1, 3));
		holdA.add(rsInput);
		holdA.add(rsAdd);
		holdA.add(rsDel);
		JPanel holdB = new JPanel();
		holdB.setLayout(new BorderLayout());
		holdB.add(rsOut, BorderLayout.CENTER);
		rs.setLayout(new GridLayout(3, 1, 5, 0));
		rs.add(rsDesc);
		rs.add(holdA);
		rs.add(holdB);

		JPanel ref = new JPanel();
		ref.setLayout(new BorderLayout(5, 5));
		ref.add(refDesc, BorderLayout.NORTH);
		ref.add(refInput, BorderLayout.WEST);

		JPanel in = new JPanel();
		in.setLayout(new BorderLayout(5, 5));
		in.add(inDesc, BorderLayout.NORTH);
		in.add(inInput, BorderLayout.CENTER);

		JPanel out = new JPanel();
		out.setLayout(new BorderLayout(5, 5));
		out.add(outDesc, BorderLayout.NORTH);
		out.add(outInput, BorderLayout.CENTER);

		inputs.add(pc);
		inputs.add(Box.createRigidArea(new Dimension(10, 10)));
		inputs.add(stack);
		inputs.add(Box.createRigidArea(new Dimension(10, 10)));
		inputs.add(rs);
		inputs.add(Box.createRigidArea(new Dimension(10, 10)));
		inputs.add(ref);
		inputs.add(Box.createRigidArea(new Dimension(10, 10)));
		inputs.add(in);
		inputs.add(Box.createRigidArea(new Dimension(10, 10)));
		inputs.add(out);

		dialog.add(inputs, BorderLayout.CENTER);

		JLabel desc = new JLabel(
				"<html>Enter a new AM1 configuration.<br />"
						+ "Use ',' to separate the values at the input/output stream.<br />"
						+ "Use the same form for the runtime stack as written in the script. (e.g. '(1:0)')<br />"
						+ "Please do <b>not</b> use whitespaces. They won't get accepted!<br /><br /></html>");
		dialog.add(desc, BorderLayout.NORTH);

		steps = new JSlider(JSlider.HORIZONTAL, STEPS_MIN, STEPS_MAX,
				STEPS_INIT);
		steps.setMajorTickSpacing(200);
		steps.setMinorTickSpacing(100);
		steps.setPaintTicks(true);
		steps.setPaintLabels(true);

		JPanel holder = new JPanel();
		holder.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray),
				"Option to avoid endless loops"));
		holder.setLayout(new BorderLayout());
		holder.add(new JLabel(" Ask after"), BorderLayout.WEST);
		holder.add(steps, BorderLayout.CENTER);
		holder.add(new JLabel("steps. "), BorderLayout.EAST);
		inputs.add(Box.createRigidArea(new Dimension(10, 10)));
		inputs.add(holder);
	}

	/**
	 * Create the base layout including the 'OK' and 'Cancel' button.
	 */
	private void createBaseLayout() {
		buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 3));

		ok = new JButton("OK");
		ok.addActionListener(this);
		cancel = new JButton("Cancel");
		cancel.addActionListener(this);

		buttons.add(new JPanel());
		buttons.add(ok);
		buttons.add(cancel);
		buttons.add(new JPanel());

		buttons.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		dialog.add(buttons, BorderLayout.SOUTH);
	}

	/**
	 * Use this method to show this dialog and wait for the result.
	 * 
	 * @return a new {@link SimulationSet} or <b>null</b> if the user cancels
	 *         this dialog.
	 */
	public static SimulationSet showAndWaitForReturn() {
		new AM1InputDialog();
		return new SimulationSet(machineConfiguration, steps.getValue());
	}

	/**
	 * Show an error dialog if the am1 configuration is not valid.
	 * 
	 * @param description
	 */
	private void showError(String description) {
		JOptionPane.showMessageDialog(null, description, "Invalid input",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Cut the String in after 15 characters and add "..." at the end.<br />
	 * Example:<br />
	 * "12345678910111213" -> "123456789101112..."
	 * 
	 * @param in
	 * @return a shortened String with "..." added
	 */
	private String beautifyString(String in) {
		if (in.length() > MAX_STRING_LENGTH) {
			return in.substring(0, MAX_STRING_LENGTH) + "...";
		}
		return in;
	}

	/**
	 * Check a String if its a valid program counter.
	 * 
	 * @param pc
	 * @return true if the given string is a valid program counter, false
	 *         otherwise.
	 */
	private boolean checkPc(String pc) {
		if (pc == null || pc.isEmpty()) {
			showError("The program counter must not be empty!");
			return false;
		}
		try {
			int i = Integer.parseInt(pc);
			if (i == 0) {
				showError("The program counter must not be '0'!");
				return false;
			}
		} catch (NumberFormatException e) {
			showError("This value is not a valid program counter:\n\n\""
					+ beautifyString(pc) + "\"");
			return false;
		}
		return true;
	}

	/**
	 * Check a String if its a valid stack.
	 * 
	 * @param stack
	 * @return true if the given string is a valid stack, false otherwise.
	 */
	private boolean checkStack(String stack) {
		if (stack.isEmpty()
				|| stack.trim().matches(
						EMPTY + "|(" + INTEGER + ":)*" + INTEGER)) {
			return true;
		}
		showError("This value is not a valid stack configuration:\n\n\""
				+ beautifyString(stack) + "\"");
		return false;
	}

	/**
	 * Check a String if its a valid runtime stack entry.
	 * 
	 * @param run
	 * @return true if the given string is a valid runtime stack entry, false
	 *         otherwise.
	 */
	private boolean checkRuntimeStackInput(String run) {
		if (run.isEmpty()) {
			showError("This value is not a valid runtime stack configuration.\nIt must not be empty!\n");
			return false;
		}
		if (run.matches("\\((" + INTEGER + ":)*" + INTEGER + "(\\))?")) {
			return true;
		}
		showError("This value is not a valid runtime stack configuration:\n\n\""
				+ beautifyString(run) + "\"");
		return false;
	}

	/**
	 * Check a String if its a valid reference counter.
	 * 
	 * @param run
	 * @return true if the given string is a valid reference counter, false
	 *         otherwise.
	 */
	private boolean checkRef(String ref) {
		if (ref == null || ref.isEmpty()) {
			showError("The reference counter must not be empty!");
			return false;
		}
		try {
			Integer.parseInt(ref);
		} catch (NumberFormatException e) {
			showError("This value is not a valid reference counter:\n\n\""
					+ beautifyString(ref) + "\"");
			return false;
		}
		return true;
	}

	/**
	 * Check a String if its a valid input/output stream entry.
	 * 
	 * @param run
	 * @return true if the given string is a valid input/output stream entry,
	 *         false otherwise.
	 */
	private boolean checkStream(String stream) {
		if (stream.isEmpty()
				|| stream.trim().matches(
						EMPTY + "|(" + INTEGER + ",)*" + INTEGER)) {
			return true;
		}
		showError("This value is not a valid input/ouput stream configuration:\n\n\""
				+ beautifyString(stream) + "\"");
		return false;
	}

	/**
	 * Checks the elements the users wants to type in as new AM1 configuration.
	 * 
	 * @param pc
	 * @param stack
	 * @param ref
	 * @param in
	 * @param out
	 * @return <b>true</b> if everything is valid, <b>false</b> otherwise.
	 */
	private boolean checkInput(String pc, String stack, String ref, String in,
			String out) {
		return checkPc(pc) && checkStack(stack) && checkRef(ref)
				&& checkStream(in) && checkStream(out);
	}

	/**
	 * Create a new {@link RuntimeStackEntry} based on a string.
	 * 
	 * @param rse
	 * @return a new valid {@link RuntimeStackEntry} if the string is a valid
	 *         runtime stack entry, null otherwise.
	 */
	private RuntimeStackEntry buildRuntimeStackEntry(String rse) {
		if (checkRuntimeStackInput(rse)) {
			rse = rse.replace("(", "");
			boolean closed = rse.contains(")");
			rse = rse.replace(")", "");

			LinkedList<Integer> result = new LinkedList<Integer>();
			String delims = "[:]";
			String[] tokens = rse.split(delims);
			for (String s : tokens) {
				if (!s.isEmpty())
					try {
						result.add(Integer.parseInt(s));
					} catch (NumberFormatException e) {
						showError("This value is not a valid integer:\n\n\""
								+ beautifyString(s) + "\"");
						return null;
					}
			}
			RuntimeStackEntry r = new RuntimeStackEntry(result, closed);
			return r;
		} else {
			return null;
		}
	}

	/**
	 * Handle 'OK' and 'Cancel' click and handle runtime stack input/delete
	 * buttons
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == rsAdd) {
			RuntimeStackEntry e = buildRuntimeStackEntry(rsInput.getText()
					.trim());
			if (e != null) {
				if (!runStack.isEmpty()) {
					if (runStack.getLast().isClosed()) {
						runStack.add(e);
						rsOut.setText(RuntimeStack
								.printAListAsRuntimeStack(runStack));
					} else {
						showError("This element could not be added!\nThe last element in the runtime stack has to be closed.");
					}
				} else {
					runStack.add(e);
					rsOut.setText(RuntimeStack
							.printAListAsRuntimeStack(runStack));
				}
			}
		}

		if (arg0.getSource() == rsDel) {
			RuntimeStackEntry e = buildRuntimeStackEntry(rsInput.getText()
					.trim());
			if (e != null) {
				if (!runStack.remove(e)) {
					showError("The runtime stack does not contain this entry!\nIt could not be removed.");
				} else {
					rsOut.setText(RuntimeStack
							.printAListAsRuntimeStack(runStack));
				}
			}
		}

		if (arg0.getSource() == cancel) {
			machineConfiguration = null;
			dialog.setVisible(false);
			dialog.dispose();
		}

		if (arg0.getSource() == ok) {
			machineConfiguration = buildMachineConfiguration(pcInput.getText()
					.trim(), stackInput.getText(), refInput.getText().trim(),
					inInput.getText(), outInput.getText());
			if (machineConfiguration != null) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		}
	}

	/**
	 * Create a new {@link LinkedList} based on a string and a delimiter.
	 * 
	 * @param st
	 * @param delim
	 * @return a new {@link LinkedList} which could be used to create a new
	 *         {@link Stream}.
	 */
	private LinkedList<Integer> createStream(String st, String delim) {
		if (st.trim().equals(EMPTY)) {
			return new LinkedList<Integer>();
		}
		LinkedList<Integer> result = new LinkedList<Integer>();
		String delims = "[" + delim + "]";
		String[] tokens = st.split(delims);
		for (String s : tokens) {
			if (!s.isEmpty())
				try {
					result.add(Integer.parseInt(s));
				} catch (NumberFormatException e) {
					showError("This value is not a valid integer:\n\n\""
							+ beautifyString(s) + "\"");
					return null;
				}
		}
		return result;
	}

	/**
	 * Create a new {@link MachineConfiguration} based on several strings.
	 * 
	 * @param pc
	 * @param stack
	 * @param ref
	 * @param in
	 * @param out
	 * @return a new {@link MachineConfiguration} if all given parameter are
	 *         valid machine components.
	 */
	private MachineConfiguration buildMachineConfiguration(String pc,
			String stack, String ref, String in, String out) {
		if (checkInput(pc, stack, ref, in, out)) {
			LinkedList<Integer> stackList = createStream(stack, ":");
			LinkedList<Integer> inList = createStream(in, ",");
			LinkedList<Integer> outList = createStream(out, ",");
			if (stackList != null && inList != null && outList != null)
				// Integer.parseInt is save here because
				// the Strings are checked in checkInput already!
				return new MachineConfiguration(Integer.parseInt(pc),
						stackList, runStack, Integer.parseInt(ref), inList,
						outList);
			return null;
		}
		return null;
	}
}
