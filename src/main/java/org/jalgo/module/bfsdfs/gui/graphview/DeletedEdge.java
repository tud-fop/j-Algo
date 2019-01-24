package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Calendar;

import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.gui.ComponentUtility;

public class DeletedEdge extends VisualGraphElement {
	/**
	 * The start position of the edge.
	 */
	private Point start;
	
	/**
	 * The end position of the edge.
	 */
	private Point end;
	
	/**
	 * <code>True</code>, if edge is bidirectional, <code>False</code> otherwise.
	 */
	private boolean bidirectional;
	
	/**
	 * The time of the deletion.
	 */
	private long time;
	
	/**
	 * Drawing strategy.
	 */
	private EdgeDrawingStrategy drawingStrategy;
	
	/**
	 * Constructor.
	 * @param edge The edge.
	 * @param graphController The used <code>GraphController</code> instance.
	 * @author Anselm Schmidt
	 */
	public DeletedEdge(VisualEdge edge, GraphController graphController) {
		super(graphController);
		
		start = graphController.getNodePosition(edge.getStartNode());
		end = graphController.getNodePosition(edge.getEndNode());
		focused = edge.isFocused();
		drawingStrategy = edge.getDrawingStrategy();
		bidirectional = edge.isBidirectional();
		
		time = Calendar.getInstance().getTimeInMillis();
	}
	
	/**
	 * Constructor for single direction.
	 * @param edge The edge.
	 * @param startNode The id of the direction's start node.
	 * @param endNode The id of the direction's end node.
	 * @param graphController The used <code>GraphController</code> instance.
	 * @author Anselm Schmidt
	 */
	public DeletedEdge(VisualEdge edge, int startNode, int endNode,
			GraphController graphController) {
		super(graphController);
		
		start = graphController.getNodePosition(startNode);
		end = graphController.getNodePosition(endNode);
		focused = edge.isFocused(endNode);
		drawingStrategy = edge.getDrawingStrategy();
		bidirectional = false;
		
		time = Calendar.getInstance().getTimeInMillis();
	}
	
	@Override
	/**
	 * Not used.
	 */
	public boolean hitBoundaries(Point point) {
		return false;
	}

	@Override
	/**
	 * Drawing method. Draws the deleted edge.
	 * @param g Used <code>Graphics</code> interface.
	 * @param animated <code>True</code>, if animations are enabled.
	 * <code>False</code> otherwise.
	 */
	public boolean paint(Graphics g, boolean animated) {
		if(animated) {
			return drawingStrategy.paint(g, start, end, bidirectional, focused, focused,
					focused, ALPHA_100_PERCENT, AnimationType.DELETE, time,
					ComponentUtility.BEAMER_MODE);
		}
		
		return false;
	}

	@Override
	/**
	 * Not used. A deleted edge cannot be hit.
	 */
	public boolean wasHit(Point point) {
		return false;
	}

	@Override
	/**
	 * Get the rectangle around the edge.
	 * @param g Not used.
	 * @return The rectangle around the edge.
	 * @author Anselm Schmidt 
	 */
	public Rectangle getRect(Graphics g) {
		// get the start and the end point 
		Point start = new Point(this.start);
		Point end = new Point(this.end);
		int space = EDGE_WIDTH + EDGE_ARROW_WIDTH;
		
		// correct coordinates
		if(start.x > end.x) {
			int swap = start.x;
			start.x = end.x;
			end.x = swap;
		}
		if(start.y > end.y) {
			int swap = start.y;
			start.y = end.y;
			end.y = swap;
		}
		
		start.x -= space;
		start.y -= space;
		end.x += space;
		end.y += space;
		
		// return rectangle around edge
		return new Rectangle(start.x, start.y, end.x - start.x, end.y - start.y);
	}

}
