package org.jalgo.module.app.controller.undoRedo;

import org.jalgo.module.app.core.graph.Node;

/**
 * Represents the action that a node was added.
 */
public class AddNodeUndoAction implements UndoableAction {

	private Node node;
	private UndoManager undoManager;
	
	/**
	 * Creates a new undo action.
	 * 
	 * @param undoManager The Manager which controls the action.
	 * @param node The node which was added.
	 */
	public AddNodeUndoAction(UndoManager undoManager, Node node) {
		this.undoManager = undoManager;
		this.node = node;
	}

	public void redo() {
		undoManager.getGraphController().addNode(node);		
	}

	public void undo() {
		undoManager.getGraphController().doRemoveNode(node);	
	}

}
