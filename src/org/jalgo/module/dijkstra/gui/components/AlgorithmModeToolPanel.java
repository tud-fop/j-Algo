package org.jalgo.module.dijkstra.gui.components;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import org.jalgo.main.util.Messages;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.gui.event.AlgorithmModeToolPanelActionHandler;
import org.jalgo.module.dijkstra.model.Graph;
import org.jalgo.module.dijkstra.model.Node;

/**
 * This panel provides tools for the algorithm mode.
 * 
 * @author Alexander Claus
 */
public class AlgorithmModeToolPanel
extends JPanel
implements Observer {

	private Controller controller;
	private AlgorithmModeToolPanelActionHandler action;

	private JComboBox startNodeSelection;
	private JTextField algStepSelection;
	private JLabel ofNStepsLabel;
	private JButton gotoButton;
	private JButton undoBlockStepButton;
	private JButton undoButton;
	private JButton performButton;
	private JButton performBlockStepButton;
	private JToggleButton startAnimButton;

	public AlgorithmModeToolPanel(Controller controller) {
		super(new GridLayout(2, 1, 5, 5));
		this.controller = controller;

		setBorder(BorderFactory.createTitledBorder(new EtchedBorder(),
			Messages.getString("dijkstra", //$NON-NLS-1$
			"AlgorithmModeToolPanel.Tools"))); //$NON-NLS-1$

		action = new AlgorithmModeToolPanelActionHandler(controller, this);

		JPanel northPane = new JPanel();
		northPane.add(Box.createHorizontalStrut(5));
		northPane.setLayout(new BoxLayout(northPane, BoxLayout.LINE_AXIS));
		JLabel startNodeLabel = new JLabel(Messages.getString("dijkstra", //$NON-NLS-1$
			"AlgorithmModeToolPanel.Start_node")); //$NON-NLS-1$
		northPane.add(startNodeLabel);
		northPane.add(Box.createHorizontalStrut(5));
		startNodeSelection = new JComboBox();
		startNodeSelection.setPreferredSize(new Dimension(50, 16));
		startNodeSelection.setActionCommand("startNode");
		startNodeSelection.addActionListener(action);
		northPane.add(startNodeSelection);
		northPane.add(Box.createHorizontalStrut(5));
		JLabel algStepLabel = new JLabel(Messages.getString("dijkstra", //$NON-NLS-1$
			"AlgorithmModeToolPanel.Algorithm_step"), SwingConstants.RIGHT); //$NON-NLS-1$
		northPane.add(algStepLabel);
		northPane.add(Box.createHorizontalStrut(5));
		algStepSelection = new JTextField(10);
		algStepSelection.addKeyListener(action);
		northPane.add(algStepSelection);
		northPane.add(Box.createHorizontalStrut(5));
		ofNStepsLabel = new JLabel("      "); //$NON-NLS-1$
		northPane.add(ofNStepsLabel);
		northPane.add(Box.createHorizontalStrut(5));
		add(northPane);

		JPanel southPane = new JPanel();
		southPane.setLayout(new BoxLayout(southPane, BoxLayout.LINE_AXIS));
		gotoButton = new JButton(Messages.getString("dijkstra", //$NON-NLS-1$
			"AlgorithmModeToolPanel.Go_to")); //$NON-NLS-1$
		gotoButton.setActionCommand("goto");
		gotoButton.addActionListener(action);
		southPane.add(gotoButton);
		Dimension controlButtonDim = new Dimension(40, gotoButton.getPreferredSize().height);
		undoBlockStepButton = new JButton(new ImageIcon(
			Messages.getResourceURL("main", "Icon.Undo_blockstep"))); //$NON-NLS-1$ //$NON-NLS-2$
		undoBlockStepButton.setMaximumSize(controlButtonDim);
		undoBlockStepButton.setPreferredSize(controlButtonDim);
		undoBlockStepButton.setActionCommand("perform"); //$NON-NLS-1$
		undoBlockStepButton.putClientProperty("forward", false);
		undoBlockStepButton.putClientProperty("macroStep", true);
		undoBlockStepButton.addActionListener(action);
		southPane.add(undoBlockStepButton);
		undoButton = new JButton(new ImageIcon(
			Messages.getResourceURL("main", "Icon.Undo_step"))); //$NON-NLS-1$ //$NON-NLS-2$
		undoButton.setMaximumSize(controlButtonDim);
		undoButton.setPreferredSize(controlButtonDim);
		undoButton.setActionCommand("perform"); //$NON-NLS-1$
		undoButton.putClientProperty("forward", false);
		undoButton.putClientProperty("macroStep", false);
		undoButton.addActionListener(action);
		southPane.add(undoButton);
		performButton = new JButton(new ImageIcon(
			Messages.getResourceURL("main", "Icon.Perform_step"))); //$NON-NLS-1$ //$NON-NLS-2$
		performButton.setMaximumSize(controlButtonDim);
		performButton.setPreferredSize(controlButtonDim);
		performButton.setActionCommand("perform"); //$NON-NLS-1$
		performButton.putClientProperty("forward", true);
		performButton.putClientProperty("macroStep", false);
		performButton.addActionListener(action);
		southPane.add(performButton);
		performBlockStepButton = new JButton(new ImageIcon(
			Messages.getResourceURL("main", "Icon.Perform_blockstep"))); //$NON-NLS-1$ //$NON-NLS-2$
		performBlockStepButton.setMaximumSize(controlButtonDim);
		performBlockStepButton.setPreferredSize(controlButtonDim);
		performBlockStepButton.setActionCommand("perform"); //$NON-NLS-1$
		performBlockStepButton.putClientProperty("forward", true);
		performBlockStepButton.putClientProperty("macroStep", true);
		performBlockStepButton.addActionListener(action);
		southPane.add(performBlockStepButton);
		startAnimButton = new JToggleButton(Messages.getString(
			"dijkstra", "AlgorithmModeToolPanel.Start_animation")); //$NON-NLS-1$ //$NON-NLS-2$
		startAnimButton.setActionCommand("startAnim");
		startAnimButton.addActionListener(action);
		southPane.add(startAnimButton);
		add(southPane);

		controller.addObserver(this);
	}

	/**
	 * Updates the enabled state of all control components. This method is used
	 * for blocking user input during animation.
	 * 
	 * @param enabled
	 */
	private void setControlsEnabled(boolean enabled) {
		startNodeSelection.setEnabled(enabled);
		algStepSelection.setEnabled(enabled);
		gotoButton.setEnabled(enabled);
		undoBlockStepButton.setEnabled(enabled);
		undoButton.setEnabled(enabled);
		performButton.setEnabled(enabled);
		performBlockStepButton.setEnabled(enabled);
	}

	/**
	 * Retrieves the step index, which is entered in the textfield.
	 * 
	 * @return the step which the user has selected
	 */
	public int getSelectedStep() {
		return Integer.valueOf(algStepSelection.getText()).intValue()-1;
	}

	/**
	 * Updates the enabled state of the goto-step-button. This method is used
	 * for synchronize this state with the content (empty or not)of the textfield.
	 * 
	 * @param enabled the new enabled state of the goto-button
	 */
	public void setGotoButtonEnabled(boolean enabled) {
		gotoButton.setEnabled(enabled);
	}

	/**
	 * Updates the states of all components.
	 */
	public void update(Observable o, Object arg) {
        Controller controller = (Controller)o;
		if (controller.getEditingMode() != Controller.MODE_ALGORITHM) return;
        Graph graph = controller.getGraph();

        if (controller.getAnimationMillis() > 0) setControlsEnabled(false);
        else setControlsEnabled(true);

        // update startnode combo box
		int iSelectedIndex = startNodeSelection.getSelectedIndex();
		int iMaxNode =
			(graph.getNodeList() == null) ? 0 : graph.getNodeList().size();
		startNodeSelection.removeActionListener(action);
		startNodeSelection.removeAllItems();
		for (int i = 0; i < iMaxNode; i++)
			startNodeSelection.insertItemAt("" + (i + 1), i); //$NON-NLS-1$
		// if no node is selected as startnode, set the selection to node #1
		// and generate the states
		if (iSelectedIndex == -1) {
			Node node = graph.getStartNode();
			if (node != null)
				startNodeSelection.setSelectedIndex(node.getIndex() - 1);
			else startNodeSelection.setSelectedIndex(-1);
		}
		else {
			Node node = graph.getStartNode();
			iSelectedIndex =
				(node == null) ? iSelectedIndex : (node.getIndex() - 1);
			startNodeSelection.setSelectedIndex(iSelectedIndex);
		}
		startNodeSelection.addActionListener(action);

		// update label
		if ((controller.getCurrentStep()+1) > 0)
			algStepSelection.setText("" + (controller.getCurrentStep()+1)); //$NON-NLS-1$
		else algStepSelection.setText(""); //$NON-NLS-1$

		if ((controller.getStepCount()) > 0)
			ofNStepsLabel.setText(Messages.getString(
				"dijkstra", "AlgorithmModeToolPanel.Of") + //$NON-NLS-1$ //$NON-NLS-2$
				(controller.getStepCount()));
		else ofNStepsLabel.setText(" "); //$NON-NLS-1$

		// update enabled state of goto button
		if (gotoButton.isEnabled()) gotoButton.setEnabled(
			algStepSelection.getText().length() > 0);

		// update enabled state of control buttons
		updateButtonState(undoBlockStepButton);
		updateButtonState(undoButton);
		updateButtonState(performButton);
		updateButtonState(performBlockStepButton);

		// update state of start anim button
		startAnimButton.setSelected(controller.getAnimationMillis() > 0);
	}

	/**
	 * Helper method for {@link #update(Observable, Object)}.
	 * 
	 * @param button the button, whose state should be updated
	 */
	private void updateButtonState(JButton button) {
		boolean bEnable = false;

		if ((Boolean)button.getClientProperty("macroStep"))
			bEnable = ((Boolean)button.getClientProperty("forward") ?
			controller.hasNextMacroStep(controller.getCurrentStep()) :
			controller.hasPrevMacroStep(controller.getCurrentStep()));
		else bEnable = ((Boolean)button.getClientProperty("forward") ?
			controller.hasNextStep(controller.getCurrentStep()) :
			controller.hasPrevStep(controller.getCurrentStep()));
		if (controller.getAnimationMillis() > 0 &&
			controller.getEditingMode() == Controller.MODE_ALGORITHM)
			bEnable = false;
		button.setEnabled(bEnable);
	}
}