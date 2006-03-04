package org.jalgo.module.dijkstra.gui.event;

import java.awt.Point;

import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.DeleteNodeAction;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.gui.components.GraphDisplay;
import org.jalgo.module.dijkstra.model.Node;
import org.jalgo.module.dijkstra.util.DefaultExceptionHandler;

/**
 * This class represents the state, which collaborates with the mode
 * {@link org.jalgo.module.dijkstra.gui.Controller#MODE_DELETE_NODE}.
 * 
 * @author Alexander Claus
 */
public class StateRemoveNode
extends GraphDisplayState {

	private Node selectedNode;

	private Node toBeDeleted;

	public StateRemoveNode(GraphDisplay display, Controller controller) {
		super(display, controller);
	}

	@Override
	public void mousePressed(Point p) {
		toBeDeleted = display.findNode(p);
		if (toBeDeleted != null) {
			toBeDeleted.setActive(true);
			toBeDeleted.getVisual().update();
		}
	}

	@Override
	public void mouseReleased(Point p) {
		Node currentNode = display.findNode(p);
		if (currentNode != null && currentNode == toBeDeleted) {
			try {
				new DeleteNodeAction(controller, toBeDeleted);
			}
			catch (ActionException ex) {
				new DefaultExceptionHandler(ex);
			}
		}
		toBeDeleted = null;
	}

	@Override
	public void mouseClicked(Point p) {
		// this method has no effect
	}

	@Override
	public void mouseMoved(Point p) {
		Node currentNode = display.findNode(p);
		// mouse leaved selected node
		if (currentNode != selectedNode && selectedNode != null) {
			selectedNode.setActive(false);
			selectedNode.setHighlighted(false);
			selectedNode.getVisual().update();
		}
		selectedNode = currentNode;
		// mouse entered currentNode
		if (selectedNode != null) {
			selectedNode.setHighlighted(true);
			selectedNode.getVisual().update();
		}
	}

	@Override
	public void mouseDragged(Point p) {
		Node currentNode = display.findNode(p);
		// mouse leaved selectedNode
		if (selectedNode != null && currentNode != selectedNode) {
			selectedNode.setActive(false);
			selectedNode.setHighlighted(false);
			selectedNode.getVisual().update();
		}
		selectedNode = currentNode;
		// mouse entered currentNode
		if (selectedNode != null) {
			if (selectedNode == toBeDeleted) selectedNode.setActive(true);
			selectedNode.setHighlighted(true);
			selectedNode.getVisual().update();
		}
	}
}