package org.jalgo.module.dijkstra.gui.event;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.SetEditingModeAction;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.util.DefaultExceptionHandler;

/**
 * This class handles all events around the tool panel in editing mode.
 * 
 * @author Alexander Claus
 */
public class EditModeToolPanelActionHandler
implements ActionListener {

	private final Controller controller;

	public EditModeToolPanelActionHandler(Controller controller) {
		this.controller = controller;
	}

	/**
	 * Handles button clicks.
	 */
	public void actionPerformed(ActionEvent e) {
		int mode = controller.getEditingMode();
		int newMode = Controller.MODE_NO_TOOL_ACTIVE;
		if (e.getActionCommand().equals("addMoveNode"))
			newMode = Controller.MODE_ADD_MOVE_NODE;
		else if (e.getActionCommand().equals("addEvalEdge"))
			newMode = Controller.MODE_ADD_WEIGH_EDGE;
		else if (e.getActionCommand().equals("removeNode"))
			newMode = Controller.MODE_DELETE_NODE;
		else if (e.getActionCommand().equals("removeEdge"))
			newMode = Controller.MODE_DELETE_EDGE;
		try {
			// current mode disabled
			if (mode == newMode) new SetEditingModeAction(
				controller, Controller.MODE_NO_TOOL_ACTIVE);
			else new SetEditingModeAction(controller, newMode);
		}
		catch (ActionException ex) {
			new DefaultExceptionHandler(ex);
		}
		controller.setModifiedFlag();
	}
}