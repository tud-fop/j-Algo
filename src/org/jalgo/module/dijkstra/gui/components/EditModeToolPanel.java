package org.jalgo.module.dijkstra.gui.components;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.EtchedBorder;

import org.jalgo.main.gui.event.StatusLineUpdater;
import org.jalgo.main.util.Messages;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.gui.event.EditModeToolPanelActionHandler;

/**
 * This panel provides tools for the editing mode.
 * 
 * @author Alexander Claus
 */
public class EditModeToolPanel
extends JPanel {

	// action handlers
	private ButtonGroupManager groupManager;
	private EditModeToolPanelActionHandler action;

	private JToggleButton addMoveNodeButton;
	private JToggleButton addEvalEdgeButton;
	private JToggleButton removeNodeButton;
	private JToggleButton removeEdgeButton;
	JToggleButton invisibleButton;

	public EditModeToolPanel(Controller controller) {
		super(new GridLayout(2, 2, 5, 5));

		setBorder(BorderFactory.createTitledBorder(new EtchedBorder(),
			Messages.getString("dijkstra", "EditModeToolPanel.Tools"))); //$NON-NLS-1$ //$NON-NLS-2$
		ButtonGroup group = new ButtonGroup();

		groupManager = new ButtonGroupManager();
		action = new EditModeToolPanelActionHandler(controller);

		addMoveNodeButton = createToggleButton(
			"EditModeToolPanel.Add_move_node", //$NON-NLS-1$
			"EditModeToolPanel.Add_move_node_tooltip", //$NON-NLS-1$
			"addMoveNode");//$NON-NLS-1$
		group.add(addMoveNodeButton);
		add(addMoveNodeButton);

		addEvalEdgeButton = createToggleButton(
			"EditModeToolPanel.Add_eval_edge", //$NON-NLS-1$
			"EditModeToolPanel.Add_eval_edge_tooltip", //$NON-NLS-1$
			"addEvalEdge"); //$NON-NLS-1$
		group.add(addEvalEdgeButton);
		add(addEvalEdgeButton);

		removeNodeButton = createToggleButton(
			"EditModeToolPanel.Remove_node", //$NON-NLS-1$
			"EditModeToolPanel.Remove_node_tooltip", //$NON-NLS-1$
			"removeNode"); //$NON-NLS-1$
		group.add(removeNodeButton);
		add(removeNodeButton);

		removeEdgeButton = createToggleButton(
			"EditModeToolPanel.Remove_edge", //$NON-NLS-1$
			"EditModeToolPanel.Remove_edge_tooltip", //$NON-NLS-1$
			"removeEdge"); //$NON-NLS-1$
		group.add(removeEdgeButton);
		add(removeEdgeButton);

		// invisible button is for disabling any of the buttons
		invisibleButton = new JToggleButton();
		group.add(invisibleButton);
	}

	/**
	 * Unselects all buttons.
	 */
	@SuppressWarnings("synthetic-access")
	public void reset() {
		invisibleButton.setSelected(true);
		groupManager.lastPressedButton = null;
	}

	/**
	 * Helper method to construct component.
	 * 
	 * @param labelKey the key to get the label text
	 * @param tooltipKey the key to get the tooltip text
	 * @param actionCommand the action command
	 * @return a new <code>JToggleButton</code> with the given properties
	 */
	private JToggleButton createToggleButton(String labelKey,
		String tooltipKey, String actionCommand) {
		JToggleButton button = new JToggleButton(Messages.getString(
			"dijkstra", labelKey)); //$NON-NLS-1$
		button.setToolTipText(Messages.getString(
			"dijkstra", tooltipKey)); //$NON-NLS-1$
		button.setActionCommand(actionCommand);
		button.addActionListener(groupManager);
		button.addActionListener(action);
		button.addMouseListener(StatusLineUpdater.getInstance());
		return button;
	}

	/**
	 * This class is responsible for the behaviour of unselecting a button.
	 */
	private class ButtonGroupManager
	implements ActionListener {

		private JToggleButton lastPressedButton;

		public void actionPerformed(ActionEvent e) {
			if (lastPressedButton == e.getSource()) {
				lastPressedButton = null;
				invisibleButton.setSelected(true);
			}
			else lastPressedButton = (JToggleButton)e.getSource();
		}
	}
}