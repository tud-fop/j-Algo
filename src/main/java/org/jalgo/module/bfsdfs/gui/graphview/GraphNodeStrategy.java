package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Calendar;

import org.jalgo.module.bfsdfs.gui.GUIConstants;
import org.jalgo.module.bfsdfs.gui.GraphAnimations;
import org.jalgo.module.bfsdfs.gui.GraphDrawing;

/**
 * Strategy class to draw a simple graph node.
 * @author Anselm Schmidt
 *
 */
public class GraphNodeStrategy implements NodeDrawingStrategy, GUIConstants {	
	/**
	 * Drawing method. Draws the node of a graph.
	 * @param g Used <code>Graphics</code> instance.
	 * @param id Id of the node.
	 * @param position Position of the node.
	 * @param focused <code>True</code>, if the node is focused,
	 * <code>False</code> otherwise.
	 * @param alpha Alpha value of the drawed node.
	 * @param distance The distance of the node, which will be drawn next to the node.
	 * <code>0</code>, if no distance should be drawn.
	 * @param type The animation type of the node.
	 * <code>AnimationType.NONE</code> if no animation will be performed.
	 * @param time The start time of the node's animation.
	 * @param beamerMode Not used.
	 * @author Anselm Schmidt
	 */
	public boolean paint(Graphics g, int id, Point position, boolean focused,
			int alpha, int distance, AnimationType type, long time, boolean beamerMode) {
		boolean repaint = false;
		
		// calculate animation time
		long animation = Calendar.getInstance().getTimeInMillis() - time;
		
		// check animation
		if(type == AnimationType.DELETE) {
			// animate node: calculate new alpha value and repaint
			alpha = GraphAnimations.hide(alpha, animation, NODE_ANIMATION_TIME);
			repaint = true;
		}
		
		if(focused) {
			// draw focused node
			GraphDrawing.drawNode((Graphics2D) g, String.valueOf(id), position,
					NODE_BEAMER_FONT, NODE_BEAMER_SIZE, NODE_BEAMER_BORDER_SIZE,
					NODE_BORDER_COLOR_FOCUSED, NODE_LABEL_COLOR_FOCUSED,
					NODE_COLOR_TOP, NODE_COLOR_BOTTOM, alpha, NODE_GRADIENT_HEIGHT);
		}
		else {
			// draw non-focused node
			GraphDrawing.drawNode((Graphics2D) g, String.valueOf(id), position,
					NODE_BEAMER_FONT, NODE_BEAMER_SIZE, NODE_BEAMER_BORDER_SIZE,
					NODE_BORDER_COLOR, NODE_LABEL_COLOR, NODE_COLOR_TOP,
					NODE_COLOR_BOTTOM, alpha, NODE_GRADIENT_HEIGHT);
		}
		
		// repaint if needed
		return repaint;
	}
}
