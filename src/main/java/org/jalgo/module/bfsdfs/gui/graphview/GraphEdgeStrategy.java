package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Calendar;

import org.jalgo.module.bfsdfs.gui.GUIConstants;
import org.jalgo.module.bfsdfs.gui.GraphAnimations;
import org.jalgo.module.bfsdfs.gui.GraphDrawing;

/**
 * Strategy class to draw a simple graph edge.
 * @author Anselm Schmidt
 */
public class GraphEdgeStrategy implements EdgeDrawingStrategy, GUIConstants {	
	/**
	 * Drawing method. Draws the edge of a graph.
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
	 * @param time The start time of the edge's animation.
	 * @param beamerMode Not used.
	 * @return <code>True</code>, if a repaint is needed because of animations,
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	public boolean paint(Graphics g, Point start, Point end, boolean bidirectional,
			boolean focusedStart, boolean focusedEnd, boolean focusedEdge, int alpha,
			AnimationType type, long time, boolean beamerMode) {
		boolean repaint = false;
		
		// calculate node radius		
		int radius = (int) Math.round((float) NODE_BEAMER_SIZE / 2f);
		
		// calculate animation time
		long animation = Calendar.getInstance().getTimeInMillis() - time;
		
		// check animation
		if(type == AnimationType.DELETE) {
			// animate node: calculate new alpha value and repaint
			alpha = GraphAnimations.hide(alpha, animation, NODE_ANIMATION_TIME);
			repaint = true;
		}
		
		// select the right colors depending on the focus
		Color edgeColor = EDGE_COLOR;
		Color startColor = EDGE_COLOR;
		Color endColor = EDGE_COLOR;
		
		if(focusedEdge) {
			edgeColor = EDGE_COLOR_FOCUSED;
		}
		if(focusedStart) {
			startColor = EDGE_COLOR_FOCUSED;
		}
		
		if(focusedEnd) {
			endColor = EDGE_COLOR_FOCUSED;
		}
		
		// draw clipped edge
		GraphDrawing.drawClippedEdge((Graphics2D) g, start, end, EDGE_WIDTH,
				bidirectional, radius, radius, startColor, endColor, edgeColor,
				EDGE_ARROW_WIDTH, EDGE_ARROW_LENGTH, EDGE_CIRCLE_SIZE, alpha);
		
		// repaint if needed
		return repaint;
	}
}
