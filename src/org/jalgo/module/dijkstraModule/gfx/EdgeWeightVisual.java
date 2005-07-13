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
 * Created on 10.05.2005
 */

package org.jalgo.module.dijkstraModule.gfx;

import org.eclipse.draw2d.Connection;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.MidpointLocator;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Font;
import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.model.Edge;

/**
 * Visual representation of an edge weight, displayed at the center of an {@link EdgeVisual}.
 * It allows the weight to be changed interactively with the mouse.
 * <br><br><i>Note:</i> An edge weight visual has three actual drawing components:
 * a label, a {@link Circle} and a {@link Square}. The label is always visible, but only one of the 
 * other two is displayed at any point in time. The circle is used to "punch a hole"
 * in the edge visual under it, so the label can be easily read. The square is used
 * when the edge weight visual is highlighted or its weight is interactively changed.
 * @author Martin Winter
 */
public class EdgeWeightVisual 
extends Visual 
implements MouseListener, MouseMotionListener {
	
	private int weight;
	private Label label;
	private Circle circle;
	private Square box;
	private EdgeVisual edgeVisual;
	private Connection connection;
	private MidpointLocator locator;
	
	private Point initialLocation;
	private int initialWeight;
	private boolean mouseButtonPressed = false;	// Set when mouse button pressed, reset when released.
	private boolean inOperation = false;			// Set when this edge weight visual is the subject of the current operation.
	private boolean containsMouse = false;			// Set when mouse enters, reset when mouse exits.
	private boolean oldContainsMouse = false;		// Needed to fix a bug with mouse events firing repeatedly.

	/**
	 * Creates a new edge weight visual on an edge visual.
	 * @param device the current device (needed for creating colors)
	 * @param parent the graph parent that the edge visual should appear on
	 * @param edgeVisual the edge visual to which this edge weight visual is attached
	 * @param modelEdge the model edge this edge weight visual represents
	 */
	public EdgeWeightVisual(Device device, GraphParent parent, EdgeVisual edgeVisual, Edge modelEdge) {
		super(parent);
		
		this.edgeVisual = edgeVisual;
		this.connection = edgeVisual.getConnectionForEdgeWeightVisual();
		
		this.weight = modelEdge.getWeight();
		
		box = new Square(new Point(0, 0), 13);
		box.setBackgroundColor(parent.blackColor);
		box.setLineWidth(0);
		
		circle = new Circle(new Point(0, 0), 13);
		circle.setBackgroundColor(parent.whiteColor);
		circle.setForegroundColor(parent.whiteColor);
		circle.setLineWidth(0);
		
		label = new Label(String.valueOf(getWeight()));
		Font labelFont = new Font(device, "Verdana", 14, SWT.BOLD);
		label.setFont(labelFont);
		label.setForegroundColor(parent.blackColor);
		
		// Do not add box yet.
		locator = new MidpointLocator(connection, 0);		
		connection.add(circle, locator);
		connection.add(label, locator);
		
		// Register for mouse events.
		box.addMouseListener(this);
		box.addMouseMotionListener(this);
		circle.addMouseListener(this);
		circle.addMouseMotionListener(this);
		label.addMouseListener(this);
		label.addMouseMotionListener(this);
	}
	
	/**
	 * Sets the weight of the edge weight visual.
	 * It does <b>not</b> change the weight of the model edge.
	 * Values are automatically limited to the range 0 ... 99.
	 * @param weight the weight
	 */
	public void setWeight(int weight) {
		if (weight < 0) {
			weight = 0;
		} else if (weight > 99) {
			weight = 99;
		}
		this.weight = weight;
	}
	
	/**
	 * Returns the weight of the edge weight visual.
	 * It does <b>not</b> fetch the the weight from the model edge.
	 * @return the weight
	 */
	public int getWeight() {
		return weight;
	}

	
	/* Methods inherited from Visual. */
	
	/**
	 * Adds the actual drawing elements to the graph parent.
	 * Since the drawing elements of an edge weight visual are automatically added
	 * to the graph parent by the edge visual owning it, this method does nothing.
	 * @param parent the graph parent that the drawing elements should appear on
	 */
	public void addToParent(GraphParent parent) {
	}

	/**
	 * Update appearance according to flags.
	 * Call this method after modifying any flags.
	 */
	public void update() {
		if (isInEditingMode()) {
			// Flag with highest priority comes first.
			if (isActive()) {
				box.setBackgroundColor(parent.redColor);
				label.setForegroundColor(parent.whiteColor);
				removeConnectionChildren();
				connection.add(box, locator);
				connection.add(label, locator);
			}
			else if (isHighlighted()) {
				box.setBackgroundColor(parent.blackColor);
				label.setForegroundColor(parent.whiteColor);
				removeConnectionChildren();
				connection.add(box, locator);
				connection.add(label, locator);
			}
			else if (isChanged()) {
				label.setForegroundColor(parent.redColor);
				removeConnectionChildren();
				connection.add(circle, locator);
				connection.add(label, locator);
			}
			else {
				label.setForegroundColor(parent.blackColor);
				removeConnectionChildren();
				connection.add(circle, locator);
				connection.add(label, locator);
			}
		} else {
			if (isChosen()) {
				label.setForegroundColor(parent.greenColor);
				removeConnectionChildren();
				connection.add(circle, locator);
				connection.add(label, locator);
			}
			else if (isBorder()) {
				if (isActive()) {
					if (isConflict()) {
						label.setForegroundColor(parent.orangeColor);
						removeConnectionChildren();
						connection.add(circle, locator);
						connection.add(label, locator);
					} else {
						label.setForegroundColor(parent.redColor);
						removeConnectionChildren();
						connection.add(circle, locator);
						connection.add(label, locator);
					}
				} else {
					label.setForegroundColor(parent.orangeColor);
					removeConnectionChildren();
					connection.add(circle, locator);
					connection.add(label, locator);
				}
			}
			else {
				label.setForegroundColor(parent.grayColor);
				removeConnectionChildren();
				connection.add(circle, locator);
				connection.add(label, locator);
			}
		}
		
		// Explicitly perform drawing update.
		performUpdate();
	}
	
	/**
	 * Forces the update manager to immediately refresh the display.
	 */
	public void performUpdate() {
		for (int i = 0; i < connection.getChildren().size(); i++) {
			((Figure)connection.getChildren().get(i)).getUpdateManager().performUpdate();
		}
		label.setText(String.valueOf(getWeight()));
	}
	
	/**
	 * Removes the box or the circle from the edge visual's connection,
	 * depending on which is currently displayed (see class description above).
	 * After calling this method, the other element (box or circle) can be added.
	 */
	private void removeConnectionChildren() {
		if (connection.getChildren().contains(box)) {
			connection.remove(box);
		}
		if (connection.getChildren().contains(circle)) {
			connection.remove(circle);
		}
	}


	/* Methods inherited from MouseListener interface. */
	
	public void mouseDoubleClicked(MouseEvent event) {
	}
	
	/**
	 * Responds when the mouse button is pressed.
	 * <ul>
	 * <li>If in MODE_ADD_WEIGH_EDGE, initiates a weigh edge operation.</li>
	 * <li>If in MODE_DELETE_EDGE, initiates a delete edge operation.</li>
	 * </ul>
	 */
	public void mousePressed(MouseEvent event) {
		mouseButtonPressed = true;
		
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
				inOperation = true;
				initialLocation = event.getLocation();
				initialWeight = getWeight();
				parent.setDraggingEdgeWeightVisual(this);
				edgeVisual.setActive(true);
				edgeVisual.update();
				break;
			}
			case (Controller.MODE_DELETE_EDGE): {
				inOperation = true;
				edgeVisual.setActive(true);
				edgeVisual.update();
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
	 * <li>If in MODE_ADD_WEIGH_EDGE, finishes a weigh edge operation.</li>
	 * <li>If in MODE_DELETE_EDGE, finishes a delete edge operation.
	 * The edge is only deleted if the mouse is over the edge visual 
	 * and the mouse button was pressed before. Otherwise, the user 
	 * probably has changed his mind and nothing is deleted.</li>
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
				break;
			}
			case (Controller.MODE_DELETE_NODE): {
				break;
			}
			case (Controller.MODE_ADD_WEIGH_EDGE): {
				parent.setDraggingEdgeWeightVisual(null);
				edgeVisual.setActive(false);
				edgeVisual.update();
				parent.weighEdgeForVisual(edgeVisual);
				break;
			}
			case (Controller.MODE_DELETE_EDGE): {
				if (containsMouse && inOperation) {
					parent.deleteEdgeForVisual(edgeVisual);
				} else {
					inOperation = false;
					setActive(false);
				}
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
	 * <li>If in MODE_ADD_WEIGH_EDGE, changes the weight.</li>
	 * </ul>
	 */
	public void mouseDragged(MouseEvent event) {
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
				if (parent.isDragWeighEdge()) {
					if (parent.getDraggingEdgeWeightVisual() != this) {
						// When this edge weight visual is the top one of two overlapping edge weight visuals, 
						// pass events on to the visual that is registered with the parent for dragging.
						parent.getDraggingEdgeWeightVisual().mouseDragged(event);
					} else {
						// Change weight according to the change of the mouse position.
						Point location = event.getLocation();
						int delta = location.getDifference(initialLocation).height;
						setWeight(initialWeight - (delta / 5));
						performUpdate();
					}
					break;
				}
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
	 * <li>If in MODE_ADD_WEIGH_EDGE or MODE_DELETE_EDGE, highlights the edge.</li>
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
				break;
			}
			case (Controller.MODE_DELETE_NODE): {
				break;
			}
			case (Controller.MODE_ADD_WEIGH_EDGE): {
				// Highlight only if there is no current dragging operation.
				if (!parent.isDrag()) {
					if (!oldContainsMouse) {
						oldContainsMouse = true;
						edgeVisual.setHighlighted(true);
						edgeVisual.update();
						parent.setCleanupEdgeWeightVisual(this);	// Register for cleanup.
					}
				}
				break;
			}
			case (Controller.MODE_DELETE_EDGE): {
				// Become active again if mouse button is still pressed 
				// and edge weight visual was clicked on before.
				if (!oldContainsMouse) {
					oldContainsMouse = true;
					if (mouseButtonPressed && inOperation) {
						edgeVisual.setActive(true);
					}
					edgeVisual.setHighlighted(true);
					edgeVisual.update();
				}
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
	 * <li>If in MODE_ADD_WEIGH_EDGE or MODE_DELETE_EDGE, unhighlights the edge.</li>
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
				break;
			}
			case (Controller.MODE_DELETE_NODE): {
				break;
			}
			case (Controller.MODE_ADD_WEIGH_EDGE): {
				oldContainsMouse = false;
				edgeVisual.setHighlighted(false);
				edgeVisual.update();
				break;
			}
			case (Controller.MODE_DELETE_EDGE): {
				oldContainsMouse = false;
				edgeVisual.setActive(false);
				edgeVisual.setHighlighted(false);
				edgeVisual.update();
				break;
			}
			case (Controller.MODE_ALGORITHM): {
				break;
			}
		}
	}

	public void mouseHover(MouseEvent event) {
	}

	public void mouseMoved(MouseEvent event) {
	}
}
