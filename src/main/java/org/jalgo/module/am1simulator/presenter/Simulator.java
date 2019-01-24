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

package org.jalgo.module.am1simulator.presenter;

import org.jalgo.module.am1simulator.view.components.PresentationAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

import org.jalgo.module.am1simulator.parser.am1.AM1Parser;
import org.jalgo.module.am1simulator.view.GUIBuilder;
import org.jalgo.module.am1simulator.view.GuiUtilities;
import org.jalgo.module.am1simulator.view.ViewContainer;
import org.jalgo.module.am1simulator.view.components.AM1InputDialog;
import org.jalgo.module.am1simulator.view.components.TablePanel;
import org.jalgo.module.am1simulator.view.components.Toolbar.ButtonType;
import org.jalgo.module.am1simulator.model.AM1History;
import org.jalgo.module.am1simulator.model.SimulationSet;
import org.jalgo.module.am1simulator.model.am1.*;
import org.jalgo.module.am1simulator.model.am1.machine.MachineConfiguration;

/**
 * Presenter (MVP-Pattern) to handle the AM1 simulation.
 * 
 * @author Max Leuth&auml;user
 */
public class Simulator implements UpdateEventCreater<Integer> {
	/**
	 * The view in the MVP-Pattern which actually a {@link ViewContainer}.
	 */
	private ViewContainer view;

	/**
	 * This history ({@link AM1History}) is used to store already calculated
	 * steps of the simulation.s
	 */
	private AM1History<MachineConfiguration> history;
	private int currentStep;
	private AM1Program am1Program;
	private MachineConfiguration currentMc;
	/**
	 * Flag to allow or disallow scrolling, printing descriptions and
	 * highlighting. This flag is set to false during a simulation in one single
	 * step to avoid performance issues.
	 */
	private boolean drawAddon = true;
	/**
	 * Flag which is used to handle jump command failures.
	 */
	private boolean jumpFails = false;

	/**
	 * Value of steps the user wants to simulate in one single step before he
	 * gets ask to continue. (avoids endless loops)
	 */
	private int steps;

	/**
	 * The current state. Either {@link Editing} or {@link Simulating}.
	 */
	private State currentState = new Editing();

	public Simulator() {
	}

	/**
	 * @return the maximal available program counter;
	 */
	public int getMaxPC() {
		return am1Program.size();
	}

	/**
	 * @return the history of the simulation.
	 */
	public AM1History<MachineConfiguration> getHistory() {
		return history;
	}

	/**
	 * @return the view ({@link ViewContainer}).
	 */
	public ViewContainer getView() {
		return view;
	}

	/**
	 * @param m
	 *            {@link MachineConfiguration} which is used here.
	 */
	public void setCurrentMachineConfiguration(MachineConfiguration m) {
		currentMc = m;
	}

	/**
	 * Show an error message to the user because an command could not be
	 * executed.
	 * 
	 * @param reason
	 */
	private void showError(String reason) {
		JOptionPane.showMessageDialog(
				null,
				"There was an error while simulating.\n"
						+ "The following command could not be executed:\n"
						+ am1Program.get(
								currentMc.getProgramCounter().get() - 1)
								.getCodeText() + "\n\n" + "Reason:\n" + reason);
	}

	/**
	 * @param program
	 *            The {@link AM1Program} which should be used during the
	 *            simulation.
	 * 
	 * @throws IllegalArgumentException
	 */
	public void setAM1Program(AM1Program program)
			throws IllegalArgumentException {
		if (program != null) {
			am1Program = program;
		} else
			throw new IllegalArgumentException(
					"Null argument is not allowed for AM1Program.");
	}

	/**
	 * Show the description for the corresponding statement.
	 * 
	 * @param i
	 *            Position of the statement in the {@link AM1Program}.
	 */
	private void showDescription(int i) {
		if (drawAddon) {
			if (am1Program.size() < 1) {
				return;
			}

			if (i <= 0) {
				i = 1;
			}

			if (i > am1Program.size()) {
				i = am1Program.size();
			}
			view.getConsolePanel()
					.getDescriptionEditor()
					.setText(
							am1Program.get(i - 1).getCodeText() + "<br /><p>"
									+ am1Program.get(i - 1).getDescription()
									+ "</p>");
		}
	}

	/**
	 * Show the currently visible page range.
	 */
	private void showRange() {
		int t = 0;
		if (view.getTablePanel().getTableModel().getPageOffset() == 0) {
			t = 1;
		} else {
			t = view.getTablePanel().getTableModel().getPageSize()
					* view.getTablePanel().getTableModel().getPageOffset();
		}

		view.getTablePanel()
				.setRange(
						"<html>&nbsp;&nbsp;Showing steps from <b>"
								+ t
								+ "</b> to <b>"
								+ (view.getTablePanel().getTableModel()
										.getPageSize() * (view.getTablePanel()
										.getTableModel().getPageOffset() + 1))
								+ "</b></html>");
	}

