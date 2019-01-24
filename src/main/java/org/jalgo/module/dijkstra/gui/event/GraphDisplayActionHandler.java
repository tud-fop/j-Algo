package org.jalgo.module.dijkstra.gui.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.gui.components.GraphDisplay;

/**
 * This class handles mouse events on the graph display component. The events
 * are delegated to state objects. So this class represents the client in the
 * state design pattern.
 * 
 * @author Alexander Claus
 */
public class GraphDisplayActionHandler
implements MouseListener, MouseMotionListener {

	private final GraphDisplay display;
	private final Controller controller;

	private GraphDisplayState currentState;
	private GraphDisplayState addMoveNodeState;
	private GraphDisplayState addEvalEdgeState;
	private GraphDisplayState removeNodeState;
	private GraphDisplayState removeEdgeState;

	public GraphDisplayActionHandler(GraphDisplay display, Controller controller) {
		this.display = display;
		this.controller = controller;

		addMoveNodeState = new StateAddMoveNode(display, controller);
		addEvalEdgeState = new StateAddEvalEdge(display, controller);
		removeNodeState = new StateRemoveNode(display, controller);
		removeEdgeState = new StateRemoveEdge(display, controller);
	}

	private void updateState() {
		switch (controller.getEditingMode()) {
			case Controller.MODE_ADD_MOVE_NODE:
				currentState = addMoveNodeState;
				break;
			case Controller.MODE_ADD_WEIGH_EDGE:
				currentState = addEvalEdgeState;
				break;
			case Controller.MODE_DELETE_NODE:
				currentState = removeNodeState;
				break;
			case Controller.MODE_DELETE_EDGE:
				currentState = removeEdgeState;
				break;
			case Controller.MODE_NO_TOOL_ACTIVE:
				currentState = null;
				break;
			case Controller.MODE_ALGORITHM:
				currentState = null;
				break;
		}

	}

	public void mouseClicked(MouseEvent e) {
		updateState();
		if (currentState != null) currentState.mouseClicked(e.getPoint());
		display.repaint();
	}

	public void mousePressed(MouseEvent e) {
		updateState();
		if (currentState != null) currentState.mousePressed(e.getPoint());
		display.repaint();
	}

	public void mouseReleased(MouseEvent e) {
		updateState();
		if (currentState != null) currentState.mouseReleased(e.getPoint());
		display.repaint();
	}

	public void mouseDragged(MouseEvent e) {
		updateState();
		if (currentState != null) currentState.mouseDragged(e.getPoint());
		display.repaint();
	}

	public void mouseMoved(MouseEvent e) {
		updateState();
		if (currentState != null) currentState.mouseMoved(e.getPoint());
		display.repaint();
	}

	public void mouseEntered(MouseEvent e) {
	// this method has no effect
	}

	public void mouseExited(MouseEvent e) {
	// this method has no effect
	}
}