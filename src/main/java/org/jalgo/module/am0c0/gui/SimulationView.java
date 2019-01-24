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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTable;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.model.AM0PagingTableModel;
import org.jalgo.module.am0c0.model.SimulationSet;
import org.jalgo.module.am0c0.core.Simulator;
import org.jalgo.module.am0c0.gui.GuiConstants;
import org.jalgo.module.am0c0.gui.View;
import org.jalgo.module.am0c0.gui.jeditor.JEditor;
import org.jalgo.module.am0c0.gui.jeditor.jedit.tokenmarker.AM0TokenMarker;
import org.jalgo.module.am0c0.model.am0.AM0Program;
import org.jalgo.module.am0c0.model.am0.MachineConfiguration;
import org.jalgo.module.am0c0.model.am0.SimulationStatement;

/**
 * This class shows the simulation in the JAlgo module C0/AM0.
 * 
 * @author Max Leuth&auml;user
 */
public class SimulationView extends View {
	private static final long serialVersionUID = 1L;
	private ButtonHandler bh;
	private JSplitPane vSplitPane, hSplitPane;
	private JEditor am0code;
	private JEditorPane commandHint;
	private JPanel am0Pane, codeTablePane, commandHintPane, buttonsPanel,
			navigationPane;
	private JTable codeTable;
	private AM0PagingTableModel amTableModel;
	private JButton back, prev, doIt, next, config, clear, jumpUp, jumpDown;
	private JLabel step;
	private JLabel range;
	private int steps;

	private Simulator simController;

	public SimulationView(Simulator simController) {
		initComponents();
		initComponentAttributs();
		attachButtonHandler();
		add(vSplitPane);
		this.simController = simController;
	}

	/**
	 * @return {@link AM0PagingTableModel} which is used to show the machine
	 *         configurations.
	 */
	public AM0PagingTableModel getTableModel() {
		return amTableModel;
	}

	/**
	 * Show a am0 statement description.
	 * 
	 * 
	 * @param s
	 *            Description that should be printed.
	 */
	public void showDescription(String s) {
		if (s.isEmpty()) {
			commandHint.setText(""); //$NON-NLS-1$
		} else
			commandHint
					.setText("<html><body style=\"background-color: #ffffff;\">" //$NON-NLS-1$
							+ s + "</body></html>"); //$NON-NLS-1$
		commandHint.setCaretPosition(0);
	}

	/**
	 * @return the {@link JLabel} which is used to show the current step.
	 */
	public JLabel getStepLabel() {
		return step;
	}

	/**
	 * @return the {@link JLabel} which is used to show the current step range.
	 */
	public JLabel getRangeLabel() {
		return range;
	}

	/**
	 * @param am
	 *            The new am0 program to show in simulation.
	 */
	public void setAM0Program(AM0Program am) {
		am0code.updateModel(am);
		am0code.setCaretPosition(am0code.getLineStartOffset(0));
		am0code.setLineMarker(0, 0);
	}

	/**
	 * Scrolls to the current line.
	 * 
	 * @param pc
	 *            The program counter which indicates the program line that
	 *            should be highlighted.
	 */
	public void scroll(int pc) {
		codeTable.scrollRectToVisible(codeTable.getCellRect(
				codeTable.getRowCount() - 1, codeTable.getColumnCount(), true));

		if (pc >= 1 && pc <= am0code.getLineCount()) {
			am0code.setCaretPosition(am0code.getLineStartOffset(pc - 1));
			am0code.setLineMarker(pc - 1, pc - 1);
		}
	}

