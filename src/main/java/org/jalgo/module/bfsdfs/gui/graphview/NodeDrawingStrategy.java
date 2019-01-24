package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Graphics;
import java.awt.Point;

/**
 * Interface for the drawing strategy of a node.
 * @author Anselm Schmidt
 */
interface NodeDrawingStrategy {
	/**
	 * Drawing method. Draws the node of a graph or tree.
	 * @param g Used <code>Graphics</code> instance.
	 * @param id Id of the node.
	 * @param position Position of the node.
	 * @param focused <code>True</code>, if the node is focused,
	 * <code>False</code> otherwise.
	 * @param alpha Alpha value of the drawed node.
	 * @param distance The distance of the node, which will be drawn next to the node.
	 * <code>0</code>, if no distance should be drawn.
	 * @param type The type of the current animation.
	 * <code>AnimationType.NONE</code> if no animation will be performed.
	 * @param time The start time of the current animation.
	 * @param beamerMode <code>True</code>, if the beamer mode has been activated.
	 * <code>False</code> otherwise.
	 * @return <code>True</code>, if the element has to be redrawn due to animations,
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	public boolean paint(Graphics g, int id, Point position, boolean focused,
			int alpha, int distance, AnimationType type, long time, boolean beamerMode);
}
