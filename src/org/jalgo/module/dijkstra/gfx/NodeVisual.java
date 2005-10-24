/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on 06.05.2005
 */

package org.jalgo.module.dijkstra.gfx;

import org.eclipse.draw2d.EllipseAnchor;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.Polyline;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.model.Node;

/**
 * Visual representation of a node.
 * It can be connected with other node visuals using {@link EdgeVisual} objects.
 * <br><br><i>Note:</i> What is actually drawn is two circles (an inner and an outer circle),
 * and a label. The inner circle is visible, whereas the outer circle acts as a spacer
 * for the line connection of edge visuals (see the class description there for more info).
 * @author Martin Winter, Hannes Strass
 */
public class NodeVisual 
extends Visual 
implements MouseListener, MouseMotionListener {

	private Circle innerCircle;
	private Circle outerCircle;
	private int innerCircleRadius = 17;
	private int outerCircleRadius = 27;
	private EllipseAnchor innerAnchor;
	private EllipseAnchor outerAnchor;
	
	private Point center;
	private Point lastLocation;
	private Polyline newEdgeLine = null;
	private boolean mouseButtonPressed = false;	// Set when mouse button pressed, reset when released.
	private boolean inOperation = false;			// Set when this node visual is the subject of the current operation.
	private boolean containsMouse = false;			// Set when mouse enters, reset when mouse exits.
	
	private Label label;
	
	/**
	 * Creates a new node visual.
	 * @param device the current device (needed for creating colors)
	 * @param parent the graph parent that the edge visual should appear on
	 * @param modelNode the model node that this node visual represents
	 */
	public NodeVisual(Device device, GraphParent parent, Node modelNode) {
		super(parent);
		
		this.center = modelNode.getPosition().getScreenPoint(parent.getBounds());
		
		setFlags(modelNode.getFlags());
		
		innerCircle = new Circle(center, innerCircleRadius);
		innerCircle.setOpaque(true);
		innerCircle.setOutline(true);
		innerCircle.setLineWidth(3);
		
		outerCircle = new Circle(center, outerCircleRadius);
		
		innerAnchor = new EllipseAnchor(innerCircle);
		outerAnchor = new EllipseAnchor(outerCircle);
		
		label = new Label(modelNode.getLabel());
		Font labelFont = new Font(device, "Verdana", 16, SWT.BOLD);
		label.setFont(labelFont);
		label.setBounds(innerCircle.getBounds().getTranslated(0, -1));
		innerCircle.add(label);
		
		update();	// Initialize appearance.
	}
	
	/**
	 * Returns the anchor of the inner circle.
	 * @return the anchor of the inner circle
	 */
	public EllipseAnchor getInnerAnchor() {
		return innerAnchor;
	}
	
	/**
	 * Returns the anchor of the outer circle
	 * @return the anchor of the outer circle
	 */
	public EllipseAnchor getOuterAnchor() {
		return outerAnchor;
	}
	
	/**
	 * Sets the center of the node visual in pixel coordinates.
	 * @param center the center of the node visual in pixel coordinates
	 */
	public void setCenter(Point center) {
		this.center = center;
		innerCircle.setCenter(center);
		outerCircle.setCenter(center);
	}
	
	/**
	 * Returns the center of the node visual in pixel coordinates.
	 * @return the center of the node visual in pixel coordinates
	 */
	public Point getCenter() {
		return center;
	}
	
	/**
	 * Returns the line representing a new edge.
	 * The edge has been dragged out from this node visual 
	 * but has not yet been connected to a second node.
	 * @return the line representing a new edge
	 */
	public Polyline getNewEdgeLine() {
		return newEdgeLine;
	}
	
	/**
	 * Sets the target node visual for a new edge.
	 * The line representing the new edge is connected to the target.
	 * @param nodeVisual the target node visual
	 */
	public void setNewEdgeTarget(NodeVisual nodeVisual) {
		if ( (newEdgeLine != null) && (nodeVisual != null) ) {
			newEdgeLine.setEndpoints(getCenter(), nodeVisual.getCenter());
			newEdgeLine.getUpdateManager().performUpdate();
		}
	}
	
	/**
	 * Cancels the creation of a new edge.
	 * Called from {@link GraphParent} when in MODE_ADD_WEIGH_EDGE 
	 * and the mouse button is released before connecting a new edge line to a target node visual.
	 */
	public void cancelNewEdge() {
		inOperation = false;
		mouseButtonPressed = false;
		parent.remove(newEdgeLine);
		newEdgeLine = null;
		setActive(false);
		if (!containsMouse) {
			setHighlighted(false);
		}
		update();
	}
	

	/* Methods inherited from Visual. */
	
	/**
	 * Adds the actual drawing elements to the graph parent.
	 * This is the inner circle (see class description above).
	 * @param parent the graph parent that the drawing elements should appear on
	 */
	public void addToParent(GraphParent parent) {
		parent.add(innerCircle);						// Add innerCircle only, label is added automatically.
		innerCircle.addMouseListener(this);			// Register for mouse presses.
		innerCircle.addMouseMotionListener(this);		// Register for mouse dragging (when mouse _inside_ innerCircle).
	}
	
	/**
	 * Update appearance according to flags.
	 * Call this method after modifying any flags.
	 */
	public void update() {
		if (isInEditingMode()) {
			// Flag with highest priority comes first.
			if (isActive()) {
				innerCircle.setForegroundColor(parent.redColor);
				innerCircle.setBackgroundColor(parent.redColor);
				label.setForegroundColor(parent.whiteColor);
			}
			else if (isHighlighted()) {
				innerCircle.setForegroundColor(parent.blackColor);
				innerCircle.setBackgroundColor(parent.blackColor);
				label.setForegroundColor(parent.whiteColor);
			}
			else if (isChanged()) {
				innerCircle.setForegroundColor(parent.redColor);
				innerCircle.setBackgroundColor(parent.whiteColor);
				label.setForegroundColor(parent.redColor);
			}
			else {
				innerCircle.setForegroundColor(parent.blackColor);
				innerCircle.setBackgroundColor(parent.whiteColor);
				label.setForegroundColor(parent.blackColor);
			}
		}
		else {
			if (isStart()) {
				if (isActive()) {
					innerCircle.setForegroundColor(parent.blueColor);
					innerCircle.setBackgroundColor(parent.blueColor);
					label.setForegroundColor(parent.whiteColor);
				} else {
					innerCircle.setForegroundColor(parent.blueColor);
					innerCircle.setBackgroundColor(parent.whiteColor);
					label.setForegroundColor(parent.blueColor);
				}
			}
			else if (isChosen()) {
				if (isActive()) {
					innerCircle.setForegroundColor(parent.greenColor);
					innerCircle.setBackgroundColor(parent.greenColor);
					label.setForegroundColor(parent.whiteColor);
				} else {
					innerCircle.setForegroundColor(parent.greenColor);
					innerCircle.setBackgroundColor(parent.whiteColor);
					label.setForegroundColor(parent.greenColor);
				}
			}
			else if (isBorder()) {
				if (isActive()) {
					innerCircle.setForegroundColor(parent.redColor);
					innerCircle.setBackgroundColor(parent.redColor);
					label.setForegroundColor(parent.whiteColor);
				} else {
					innerCircle.setForegroundColor(parent.orangeColor);
					innerCircle.setBackgroundColor(parent.whiteColor);
					label.setForegroundColor(parent.orangeColor);
				}
			}
			else {
				innerCircle.setForegroundColor(parent.grayColor);
				innerCircle.setBackgroundColor(parent.whiteColor);
				label.setForegroundColor(parent.grayColor);
			}
		}
		
		// Explicitly perform drawing update.
		performUpdate();
	}
	
	/**
	 * Forces the update manager to immediately refresh the display.
	 */
	public void performUpdate() {
		innerCircle.getUpdateManager().performUpdate();
	}
	

	/* Methods inherited from MouseListener interface. */
	
	public void mouseDoubleClicked(MouseEvent event) {
		// this method has no effect
	}
	
	/**
	 * Responds when the mouse button is pressed.
	 * <ul>
	 * <li>If in MODE_ADD_MOVE_NODE, initiates a move node operation.</li>
	 * <li>If in MODE_DELETE_NODE, initiates a delete node operation.</li>
	 * <li>If in MODE_ADD_WEIGH_EDGE, initiates an add edge operation.</li>
	 * </ul>
	 */
	public void mousePressed(MouseEvent event) {
		mouseButtonPressed = true;		// Necessary for maintaining state when crossing other elements.

		switch (getControllerMode())
		{
			case (Controller.MODE_NO_TOOL_ACTIVE): {
				break;
			}
			case (Controller.MODE_ADD_MOVE_NODE): {
				inOperation = true;
				lastLocation = event.getLocation();
				parent.setDraggingNodeVisual(this);
				setActive(true);
				update();
				break;
			}
			case (Controller.MODE_DELETE_NODE): {
				inOperation = true;
				setActive(true);
				update();
				break;
			}
			case (Controller.MODE_ADD_WEIGH_EDGE): {
				if (!parent.isDragAddEdge()) {
					// There is no current dragging operation. This is the source visual.
					inOperation = true;
					setActive(true);
					
					// Create new, temporary edge line.
					newEdgeLine = new Polyline();
					newEdgeLine.setEndpoints(getCenter(), getCenter());
					newEdgeLine.setForegroundColor(parent.redColor);
					newEdgeLine.setLineWidth(7);
					parent.add(newEdgeLine, parent.getNewEdgeLineInsertionIndex());		// Above edges, below nodes.
					parent.setNewEdgeSource(this);
					
					update();
				}
				break;
			}
			case (Controller.MODE_DELETE_EDGE): {
				break;
			}
			case (Controller.MODE_ALGORITHM): {
				break;
			}
		}
	}
	
	/**
	 * Responds when the mouse button is released.
	 * <ul>
	 * <li>If in MODE_ADD_MOVE_NODE, finishes an add node or move node operation.</li>
	 * <li>If in MODE_DELETE_NODE, initiates a delete node operation.
	 * The node is only deleted if the mouse is over the node visual 
	 * and the mouse button was pressed before. Otherwise, the user 
	 * probably has changed his mind and nothing is deleted.</li>
	 * <li>If in MODE_ADD_WEIGH_EDGE, either moves the line representing a new edge
	 * (user adds line by click/move/click), or finishes an add edge operation (click/drag/release).</li>
	 * </ul>
	 */
	public void mouseReleased(MouseEvent event) {
		mouseButtonPressed = false;
		
		switch (getControllerMode())
		{
			case (Controller.MODE_NO_TOOL_ACTIVE): {
				break;
			}
			case (Controller.MODE_ADD_MOVE_NODE): {
				// Stop dragging, but prevent an additional move action if this click just created this node visual.
				inOperation = false;
				if (parent.isDragMoveNode()) {
					parent.setDraggingNodeVisual(null);
					parent.moveNodeForVisual(this, this.getCenter());
				}
				break;
			}
			case (Controller.MODE_DELETE_NODE): {
				if (containsMouse && inOperation) {
					parent.deleteNodeForVisual(this);
				} else {
					inOperation = false;
					setActive(false);
				}
				break;
			}
			case (Controller.MODE_ADD_WEIGH_EDGE): {
				if (parent.isDragAddEdge()) {
					if (inOperation) {
						// This is the source node visual.
						// User wants to add edge by click/move/click.
						if (newEdgeLine != null) {
							newEdgeLine.setEndpoints(getCenter(), event.getLocation());
							newEdgeLine.getUpdateManager().performUpdate();
						}
					} else {
						// This is the target node visual.
						// Make the connection. newEdgeLine will disappear during the next update.
						newEdgeLine = null;
						parent.addEdgeForVisual(this);
						parent.setNewEdgeSource(null);
						setActive(false);
						update();
					}
				}
				break;
			}
			case (Controller.MODE_DELETE_EDGE): {
				break;
			}
			case (Controller.MODE_ALGORITHM): {
				break;
			}
		}
	}


	/* Methods inherited from MouseMotionListener interface. */
	
	/**
	 * Responds when the mouse is dragged.
	 * <ul>
	 * <li>If in MODE_ADD_MOVE_NODE, moves the node visual.</li>
	 * <li>If in MODE_ADD_WEIGH_EDGE, either passes the drag event on to an edge weight visual
	 * or moves the line representing a new edge (user adds line by click/move/click).</li>
	 * </ul>
	 */
	public void mouseDragged(MouseEvent event) {
		// This method gets called when the mouse is inside the innerCircle.
		// When outside the circle, the GraphParent's mouseDragged() method gets called.

		switch (getControllerMode())
		{
			case (Controller.MODE_NO_TOOL_ACTIVE): {
				break;
			}
			case (Controller.MODE_ADD_MOVE_NODE): {
				// When this node visual is on top of two overlapping node visuals, pass events on 
				// to the node visual that is registered with the parent for dragging.
				if (parent.isDragMoveNode()) {
					if (parent.getDraggingNodeVisual() != this) {
						parent.getDraggingNodeVisual().mouseDragged(event);
					} else {
						Point location = event.getLocation();
						Dimension delta = location.getDifference(lastLocation);
						lastLocation = location;
						setCenter(getCenter().getTranslated(delta));
						performUpdate();
					}
				}
				break;
			}
			case (Controller.MODE_DELETE_NODE): {
				break;
			}
			case (Controller.MODE_ADD_WEIGH_EDGE): {
				// When the mouse hits this node visual while weighing an edge weight visual, 
				// pass events on to the edge weight visual that is registered with the parent for dragging.
				if (parent.isDragWeighEdge()) {
					parent.getDraggingEdgeWeightVisual().mouseDragged(event);
				} else if (inOperation) {
					// This is the source node visual.
					if (newEdgeLine != null) {
						newEdgeLine.setEndpoints(getCenter(), event.getLocation());
						newEdgeLine.getUpdateManager().performUpdate();
					}
				} else {
					// This is the target node visual.
				}
				break;
			}
			case (Controller.MODE_DELETE_EDGE): {
				break;
			}
			case (Controller.MODE_ALGORITHM): {
				break;
			}
		}
	}

	/**
	 * Responds when the mouse has entered.
	 * <ul>
	 * <li>If in MODE_ADD_MOVE_NODE, MODE_DELETE_NODE or MODE_ADD_WEIGH_EDGE, 
	 * highlights the node and/or marks it active.</li>
	 * </ul>
	 */
	public void mouseEntered(MouseEvent event) {
		containsMouse = true;
		
		switch (getControllerMode())
		{
			case (Controller.MODE_NO_TOOL_ACTIVE): {
				break;
			}
			case (Controller.MODE_ADD_MOVE_NODE): {
				// Highlight only if there is no current dragging operation.
				if (!parent.isDrag()) {
					setHighlighted(true);
					update();
				}
				break;
			}
			case (Controller.MODE_DELETE_NODE): {
				// Become active again if mouse button is still pressed 
				// and node visual was clicked on before.
				if (mouseButtonPressed && inOperation) {
					setActive(true);
				}
				setHighlighted(true);
				update();
				break;
			}
			case (Controller.MODE_ADD_WEIGH_EDGE): {
				if (!parent.isDrag()) {
					// Mark potential source active.
					setHighlighted(true);
				} else if (parent.isDragAddEdge()) {
					// Mark potential target active.
					setActive(true);
					parent.getNewEdgeSource().setNewEdgeTarget(this);
				}
				update();
				break;
			}
			case (Controller.MODE_DELETE_EDGE): {
				break;
			}
			case (Controller.MODE_ALGORITHM): {
				break;
			}
		}
	}

	/**
	 * Responds when the mouse has exited.
	 * <ul>
	 * <li>If in MODE_ADD_MOVE_NODE, MODE_DELETE_NODE or MODE_ADD_WEIGH_EDGE, 
	 * unhighlights the node and/or unmarks it active.</li>
	 * </ul>
	 */
	public void mouseExited(MouseEvent event) {
		containsMouse = false;

		switch (getControllerMode())
		{
			case (Controller.MODE_NO_TOOL_ACTIVE): {
				break;
			}
			case (Controller.MODE_ADD_MOVE_NODE): {
				// Unmark when not dragging.
				if (!mouseButtonPressed) {
					setActive(false);
					setHighlighted(false);
					update();
				}
				break;
			}
			case (Controller.MODE_DELETE_NODE): {
				setActive(false);
				setHighlighted(false);
				update();
				break;
			}
			case (Controller.MODE_ADD_WEIGH_EDGE): {
				// Unmark when not dragging and not in add edge mode.
				if (!mouseButtonPressed && !inOperation) {
					setActive(false);
					setHighlighted(false);
					update();
				}
				break;
			}
			case (Controller.MODE_DELETE_EDGE): {
				break;
			}
			case (Controller.MODE_ALGORITHM): {
				break;
			}
		}
	}

	public void mouseHover(MouseEvent event) {
		// this method has no effect
	}

	/**
	 * Responds when the mouse has moved.
	 * <ul>
	 * <li>If in MODE_ADD_WEIGH_EDGE, moves the line representing a new edge (user adds line by click/move/click).</li>
	 * </ul>
	 */
	public void mouseMoved(MouseEvent event) {
		switch (getControllerMode())
		{
			case (Controller.MODE_NO_TOOL_ACTIVE): {
				break;
			}
			case (Controller.MODE_ADD_MOVE_NODE): {
				break;
			}
			case (Controller.MODE_DELETE_NODE): {
				break;
			}
			case (Controller.MODE_ADD_WEIGH_EDGE): {
				// This is called when adding an edge by click/move/click,
				// as opposed to click/drag/release.
				if (inOperation) {
					// This is the source node visual.
					if (newEdgeLine != null) {
						newEdgeLine.setEndpoints(getCenter(), event.getLocation());
						newEdgeLine.getUpdateManager().performUpdate();
					}
				} else {
					// This is the target node visual.
				}
				break;
			}
			case (Controller.MODE_DELETE_EDGE): {
				break;
			}
			case (Controller.MODE_ALGORITHM): {
				break;
			}
		}
	}
}
