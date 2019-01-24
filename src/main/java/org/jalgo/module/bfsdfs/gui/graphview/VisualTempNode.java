package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.gui.GraphDrawing;

/**
 * Represents a node that has not been created yet.
 * This class is used when the user is putting a new node. 
 * @author Anselm Schmidt
 */
public class VisualTempNode extends VisualGraphElement {
	
	/**
	 * Position of the temporary node.
	 */
	protected Point position;
	
	/**
	 * Constructor. Creates a temporary node.
	 * @param position The position of the temporary node.
	 * @param graphController Used <code>GraphController</code> instance.
	 * @author Anselm Schmidt
	 */
	public VisualTempNode(Point position, GraphController graphController) {
		super(graphController);
		this.position = position;
	}
	
	@Override
	/**
	 * Unused method. A temporary node cannot be hit.
	 * @author Anselm Schmidt
	 */
	public boolean hitBoundaries(Point point) {
		return false;
	}

	/**
	 * Drawing method. Draws the temporary node.
	 * @param g Used <code>Graphics</code> instance.
	 * @param animated Not used.
	 * Temporary graph elements don't have any animations.
	 * @author Anselm Schmidt
	 */
	@Override
	public boolean paint(Graphics g, boolean animated) {
		// ask GraphController for the if of the next node
		int id = this.getGraphController().getNextNodeId();
		
		// draw temporary node
		GraphDrawing.drawNode(g, String.valueOf(id), position, NODE_BEAMER_FONT,
				NODE_BEAMER_SIZE, NODE_BEAMER_BORDER_SIZE, NODE_BORDER_COLOR,
				NODE_LABEL_COLOR, NODE_COLOR_TOP, NODE_COLOR_BOTTOM, TEMP_ALPHA_VALUE,
				NODE_GRADIENT_HEIGHT);
		
		// no repaint needed
		return false;
	}

	/**
	 * Unused method. A temporary edge cannot be hit.
	 * @author Anselm Schmidt
	 */
	@Override
	public boolean wasHit(Point point) {
		return false;
	}

	@Override
	/**
	 * Get the rectangle around the node.
	 * @param g Not used.
	 * @return The rectangle around the edge.
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
