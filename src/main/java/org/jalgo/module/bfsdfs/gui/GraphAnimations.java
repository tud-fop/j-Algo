package org.jalgo.module.bfsdfs.gui;

import java.awt.Point;

/**
 * Collection of helper methods to animate different graph elements.
 * @author Anselm Schmidt
 */
public abstract class GraphAnimations {
	/**
	 * Animate an edge. Get the size of the edge depending on the animation time.
	 * @param start Start point of the edge.
	 * @param end End point of the edge.
	 * @param clipping Size of the edge part which will be removed due to clipping. 
	 * @param time The time since the animation started, in milliseconds.
	 * @param totalTime The length of the animation, in milliseconds.
	 * @return The new end point of the edge.
	 * @author Anselm Schmidt
	 */
	public static Point animateEdge(Point start, Point end, int clipping, long time,
			long totalTime) {
		if(time == 0) {
			// no animation yet
			return end;
		}
		
		if(time > totalTime) {
			// animation already finished
			return end;
		}
		
		// calculate animation length factor
		double factor = (double) time / (double) totalTime;
		
		// calculate length of the edge
		double length = Math.sqrt(GraphCalculation.calcPointPointDistSqr(start.x,
				start.y, end.x, end.y));
		
		// calculate edge length after clipping
		double lengthAfterClipping = length - clipping;
		
		// change this length using the animation length factor
		lengthAfterClipping = factor * lengthAfterClipping;
		
		// calculate unit vector of the edge
		double unitX = (end.x - start.x) / length;
		double unitY = (end.y - start.y) / length;
		
		// calculate new length
		length = lengthAfterClipping + clipping;
		
		// calculate new end point
		return new Point(start.x + (int) Math.round(unitX * length), start.y + (int)
				Math.round(unitY * length));
	}
	
	/**
	 * Animate a node. Get the alpha value of the node depending on the animation time.
	 * @param alpha The full alpha value of the node.
	 * @param time The time since the animation started, in milliseconds. 
	 * @param totalTime The length of the animation, in milliseconds.
	 * @return The new alpha value of the node.
	 * @author Anselm Schmidt
	 */
	public static int animateNode(int alpha, long time, long totalTime) {
		if(time == 0) {
			// no animation yet
			return 0;
		}
		
		if(time > totalTime) {
			// animation already finished
			return alpha;
		}
		
		// calculate animation alpha factor
		double factor = (double) time / (double) totalTime;
		
		// return the new alpha value
		return (int) Math.round(factor * alpha);
	}
	
	/**
	 * Animate the node cursor. Get the alpha value of the cursor depending on the time.
	 * Will use cosinus to perform a cyclic animation, so <code>time</code> can be as
	 * large as you want.
	 * @param alpha The full alpha value of the node cursor.
	 * Use <code>GUIConstants.ALPHA_100_PERCENT</code> as default value.
	 * @param time The time since the animation started.
	 * @param totalTime The time for one blinking animation cyclus.
	 * This includes one blending in and one blending out animation.
	 */
	public static int animateNodeCursor(int alpha, long time, long totalTime) {
		if(time == 0) {
			// no animation yet
			return 0;
		}
		
		// calculate animation factor by using cosinus 
		double factor = (-Math.cos((double) time / totalTime * 2 * Math.PI) + 1) / 2;
		
		// return the new alpha value
		return (int) Math.round(factor * alpha);
	}
	
	/**
	 * Hide an element. Get the alpha value of the element depending on the time.
	 * @param alpha The full alpha value of the node.
	 * @param time The time since the animation started, in milliseconds.
	 * @param totalTime The length of the animation, in milliseconds.
	 * @return The new alpha value of the node or <code>0</code>,
	 * if <code>totalTime</code> is smaller than <code>time</code>. 
	 * @author Anselm Schmidt
	 */
	public static int hide(int alpha, long time, long totalTime) {
		if(time == 0) {
			// no animation yet
			return alpha;
		}
		
		if(time >= totalTime) {
			// animation already finished
			return 0;
		}
		
		// calculate animation alpha factor
		double factor = (double) time / (double) totalTime;
		
		// return the new alpha value
		return alpha - (int) Math.round(factor * alpha);
	}
}
