package org.jalgo.module.dijkstra.gui.components;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EtchedBorder;

import org.jalgo.main.util.Messages;
import org.jalgo.module.dijkstra.gui.Controller;

/**
 * The class <code>AlgorithmResultTablePanel</code> represents the result table
 * in algorithm mode. It displays the shortest paths and so on.<br>
 * The event handling for this table is handled in
 * {@link org.jalgo.module.dijkstra.gui.components.ResultTableModel}.
 * 
 * @author Alexander Claus
 */
public class AlgorithmResultTablePanel
extends JPanel {

	public AlgorithmResultTablePanel(Controller controller) {
		setBorder(BorderFactory.createTitledBorder(new EtchedBorder(),
			Messages.getString("dijkstra", //$NON-NLS-1$
				"AlgorithmModeResultTablePanel.Result"))); //$NON-NLS-1$
		setLayout(new BorderLayout());

		JTable table = new JTable(new ResultTableModel(controller));
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(1).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);		
		table.getTableHeader().setReorderingAllowed(false);
		table.setShowGrid(false);
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.getViewport().setBackground(Color.WHITE);

		add(scrollPane, BorderLayout.CENTER);
	}
}