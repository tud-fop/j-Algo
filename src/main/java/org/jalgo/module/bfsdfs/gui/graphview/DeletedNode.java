package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Calendar;

import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.gui.ComponentUtility;
import org.jalgo.module.bfsdfs.gui.graphview.AnimationType;;

public class DeletedNode extends VisualGraphElement {
	/**
	 * The position of the deleted node. 
	 */
	private Point position;
	
	/**
	 * Distance of the node. Only used by tree nodes.
	 */
	protected int distance;
	
	/**
	 * The time of the deletion.
	 */
	private long time;
	
	/**
	 * The id of the node
	 */
	private int id;
	
	/**
	 * Drawing strategy.
	 */
	private NodeDrawingStrategy drawingStrategy;
	
	/**
	 * Constructor.
	 * @param node The node.
	 * @param graphController The used <code>GraphController</code> instance.
	 * @author Anselm Schmidt
	 */
	public DeletedNode(VisualNode node, GraphController graphController) {
		super(graphController);
		
		id = node.getId();
		distance = node.getDistance();
		position = node.getPosition();
		focused = node.isFocused();
		drawingStrategy = node.getDrawingStrategy();
		
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
	 * Drawing method. Draws the deleted node.
	 * @param g Used <code>Graphics</code> instance.
	 * @param animated <code>True</code>, if animations are enabled.
	 * <code>False</code> otherwise.
	 */
	public boolean paint(Graphics g, boolean animated) {
		if(animated) {
			return drawingStrategy.paint(g, id, position, focused, ALPHA_100_PERCENT,
					distance, AnimationType.DELETE, time, ComponentUtility.BEAMER_MODE);
		}
		
		return false;
	}

	@Override
	/**
	 * Not used. A deleted node cannot be hit.
	 */
	public boolean wasHit(Point point) {
		return false;
	}

	@Override
	/**
	 * Get the rectangle around the node.
	 * @param g Not used.
	 * @return The rectangle around the node.
	 * @author Anselm Schmidt
	 */
	public Rectangle getRect(Graphics g) {
		// calculate the radius of the node
		int radius = (int) Math.round((double) NODE_BEAMER_SIZE / 2) +
			NODE_BEAMER_BORDER_SIZE;
		
		// return rectangle around node
		return new Rectangle(position.x - radius, position.y - radius,
				2 * radius, 2 * radius);
	}
}
