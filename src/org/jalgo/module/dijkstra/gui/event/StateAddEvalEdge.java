package org.jalgo.module.dijkstra.gui.event;

import java.awt.Point;

import org.jalgo.module.dijkstra.actions.ActionException;
import org.jalgo.module.dijkstra.actions.NewEdgeAction;
import org.jalgo.module.dijkstra.actions.WeighEdgeAction;
import org.jalgo.module.dijkstra.gfx.EdgeVisual;
import org.jalgo.module.dijkstra.gfx.EdgeWeightVisual;
import org.jalgo.module.dijkstra.gfx.NewEdgeLine;
import org.jalgo.module.dijkstra.gfx.NodeVisual;
import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.gui.components.GraphDisplay;
import org.jalgo.module.dijkstra.model.Edge;
import org.jalgo.module.dijkstra.model.Node;
import org.jalgo.module.dijkstra.util.DefaultExceptionHandler;

/**
 * This class represents the state, which collaborates with the mode
 * {@link org.jalgo.module.dijkstra.gui.Controller#MODE_ADD_WEIGH_EDGE}.
 * 
 * @author Alexander Claus
 */
public class StateAddEvalEdge
extends GraphDisplayState {

	private EdgeWeightVisual clickedEdgeWeight;
	private Edge evaluatedEdge;
	private boolean draggingEdgeCreation;
	private boolean edgeCreation;
	private Node selectedNode;
	private Node sourceNode;
	private Edge selectedEdge;

	public StateAddEvalEdge(GraphDisplay display, Controller controller) {
		super(display, controller);
	}

	@Override
	public void mousePressed(Point p) {
		Edge clickedEdge = display.findEdge(p);
		// finding an edge means, that user wants to evaluate the edge
		if (clickedEdge != null) {
			clickedEdgeWeight =
				((EdgeVisual)clickedEdge.getVisual()).getWeightVisual();
			if (clickedEdgeWeight != null) {
				evaluatedEdge = clickedEdge;
				clickedEdgeWeight.beginDragging(p);
				evaluatedEdge.setActive(true);
				evaluatedEdge.getVisual().update();
			}
		}
		// if no edge is selected, perhaps it is the start node of a new
		// edge?
		else {
			Node clickedNode = display.findNode(p);
			if (clickedNode != null) {
				// this is the start node of a new edge
				if (display.getNewEdgeLine() == null) {
					sourceNode = clickedNode;
					sourceNode.setActive(true);
					sourceNode.getVisual().update();
					display.setNewEdgeLine(new NewEdgeLine(sourceNode));
					edgeCreation = true;
				}
				// else this mouse press is the begin of the 2nd click
				// of new edge creation
			}
		}
	}

	@Override
	public void mouseReleased(Point p) {
		// end of evaluating the edge
		if (clickedEdgeWeight != null) {
			try {
				new WeighEdgeAction(controller, evaluatedEdge,
					clickedEdgeWeight.getWeight());
				evaluatedEdge.setActive(false);
				evaluatedEdge.getVisual().update();
			}
			catch (ActionException ex) {
				new DefaultExceptionHandler(ex);
			}
			evaluatedEdge = null;
			clickedEdgeWeight = null;
		}
		// end of creating a new edge, react only to release events,
		// when
		// edge is created by dragging. otherwise mouseClicked is
		// responsible
		else if (display.getNewEdgeLine() != null) {
			if (draggingEdgeCreation) {
				Node clickedNode = display.findNode(p);
				if (clickedNode != null) {
					// it is really the end node, not the start node
					if (sourceNode != clickedNode) try {
						new NewEdgeAction(controller, sourceNode, clickedNode);
						clickedNode.setActive(false);
						clickedNode.setHighlighted(true);
						clickedNode.getVisual().update();
					}
					catch (ActionException ex) {
						new DefaultExceptionHandler(ex);
					}
				}
				else {
					Node startNode = display.findNode((Point)
						display.getNewEdgeLine().getP1());
					// it is only for robust application, normally, the start
					// node can't be null!
					if (startNode != null) {
						startNode.setActive(false);
						startNode.setHighlighted(false);
						startNode.getVisual().update();
					}
				}
				display.setNewEdgeLine(null);
				draggingEdgeCreation = false;
				edgeCreation = false;
			}
		}
	}

	@Override
	public void mouseClicked(Point p) {
		// clicking in add_eval_edge mode means to create edge by selecting
		// nodes
		Node clickedNode = display.findNode(p);
		if (clickedNode != null) {
			// clickedNode is the target node
			if (display.getNewEdgeLine() != null &&
				display.getNewEdgeLine().getSourceNode() != clickedNode) {
				try {
					new NewEdgeAction(controller,
						display.getNewEdgeLine().getSourceNode(),
						clickedNode);
					display.setNewEdgeLine(null);
					edgeCreation = false;
					clickedNode.setHighlighted(true);
					clickedNode.getVisual().update();
					sourceNode = null;
				}
				catch (ActionException ex) {
					new DefaultExceptionHandler(ex);
				}
			}
		}
		else {
			// user aborted new edge creation by clicking somewhere
			if (sourceNode != null) {
				sourceNode.setActive(false);
				sourceNode.setHighlighted(false);
				sourceNode.getVisual().update();
			}
			display.setNewEdgeLine(null);
			edgeCreation = false;
		}
	}

	@Override
	public void mouseMoved(Point p) {
		Edge currentEdge = display.findEdge(p);
		// mouse leaved selectedEdge
		if (selectedEdge != null &&
			currentEdge != selectedEdge) {
			selectedEdge.setHighlighted(false);
			selectedEdge.getVisual().update();
		}
		selectedEdge = currentEdge;
		// there is an edge under mouse, do not highlight, when edge is created
		if (selectedEdge != null &&
			!edgeCreation) {
			selectedEdge.setHighlighted(true);
			selectedEdge.getVisual().update();
		}
		// when no edge, perhaps there is a node under the mouse?
		else {
			Node currentNode = display.findNode(p);
			// the user is searching the target node for a new edge
			if (display.getNewEdgeLine() != null) {
				if (currentNode != null) {
					display.getNewEdgeLine().setP2(
						((NodeVisual)currentNode.getVisual()).getCenter());
					currentNode.setActive(true);
				}
				else {
					// mouse leaved selected node, source node is ignored
					if (selectedNode != null &&
						selectedNode != sourceNode) {
						selectedNode.setActive(false);
						selectedNode.setHighlighted(false);
						selectedNode.getVisual().update();
						selectedNode = null;
					}
					display.getNewEdgeLine().setP2(p);
				}
			}
			// the user is searching a source node for a new edge
			// mouse leaved selected Edge
			else if (selectedNode != null &&
				currentNode != selectedNode) {
				selectedNode.setActive(false);
				selectedNode.setHighlighted(false);
				selectedNode.getVisual().update();
			}
			selectedNode = currentNode;
			// there is a node under mouse
			if (selectedNode != null) {
				selectedNode.setHighlighted(true);
				selectedNode.getVisual().update();
			}
		}
	}

	@Override
	public void mouseDragged(Point p) {
		// evaluating the edge weight
		if (clickedEdgeWeight != null)
			clickedEdgeWeight.updateWeightToDragging(p);
		else {
			Node currentNode = display.findNode(p);
			// the user is searching the target node for a new edge
			if (display.getNewEdgeLine() != null) {
				if (currentNode != null) {
					display.getNewEdgeLine().setP2(
						((NodeVisual)currentNode.getVisual()).getCenter());
					currentNode.setActive(true);
					currentNode.getVisual().update();
					selectedNode = currentNode;
				}
				else {
					// mouse leaved selected node, source node is ignored
					if (selectedNode != null &&
						selectedNode != sourceNode) {
						selectedNode.setActive(false);
						selectedNode.setHighlighted(false);
						selectedNode.getVisual().update();
						selectedNode = null;
					}
					display.getNewEdgeLine().setP2(p);
				}
				draggingEdgeCreation = true;
			}
		}
	}
}