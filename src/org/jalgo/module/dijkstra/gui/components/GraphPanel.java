package org.jalgo.module.dijkstra.gui.components;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import org.jalgo.main.gui.event.StatusLineUpdater;
import org.jalgo.main.util.Messages;
import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.RescaleGraphAction;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.util.DefaultExceptionHandler;

/**
 * The class <code>GraphPanel</code> holds the <code>GraphDisplay</code>
 * component and, in editing mode, a button to rearrange the nodes.
 * 
 * @author Alexander Claus
 */
public class GraphPanel
extends JPanel {

	private JButton arrangeButton;
	private JPanel buttonPane;
	private GraphDisplay graphDisplay;

	public GraphPanel(final Controller controller) {
		super(new BorderLayout(5, 5));
		setBorder(BorderFactory.createTitledBorder(new EtchedBorder(),
			Messages.getString("dijkstra", "GraphPanel.Graph"))); //$NON-NLS-1$ //$NON-NLS-2$
		graphDisplay = new GraphDisplay(controller);
		add(graphDisplay, BorderLayout.CENTER);

		arrangeButton = new JButton(Messages.getString("dijkstra", //$NON-NLS-1$
			"GraphPanel.Arrange_nodes_automatically")); //$NON-NLS-2$
		arrangeButton.setToolTipText(Messages.getString("dijkstra", //$NON-NLS-1$)
			"GraphPanel.Arrange_tooltip")); //$NON-NLS-2$
		arrangeButton.addMouseListener(StatusLineUpdater.getInstance());
		arrangeButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					new RescaleGraphAction(controller);
				}
				catch (ActionException ex) {
					new DefaultExceptionHandler(ex);
				}
			}
		});
		buttonPane = new JPanel();
		buttonPane.setLayout(new BorderLayout());
		buttonPane.add(arrangeButton, BorderLayout.EAST);
	}

	/**
	 * Updates the controller mode.
	 * 
	 * @param isEditMode <code>true</code>, if the controller is in editing mode,
	 * 					<code>false</code>, otherwise
	 */
	public void setEditMode(boolean isEditMode) {
		if (isEditMode) add(buttonPane, BorderLayout.SOUTH);
		else remove(buttonPane);
		updateUI();
		validate();
	}
}