package org.jalgo.module.bfsdfs.gui.graphview;

/**
 * This enumeration represents the current mode of the <code>InteractiveGraphView</code>
 * @author Anselm Schmidt
 *
 */
public enum EditMode {
	/**
	 *  A mouse click will start a new edge.
	 */
	START_EDGE,
	/**
	 * A mouse click will start a new bidirectional edge.
	 */
	START_DOUBLE_EDGE,
	/**
	 * Elements can be removed with the mouse.
	 */
	ERASE,
	/**
	 * A mouse click will create a new graph node.
	 */
	PUT_NODE,
	/**
	 * A node is moved by the user.
	 */
	MOVE_NODE,
	/**
	 * A node is moved by the user using drag and drop.
	 */
	DRAG_NODE,
	/**
	 * An edge is drawn by the user.
	 */
	DRAW_EDGE,
	/**
	 * An edge is drawn by the user using mouse drag & drop.
	 */
	DRAG_EDGE,
	/**
	 * A bidirectional edge is drawn by the user.
	 */
	DRAW_DOUBLE_EDGE,
	/**
	 * A bidirectional edge is drawn by the user using drag & drop.
	 */
	DRAG_DOUBLE_EDGE,
	/**
	 * Algorithm is active. No interactivity.	
	 */
	ALGORITHM
}
