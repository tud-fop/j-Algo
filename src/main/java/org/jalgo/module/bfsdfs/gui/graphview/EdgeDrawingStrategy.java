package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Graphics;
import java.awt.Point;

/**
 * Interface for the drawing strategy of an edge
 * @author Anselm Schmidt
 *
 */
interface EdgeDrawingStrategy {
	/**
	 * Drawing method. Draws the edge of a graph or tree.
	 * @param g Used <code>Graphics</code> instance.
	 * @param start Start point of the edge.
	 * @param end End point of the edge.
	 * @param bidirectional <code>True</code>, if the edge is bidirectional,
	 * <code>False</code> otherwise.
	 * @param focusedStart <code>True</code>, if the start arrow of the edge is focused,
	 * <code>False</code> otherwise.
	 * @param focusedEnd <code>True</code>, if the end arrow of the edge is focused,
	 * <code>False</code> otherwise.
	 * @param focusedEdge <code>True</code>, if the edge is focused,
	 * <code>False</code> otherwise.
	 * @param alpha Alpha value of the drawed edge.
	 * @param type The animation type of the edge.
	 * <code>AnimationType.NONE</code> if no animation will be performed.
	 * @param time The animation start time of the edge.
	 * @param beamerMode <code>True</code>, if the beamer mode has been activated.
	 * <code>False</code> otherwise.
	 * @return <code>True</code>, if a repaint is needed because of animations,
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 * @param animationType 
	 */
	public boolean paint(Graphics g, Point start, Point end, boolean bidirectional,
			boolean focusedStart, boolean focusedEnd, boolean focusedEdge,
			int alpha, AnimationType type, long time, boolean beamerMode);
}