	/**
	 * Create and use the simulation table.
	 */
	private void useCodeTablePane() {
		codeTablePane = new JPanel();
		codeTablePane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray),
				Messages.getString("am0c0", "SimulationView.3"))); //$NON-NLS-1$
		codeTablePane.setLayout(new BorderLayout(5, 10));
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());

		JPanel a = new JPanel();
		a.setLayout(new GridLayout(1, 3));
		a.add(step);
		a.add(jumpUp);
		a.add(jumpDown);

		p.add(range, BorderLayout.WEST);
		p.add(a, BorderLayout.EAST);
		codeTablePane.add(
				AM0PagingTableModel.createPagingScrollPaneForTable(codeTable),
				BorderLayout.CENTER);

		navigationPane = new JPanel();
		navigationPane.setLayout(new BorderLayout(5, 10));
		navigationPane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray), "Navigation"));
		navigationPane.add(p, BorderLayout.NORTH);
		navigationPane.add(buttonsPanel, BorderLayout.SOUTH);
	}

	/**
	 * Create and use the simulation panel. All animations are visible here.
	 * 
	 * @deprecated
	 */
	@SuppressWarnings("unused")
	private void useAm0Pane() {
		am0Pane = new JPanel();
		am0Pane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray),
				Messages.getString("am0c0", "SimulationView.4"))); //$NON-NLS-1$
		am0Pane.setMinimumSize(new Dimension(550, 0));
		am0Pane.setLayout(new BorderLayout());
		am0Pane.add(buttonsPanel, BorderLayout.SOUTH);
	}

	@Override
	protected void initComponents() {
		bh = new ButtonHandler();
		am0code = new JEditor(new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {
				// Modified flag could be ignored here.
			}
		}, null);

		jumpUp = new JButton(Messages.getString("am0c0", "SimulationView.5")); //$NON-NLS-1$
		jumpDown = new JButton(Messages.getString("am0c0", "SimulationView.6")); //$NON-NLS-1$
		step = new JLabel(Messages.getString("am0c0", "SimulationView.7")); //$NON-NLS-1$
		range = new JLabel(""); //$NON-NLS-1$
		back = new JButton(Messages.getString("am0c0", "SimulationView.9")); //$NON-NLS-1$
		back.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", //$NON-NLS-1$
				GuiConstants.BACK_TO_EDITOR_ICON)));
		prev = new JButton(Messages.getString("am0c0", "SimulationView.11")); //$NON-NLS-1$
		prev.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", //$NON-NLS-1$
				GuiConstants.PREVIOUS_ICON)));
		next = new JButton(Messages.getString("am0c0", "SimulationView.13")); //$NON-NLS-1$
		next.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", //$NON-NLS-1$
				GuiConstants.NEXT_ICON)));
		clear = new JButton(Messages.getString("am0c0", "SimulationView.15")); //$NON-NLS-1$
		clear.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", //$NON-NLS-1$
				GuiConstants.CLEAR_ICON)));
		doIt = new JButton(Messages.getString("am0c0", "SimulationView.17")); //$NON-NLS-1$
		doIt.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", //$NON-NLS-1$
				GuiConstants.LAST_ICON)));
		config = new JButton(Messages.getString("am0c0", "SimulationView.19")); //$NON-NLS-1$
		config.setIcon(new ImageIcon(Messages.getResourceURL("am0c0", //$NON-NLS-1$
				GuiConstants.INITIAL_CONFIG_ICON)));
		buttonsPanel = new JPanel();
		
		commandHint = new JEditorPane();
		commandHint.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES,
				true);
		commandHint.setFont(GuiConstants.STANDARDFONT_SERIF);

		amTableModel = new AM0PagingTableModel(this);
		codeTable = new JTable(amTableModel);

		useCodeTablePane();

		commandHintPane = new JPanel();

		hSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, am0code,
				commandHintPane);

		JPanel a = new JPanel();
		a.setLayout(new BorderLayout());
		a.add(codeTablePane, BorderLayout.CENTER);
		a.add(navigationPane, BorderLayout.SOUTH);
		vSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
				hSplitPane, a);
	}

	@Override
	protected void attachButtonHandler() {
		back.addActionListener(bh);
		prev.addActionListener(bh);
		next.addActionListener(bh);
		doIt.addActionListener(bh);
		config.addActionListener(bh);
		clear.addActionListener(bh);
		jumpUp.addActionListener(bh);
		jumpDown.addActionListener(bh);
		jumpUp.addMouseListener(bh);
		jumpDown.addMouseListener(bh);
		back.addMouseListener(bh);
		prev.addMouseListener(bh);
		next.addMouseListener(bh);
		doIt.addMouseListener(bh);
		config.addMouseListener(bh);
		clear.addMouseListener(bh);
	}

	/**
	 * @return the steps the user want to execute until he is asked for more.
	 */
	public int getStepsToStopAfter() {
		return steps;
	}

	@Override
	protected void initComponentAttributs() {
		setLayout(new BorderLayout());
		am0code.setTokenMarker(new AM0TokenMarker());
		am0code.setCodeHighlightMode(false);
		am0code.getPainter().setLineHighlightEnabled(false);
		am0code.showLineMarker(true);
		am0code.setEnabled(false);
		am0code.setMinimumSize(new Dimension(160, 300));

		setAM0CodeListeners();

		prev.setEnabled(false);
		clear.setEnabled(false);

		buttonsPanel.setLayout(new GridLayout(1, 5));

		buttonsPanel.add(config);
		buttonsPanel.add(clear);
		buttonsPanel.add(prev);
		buttonsPanel.add(next);
		buttonsPanel.add(doIt);

		commandHint.setEditable(false);
		commandHint.setContentType("text/html"); //$NON-NLS-1$
		commandHintPane.setBorder(BorderFactory.createTitledBorder(
				BorderFactory.createLineBorder(Color.gray),
				Messages.getString("am0c0", "SimulationView.31"))); //$NON-NLS-1$
		commandHintPane.setLayout(new BorderLayout());
		commandHintPane.add(getScrollPane(commandHint), BorderLayout.CENTER);
		commandHintPane.add(back, BorderLayout.SOUTH);
		commandHintPane.setMinimumSize(new Dimension(250, 150));

		hSplitPane
				.setDividerLocation(GuiConstants.getJAlgoWindow().getSize().height / 2);

		hSplitPane.setOneTouchExpandable(true);
		vSplitPane.setOneTouchExpandable(true);

		codeTable.setEnabled(true);
		codeTable.setRowHeight(codeTable.getRowHeight() + 8);
		codeTable.setDefaultRenderer(Object.class, new AMTableRenderer());
		codeTable.setShowGrid(false);
		codeTable.setIntercellSpacing(new Dimension(15, 2));
		codeTable.getTableHeader().setReorderingAllowed(false);
	}

	private void setAM0CodeListeners() {
		am0code.getPainter().addMouseListener(new MouseListener() {

			private void setDescription(int y) {
				int line = am0code.yToLine(y);
				SimulationStatement statement = (SimulationStatement) am0code
						.getCodeFromLine(line, null, null);
				if (statement != null) {
					String description = statement.getCodeText() + "<br /><p>" //$NON-NLS-1$
							+ statement.getDescription() + "</p>"; //$NON-NLS-1$

					showDescription(description);
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				setDescription(e.getY());
			}

			@Override
			public void mousePressed(MouseEvent e) {
			}

			@Override
			public void mouseExited(MouseEvent e) {
			}

			@Override
			public void mouseEntered(MouseEvent e) {
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				setDescription(e.getY());
			}
		});
	}

	private class ButtonHandler implements ActionListener, MouseListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (arg0.getSource() == back) {
				simController.getController().showEditor();
			}
			if (arg0.getSource() == prev) {
				amTableModel.showLastPage();
				if (!simController.previousStep()) {
					prev.setEnabled(false);
				} else {
					next.setEnabled(true);
					doIt.setEnabled(true);
				}
			}
			if (arg0.getSource() == next) {
				if (simController.simulatorNeedsInitialConfig()) {
					SimulationSet s = AM0InputDialog
							.showAndWaitForReturn(simController);
					MachineConfiguration m = s.getMachineConfiguration();
					if (m != null) {
						steps = s.getSteps();
						config.setEnabled(false);
						simController.getHistory().add(
								new MachineConfiguration(m));
						simController.setCurrentMachineConfiguration(m);
						simController.addResultToTable(m, 0);
						amTableModel.showLastPage();
						clear.setEnabled(true);
						if (!simController.nextStep()) {
							next.setEnabled(false);
							doIt.setEnabled(false);
							prev.setEnabled(true);
						}
					}
				} else {
					amTableModel.showLastPage();
					if (!simController.nextStep()) {
						next.setEnabled(false);
						doIt.setEnabled(false);
					}
					prev.setEnabled(true);
					clear.setEnabled(true);
				}
			}
			if (arg0.getSource() == doIt) {
				if (simController.simulatorNeedsInitialConfig()) {
					SimulationSet s = AM0InputDialog
							.showAndWaitForReturn(simController);
					MachineConfiguration m = s.getMachineConfiguration();
					if (m != null) {
						steps = s.getSteps();
						simController.getHistory().add(
								new MachineConfiguration(m));
						simController.setCurrentMachineConfiguration(m);
						simController.addResultToTable(m, 0);
						next.setEnabled(false);
						doIt.setEnabled(false);
						config.setEnabled(false);
						prev.setEnabled(true);
						clear.setEnabled(true);
						simController.stepToEnd();
						amTableModel.showLastPage();
					}
				} else {
					next.setEnabled(false);
					doIt.setEnabled(false);
					config.setEnabled(false);
					clear.setEnabled(true);
					simController.stepToEnd();
					prev.setEnabled(true);
					amTableModel.showLastPage();
				}
			}
			if (arg0.getSource() == config) {
				SimulationSet s = AM0InputDialog
						.showAndWaitForReturn(simController);
				MachineConfiguration m = s.getMachineConfiguration();
				if (m != null) {
					steps = s.getSteps();
					simController.getHistory().add(new MachineConfiguration(m));
					simController.setCurrentMachineConfiguration(m);
					simController.addResultToTable(m, 0);
					clear.setEnabled(true);
					config.setEnabled(false);
				}
			}
			if (arg0.getSource() == clear) {
				resetSimulation();
				clear.setEnabled(false);
			}

			if (arg0.getSource() == jumpUp) {
				amTableModel.showFirstPage();
				codeTable.scrollRectToVisible(codeTable.getCellRect(0,
						codeTable.getColumnCount(), true));
			}

			if (arg0.getSource() == jumpDown) {
				amTableModel.showLastPage();
				codeTable.scrollRectToVisible(codeTable.getCellRect(
						codeTable.getRowCount() - 1,
						codeTable.getColumnCount(), true));
			}
		}

		@Override
		public void mouseClicked(MouseEvent e) {
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			if (e.getSource() == jumpUp) {
				simController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "SimulationView.32")); //$NON-NLS-1$
			}
			if (e.getSource() == jumpDown) {
				simController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "SimulationView.33")); //$NON-NLS-1$
			}
			if (e.getSource() == back) {
				simController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "SimulationView.34")); //$NON-NLS-1$
			}
			if (e.getSource() == prev) {
				simController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "SimulationView.35")); //$NON-NLS-1$
			}
			if (e.getSource() == next) {
				simController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "SimulationView.36")); //$NON-NLS-1$

			}
			if (e.getSource() == doIt) {
				simController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "SimulationView.37")); //$NON-NLS-1$
			}
			if (e.getSource() == config) {
				simController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "SimulationView.38")); //$NON-NLS-1$
			}
			if (e.getSource() == clear) {
				simController.getController().writeOnStatusbar(
						Messages.getString("am0c0", "SimulationView.39")); //$NON-NLS-1$
			}
		}

		@Override
		public void mouseExited(MouseEvent e) {
			simController.getController().writeOnStatusbar(""); //$NON-NLS-1$
		}

		@Override
		public void mousePressed(MouseEvent e) {
		}

		@Override
		public void mouseReleased(MouseEvent e) {
		}
	}

	/**
	 * Resets the whole simulation. Use this carefully.
	 */
	public void resetSimulation() {
		config.setEnabled(true);
		am0code.setCaretPosition(am0code.getLineStartOffset(0));
		am0code.setLineMarker(0, 0);
		simController.clear();
		next.setEnabled(true);
		prev.setEnabled(false);
		doIt.setEnabled(true);
	}

	@Override
	public void setPresentationMode(boolean presentationMode) {
		if (presentationMode) {
			am0code.setFont(GuiConstants.PRESENTATIONFONT);
			commandHint.setFont(GuiConstants.PRESENTATIONFONT_SERIF);
			codeTable.setFont(GuiConstants.PRESENTATIONFONT);
		} else {
			am0code.setFont(GuiConstants.STANDARDFONT);
			commandHint.setFont(GuiConstants.STANDARDFONT_SERIF);
			codeTable.setFont(GuiConstants.STANDARDFONT);
		}
	}
}
