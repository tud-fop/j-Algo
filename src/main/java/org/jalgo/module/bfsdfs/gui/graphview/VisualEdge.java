package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.util.Calendar;

import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.gui.ComponentUtility;
import org.jalgo.module.bfsdfs.gui.GraphCalculation;

/**
 * <code>VisualEdge</code> represents an animated edge which will be drawn by a
 * <code>GraphView</code> or <code>TreeView</code> element.
 * @author Anselm Schmidt
 *
 */
public class VisualEdge extends VisualGraphElement {
	
	/**
	 * Start node of the edge.
	 */
	private int startNode;
	
	/**
	 * End node of the edge.
	 */
	private int endNode;
	
	/**
	 * <code>True</code>, if edge is bidirectional, <code>False</code> otherwise.
	 */
	private boolean bidirectional;
	
	/**
	 * <code>True</code>, if the start of the edge is focused,
	 * <code>False</code> otherwise.
	 */
	private boolean startFocused;
	
	/**
	 * <code>True</code>, if the end of the edge is focused,
	 * <code>False</code> otherwise.
	 */
	private boolean endFocused;
	
	/**
	 * Drawing strategy for the edge.
	 */
	private EdgeDrawingStrategy drawingStrategy;
	
	/**
	 * <code>True</code> if the method <code>changeBidirectionalEdgeOnce</code> has
	 * already been called. <code>False</code> otherwise. If <code>True</code>, the
	 * bidirectional edge will be changed on the next call of this method.
	 */
	private boolean changing;
	
	/**
	 * Constructor. Creates a new <code>VisualEdge</code> instance.
	 * @param startNode ID of the start node.
	 * @param endNode ID of the end node.
	 * @param bidirectional <code>True</code>, if the edge is bidirectional,
	 * <code>False</code> otherwise.
	 * @param drawingStrategy Used drawing strategy.
	 * @param graphController Used <code>GraphController</code> instance.
	 * @author Anselm Schmidt
	 */
	public VisualEdge(int startNode, int endNode, boolean bidirectional,
			EdgeDrawingStrategy drawingStrategy, GraphController graphController) {
		super(graphController);
		this.startNode = startNode;
		this.endNode = endNode;
		this.bidirectional = bidirectional;
		this.drawingStrategy = drawingStrategy;
		
		startFocused = false;
		endFocused = false;
		changing = false;
		
		// start creation animation
		animationStartTime = Calendar.getInstance().getTimeInMillis();
		animationType = AnimationType.CREATE;
	}
	
	/**
	 * Get the start node of the edge.
	 * @return Id of the start node.
	 * @author Anselm Schmidt
	 */
	public int getStartNode() {
		return startNode;
	}
	
	/**
	 * Get the end node of the edge.
	 * @return Id of the start node.
	 * @author Anselm Schmidt
	 */
	public int getEndNode() {
		return endNode;
	}
	
	/**
	 * Change the drawing strategy of the edge.
	 * @param strategy New drawing strategy.
	 * @author Anselm Schmidt
	 */
	public void changeDrawingStrategy(EdgeDrawingStrategy strategy) {
		drawingStrategy = strategy;
	}

