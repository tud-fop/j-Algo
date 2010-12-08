package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.Calendar;

import org.jalgo.module.bfsdfs.gui.GUIConstants;
import org.jalgo.module.bfsdfs.gui.GraphAnimations;
import org.jalgo.module.bfsdfs.gui.GraphDrawing;

public class AlgoFinishedNodeStrategy implements NodeDrawingStrategy, GUIConstants {
	/**
	 * Drawing method. Draws the finished node of a tree.
	 * @param g Used <code>Graphics</code> instance.
	 * @param id Id of the node.
	 * @param position Position of the node.
	 * @param focused <code>True</code>, if the node is focused,
	 * <code>False</code> otherwise.
	 * @param alpha Alpha value of the drawed edge.
	 * @param distance The distance of the node, which will be drawn next to the node.
	 * <code>0</code>, if no distance should be drawn.
	 * @param type The animation type of the edge.
	 * <code>AnimationType.NONE</code> if no animation will be performed.
	 * @param time The creation time of the edge. Can be used for animations.
	 * @param beamerMode <code>True</code>, if the beamer mode has been activated.
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	public boolean paint(Graphics g, int id, Point position, boolean focused,
			int alpha, int distance, AnimationType type, long time, boolean beamerMode) {
		boolean repaint = false;
		Font distanceFont = NODE_DISTANCE_FONT;
		
		// check, if beamer mode is activated
		if(beamerMode) {
			distanceFont = NODE_BEAMER_DISTANCE_FONT;
		}
		
		// calculate animation time
		long animation = Calendar.getInstance().getTimeInMillis() - time;
		
		// check animation
		if(type == AnimationType.DELETE) {
			// animate deleted node: calculate new alpha value and repaint
			alpha = GraphAnimations.hide(alpha, animation, NODE_ANIMATION_TIME);
			repaint = true;
		}			
		else if(animation < NODE_ANIMATION_TIME && type != AnimationType.NONE) {
			// animate node: calculate new alpha value and repaint
			alpha = GraphAnimations.animateNode(alpha, animation, NODE_ANIMATION_TIME);
			repaint = true;
		}
		
		GraphDrawing.drawNode((Graphics2D) g, String.valueOf(id), position,
				NODE_BEAMER_FONT, NODE_BEAMER_SIZE, NODE_BORDER_SIZE,
				NODE_FINISHED_BORDER_COLOR, NODE_LABEL_COLOR_FINISHED,
				NODE_COLOR_FINISHED_TOP, NODE_COLOR_FINISHED_BOTTOM, alpha,
				NODE_GRADIENT_HEIGHT, distanceFont, NODE_DISTANCE_COLOR, distance);
		
		// repaint if needed
		return repaint;
	}
}
