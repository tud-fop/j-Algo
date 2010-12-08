/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2010 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
package org.jalgo.module.am0c0.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.LinkedList;
import java.util.TreeMap;

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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.core.Simulator;
import org.jalgo.module.am0c0.model.SimulationSet;
import org.jalgo.module.am0c0.model.am0.MachineConfiguration;

/**
 * Simple modal dialog to allow the user to type in a new AM configuration.
 * Returns a valid {@link MachineConfiguration}
 * 
 * @see AM0InputDialog#showAndWaitForReturn()
 * 
 * @author Max Leuth&auml;user
 */
public class AM0InputDialog extends JComponent implements ActionListener {
	private static final long serialVersionUID = 1L;
	private static MachineConfiguration machineConfiguration = null;
	private JButton ok, cancel, ramAdd, ramDel;
	private JLabel pcDesc, stackDesc, ramDesc, inDesc, outDesc, ramInput;
	private JTextField pcInput, stackInput, inInput, outInput, ramAdInput,
			ramValueInput;
	private JPanel inputs, buttons;
	private JDialog dialog;
	private TreeMap<Integer, Integer> ramMap = new TreeMap<Integer, Integer>();
	private static JSlider steps;
	private Simulator simController;

	static final int STEPS_MIN = 2000;
	/**
	 * Constants to stop the presentation after the given number of steps and
	 * ask the user if he wants to go on.
	 */
	static final int STEPS_MAX = 10000;
	static final int STEPS_INIT = 8000;

	private AM0InputDialog(Simulator s) {
		simController = s;
		dialog = new JDialog();
		dialog.setTitle(Messages.getString("am0c0", "AM0InputDialog.0")); //$NON-NLS-1$
		dialog.setResizable(false);
		dialog.setModal(true);
		dialog.setLayout(new BorderLayout());
		((JPanel) dialog.getContentPane()).setBorder(BorderFactory
				.createEmptyBorder(10, 10, 10, 10));
		createBaseLayout();
		createInputLayout();

		dialog.pack();
		dialog.setLocation(Toolkit.getDefaultToolkit().getScreenSize().width
				/ 2 - (dialog.getSize().width / 2), Toolkit.getDefaultToolkit()
				.getScreenSize().height / 2 - (dialog.getSize().height / 2));
		dialog.setVisible(true);
	}

