package org.jalgo.module.app.controller.undoRedo;

import java.util.Set;

import org.jalgo.module.app.core.graph.Edge;
import org.jalgo.module.app.core.graph.Node;

/**
 * Represents the action that a node was removed.
 */
public class RemoveNodeUndoAction implements UndoableAction {

	private Node node;
	private UndoManager undoManager;
	private Set<Edge> relatedEdges;
	
	/**
	 * Creates a new undo action.
	 * 
	 * @param undoManager The Manager which controls the action.
	 * @param node The node which was added.
	 * @param relatedEdges A Set of all edges belonging to the node.
	 */
	public RemoveNodeUndoAction(UndoManager undoManager, Node node, Set<Edge> relatedEdges) {
		this.undoManager = undoManager;
		this.node = node;
		this.relatedEdges = relatedEdges;
	}

	public void redo() {
		undoManager.getGraphController().doRemoveNode(node);
	}

	public void undo() {
		undoManager.getGraphController().addUndoNode(node);
		for (Edge edge : relatedEdges) {
			undoManager.getGraphController().addEdge(edge);
		}
	}

}
