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
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import org.jalgo.module.am1simulator.presenter.Simulator;
import org.jalgo.module.am1simulator.presenter.UpdateEvent;
import org.jalgo.module.am1simulator.presenter.UpdateListener;
import org.jalgo.module.am1simulator.model.AM1PagingTableModel;

/**
 * Panel which is used to show the simulation steps with their specific AM1
 * configurations.
 * 
 * @author Max Leuth&auml;user
 */
public class TablePanel extends JPanel implements UpdateListener<Integer> {
	private static final long serialVersionUID = 1L;
	private JLabel steps, currentStep;
	private AM1PagingTableModel amTableModel;
	private JTable amTable;
	private JLabel initialeConfig;
	private Simulator simulator;

	public TablePanel(Simulator simulator) {
		this.simulator = simulator;
		setBorder(BorderFactory.createTitledBorder("Simulation"));
		setLayout(new BorderLayout(10, 10));

		JPanel stepsPanel = new JPanel();
		stepsPanel.setLayout(new BorderLayout(10, 10));
		stepsPanel.setBorder(BorderFactory.createTitledBorder("Progress:"));

		steps = new JLabel();
		initialeConfig = new JLabel("No initial configuration.");
		initialeConfig.setHorizontalAlignment(SwingConstants.RIGHT);
		currentStep = new JLabel("Not started yet.");

		stepsPanel.add(steps, BorderLayout.WEST);
		stepsPanel.add(currentStep, BorderLayout.EAST);

		amTableModel = new AM1PagingTableModel();
		amTable = new JTable(amTableModel);
		amTable.setDefaultRenderer(Object.class, new AMTableRenderer());

		amTable.setRowHeight(amTable.getRowHeight() + 5);
		amTable.setShowGrid(false);
		amTable.setIntercellSpacing(new Dimension(12, 2));
		amTable.getTableHeader().setReorderingAllowed(false);

		add(initialeConfig, BorderLayout.NORTH);
		add(amTableModel.createPagingScrollPaneForTable(amTable),
				BorderLayout.CENTER);
		add(stepsPanel, BorderLayout.SOUTH);
	}

	/**
	 * Set the range label to s.
	 * 
	 * @param s
	 */
	public void setRange(String s) {
		steps.setText(s);
	}

	/**
	 * Set the step label to s.
	 * 
	 * @param s
	 */
	public void setStep(String s) {
		currentStep.setText(s);
	}

	/**
	 * Set a new initial configuration to the config label.
	 * 
	 * @param s
	 */
	public void setInitialConfiguration(String s) {
		initialeConfig.setText(s);
	}

	/**
	 * @return the used {@link AM1PagingTableModel}.
	 */
	public AM1PagingTableModel getTableModel() {
		return amTableModel;
	}

	/**
	 * @return the used {@link JTable}.
	 */
	public JTable getTable() {
		return amTable;
	}

	@Override
	public void handleUpdateEvent(UpdateEvent<Integer> event) {
		int pc = event.getValues().get(0);
		amTable.scrollRectToVisible(amTable.getCellRect(
				amTable.getRowCount() - 1, amTable.getColumnCount(), true));
		int p = pc < 1 ? 1 : pc;
		simulator.getView().getEditorPanel().removeMarkers();
		if (p <= simulator.getAM1Program().size()) {
			simulator.getView().getEditorPanel()
					.markStatement(simulator.getAM1Program().get(p - 1));
		}
	}
}
