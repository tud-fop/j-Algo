package org.jalgo.module.app.controller.undoRedo;

import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.Set;

import org.jalgo.module.app.controller.GraphController;
import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.graph.Edge;
import org.jalgo.module.app.core.graph.Node;

/**
 * Represents an object, which can handle undo and redo operations.
 */
public class UndoManager {

	/**
	 * The undo stack.
	 */
	private LinkedList<UndoableAction> undoableActions;

	/**
	 * The redo stack.
	 */
	private LinkedList<UndoableAction> redoableActions;
	private GraphController graphController;

	/**
	 * Initialized the undo and redo stacks and sets the reference to the
	 * controller.
	 * 
	 * @param graphController The link back to the graph controller.
	 */
	public UndoManager(GraphController graphController) {
		this.graphController = graphController;
		undoableActions = new LinkedList<UndoableAction>();
		redoableActions = new LinkedList<UndoableAction>();
	}

	/**
	 * Puts a new undo object which represented an add action on the undo stack
	 * and clears the redo stack.
	 * 
	 * @param node
	 *            The node which was added.
	 */
	public void addUndoAddAction(Node node) {
		undoableActions.addFirst(new AddNodeUndoAction(this, node));
		redoableActions = new LinkedList<UndoableAction>();
	}

	/**
	 * Puts a new undo object which represented an remove action on the undo
	 * stack and clears the redo stack.
	 * 
	 * @param node
	 *            The node which was removed.
	 * @param relatedEdges
	 *            The edges belonging to the node which will removed. They are
	 *            needed for restoring.
	 */
	public void addUndoRemoveAction(Node node, Set<Edge> relatedEdges) {
		undoableActions.addFirst(new RemoveNodeUndoAction(this, node,
				relatedEdges));
		redoableActions = new LinkedList<UndoableAction>();
	}

	/**
	 * Puts a new undo object which represented an add action on the undo stack
	 * and clears the redo stack.
	 * 
	 * @param edge
	 *            The edge which was added.
	 */
	public void addUndoAddAction(Edge edge) {
		undoableActions.addFirst(new AddEdgeUndoAction(this, edge));
		redoableActions = new LinkedList<UndoableAction>();
	}

	/**
	 * Puts a new undo object which represented an remove action on the undo
	 * stack and clears the redo stack.
	 * 
	 * @param edge
	 *            The edge which was removed.
	 */
	public void addUndoRemoveAction(Edge edge) {
		undoableActions.addFirst(new RemoveEdgeUndoAction(this, edge));
		redoableActions = new LinkedList<UndoableAction>();
	}

	/**
	 * Puts a new undo object which represented an action that altered the
	 * weight of an edge on the undo stack and clears the redo stack.
	 * 
	 * @param edge
	 * @param previousWeight
	 */
	public void addUndoWeightAlteredAction(Edge edge, DataType previousWeight) {
		undoableActions.addFirst(new AlterEdgeWeightUndoAction(this, edge,
				previousWeight));
		redoableActions = new LinkedList<UndoableAction>();
	}

	/**
	 * Puts a new undo object which represented an action that altered the
	 * position of a node on the undo stack and clears the redo stack.
	 * 
	 * @param node
	 * @param previousPosition
	 */
	public void addUndoNodePositionAlteredAction(Node node,
			Point2D previousPosition) {
		undoableActions.addFirst(new AlterNodePositionUndoAction(this, node,
				previousPosition));
		redoableActions = new LinkedList<UndoableAction>();
	}

	/**
	 * Gets the undo action number.
	 * 
	 * @return The number of possible undo actions.
	 */
	public int possibleUndos() {
		return undoableActions.size();
	}

	/**
	 * Gets the redo action number.
	 * 
	 * @return The number of possible redo actions.
	 */
	public int possibleRedos() {
		return redoableActions.size();
	}

	/**
	 * Gets the possibility of a undo action.
	 * 
	 * @return <code>true</code> if <code>possibleUndos()</code> greater
	 *         than 0.
	 */
	public boolean isUndoable() {
		return possibleUndos() > 0;
	}

	/**
	 * Gets the possibility of a redo action.
	 * 
	 * @return <code>true</code> if <code>possibleRedos()</code> greater
	 *         than 0.
	 */
	public boolean isRedoable() {
		return possibleRedos() > 0;
	}

	/**
	 * Performs an undo action.
	 */
	public void undo() {
		if (isUndoable()) {
			UndoableAction action = undoableActions.removeFirst();
			action.undo();
			redoableActions.addFirst(action);
		}
	}

	/**
	 * Performs an redo action.
	 */
	public void redo() {
		if (isRedoable()) {
			UndoableAction action = redoableActions.removeFirst();
			action.redo();
			undoableActions.addFirst(action);
		}
	}

	/**
	 * Provides the link to the GraphController.
	 * 
	 * @return The GraphController.
	 */
	public GraphController getGraphController() {
		return graphController;
	}

}