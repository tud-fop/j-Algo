package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Calendar;

import org.jalgo.module.bfsdfs.gui.GUIConstants;
import org.jalgo.module.bfsdfs.gui.GraphAnimations;
import org.jalgo.module.bfsdfs.gui.GraphCalculation;
import org.jalgo.module.bfsdfs.gui.GraphDrawing;

public class AlgoEdgeStrategy implements EdgeDrawingStrategy, GUIConstants {
	/**
	 * Drawing method. Draws the edge of a tree.
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
			boolean focusedStart, boolean focusedEnd, boolean focusedEdge,
			int alpha, AnimationType type, long time, boolean beamerMode) {
		int radius = (int) Math.round((float) NODE_BEAMER_SIZE / 2f);
		boolean repaint = false;
		
		// check, if animated
		if(time > 0) {
			// calculate animation time
			long animation = Calendar.getInstance().getTimeInMillis() - time;
			
			// check animation
			if(type == AnimationType.DELETE) {
				// get current alpha value of deleted edge
				alpha = GraphAnimations.hide(alpha, animation, EDGE_ANIMATION);

				// repaint until invisibility is reached
				repaint = alpha > 1;
			}
			else if(animation < EDGE_ANIMATION && type == AnimationType.CREATE) {
				// calculate distance
				int edgeLength = (int)
						Math.round(Math.sqrt(GraphCalculation.calcPointPointDistSqr(start,
						end)));
				int distance = (int) Math.round((edgeLength - 2 * radius) *
						EDGE_TREE_CLIPPING);
				
				// check max distance
				if(distance > EDGE_TREE_MAXCLIP) {
					distance = EDGE_TREE_MAXCLIP;
				}
					
				// get current end position of the edge
				end = GraphAnimations.animateEdge(start, end, 2 *
						(radius + distance), animation, EDGE_ANIMATION);
				
				// repaint because of animation
				repaint = true;
			}
		}
		
		// select the right colors depending on the focus
		Color edgeColor = EDGE_COLOR;
		Color startColor = EDGE_COLOR;
		Color endColor = EDGE_COLOR;
		
		if(focusedEdge) {
			startColor = EDGE_COLOR_FOCUSED;
			endColor = EDGE_COLOR_FOCUSED;
			edgeColor = EDGE_COLOR_FOCUSED;
		}
		else {
			if(focusedStart) {
				startColor = EDGE_COLOR_FOCUSED;
			}
		
			if(focusedEnd) {
				endColor = EDGE_COLOR_FOCUSED;
			}
		}
		
		// calculate distance
		int edgeLength = (int)
		Math.round(Math.sqrt(GraphCalculation.calcPointPointDistSqr(start, end)));
		int distance = (int) Math.round((edgeLength - 2 * radius) * EDGE_TREE_CLIPPING);
		
		// check max distance
		if(distance > EDGE_TREE_MAXCLIP) {
			distance = EDGE_TREE_MAXCLIP;
		}
		
		// draw clipped edge
		GraphDrawing.drawClippedEdge((Graphics2D) g, start, end, EDGE_TREE_WIDTH,
				bidirectional, radius + distance, radius + distance, startColor,
				endColor, edgeColor, EDGE_TREE_ARROW_WIDTH, EDGE_TREE_ARROW_LENGTH,
				EDGE_CIRCLE_SIZE, alpha);
		
		return repaint;
	}
}
