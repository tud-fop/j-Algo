package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Calendar;

import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.gui.GUIConstants;

/**
 * <code>VisualGraphElement</code> represents a drawn element of a graph or tree 
 * @author Anselm Schmidt
 *
 */
public abstract class VisualGraphElement implements GUIConstants {
	
	private GraphController graphController = null;
	
	public GraphController getGraphController() {
		return graphController;
	}

	public VisualGraphElement(GraphController graphController){
		this.graphController = graphController;
	}
	
	/**
	 * position of the element
	 */
	protected Point position;
	
	/**
	 * <code>True</code>, if the node is focused. <code>False</code> otherwise.
	 */
	protected boolean focused;
	
	/**
	 * Alpha value of the element.
	 */
	protected short alpha = 255;
	
	/**
	 * The time of the animation start. 
	 */
	protected long animationStartTime;
	
	/**
	 * The type of the current animation.
	 */
	protected AnimationType animationType;
	
	/**
	 * Get the position of the element.
	 * @return Position of the center of the element.
	 * @author Anselm Schmidt
	 */
	public Point getPosition() {
		return position;
	}
	
	/**
	 * Check, if the current coordinates hit the element.
	 * @param point The coordinates to be checked.
	 * @return <code>True</code> if the coordinates hit the element,
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	public abstract boolean wasHit(Point point);
	
	/**
	 * Check, if the current coordinates hit the protected area around the element.
	 * @param point The coordinates to be checked.
	 * @return <code>True</code> if the coordinates hit the protected area,
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	public abstract boolean hitBoundaries(Point point);
	
	/**
	 * Drawing method. Draws the element without animation.
	 * @param g Used <code>Graphics</code> instance.
	 * @author Anselm Schmidt
	 */
	public void paint(Graphics g) {
		paint(g, false);
	}
	
	/**
	 * Drawing method. Draws the element.
	 * @param g Used <code>Graphics</code> instance.
	 * @param animated <code>True</code>, if the element is animated,
	 * <code>False</code> otherwise.
	 * @return <code>True</code>, if the element has to be redrawn due to animations,
	 * <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	public abstract boolean paint(Graphics g, boolean animated);
	
	/**
	 * Set focus of the node.
	 * @
	 * @author Anselm Schmidt
	 */
	public void setFocused(boolean focused) {
		// reset animation time
		animationStartTime = Calendar.getInstance().getTimeInMillis();
		
		// set animation type
		if(focused) {
			animationType = AnimationType.FOCUS;
		}
		else {
			animationType = AnimationType.UNFOCUS;
		}
		
		// set focus
		this.focused = focused;
	}
	
	/**
	 * Check, if the element is focussed.
	 * @return <code>True</code> if the element is focussed, <code>False</code>
	 * otherwise.
	 * @author Anselm Schmidt
	 */
	public boolean isFocused() {
		return focused;
	}
	
	/**
	 * Get the rectangle of the element. Can be used to repaint this area only.
	 * @param g Used <code>Graphics</code> interface. 
	 * @return Rectangle around the element.
	 */
	public abstract Rectangle getRect(Graphics g);
}
