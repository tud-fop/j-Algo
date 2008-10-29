package org.jalgo.module.app.controller.undoRedo;

import java.awt.geom.Point2D;

import org.jalgo.module.app.core.graph.Node;

/**
 * Represents the action that a node has changed its position.
 */
public class AlterNodePositionUndoAction implements UndoableAction {

	UndoManager undoManager;
	Node node;
	Point2D previousPosition;
	Point2D newPosition;
	
	/**
	 * Creates a new undo action.
	 * 
	 * @param undoManager The Manager which controls the action.
	 * @param node The node which position was altered.
	 * @param previousPosition The node position before the alteration.
	 */
	public AlterNodePositionUndoAction(UndoManager undoManager, Node node,
			Point2D previousPosition) {
		this.undoManager = undoManager;
		this.node = node;
		this.previousPosition = previousPosition;
		this.newPosition = node.getLocation();
	}

	public void redo() {
		undoManager.getGraphController().alterUndoNodePosition(node, newPosition);
	}

	public void undo() {
		undoManager.getGraphController().alterUndoNodePosition(node, previousPosition);
	}

}
