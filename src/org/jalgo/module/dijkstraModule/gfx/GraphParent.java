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
 * Created on 09.05.2005
 */

package org.jalgo.module.dijkstraModule.gfx;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.RGB;
import org.jalgo.module.dijkstraModule.model.Edge;
import org.jalgo.module.dijkstraModule.model.Graph;
import org.jalgo.module.dijkstraModule.model.Node;
import org.jalgo.module.dijkstraModule.model.Position;
import org.jalgo.module.dijkstraModule.gfx.NodeVisual;
import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.actions.*;

/**
 * A graph parent is not a parent in the sense of inheritance, but rather serves as a sort of 
 * canvas for {@link Visual} objects (note that it is a Draw2D Figure, not an SWT Canvas).
 * <br><br>Due to the way mouse events are sent and received, the graph parent
 * plays a vital role in the distribution of such events to individual visuals.
 * For example, when dragging a node, the mouse cursor may easily leave the bounds of the node.
 * In that instant, the node ceases to receive mouse events and the motion suddenly stops.
 * However, if the graph parent catches these events and routes them to the node, smooth motion is ensured.
 * @author Martin Winter, Hannes Strass
 */
public class GraphParent 
extends Figure 
implements MouseListener, MouseMotionListener {
		
	private Device device;
	private Controller controller;
	private Graph graph;
	private NodeVisual draggingNodeVisual = null;
	private EdgeWeightVisual draggingEdgeWeightVisual = null;
	private EdgeWeightVisual cleanupEdgeWeightVisual = null;
	private NodeVisual newEdgeSource = null;
	private int newEdgeLineInsertionIndex = -1;
	private Rectangle oldBounds = new Rectangle();
	
	/**
	 * A custom red color (#CC0000).
	 */
	public Color redColor;

	/**
	 * A custom orange color (#CC9900).
	 */
	public Color orangeColor;

	/**
	 * A custom green color (#009900).
	 */
	public Color greenColor;

	/**
	 * A custom blue color (#000099).
	 */
	public Color blueColor;

	/**
	 * A custom white color (#FFFFFF).
	 */
	public Color whiteColor;

	/**
	 * A custom gray color (#999999).
	 */
	public Color grayColor;

	/**
	 * A custom black color (#000000).
	 */
	public Color blackColor;
	
	/**
	 * These hash maps store associations between model and visual objects.
	 */
	private HashMap nodeModelsVisuals, edgeModelsVisuals, nodeVisualsModels, edgeVisualsModels;
	
	/**
	 * Creates a new graph parent.
	 * @param device the current device (needed for creating colors)
	 * @param controller the controller (needed for determining the current mode)
	 */
	public GraphParent(Device device, Controller controller) {
		this.device = device;
		this.controller = controller;
		
		RGB redColorRGB = new RGB(204, 0, 0);
		redColor = new Color(device, redColorRGB);
		RGB orangeColorRGB = new RGB(204, 153, 0);
		orangeColor = new Color(device, orangeColorRGB);
		RGB greenColorRGB = new RGB(0, 153, 0);
		greenColor = new Color(device, greenColorRGB);
		RGB blueColorRGB = new RGB(0, 0, 153);
		blueColor = new Color(device, blueColorRGB);
		RGB whiteColorRGB = new RGB(255, 255, 255);
		whiteColor = new Color(device, whiteColorRGB);
		RGB grayColorRGB = new RGB(153, 153, 153);
		grayColor = new Color(device, grayColorRGB);
		RGB blackColorRGB = new RGB(0, 0, 0);
		blackColor = new Color(device, blackColorRGB);

		// Register for mouse button events.
		addMouseListener(this);
		
		// Register for mouse drag (whenever mouse is _outside_ draggingNodeVisual's innerCircle).
		addMouseMotionListener(this);
		
		nodeModelsVisuals = new HashMap();
		edgeModelsVisuals = new HashMap();
		nodeVisualsModels = new HashMap();
		edgeVisualsModels = new HashMap();
	}
	
	/**
	 * Returns the controller instance.
	 * @return the controller instance that was passed to the constructor
	 */
	public Controller getController() {
		return controller;
	}
	
	/**
	 * Returns the mode of the controller.
	 * @return the mode of the controller as an integer field (see constants there)
	 */
	public int getControllerMode() {
		return controller.getEditingMode();
	}
	
	/**
	 * Returns <code>true</code> if a node visual is being dragged.
	 * @return <code>true</code> if a node visual is being dragged
	 */
	public boolean isDragMoveNode() {
		return (draggingNodeVisual != null);
	}

	/**
	 * Returns <code>true</code> if a new edge is being dragged.
	 * @return <code>true</code> if a new edge is being dragged
	 */
	public boolean isDragAddEdge() {
		return (newEdgeSource != null);
	}

	/**
	 * Returns <code>true</code> if an existing edge is being weighed through a drag.
	 * @return <code>true</code> if an existing edge is being weighed through a drag
	 */
	public boolean isDragWeighEdge() {
		return (draggingEdgeWeightVisual != null);
	}

	/**
	 * Returns <code>true</code> if any of the three dragging activities is taking place.
	 * @return <code>true</code> if any of the three dragging activities is taking place
	 */
	public boolean isDrag() {
		return ( isDragMoveNode() || isDragAddEdge() || isDragWeighEdge() );
	}
	
	/**
	 * Sets the node visual to be dragged.
	 * @param draggingNodeVisual the node visual to be dragged; set <code>null</code> when drag is over
	 */
	public void setDraggingNodeVisual(NodeVisual draggingNodeVisual) {
		// Will be null when mouse is released.
		this.draggingNodeVisual = draggingNodeVisual;
	}
	
	/**
	 * Returns the node visual being dragged.
	 * @return the node visual being dragged or <code>null</code> if there is none
	 */
	public NodeVisual getDraggingNodeVisual() {
		return draggingNodeVisual;
	}
	
	/**
	 * Sets the edge weight visual to be weighed through dragging.
	 * @param draggingEdgeWeightVisual the edge weight visual to be weighed through dragging; 
	 * set <code>null</code> when drag is over 
	 */
	public void setDraggingEdgeWeightVisual(EdgeWeightVisual draggingEdgeWeightVisual) {
		// Will be null when mouse is released.
		this.draggingEdgeWeightVisual = draggingEdgeWeightVisual;
	}
	
	/**
	 * Returns the edge weight visual being weighed through dragging.
	 * @return the edge weight visual being weighed through dragging or <code>null</code> if there is none
	 */
	public EdgeWeightVisual getDraggingEdgeWeightVisual() {
		return draggingEdgeWeightVisual;
	}
	
	/**
	 * Sets the edge weight visual to be "cleaned up".
	 * In the graph parent's mouseMoved() method, the registered visual's mouseExited() method is called 
	 * (which often is not automatically triggered), so it can perform proper state changes.
	 * @param cleanupEdgeWeightVisual
	 */
	public void setCleanupEdgeWeightVisual(EdgeWeightVisual cleanupEdgeWeightVisual) {
		this.cleanupEdgeWeightVisual = cleanupEdgeWeightVisual;
	}
	
	/**
	 * Sets the node visual that acts as the source for a new edge.
	 * @param nodeVisual the node visual that acts as the source for a new edge
	 */
	public void setNewEdgeSource(NodeVisual nodeVisual) {
		newEdgeSource = nodeVisual;
	}
	
	/**
	 * Returns the node visual that acts as the source for a new edge.
	 * @return the node visual that acts as the source for a new edge
	 */
	public NodeVisual getNewEdgeSource() {
		return newEdgeSource;
	}
	
	/**
	 * Returns the index at which the line representing a new edge is to be inserted.
	 * This index determines the "layer order" of the children of the graph parent.
	 * The line representing a new edge appears above all existing edges but below all nodes.
	 * @return the index at which the line representing a new edge is to be inserted
	 */
	public int getNewEdgeLineInsertionIndex() {
		return newEdgeLineInsertionIndex;
	}
	
	/**
	 * Sets the graph model visualized by this graph parent.
	 * @param graph the graph model visualized by this graph parent
	 */
	public void setGraph(Graph graph) {
		this.graph = graph;
	}
	
	/**
	 * Returns the graph model visualized by this graph parent.
	 * @return the graph model visualized by this graph parent
	 */
	public Graph getGraph() {
		return graph;
	}
	
	/**
	 * Returns the hash map associating the node models with their visuals.
	 * @return the hash map associating the node models with their visuals
	 */
	public HashMap getNodeModelsVisuals()
	{
		return this.nodeModelsVisuals;
	}
	
	/**
	 * Returns the hash map associating the edge models with their visuals.
	 * @return the hash map associating the edge models with their visuals
	 */
	public HashMap getEdgeModelsVisuals()
	{
		return this.edgeModelsVisuals;
	}
	
	/**
	 * Returns the hash map associating the node visuals with their models.
	 * @return the hash map associating the node visuals with their models
	 */
	public HashMap getNodeVisualsModels()
	{
		return this.nodeVisualsModels;
	}
	
	/**
	 * Returns the hash map associating the edge visuals with their models.
	 * @return the hash map associating the edge visuals with their models
	 */
	public HashMap getEdgeVisualsModels()
	{
		return this.edgeVisualsModels;
	}
	
	/**
	 * Creates a new node by triggering a {@link NewNodeAction}.
	 * @param location the location of the new node (pixels)
	 */
	public void newNode(Point location) {
		try {
			new NewNodeAction(controller, location, getBounds());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Moves a node by triggering a {@link MoveNodeAction}.
	 * @param nodeVisual the node visual associated with the node being moved
	 * @param newLocation the new location of the node
	 */
	public void moveNodeForVisual(NodeVisual nodeVisual, Point newLocation) {
		Node clickedNode = (Node) nodeVisualsModels.get(nodeVisual);
		try {
			new MoveNodeAction(controller, clickedNode, new Position(newLocation, bounds));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes a node by triggering a {@link DeleteNodeAction}.
	 * @param nodeVisual the node visual associated with the node being deleted
	 */
	public void deleteNodeForVisual(NodeVisual nodeVisual) {
		Node clickedNode = (Node) nodeVisualsModels.get(nodeVisual);
		try {
			new DeleteNodeAction(controller, clickedNode);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Adds a new edge between two nodes by triggering a {@link NewEdgeAction}.
	 * @param target the node visual associated with the target node of the new edge
	 */
	public void addEdgeForVisual(NodeVisual target) {
		Node sourceNode = (Node) nodeVisualsModels.get(newEdgeSource);
		Node targetNode = (Node) nodeVisualsModels.get(target);
		try {
			new NewEdgeAction(controller, sourceNode, targetNode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//newEdgeSource = null;
	}
	
	/**
	 * Changed the weight of an edge by triggering a {@link WeighEdgeAction}.
	 * @param edgeVisual the edge visual associated with the edge whose weight is being changed
	 */
	public void weighEdgeForVisual(EdgeVisual edgeVisual) {
		Edge clickedEdge = (Edge) edgeVisualsModels.get(edgeVisual);
		try {
			new WeighEdgeAction(controller, clickedEdge, edgeVisual.getWeight());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Deletes an edge by triggering a {@link DeleteEdgeAction}.
	 * @param edgeVisual the edge visual associated with the edge being deleted
	 */
	public void deleteEdgeForVisual(EdgeVisual edgeVisual) {
		Edge clickedEdge = (Edge) edgeVisualsModels.get(edgeVisual);
		try {
			new DeleteEdgeAction(controller, clickedEdge);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Overridden in order to catch resizing and then to redisplay the graph scaled to the new size.
	 */
	public void setBounds(Rectangle rect) {
		super.setBounds(rect);
		
		// Only proceed if bounds have truly changed.
		// Also, check if graph is a null pointer (happens when GUI is set up for the first time).
		if (!rect.equals(oldBounds) && (graph != null))
		{
			// Set new bounds for every nodeVisual.
			// TODO: Accessing the nodeVisuals should be easier and abstracted away (see update() method below).
			// Perhaps we should rethink the mapping between visuals and nodes ...?
			ArrayList nodeList = graph.getNodeList();
			for (int i = 0; i < nodeModelsVisuals.size(); i++)
			{
				Node node = (Node)nodeList.get(i);
				NodeVisual nodeVisual = (NodeVisual)nodeModelsVisuals.get(node);
				nodeVisual.setCenter(node.getPosition().getScreenPoint(rect));		// Use rect, not getBounds()!
			}
		}
		oldBounds = rect.getCopy();	// Save current bounds for next time.
	}
	
	/**
	 * Updates the entire visualization.
	 * setGraph() must be called with a valid graph before calling this method.
	 */
	public void update()
	{
		removeAll();
		
		Node currentNode;
		Edge currentEdge;
		ArrayList nodeList = graph.getNodeList();
		ArrayList edgeList = graph.getEdgeList();
		
		/*HashMap */nodeModelsVisuals.clear(); // = new HashMap();
		/*HashMap */edgeModelsVisuals.clear(); // = new HashMap();
		
		// create the NodeVisuals, map each Node to its NodeVisual and each NodeVisual to its Node
		for (int i = 0; i < nodeList.size(); i++)
		{
			currentNode = (Node) nodeList.get(i);
			NodeVisual n = new NodeVisual(device, this, currentNode);
			nodeModelsVisuals.put(currentNode, n);
			nodeVisualsModels.put(n, currentNode);
		}
		
		// create the EdgeVisuals, map each Edge to its EdgeVisual and each EdgeVisual to its Edge
		for (int i = 0; i < edgeList.size(); i++)
		{
			currentEdge = (Edge) edgeList.get(i);
			NodeVisual startNV, endNV;
			
			startNV = (NodeVisual) nodeModelsVisuals.get(currentEdge.getStartNode());
			endNV = (NodeVisual) nodeModelsVisuals.get(currentEdge.getEndNode());
			
			if ((startNV != null) && (endNV != null))
			{
				EdgeVisual e = new EdgeVisual(device, this, startNV, endNV, currentEdge);
				edgeModelsVisuals.put(currentEdge, e);
				edgeVisualsModels.put(e, currentEdge);
				
				// Add edges first, on lower "layers" (higher indices);
				e.addToParent(this);
			}
			else
			{
				//throw new Exception("Internal error.");
			}
		}
		
		for (int i = 0; i < nodeModelsVisuals.size(); i++)
		{
			NodeVisual n = (NodeVisual) nodeModelsVisuals.get(nodeList.get(i));
			n.addToParent(this);
		}
		
		// Determine index (layer) at which temporary edge line should be added (above edges, below nodes).
		newEdgeLineInsertionIndex = 0;
		for (int i = 0; i < getChildren().size(); i++) {
			Object child = getChildren().get(i);
			if (child.getClass().equals(PolylineConnection.class)) {
				newEdgeLineInsertionIndex++;
			}
		}
	}
	
	
	/* Methods inherited from MouseListener interface. */
	
	/**
	 * Responds when the mouse button is pressed.
	 * <ul>
	 * <li>If in MODE_ADD_MOVE_NODE, initiates a new node operation.</li>
	 * </ul>
	 */
	public void mousePressed(MouseEvent event) {
		switch (getControllerMode())
		{
			case (Controller.MODE_NO_TOOL_ACTIVE): {
				break;
			}
			case (Controller.MODE_ADD_MOVE_NODE): {
				newNode(event.getLocation());
				break;
			}
			case (Controller.MODE_DELETE_NODE): {
				break;
			}
			case (Controller.MODE_ADD_WEIGH_EDGE): {
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
	 * <li>If in MODE_ADD_MOVE_NODE and a node has been dragged, finishes a move node operation.</li>
	 * <li>If in MODE_ADD_WEIGH_EDGE, either cancels an add edge operation or finishes a weigh edge operation.</li>
	 * </ul>
	 */
	public void mouseReleased(MouseEvent event) {
		switch (getControllerMode())
		{
			case (Controller.MODE_NO_TOOL_ACTIVE): {
				break;
			}
			case (Controller.MODE_ADD_MOVE_NODE): {
				if (isDragMoveNode()) {
					moveNodeForVisual(draggingNodeVisual, event.getLocation());
				}
				break;
			}
			case (Controller.MODE_DELETE_NODE): {
				break;
			}
			case (Controller.MODE_ADD_WEIGH_EDGE): {
				if (isDragAddEdge()) {
					// Mouse was released outside of a node visual.
					newEdgeSource.cancelNewEdge();
					newEdgeSource = null;
				} else if (isDragWeighEdge()) {
					draggingEdgeWeightVisual.mouseReleased(event);
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
	
	public void mouseDoubleClicked(MouseEvent event) {
	}
	
	
	/* Methods inherited from MouseMotionListener interface. */
	
	/**
	 * Responds when the mouse is dragged.
	 * <ul>
	 * <li>If in MODE_ADD_MOVE_NODE and a node is being dragged, passes the event on to that node.</li>
	 * <li>If in MODE_ADD_WEIGH_EDGE, either passes the event on to an edge weight visual being weighed
	 * or to the source node visual of a line representing a new edge (user adds line by click/drag/release).</li>
	 * </ul>
	 */
	public void mouseDragged(MouseEvent event) {
		// This method gets called when the mouse is outside draggingNodeVisual's innerCircle.
		// When inside the circle, draggingNodeVisual's mouseDragged() method gets called instead.
		switch (getControllerMode())
		{
			case (Controller.MODE_NO_TOOL_ACTIVE): {
				break;
			}
			case (Controller.MODE_ADD_MOVE_NODE): {
				if (isDragMoveNode()) {
					draggingNodeVisual.mouseDragged(event);
				}
				break;
			}
			case (Controller.MODE_DELETE_NODE): {
				break;
			}
			case (Controller.MODE_ADD_WEIGH_EDGE): {
				if (isDragWeighEdge()) {
					draggingEdgeWeightVisual.mouseDragged(event);
				} else if (isDragAddEdge()) {
					newEdgeSource.mouseDragged(event);
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

	public void mouseEntered(MouseEvent event) {
	}

	public void mouseExited(MouseEvent event) {
	}

	public void mouseHover(MouseEvent event) {
	}

	/**
	 * Responds when the mouse has moved.
	 * <ul>
	 * <li>If in MODE_ADD_WEIGH_EDGE, either passes the event on to the source node visual 
	 * of a line representing a new edge (user adds line by click/drag/release),
	 * or calls mouseExited() with this event on an edge weight visual that was registered for clean up.</li>
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
				if (isDragAddEdge()) {
					newEdgeSource.mouseMoved(event);
				} else {
					// Clean up after exiting weight visual.
					if (cleanupEdgeWeightVisual != null) {
						cleanupEdgeWeightVisual.mouseExited(event);
						cleanupEdgeWeightVisual = null;
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
}
