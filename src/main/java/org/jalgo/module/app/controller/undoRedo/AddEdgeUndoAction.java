package org.jalgo.module.app.controller.undoRedo;

import org.jalgo.module.app.core.graph.Edge;

/**
 * Represents the action that an edge was added.
 */
public class AddEdgeUndoAction implements UndoableAction {

	private Edge edge;
	private UndoManager undoManager;

	/**
	 * Creates a new undo action.
	 * 
	 * @param undoManager The Manager which controls the action.
	 * @param edge The edge which was added.
	 */
	public AddEdgeUndoAction(UndoManager undoManager, Edge edge) {
		this.undoManager = undoManager;
		this.edge = edge;
	}

	public void redo() {
		undoManager.getGraphController().addEdge(edge);
	}

	public void undo() {
		undoManager.getGraphController().doRemoveEdge(edge);
	}

}
