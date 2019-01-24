package org.jalgo.module.dijkstra.gui.event;

import java.awt.Point;

import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.MoveNodeAction;
import org.jalgo.module.dijkstra.actions.NewNodeAction;
import org.jalgo.module.dijkstra.gfx.NodeVisual;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.gui.components.GraphDisplay;
import org.jalgo.module.dijkstra.model.Node;
import org.jalgo.module.dijkstra.model.Position;
import org.jalgo.module.dijkstra.util.DefaultExceptionHandler;

/**
 * This class represents the state, which collaborates with the mode
 * {@link org.jalgo.module.dijkstra.gui.Controller#MODE_ADD_MOVE_NODE}.
 * 
 * @author Alexander Claus
 */
public class StateAddMoveNode
extends GraphDisplayState {

	private Node draggedNode;
	private Node highlightedNode;

	public StateAddMoveNode(GraphDisplay display, Controller controller) {
		super(display, controller);
	}

	@Override
	public void mousePressed(Point p) {
		Node clickedNode = display.findNode(p);
		// finding a node means, that the user wants to drag it
		if (clickedNode != null) {
			draggedNode = clickedNode;
			draggedNode.setActive(true);
			draggedNode.getVisual().update();
		}
		// finding no node means, that the user wants to create a new node
		else try {
			new NewNodeAction(controller, p, display.getSize());
			// if succeeded, there should be a node under mouse pointer
			draggedNode = display.findNode(p);
			if (draggedNode != null) {
				draggedNode.setActive(true);
				draggedNode.getVisual().update();
			}
		}
		catch (ActionException ex) {
			new DefaultExceptionHandler(ex);
		}
	}

	@Override
	public void mouseReleased(Point p) {
		// end of dragging
		if (draggedNode != null) {
			try {
				// do not allow to move nodes away from screen!
				new MoveNodeAction(controller, draggedNode,
					new Position(new Point(
						Math.min(Math.max(0, p.x), display.getWidth()),
						Math.min(Math.max(0, p.y), display.getHeight())),
						GraphDisplay.getScreenSize()));
				draggedNode.getVisual().updateLocation(
					GraphDisplay.getScreenSize());
			}
			catch (ActionException ex) {
				new DefaultExceptionHandler(ex);
			}
			draggedNode.setActive(false);
			draggedNode.setHighlighted(true);
			highlightedNode = draggedNode;
			draggedNode.getVisual().update();
			draggedNode = null;
		}
	}

	@Override
	public void mouseClicked(Point p) {
		// this method has no effect
	}

	@Override
	public void mouseMoved(Point p) {
		Node currentNode = display.findNode(p);
		// recently selected node leaved
		if (currentNode != highlightedNode &&
			highlightedNode != null) {
			highlightedNode.setActive(false);
			highlightedNode.setHighlighted(false);
			highlightedNode.getVisual().update();
		}
		highlightedNode = currentNode;
		if (highlightedNode != null) {
			highlightedNode.setHighlighted(true);
			highlightedNode.getVisual().update();
		}
	}

	@Override
	public void mouseDragged(Point p) {
		// do not allow to move nodes away from screen!
		if (draggedNode != null)
			((NodeVisual)draggedNode.getVisual()).setCenter(new Point(
				Math.min(Math.max(0, p.x), display.getWidth()),
				Math.min(Math.max(0, p.y), display.getHeight())));
	}
}