	/**
	 * @param m
	 *            The {@link MachineConfiguration} to draw in the
	 *            {@link TablePanel}.
	 */
	public void addResultToTable(MachineConfiguration m, int position) {
		view.getTablePanel().getTableModel().addRow(m);
		if (drawAddon) {
			showRange();
			if (position >= 1) {
				view.getTablePanel().setStep(
						"Latest step: " + (position + 1) + "  ");
				showRange();
				fireUpdateEvent(new UpdateEvent<Integer>(currentMc
						.getProgramCounter().get()));
			}
			if (currentMc.getProgramCounter().get() <= getMaxPC()
					&& currentMc.getProgramCounter().get() >= 1) {
				showDescription(currentMc.getProgramCounter().get());
			}
		}

	}

	/**
	 * Removes the last line in the table.
	 */
	public void removeLastTableEntry(int position) {
		view.getTablePanel().getTableModel().removeLastRow();
		if (drawAddon) {
			showRange();
			view.getTablePanel().setStep(
					"Latest step: " + (position + 1) + "  ");
			fireUpdateEvent(new UpdateEvent<Integer>(currentMc
					.getProgramCounter().get()));
			showDescription(currentMc.getProgramCounter().get());
		}

	}

	/**
	 * Do the next step and draw the result. We only calculate something new
	 * if the step is not already in the {@link AM1History}.
	 * 
	 * @return true if the next step could executed correctly, false otherwise.
	 */
	public boolean nextStep() {
		if (currentMc.getProgramCounter().get() > getMaxPC()
				|| currentMc.getProgramCounter().get() < 1) {
			jumpFails = true;
			return false;
		}
		currentStep++;
		if (!history.stepExists(currentStep)) {
			jumpFails = false;
			try {
				currentMc = am1Program.get(
						currentMc.getProgramCounter().get() - 1).apply(
						new MachineConfiguration(currentMc));
				history.add(currentMc);
			} catch (IllegalArgumentException e) {
				// If an am1 statement fail we show this:
				view.getToolbar().setButtonsDisabledAfterError();
				showError(e.getMessage());
				return false;
			} catch (ArithmeticException e) {
				// In the special case of DIV with 0 we will crash here:
				view.getToolbar().setButtonsDisabledAfterError();
				showError(e.getMessage());
				return false;
			}
		} else {
			currentMc = history.getAtStep(currentStep);
		}
		addResultToTable(currentMc, currentStep);
		return true;
	}

	/**
	 * Do the previous step and draw the result. Nothing is calculated here,
	 * we just using the {@link AM1History}.
	 * 
	 * @return true if the previous step could executed correctly, false
	 *         otherwise.
	 */
	public boolean previousStep() {
		if (currentStep >= 1) {
			currentStep--;
			currentMc = history.getAtStep(currentStep);
			removeLastTableEntry(currentStep);
			jumpFails = false;
			return true;
		}
		return false;
	}

	/**
	 * Do the full (remaining) simulation in one single step. Basically we just
	 * use {@link Simulator#nextStep()}.
	 * 
	 * @return true if all steps could executed correctly, false otherwise.
	 */
	public boolean stepToEnd() {
		drawAddon = false;
		boolean result = true;
		while (result) {
			result = nextStep();
			if (currentStep % steps == 0) {
				drawAddon = true;
				showDescription(currentMc.getProgramCounter().get());
				view.getTablePanel().setStep(
						"Latest step: " + currentStep + "  ");
				fireUpdateEvent(new UpdateEvent<Integer>(currentMc
						.getProgramCounter().get()));
				drawAddon = false;
				int s = JOptionPane
						.showConfirmDialog(
								null,
								currentStep
										+ " steps are calculated.\n"
										+ "Do you want to continue?\n\n"
										+ "Choose 'no' to stop the simulation or 'yes' if you want to go on.",
								"Simulation", JOptionPane.YES_NO_OPTION);
				if (s == JOptionPane.NO_OPTION
						|| s == JOptionPane.CANCEL_OPTION) {
					result = true;
					break;
				}
			}
		}
		drawAddon = true;
		view.getTablePanel()
				.setStep("Latest step: " + (currentStep + 1) + "  ");
		fireUpdateEvent(new UpdateEvent<Integer>(currentMc.getProgramCounter()
				.get()));
		// We want to show the description of the jump command which failed or
		// or if the command failed itself so we can not use '-1' always.
		showDescription(currentMc.getProgramCounter().get()
				- (jumpFails || !result ? 0 : 1));
		showRange();
		return result;
	}