	/**
	 * Create the input layout including text fields and description to enter a
	 * new AM0 configuration.
	 */
	private void createInputLayout() {
		inputs = new JPanel();
		inputs.setLayout(new BoxLayout(inputs, BoxLayout.Y_AXIS));
		pcDesc = new JLabel(Messages.getString("am0c0", "AM0InputDialog.1")); //$NON-NLS-1$
		pcDesc.setFont(new Font(pcDesc.getFont().getFontName(), Font.BOLD,
				pcDesc.getFont().getSize()));
		stackDesc = new JLabel(Messages.getString("am0c0", "AM0InputDialog.2")); //$NON-NLS-1$
		stackDesc.setFont(new Font(stackDesc.getFont().getFontName(),
				Font.BOLD, stackDesc.getFont().getSize()));
		ramDesc = new JLabel(Messages.getString("am0c0", "AM0InputDialog.3")); //$NON-NLS-1$
		ramDesc.setFont(new Font(ramDesc.getFont().getFontName(), Font.BOLD,
				ramDesc.getFont().getSize()));
		inDesc = new JLabel(Messages.getString("am0c0", "AM0InputDialog.4")); //$NON-NLS-1$
		inDesc.setFont(new Font(inDesc.getFont().getFontName(), Font.BOLD,
				inDesc.getFont().getSize()));
		outDesc = new JLabel(Messages.getString("am0c0", "AM0InputDialog.5")); //$NON-NLS-1$
		outDesc.setFont(new Font(outDesc.getFont().getFontName(), Font.BOLD,
				outDesc.getFont().getSize()));

		pcInput = new JTextField();
		pcInput.setHorizontalAlignment(JTextField.CENTER);
		pcInput.setText("  1  "); //$NON-NLS-1$
		pcInput.setCaretPosition(3);
		stackInput = new JTextField();
		stackInput.setText("Ɛ"); //$NON-NLS-1$
		ramInput = new JLabel();
		ramInput.setBackground(Color.white);
		char o = '\u00D8';
		ramInput.setText("<html>h<sub>" + o + "</sub>= [ ]"); //$NON-NLS-1$ //$NON-NLS-2$
		inInput = new JTextField();
		inInput.setText("Ɛ"); //$NON-NLS-1$
		outInput = new JTextField();
		outInput.setText("Ɛ"); //$NON-NLS-1$

		ramAdInput = new JTextField("1");
		ramValueInput = new JTextField();

		ramAdd = new JButton(Messages.getString("am0c0", "AM0InputDialog.12")); //$NON-NLS-1$
		ramAdd.addActionListener(this);
		ramDel = new JButton(Messages.getString("am0c0", "AM0InputDialog.13")); //$NON-NLS-1$
		ramDel.addActionListener(this);

		JPanel pc = new JPanel();
		pc.setLayout(new BorderLayout(5, 5));
		pc.add(pcDesc, BorderLayout.NORTH);
		pc.add(pcInput, BorderLayout.WEST);

		JPanel stack = new JPanel();
		stack.setLayout(new BorderLayout(5, 5));
		stack.add(stackDesc, BorderLayout.NORTH);
		stack.add(stackInput, BorderLayout.CENTER);

		JPanel ram = new JPanel();
		JPanel holdA = new JPanel();
		holdA.setLayout(new GridLayout(1, 4));
		holdA.add(ramAdInput);
		holdA.add(ramValueInput);
		holdA.add(ramAdd);
		holdA.add(ramDel);
		JPanel holdB = new JPanel();
		holdB.setLayout(new BorderLayout());
		holdB.add(ramInput, BorderLayout.CENTER);
		ram.setLayout(new GridLayout(3, 1, 5, 0));
		ram.add(ramDesc);
		ram.add(holdA);
		ram.add(holdB);

		JPanel in = new JPanel();
		in.setLayout(new BorderLayout(5, 5));
		in.add(inDesc, BorderLayout.NORTH);
		in.add(inInput, BorderLayout.CENTER);

		JPanel out = new JPanel();
		out.setLayout(new BorderLayout(5, 5));
		out.add(outDesc, BorderLayout.NORTH);
		out.add(outInput, BorderLayout.CENTER);

		inputs.add(pc);
		inputs.add(Box.createRigidArea(new Dimension(10, 15)));
		inputs.add(stack);
		inputs.add(Box.createRigidArea(new Dimension(10, 15)));
		inputs.add(ram);
		inputs.add(Box.createRigidArea(new Dimension(10, 15)));
		inputs.add(in);
		inputs.add(Box.createRigidArea(new Dimension(10, 15)));
		inputs.add(out);

		dialog.add(inputs, BorderLayout.CENTER);

		JLabel desc = new JLabel(Messages.getString(
				"am0c0", "AM0InputDialog.14")); //$NON-NLS-1$
		dialog.add(desc, BorderLayout.NORTH);

		steps = new JSlider(JSlider.HORIZONTAL, STEPS_MIN, STEPS_MAX,
				STEPS_INIT);
		steps.setMajorTickSpacing(2000);
		steps.setMinorTickSpacing(1000);
		steps.setPaintTicks(true);
		steps.setPaintLabels(true);

		steps.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				simController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "SimulationView.23") //$NON-NLS-1$
								+ steps.getValue()
								+ Messages.getString(
										"am0c0", "SimulationView.24")); //$NON-NLS-1$
			}
		});
		steps.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
				simController.getController().writeOnStatusbar(""); //$NON-NLS-1$
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				simController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "SimulationView.26") //$NON-NLS-1$
								+ steps.getValue()
								+ Messages.getString(
										"am0c0", "SimulationView.27")); //$NON-NLS-1$
			}

			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		JPanel holder = new JPanel();
		holder.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray), "Option"));
		holder.setLayout(new BorderLayout());		
		holder.add(
				new JLabel(Messages.getString("am0c0", "SimulationView.28")), BorderLayout.WEST); //$NON-NLS-1$
		holder.add(steps, BorderLayout.CENTER);
		holder.add(
				new JLabel(Messages.getString("am0c0", "SimulationView.29")), BorderLayout.EAST); //$NON-NLS-1$
		inputs.add(Box.createRigidArea(new Dimension(10, 15)));
		inputs.add(holder);
	}

	/**
	 * Create the base layout including the 'OK' and 'Cancel' button.
	 */
	private void createBaseLayout() {
		buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 3));

		ok = new JButton(Messages.getString("am0c0", "AM0InputDialog.15")); //$NON-NLS-1$
		ok.addActionListener(this);
		cancel = new JButton(Messages.getString("am0c0", "AM0InputDialog.16")); //$NON-NLS-1$
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
	 * @return a new valid {@link MachineConfiguration} or <b>null</b> if the
	 *         user cancel this dialog.
	 */
	public static SimulationSet showAndWaitForReturn(Simulator s) {
		new AM0InputDialog(s);
		return new SimulationSet(machineConfiguration, steps.getValue());
	}

	/**
	 * Show an error dialog if the am0 configuration is not valid.
	 * 
	 * @param description
	 */
	private void showError(String description) {
		JOptionPane
				.showMessageDialog(
						null,
						description,
						Messages.getString("am0c0", "AM0InputDialog.17"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
	}

	/**
	 * Checks the numbers the users wants to type in as new ram elements.
	 * 
	 * @param add
	 *            the address which should be checked
	 * @param val
	 *            the value at the given address which should be checked
	 * @return <b>true</b> if both numbers are valid, <b>false</b> otherwise.
	 */
	private boolean checkRamInput(String add, String val) {
		if (add.isEmpty() || val.isEmpty()) {
			showError(Messages.getString("am0c0", "AM0InputDialog.18")); //$NON-NLS-1$
			return false;
		}

		@SuppressWarnings("unused")
		int value;
		int address = 0;
		try {
			address = Integer.parseInt(add);
			value = Integer.parseInt(val);
		} catch (NumberFormatException e) {
			showError(Messages.getString("am0c0", "AM0InputDialog.19") //$NON-NLS-1$
					+ Messages.getString("am0c0", "AM0InputDialog.20") + Messages.getString("am0c0", "AM0InputDialog.21") + add + "\n" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ Messages.getString("am0c0", "AM0InputDialog.6") + val); //$NON-NLS-1$
			return false;
		}
		if (address <= 0) {
			showError(Messages.getString("am0c0", "AM0InputDialog.24") //$NON-NLS-1$
					+ Messages.getString("am0c0", "AM0InputDialog.25") //$NON-NLS-1$
					+ address);
			return false;
		}
		return true;
	}

	/**
	 * Checks the address the users wants to type in as new ram address.
	 * 
	 * @param add
	 * @return <b>true</b> if address is valid, <b>false</b> otherwise.
	 */
	private boolean checkRamAddress(String add) {
		int address = 0;
		try {
			address = Integer.parseInt(add);
		} catch (NumberFormatException e) {
			showError(Messages.getString("am0c0", "AM0InputDialog.26") //$NON-NLS-1$
					+ Messages.getString("am0c0", "AM0InputDialog.27") + add); //$NON-NLS-1$
			return false;
		}
		if (address <= 0) {
			showError(Messages.getString("am0c0", "AM0InputDialog.28") //$NON-NLS-1$
					+ Messages.getString("am0c0", "AM0InputDialog.29") //$NON-NLS-1$
					+ address);
			return false;
		}
		return true;
	}

	/**
	 * Checks the elements the users wants to type in as new ram configuration.
	 * 
	 * @param pc
	 * @param stack
	 * @param in
	 * @param out
	 * @return <b>true</b> if everything is valid, <b>false</b> otherwise.
	 */
	private boolean checkInput(String pc, String stack, String in, String out) {
		if (pc.trim().replaceAll(" ", "").matches("[0-9]+")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			if (stack.trim().isEmpty()
					|| stack.trim().replaceAll(" ", "").matches( //$NON-NLS-1$ //$NON-NLS-2$
							"Ɛ|(-?[0-9]+:{1})*-?[0-9]+")) { //$NON-NLS-1$
				if (in.trim().isEmpty()
						|| in.trim().replaceAll(" ", "").matches( //$NON-NLS-1$ //$NON-NLS-2$
								"Ɛ|(-?[0-9]+,{1})*-?[0-9]+")) { //$NON-NLS-1$
					if (out.trim().isEmpty()
							|| out.trim().replaceAll(" ", "").matches( //$NON-NLS-1$ //$NON-NLS-2$
									"Ɛ|(-?[0-9]+,{1})*-?[0-9]+")) { //$NON-NLS-1$
						return true;
					} else {
						showError(Messages.getString(
								"am0c0", "AM0InputDialog.7") //$NON-NLS-1$
								+ Messages.getString(
										"am0c0", "AM0InputDialog.43") //$NON-NLS-1$
								+ Messages.getString(
										"am0c0", "AM0InputDialog.44") //$NON-NLS-1$
								+ out);
					}
				} else {
					showError(Messages.getString("am0c0", "AM0InputDialog.45") //$NON-NLS-1$
							+ Messages.getString("am0c0", "AM0InputDialog.46") //$NON-NLS-1$
							+ Messages.getString("am0c0", "AM0InputDialog.47") + in); //$NON-NLS-1$
				}
			} else {
				showError(Messages.getString("am0c0", "AM0InputDialog.48") //$NON-NLS-1$
						+ Messages.getString("am0c0", "AM0InputDialog.49") //$NON-NLS-1$
						+ Messages.getString("am0c0", "AM0InputDialog.50") + stack); //$NON-NLS-1$
			}
		} else {
			if (!pc.isEmpty()) {
				showError(Messages.getString("am0c0", "AM0InputDialog.51") //$NON-NLS-1$
						+ pc);
			} else {
				showError(Messages.getString("am0c0", "AM0InputDialog.52")); //$NON-NLS-1$
			}
		}
		return false;
	}

	/**
	 * Handle 'OK' and 'Cancel' click and handle RAM input/delete buttons
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource() == ok) {
			if (checkInput(pcInput.getText(), stackInput.getText(),
					inInput.getText(), outInput.getText())) {
				if (stackInput.getText().replaceAll(" ", "").isEmpty() //$NON-NLS-1$ //$NON-NLS-2$
						&& inInput.getText().replaceAll(" ", "").isEmpty() //$NON-NLS-1$ //$NON-NLS-2$
						&& outInput.getText().replaceAll(" ", "").isEmpty() //$NON-NLS-1$ //$NON-NLS-2$
						&& ramInput.getText().startsWith("h")) { //$NON-NLS-1$
					machineConfiguration = new MachineConfiguration();
				} else {
					int pc = Integer.parseInt(pcInput.getText().replaceAll(" ", //$NON-NLS-1$
							"")); //$NON-NLS-1$
					LinkedList<Integer> stack = null;
					LinkedList<Integer> in = null;
					LinkedList<Integer> out = null;
					String delims = "[:]"; //$NON-NLS-1$
					String[] tokens;

					if (!stackInput.getText().equals("Ɛ")) { //$NON-NLS-1$
						stack = new LinkedList<Integer>();
						tokens = stackInput.getText().replaceAll(" ", "") //$NON-NLS-1$ //$NON-NLS-2$
								.split(delims);
						for (String s : tokens) {
							if (!s.isEmpty())
								stack.add(Integer.parseInt(s));
						}
					}

					delims = "[,]"; //$NON-NLS-1$
					if (!inInput.getText().equals("Ɛ")) { //$NON-NLS-1$
						in = new LinkedList<Integer>();
						tokens = inInput.getText().replaceAll(" ", "").split( //$NON-NLS-1$ //$NON-NLS-2$
								delims);
						for (String s : tokens) {
							if (!s.isEmpty())
								in.add(Integer.parseInt(s));
						}
					}

					if (!outInput.getText().equals("Ɛ")) { //$NON-NLS-1$
						out = new LinkedList<Integer>();
						tokens = outInput.getText().replaceAll(" ", "").split( //$NON-NLS-1$ //$NON-NLS-2$
								delims);
						for (String s : tokens) {
							if (!s.isEmpty())
								out.add(Integer.parseInt(s));
						}
					}

					if (ramMap.isEmpty()) {
						machineConfiguration = new MachineConfiguration(pc,
								stack, null, in, out);
					} else {
						machineConfiguration = new MachineConfiguration(pc,
								stack, ramMap, in, out);
					}
				}
				dialog.setVisible(false);
				dialog.dispose();
			}
		}
		if (arg0.getSource() == cancel) {
			machineConfiguration = null;
			dialog.setVisible(false);
			dialog.dispose();
		}
		if (arg0.getSource() == ramAdd) {
			if (checkRamInput(ramAdInput.getText(), ramValueInput.getText())) {
				if (ramMap.containsKey(Integer.parseInt(ramAdInput.getText()))) {
					if (JOptionPane
							.showConfirmDialog(dialog, Messages.getString(
									"am0c0", "AM0InputDialog.8"), //$NON-NLS-1$
									Messages.getString(
											"am0c0", "AM0InputDialog.74"), 0) == JOptionPane.OK_OPTION) { //$NON-NLS-1$
						ramMap.put(Integer.parseInt(ramAdInput.getText()),
								Integer.parseInt(ramValueInput.getText()));
					}
				} else {
					ramMap.put(Integer.parseInt(ramAdInput.getText()),
							Integer.parseInt(ramValueInput.getText()));
				}
				String result = "["; //$NON-NLS-1$
				for (int i : ramMap.keySet()) {
					result += ", " + i + "/" + ramMap.get(i); //$NON-NLS-1$ //$NON-NLS-2$
				}
				ramInput.setText(result.replaceFirst(", ", "") + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				ramInput.setToolTipText(result.replaceFirst(", ", "") + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				ramValueInput.setText(""); //$NON-NLS-1$
				ramAdInput.setText("" + (ramMap.lastKey() + 1)); //$NON-NLS-1$
			}
		}
		if (arg0.getSource() == ramDel) {
			if (!ramAdInput.getText().isEmpty()
					&& checkRamAddress(ramAdInput.getText())) {
				if (!ramMap.containsKey(Integer.parseInt(ramAdInput.getText()))) {
					JOptionPane
							.showMessageDialog(
									null,
									Messages.getString(
											"am0c0", "AM0InputDialog.9") //$NON-NLS-1$
											+ ramAdInput.getText(),
									Messages.getString(
											"am0c0", "AM0InputDialog.87"), JOptionPane.ERROR_MESSAGE); //$NON-NLS-1$
				} else {
					ramMap.remove(Integer.parseInt(ramAdInput.getText()));
					if (ramMap.isEmpty()) {
						char o = '\u00D8';
						String result = "<html>h<sub>" + o + "</sub>= [ ]"; //$NON-NLS-1$ //$NON-NLS-2$
						ramInput.setText(result);
						ramInput.setToolTipText(result);
					} else {
						String result = "["; //$NON-NLS-1$
						for (int i : ramMap.keySet()) {
							result += ", " + i + "/" + ramMap.get(i); //$NON-NLS-1$ //$NON-NLS-2$
						}
						ramInput.setText(result.replaceFirst(", ", "") + "]"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						ramInput.setToolTipText(result.replaceFirst(", ", "") //$NON-NLS-1$ //$NON-NLS-2$
								+ "]"); //$NON-NLS-1$
					}

				}
			}
		}
	}
}