	@Override
	/**
	 * Check, if the specified point is in the boundaries of the edge.
	 * @param point The point to check.
	 * @return <code>True</code>, if the point is in the boundaries,
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	public boolean hitBoundaries(Point point) {
		// checks, if it is a circle edge
		if(startNode == endNode) {
			// circle edge: get node position
			Point pos = this.getGraphController().getNodePosition(startNode);
			
			// calculate center of the circle
			int centerX = pos.x;
			int centerY = pos.y - EDGE_CIRCLE_SIZE / 2;
			
			// calculate squared mininmal and maximal distance
			int sqrMin = (EDGE_CIRCLE_SIZE / 2 - EDGE_BOUNDARIES) *
							(EDGE_CIRCLE_SIZE / 2 - EDGE_BOUNDARIES);
			int sqrMax = (EDGE_CIRCLE_SIZE / 2 + EDGE_BOUNDARIES) *
							(EDGE_CIRCLE_SIZE / 2 + EDGE_BOUNDARIES);
			
			// calculate and round squared distance between hitting point and center
			int sqrDist = (int)
				Math.round(GraphCalculation.calcPointPointDistSqr(point.x, point.y,
						centerX, centerY));
			
			// if distance is between min and max, the circle edge has been hit 
			return sqrMin <= sqrDist && sqrDist <= sqrMax;  
		}

		// no circle edge: get node positions
		Point startPos = this.getGraphController().getNodePosition(startNode);
		Point endPos = this.getGraphController().getNodePosition(endNode);
		
		// create instance of Line2D object
		Line2D line = new Line2D.Float(startPos, endPos);
		
		// calculate distance between line and point
		long distance = Math.round(line.ptSegDist(point));
		
		// checks the size of the distance
		return distance <= EDGE_BOUNDARIES;
	}

	@Override
	/**
	 * Drawing method.
	 * Let the current <code>EdgeDrawingStrategy</code> paint the edge.
	 * @param g Used <code>Graphics</code> instance.
	 * @param animated <code>True</code>, if the element is animated,
	 * <code>False</code> otherwise.
	 * @return <code>True</code>, if the element has to be redrawn due to animations,
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	public boolean paint(Graphics g, boolean animated) {
		// get node positions
		Point startPos = this.getGraphController().getNodePosition(startNode);
		Point endPos = this.getGraphController().getNodePosition(endNode);
		
		if(!animated)  {
			// stop animation
			animationType = AnimationType.NONE;
		}
		
		// draw edge
		return drawingStrategy.paint(g, startPos, endPos, bidirectional, startFocused,
				endFocused, focused, alpha, animationType, animationStartTime,
				ComponentUtility.BEAMER_MODE);	
	}

	@Override
	/**
	 * Checks, if the specified point is on the edge.
	 * @param point The point to check.
	 * @return <code>True</code>, if the point is on the edge,
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	public boolean wasHit(Point point) {
		// checks, if it is a circle edge
		if(startNode == endNode) {
			// circle edge: get node position
			Point pos = this.getGraphController().getNodePosition(startNode);
			
			// calculate center of the circle
			int centerX = pos.x;
			int centerY = pos.y - EDGE_CIRCLE_SIZE / 2;
			
			// calculate squared mininmal and maximal distance
			int sqrMin = (EDGE_CIRCLE_SIZE / 2 - EDGE_WIDTH / 2) *
							(EDGE_CIRCLE_SIZE / 2 - EDGE_WIDTH / 2);
			int sqrMax = (EDGE_CIRCLE_SIZE / 2 + EDGE_WIDTH / 2) *
							(EDGE_CIRCLE_SIZE / 2 + EDGE_WIDTH / 2);
			
			// calculate and round squared distance between hitting point and center
			int sqrDist = (int)
				Math.round(GraphCalculation.calcPointPointDistSqr(point.x, point.y,
						centerX, centerY));
			
			// if distance is between min and max, the circle edge has been hit 
			return sqrMin <= sqrDist && sqrDist <= sqrMax;
		}
		
		// no circle edge: get node positions
		Point startPos = this.getGraphController().getNodePosition(startNode);
		Point endPos = this.getGraphController().getNodePosition(endNode);
		
		// create instance of Line2D object
		Line2D line = new Line2D.Float(startPos, endPos);
		
		// calculate distance between line and point
		long distance = Math.round(line.ptSegDist(point));
		
		// checks the size of the distance
		return distance <= EDGE_WIDTH;
	}

	/**
	 *  Make the edge bidirectional.
	 *  @author Anselm Schmidt 
	 */
	public void makeBidirectional() {
		bidirectional = true;
	}
	
	/**
	 * Remove one direction of a bidirectional edge
	 * @param startPos The id of the start node.
	 * @param endPos The id of the end node.
	 * @author Anselm Schmidt
	 */
	public void removeDirection(int startNode, int endNode) {
		if(this.startNode == startNode && this.endNode == endNode) {
			this.startNode = endNode;
			this.endNode = startNode;
		}
		
		bidirectional = false;
	}
	
	/**
	 * Check, if the edge is bidirectional.
	 * @return <code>True</code>, if the edge is bidirectional,
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	public boolean isBidirectional() {
		return bidirectional;
	}
	
	/**
	 * Set, if the the start of the edge is focused.
	 * @param focused <code>True</code>, if the start of the edge is focused,
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	public void setStartFocused(boolean focused) {
		startFocused = focused;
	}
	
	/**
	 * Set, if the the end of the edge is focused.
	 * @param focused <code>True</code>, if the end of the edge is focused,
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	public void setEndFocused(boolean focused) {
		endFocused = focused;
	}
	
	/**
	 * Change the ids of start and end node.
	 * @param start New id of the start node.
	 * @param end New id of the end node.
	 * @author Anselm Schmidt
	 */
	public void changeEdge(int start, int end) {
		startNode = start;
		endNode = end;
	}
	
	/**
	 * Changes the ids of bidirectional edge nodes.
	 * The method has to be called twice to take effect. 
	 * Throws an IllegalArgumentException if the edge isn't bidirectional.
	 * @param start New id of the first node.
	 * @param end New id of the other node.
	 * @author Anselm Schmidt
	 */
	public void changeBidirectionalEdgeOnce(int start, int end) {
		if(bidirectional) {
			if(changing) {
				// second call: change edge
				startNode = start;
				endNode = end;
				changing = false;
			}
			else {
				// first call: remember call
				changing = true;
			}
		}
		else {
			// edge isn't bidirectional
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * Converting node to string.
	 * @return A string <code>"edge <i>a</i>=><i>b</i>"</code> with the start node
	 * id instead of <code><i>a</i></code> and the end node if instead of
	 * <code><i>b</i></code>. 
	 * @author Anselm Schmidt
	 */
	@Override
	public String toString() {
		if(bidirectional) {
			return "edge " + startNode + "<=>" + endNode;
		}
		
		return "edge " + startNode + "=>" + endNode;
	}

	/**
	 * Get the drawing strategy of the edge.
	 * @return Drawing strategy.
	 * @author Anselm Schmidt
	 */
	public EdgeDrawingStrategy getDrawingStrategy() {
		return drawingStrategy;
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
		Point start = new Point(getGraphController().getNodePosition(startNode));
		Point end = new Point(getGraphController().getNodePosition(endNode));
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
	
	/**
	 * Returns, if an arrow has been focused.
	 * @param endNode The id of the end node (where the arrow is).
	 * @author Ans
	 */
	public boolean isFocused(int endNode) {
		if(this.startNode == endNode) {
			return this.startFocused;
		}
		else if(this.endNode == endNode) {
			return this.endFocused;
		}
		
		return false;
	}
}
