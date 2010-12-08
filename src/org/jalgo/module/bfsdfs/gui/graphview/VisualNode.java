package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Calendar;

import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.gui.ComponentUtility;
import org.jalgo.module.bfsdfs.gui.GraphCalculation;

/**
 * <code>VisualNode</code> represents an animated graph or tree node which will be
 * drawn in a <code>GraphView</code> or <code>TreeView</code> element.
 * @author Anselm Schmidt
 *
 */
public class VisualNode extends VisualGraphElement {
	/**
	 * Id of the node.
	 */
	protected int id;
	
	/**
	 * Distance of the node. Only used by tree nodes.
	 */
	protected int distance;
	
	/**
	 * Drawing strategy for the edge.
	 */
	protected NodeDrawingStrategy drawingStrategy;
	
	/**
	 * Constructor. Creates a new <code>VisualNode</code> instance.
	 * @param id Number of the new node.
	 * @param position Position of the new node.
	 * @param drawingStrategy Used drawing strategy.
	 * @param graphController Used <code>GraphController</code> instance.
	 * @author Anselm Schmidt
	 */
	public VisualNode(int id, Point position, NodeDrawingStrategy drawingStrategy,
			GraphController graphController) {
		super(graphController);
		this.id = id;
		this.position = position;
		this.drawingStrategy = drawingStrategy;
		
		// start creation animation
		animationStartTime = Calendar.getInstance().getTimeInMillis();
		animationType = AnimationType.CREATE;
	}
	
	/**
	 * Copy constructor.
	 * Creates a clone of an existing <code>VisualNode</code> instance.
	 * @param node Existing node.
	 * @param graphController Used <code>GraphController</code> instance.
	 * @author Anselm Schmidt
	 */
	public VisualNode(VisualNode node, GraphController graphController) {
		super(graphController);
		id = node.getId();
		position = node.getPosition();
		distance = node.getDistance();
		drawingStrategy = node.getDrawingStrategy();
		
		// start creation animation
		animationStartTime = Calendar.getInstance().getTimeInMillis();
		animationType = AnimationType.CREATE;
	}
	
	/**
	 * Check, if the current coordinates hit the node.
	 * @param point The coordinates to be checked.
	 * @return <code>True</code> if the coordinates hit the node,
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	@Override
	public boolean wasHit(Point point) {
		// checks if the distance between point and position is smaller than the radius
		return point.distance(position) <= Math.round(NODE_BEAMER_SIZE / 2);
	}
	
	/**
	 * Check, if the current coordinates hit the boundaries of the node.
	 * @param point The coordinates to be checked.
	 * @return <code>True</code> if the coordinates hit the node boundaries,
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	@Override
	public boolean hitBoundaries(Point point) {
		// create rectangle for the boundaries of the node
		Rectangle boundaries = new Rectangle();
		
		// calculate top left corner
		boundaries.x = position.x - NODE_SIZE;
		boundaries.y = position.y - NODE_SIZE;
		
		// calculate size
		boundaries.width = 2 * NODE_SIZE;
		boundaries.height = 2 * NODE_SIZE;
		
		// check if the point lays in the boundaries
		return boundaries.contains(point);
	}
	
	/**
	 * Set a new node position.
	 * @param position The new position of the node.
	 * @author Anselm Schmidt
	 */
	public void setPosition(Point position) {
		this.position = position;
	}
	
	/**
	 * Change the node id.
	 * @param id The new id of the node.
	 * @author Anselm Schmidt
	 */
	public void changeId(int id) {
		this.id = id;
	}
	
	/**
	 * Get the id of the node.
	 * @return Id of the node.
	 * @author Anselm Schmidt
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * Get the drawing strategy of the node.
	 * @return Drawing strategy.
	 * @author Anselm Schmidt
	 */
	public NodeDrawingStrategy getDrawingStrategy() {
		return drawingStrategy;
	}
	
	/**
	 * Change the drawing strategy of the node.
	 * @param strategy New drawing strategy.
	 * @author Anselm Schmidt
	 */
	public void changeDrawingStrategy(NodeDrawingStrategy strategy) {
		drawingStrategy = strategy;
	}
		
	/**
	 * Drawing method.
	 * Let the current <code>NodeDrawingStrategy</code> paint the node.
	 * @param g Used <code>Graphics</code> instance.
	 * @param animated <code>True</code>, if the element is animated,
	 * <code>False</code> otherwise.
	 * @return <code>True</code>, if the element has to be redrawn due to animations,
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	@Override
	public boolean paint(Graphics g, boolean animated) {		
		if(!animated) {
			// stop animation
			animationType = AnimationType.NONE;
		}
		
		// draw node
		return drawingStrategy.paint(g, id, position, focused, alpha, distance,
				animationType, animationStartTime, ComponentUtility.BEAMER_MODE);
	}
	
	/**
	 * Convert node to string.
	 * @return A string <code>"node <i>i</i> at [<i>x</i>;<i>y</i>]"</code> with the
	 * node id instead of <code><i>i</i></code>, the x coordinate of the node
	 * instead of <code><i>x</i></code> and the y coordinate of the node instead of
	 * <code><i>y</i></code>.
	 * @author Anselm Schmidt
	 */
	@Override
	public String toString() {
		return "node " + id + " at [" + position.x + ";" + position.y + "]";
	}
	
	/**
	 * Change distance of the node.
	 * @param distance New distance.
	 * @author Anselm Schmidt
	 */
	public void changeDistance(int distance) {
		this.distance = distance;
	}

	/**
	 * Get the distance of the node. Only used by tree nodes.
	 * @return The distance of the node.
	 * @author Anselm Schmidt
	 */
	public int getDistance() {
		return distance;
	}

	@Override
	/**
	 * Get the rectangle around the node.
	 * @param g Used <code>Graphics</code> interface.
	 * @return The rectangle around the node.
	 * @author Anselm Schmidt 
	 */
	public Rectangle getRect(Graphics g) {
		// get coordinates and calculate the radius of the node
		Point center = this.getGraphController().getNodePosition(id);
		int radius = (int) Math.round((double) NODE_BEAMER_SIZE / 2) +
			NODE_BEAMER_BORDER_SIZE;
		Font distanceFont = NODE_DISTANCE_FONT;
		
		if(ComponentUtility.BEAMER_MODE) {
			distanceFont = NODE_BEAMER_DISTANCE_FONT;
		}
		
		int distanceHeight = distanceFont.getSize();
		int distanceWidth = GraphCalculation.calcTextWidth(g, String.valueOf(distance),
				distanceFont);
		
		// return rectangle around node
		return new Rectangle(center.x - radius, center.y - radius - distanceHeight, 2 *
				radius + distanceWidth, 2 * radius + distanceHeight);
	}
}
