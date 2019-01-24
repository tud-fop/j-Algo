package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.gui.GraphDrawing;

/**
 * Represents an edge that has not been created yet.
 * This class is used when the user is drawing a new edge. 
 * @author Anselm Schmidt
 */
public class VisualTempEdge extends VisualGraphElement {
	/**
	 * Start node.
	 */
	protected VisualNode startNode;
	
	/**
	 * End node or <code>null</code>, if no end node is used.  
	 */
	protected VisualNode endNode;
	
	/**
	 * Position of the end point, if no end node is used.
	 */
	protected Point endPoint;
	
	/**
	 * Is temporary edge bidirectional?
	 */
	protected boolean bidirectional;
	
	/**
	 * Constructor. Creates a temporary edge.
	 * @param startNode Start node of the temporary edge.
	 * @param endNode End node of the temporary edge or <code>null</code>
	 * if no end node is used.
	 * @param endPoint End point of the temporary edge.
	 * Will be ignored if an end node is used.
	 * @param bidirectional <code>True</code> if the temporary edge bidirectional,
	 * <code>False</code> otherwise.
	 * @param graphController Used <code>GraphController</code> instance.
	 * @author Anselm Schmidt
	 */
	public VisualTempEdge(VisualNode startNode, VisualNode endNode, Point endPoint,
			boolean bidirectional, GraphController graphController) {
		super(graphController);
		this.startNode = startNode;
		this.endNode = endNode;
		this.bidirectional = bidirectional;
		
		if(endNode == null) {
			// no end node used: use end point
			this.endPoint = endPoint;
		}
	}
	
	@Override
	/**
	 * Unused method. A temporary edge cannot be hit.
	 * @author Anselm Schmidt
	 */
	public boolean hitBoundaries(Point point) {
		return false;
	}

	@Override
	/**
	 * Drawing method. Draws the temporary edge.
	 * @param g Used <code>Graphics</code> instance.
	 * @param animated Not used.
	 * Temporary graph elements don't have any animations.
	 * @author Anselm Schmidt
	 */
	public boolean paint(Graphics g, boolean animated) {
		if(startNode != null && (endNode != null || endPoint != null)) {
			int radius = (int) Math.round((float) NODE_BEAMER_SIZE / 2f);
			
			if(endNode == null) {
				// no end node is used: draw semi-clipped edge
				GraphDrawing.drawClippedEdge(g, startNode.getPosition(), endPoint,
						EDGE_WIDTH, bidirectional, radius, 0, EDGE_COLOR, EDGE_COLOR,
						EDGE_COLOR, EDGE_ARROW_WIDTH, EDGE_ARROW_LENGTH,
						EDGE_CIRCLE_SIZE, TEMP_ALPHA_VALUE);
			}
			else {
				// end node is used: draw fully clipped edge
				GraphDrawing.drawClippedEdge(g, startNode.getPosition(),
						endNode.getPosition(), EDGE_WIDTH, bidirectional, radius,
						radius, EDGE_COLOR, EDGE_COLOR, EDGE_COLOR, EDGE_ARROW_WIDTH,
						EDGE_ARROW_LENGTH, EDGE_CIRCLE_SIZE, TEMP_ALPHA_VALUE);
			}
		}
		
		// no repaint needed
		return false;
	}

	@Override
	/**
	 * Unused method. A temporary edge cannot be hit.
	 * @author Anselm Schmidt
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
		// get start and end coordinates 
		Point start = new
			Point(this.getGraphController().getNodePosition(startNode.getId()));
		Point end = new Point(endPoint);
		
		if(endNode != null) {
			end = this.getGraphController().getNodePosition(endNode.getId());
		}
		
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
		
		// return rectangle around edge
		return new Rectangle(start.x, start.y, end.x - start.x, end.y - start.y);
	}
}
