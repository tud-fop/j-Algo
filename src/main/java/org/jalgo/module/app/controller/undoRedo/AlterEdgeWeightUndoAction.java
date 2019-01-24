package org.jalgo.module.app.controller.undoRedo;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.graph.Edge;

/**
 * Represents the action that an edge has changed its weight.
 */
public class AlterEdgeWeightUndoAction implements UndoableAction {

	private UndoManager undoManager;
	private Edge edge;
	private DataType previousWeight;
	private DataType newWeight;
	
	/**
	 * Creates a new undo action.
	 * 
	 * @param undoManager The Manager which controls the action.
	 * @param edge The edge which weight was altered.
	 * @param previousWeight The weight before alteration.
	 */
	public AlterEdgeWeightUndoAction(UndoManager undoManager, Edge edge,
			DataType previousWeight) {
		this.undoManager = undoManager;
		this.edge = edge;
		this.previousWeight = previousWeight;
		this.newWeight = edge.getWeight();
	}

	public void redo() {
		undoManager.getGraphController().alterUndoEdgeWeight(edge, newWeight);
	}

	public void undo() {
		undoManager.getGraphController().alterUndoEdgeWeight(edge, previousWeight);
	}

}