	/**
	 * Clear the whole simulation. Warning: this will destroy the current
	 * simulation data. <b>Use this with care!</b>
	 */
	public void clear() {
		view.getTablePanel().getTableModel().clear();
		history.clear();
		view.getTablePanel().setStep("Not started yet.");
		view.getTablePanel().setRange("");
		view.getConsolePanel().getDescriptionEditor().setText("");
		currentStep = 0;
	}

	/**
	 * @return true if the simulation needs an initial configuration.
	 */
	public boolean simulatorNeedsInitialConfig() {
		return history.getCount() == 0;
	}

	public AM1Program getAM1Program() {
		return am1Program;
	}

	public void init(final JComponent contentPane, final JMenu menu, final JToolBar toolbar) {
		view = new ViewContainer.Builder(GUIBuilder.newMainPanel())
				.console(GUIBuilder.newConsolePanel())
				.editor(GUIBuilder.newEditorPanel(this))
				.file(GUIBuilder.newFileJLabel())
				.postion(GUIBuilder.newDefaultJLabel())
				.table(GUIBuilder.newTablePanel(this))
				.toolbar(GUIBuilder.newToolbar()).build();

		view.getToolbar().addUserInputListener(new UserInputListener());
		view.getTablePanel().getTableModel()
				.addUserInputListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						view.getTablePanel().getTableModel().pageUp();
						showRange();
					}
				}, new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent arg0) {
						view.getTablePanel().getTableModel().pageDown();
						showRange();
					}
				});
		history = new AM1History<MachineConfiguration>();
		currentStep = 0;
		menu.add(new PresentationAction(this));
		contentPane.add(view.getMainPanel());
	}

	/**
	 * Abstract state class to handle different behavior between editing and
	 * simulating in general.
	 * 
	 * @see Simulating
	 * @see Editing
	 */
	private abstract class State {
		/**
		 * Run the simulation which means do one step forward after parsing and
		 * so on.
		 */
		public void run() {
		};

		/**
		 * Scroll to the top of the machine configuration table.
		 */
		public void top() {
			view.getTablePanel().getTableModel().showFirstPage();
			view.getTablePanel()
					.getTable()
					.scrollRectToVisible(
							view.getTablePanel()
									.getTable()
									.getCellRect(
											0,
											view.getTablePanel().getTable()
													.getColumnCount(), true));
		};

		/**
		 * Scroll to the bottom of the machine configuration table.
		 */
		public void bottom() {
			view.getTablePanel().getTableModel().showLastPage();
			view.getTablePanel()
					.getTable()
					.scrollRectToVisible(
							view.getTablePanel()
									.getTable()
									.getCellRect(
											view.getTablePanel().getTable()
													.getRowCount() - 1,
											view.getTablePanel().getTable()
													.getColumnCount(), true));
		};

		/**
		 * Enter a new inital configuration.
		 */
		public void initial() {
			SimulationSet s = AM1InputDialog.showAndWaitForReturn();
			MachineConfiguration r = s.getMachineConfiguration();
			if (r != null) {
				view.getToolbar().setButtonStatus(ButtonType.INITIAL, true);
				view.getEditorPanel().removeMarkers();
				Simulator.this.clear();
				view.getToolbar().setButtonStatus(ButtonType.FORWARD, true);
				view.getToolbar().setButtonStatus(ButtonType.BACK, false);
				view.getToolbar().setButtonStatus(ButtonType.ONESTEP, true);
				view.getTablePanel().setInitialConfiguration(
						"<html>" + r.toString() + "</html>");
				setCurrentMachineConfiguration(r);
				steps = s.getSteps();
				getHistory().add(new MachineConfiguration(r));
				addResultToTable(r, 0);
			} else {
				view.getTablePanel().setInitialConfiguration(
						"No initial configuration");
			}
		};

		/**
		 * Clear / delete the current simulation progress completely.
		 */
		public void clear() {
			view.getToolbar().setButtonStatus(ButtonType.INITIAL, true);
			view.getEditorPanel().removeMarkers();
			view.getTablePanel().setInitialConfiguration(
					"No initial configuration.");
			Simulator.this.clear();
			view.getToolbar().setButtonStatus(ButtonType.FORWARD, true);
			view.getToolbar().setButtonStatus(ButtonType.BACK, false);
			view.getToolbar().setButtonStatus(ButtonType.ONESTEP, true);
		};

		/**
		 * Do one step backward in the simulation history.
		 */
		public void back() {
			view.getTablePanel().getTableModel().showLastPage();
			if (!previousStep()) {
				view.getToolbar().setButtonStatus(ButtonType.BACK, false);
			} else {
				view.getToolbar().setButtonStatus(ButtonType.FORWARD, true);
				view.getToolbar().setButtonStatus(ButtonType.ONESTEP, true);
			}
		};

		/**
		 * Do one step forward in the simulation history.
		 */
		public void forward() {
			if (simulatorNeedsInitialConfig()) {
				SimulationSet s = AM1InputDialog.showAndWaitForReturn();
				MachineConfiguration m = s.getMachineConfiguration();
				if (m != null) {
					view.getTablePanel().setInitialConfiguration(
							"<html>" + m.toString() + "</html>");
					steps = s.getSteps();
					view.getToolbar()
							.setButtonStatus(ButtonType.INITIAL, false);
					getHistory().add(new MachineConfiguration(m));
					setCurrentMachineConfiguration(m);
					addResultToTable(m, 0);
					view.getTablePanel().getTableModel().showLastPage();
					view.getToolbar().setButtonStatus(ButtonType.CLEAR, true);
					view.getToolbar().setButtonStatus(ButtonType.BACK, true);
					if (!nextStep()) {
						view.getToolbar().setButtonStatus(ButtonType.ONESTEP,
								false);
						view.getToolbar().setButtonStatus(ButtonType.FORWARD,
								false);
					}
				}
			} else {
				view.getTablePanel().getTableModel().showLastPage();
				view.getToolbar().setButtonStatus(ButtonType.CLEAR, true);
				view.getToolbar().setButtonStatus(ButtonType.BACK, true);
				if (!nextStep()) {
					view.getToolbar()
							.setButtonStatus(ButtonType.ONESTEP, false);
					view.getToolbar()
							.setButtonStatus(ButtonType.FORWARD, false);
				}
			}
			view.getConsolePanel().switchToDescription();
		};

		/**
		 * Do the simulation in one single step.
		 */
		public void onestep() {
			if (simulatorNeedsInitialConfig()) {
				SimulationSet s = AM1InputDialog.showAndWaitForReturn();
				MachineConfiguration m = s.getMachineConfiguration();
				if (m != null) {
					steps = s.getSteps();
					view.getTablePanel().setInitialConfiguration(
							"<html>" + m.toString() + "</html>");
					getHistory().add(new MachineConfiguration(m));
					setCurrentMachineConfiguration(m);
					addResultToTable(m, 0);
					view.getToolbar()
							.setButtonStatus(ButtonType.FORWARD, false);
					view.getToolbar()
							.setButtonStatus(ButtonType.ONESTEP, false);
					view.getToolbar()
							.setButtonStatus(ButtonType.INITIAL, false);
					view.getToolbar().setButtonStatus(ButtonType.BACK, true);
					view.getToolbar().setButtonStatus(ButtonType.CLEAR, true);
					stepToEnd();
					view.getTablePanel().getTableModel().showLastPage();
				}
			} else {
				view.getToolbar().setButtonStatus(ButtonType.FORWARD, false);
				view.getToolbar().setButtonStatus(ButtonType.ONESTEP, false);
				view.getToolbar().setButtonStatus(ButtonType.INITIAL, false);
				view.getToolbar().setButtonStatus(ButtonType.CLEAR, true);
				stepToEnd();
				view.getToolbar().setButtonStatus(ButtonType.BACK, true);
				view.getTablePanel().getTableModel().showLastPage();
			}
			view.getConsolePanel().switchToDescription();
		};

		/**
		 * Open a new file.
		 */
		public void open() {
			String in = view.getEditorPanel().loadCode();
			if (!in.isEmpty()) {
				view.writeFileName(in);
			}
		};

		/**
		 * Save the currently open and maybe edited file.
		 */
		public void save() {
			String out = view.getEditorPanel().saveCode();
			if (!out.isEmpty()) {
				view.writeFileName(out);
			}
		};
	}

	/**
	 * The state which indicates the user is in editing mode.
	 */
	private class Editing extends State {
		@Override
		public void run() {
			AM1Parser am1Parser = new AM1Parser();
			String text = view.getEditorPanel().getEditor().getText();
			if (!text.isEmpty()) {
				if (am1Parser.parse(text)) {
					currentState = new Simulating();
					view.getToolbar().setRunButtonToStop();
					view.getToolbar().setButtonStatus(ButtonType.INITIAL, true);
					view.getToolbar().setButtonStatus(ButtonType.FORWARD, true);
					view.getToolbar().setButtonStatus(ButtonType.ONESTEP, true);
					view.getToolbar().setButtonStatus(ButtonType.TOP, true);
					view.getToolbar().setButtonStatus(ButtonType.BOTTOM, true);
					view.getToolbar().setButtonStatus(ButtonType.OPEN, false);

					view.getEditorPanel().getEditor().setEditable(false);
					view.getConsolePanel()
							.getConsole()
							.setText(
									"<p><b><font color=\"green\">Validation OK!</font></b></p>");
					try {
						setAM1Program(am1Parser.getProgram());
					} catch (IllegalAccessException e) {
						System.err
								.println("You must not call this method before calling parse()!");
						e.printStackTrace();
					}
					String p = getAM1Program().toString();
					view.getEditorPanel().getEditor()
							.setText(p.substring(0, p.length() - 1));
					if (text.equals(p.substring(0, p.length() - 1))) {
						view.getEditorPanel().setModified(false);
						view.setStatusbarModified(false);
					}
				} else {
					view.getConsolePanel().getConsole()
							.setText(am1Parser.getErrorText());
				}
			}
		}
	}

	/**
	 * The state which indicates the user is in simulating mode.
	 */
	private class Simulating extends State {
		@Override
		public void run() {
			currentState = new Editing();
			view.getEditorPanel().getEditor().setEditable(true);
			view.getToolbar().setRunButtonToRun();
			view.getToolbar().setButtonStatus(ButtonType.INITIAL, false);
			view.getToolbar().setButtonStatus(ButtonType.FORWARD, false);
			view.getToolbar().setButtonStatus(ButtonType.ONESTEP, false);
			view.getToolbar().setButtonStatus(ButtonType.BACK, false);
			view.getToolbar().setButtonStatus(ButtonType.CLEAR, false);
			view.getToolbar().setButtonStatus(ButtonType.TOP, false);
			view.getToolbar().setButtonStatus(ButtonType.BOTTOM, false);
			view.getToolbar().setButtonStatus(ButtonType.OPEN, true);

			view.getEditorPanel().removeMarkers();
			Simulator.this.clear();
			view.getTablePanel().setInitialConfiguration(
					"No initial configuration.");

			view.getConsolePanel().switchToConsole();
		}
	}

	/**
	 * Class which handles all incoming user input and manipulates the model as
	 * specified in the MVP-Pattern as part of the presenter.
	 * 
	 * @author Max Leuth&auml;user
	 */
	private class UserInputListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if (ButtonType.RUN.isButtonEvent(cmd)) {
				currentState.run();
			}
			if (ButtonType.TOP.isButtonEvent(cmd)) {
				currentState.top();
			}
			if (ButtonType.BOTTOM.isButtonEvent(cmd)) {
				currentState.bottom();
			}
			if (ButtonType.INITIAL.isButtonEvent(cmd)) {
				currentState.initial();
			}
			if (ButtonType.CLEAR.isButtonEvent(cmd)) {
				currentState.clear();
			}
			if (ButtonType.BACK.isButtonEvent(cmd)) {
				currentState.back();
			}
			if (ButtonType.FORWARD.isButtonEvent(cmd)) {
				currentState.forward();
			}
			if (ButtonType.ONESTEP.isButtonEvent(cmd)) {
				currentState.onestep();
			}
			if (ButtonType.OPEN.isButtonEvent(cmd)) {
				currentState.open();
			}
			if (ButtonType.SAVE.isButtonEvent(cmd)) {
				currentState.save();
			}
		}
	}

	@Override
	public void fireUpdateEvent(UpdateEvent<Integer> update) {
		view.getTablePanel().handleUpdateEvent(update);
	}

	public void setPresentationMode(boolean selected) {
		if (selected) {
			view.getEditorPanel().getEditor().setFont(GuiUtilities.PRESENTATIONEDITORFONT);
			view.getTablePanel().getTable().setFont(GuiUtilities.PRESENTATIONFONT);
			view.getConsolePanel().getDescriptionEditor().setFont(GuiUtilities.PRESENTATIONFONT);
			view.getConsolePanel().getConsole().setFont(GuiUtilities.PRESENTATIONFONT);
		} else {
			view.getEditorPanel().getEditor().setFont(GuiUtilities.STANDARDEDITORFONT);
			view.getTablePanel().getTable().setFont(GuiUtilities.STANDARDFONT);
			view.getConsolePanel().getDescriptionEditor().setFont(GuiUtilities.STANDARDFONT);
			view.getConsolePanel().getConsole().setFont(GuiUtilities.STANDARDFONT);
		}
	}
}
