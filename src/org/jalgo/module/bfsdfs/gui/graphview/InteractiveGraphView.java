package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.ImageIcon;

import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.gui.GUITest;
import org.jalgo.module.bfsdfs.gui.GraphCalculation;

/**
 * <code>InteractiveGraphView</code> represents an area where a graph will be
 * drawn and the user can edit this graph. There are different edit modes which
 * influence the handling of mouse events and motions
 * 
 * @author Anselm Schmidt
 * 
 */
public class InteractiveGraphView extends GraphView implements MouseListener,
		MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	/**
	 * The current edit mode.
	 */
	protected EditMode mode;

	/**
	 * The erase cursor
	 */
	protected Cursor eraseCursor;

	/**
	 * The default cursor
	 */
	protected Cursor defaultCursor;

	/**
	 * Selected node. In edit mode MOVE_NODE the node that is being moved
	 * around. In the edit modes DRAW_EDGE, DRAG_EDGE, DRAW_DOUBLE_EDGE and
	 * DRAG_DOUBLE_EDGE, it is the start node of the new edge.
	 */
	protected VisualNode selectedNode;

	/**
	 * Current mouse position. Will be used in the edit modes DRAW_EDGE,
	 * DRAW_DOUBLE_EDGE, DRAG_EDGE and DRAG_DOUBLE_EDGE to draw a temporary edge
	 * that shows the edge that will be created as soon as the user clicks or
	 * drops at the position of an existing node. In the edit mode PUT_NODE it
	 * will be used to draw a temporary node where a new node will be added as
	 * soon as the user clicks at the current position.
	 */
	protected Point mousePosition;

	/**
	 * Is dragging still possible? Will be <code>True</code>, if the user
	 * pressed, but didn't release the mouse, <code>False</code> otherwise.
	 */
	protected boolean isDraggingPossible;
	
	/**
	 * Will be <code>True</code>, if the mouse pointer is inside the component,
	 * <code>False</code> otherwise.
	 */
	protected boolean isMouseIn;

	/**
	 * Remove an edge or a direction of a bidirectional edge from the graph.
	 * If <code>edge</code> is bidirectional, it depends on the hitting area, whether
	 * the whole edge or just one direction will be removed.
	 * 
	 * @param edge
	 *            Edge to change.
	 * @param hitAt
	 *            Point, the edge has been hit at.
	 * @author Anselm Schmidt
	 */
	protected void removeEdgeOrDirection(VisualEdge edge, Point hitAt) {
		// check, if it is a bidirectional edge
		if (edge.isBidirectional()) {
			// bidirectional edge: get start end end node id and position
			int id1 = edge.getStartNode();
			int id2 = edge.getEndNode();
			
			Point end1 = this.getGraphController().getNodePosition(id1);
			Point end2 = this.getGraphController().getNodePosition(id2);
			
			// get the hit area of the bidirectional edge
			short hitArea = GraphCalculation.getBidirectionalEdgeHitArea(end1.x,
					end1.y, end2.x, end2.y, mousePosition, EDGE_ARROW_LENGTH,
					NODE_SIZE);

			// remove edge or directions depending on the hit area
			switch (hitArea) {
			case 1:
				// remove first direction
				this.getGraphController().removeEdge(id2, id1);
				break;

			case 2:
				// remove second direction
				this.getGraphController().removeEdge(id1, id2);
				break;

			default:
				// remove both directions
				this.getGraphController().removeEdge(id1, id2);
				this.getGraphController().removeEdge(id2, id1);
			}
		}
		else {
			// remove edge
			this.getGraphController().removeEdge(edge.getStartNode(),
					edge.getEndNode());
		}
	}

	/**
	 * Add a node to the graph using the <code>GraphController</code> singleton.
	 * 
	 * @param position
	 *            Position of the new node.
	 * @author Anselm Schmidt
	 */
	protected void addNode(Point position) {
		if(!hitNode(null, position)) {		
			GUITest.write("[GRAPH] addNode([" + position.x + ";" + position.y + "])",
					true);
		
			// add node by using the graph controller
			this.getGraphController().addNode(position);
		}
	}
	
	/**
	 * Add an edge to the graph using the <code>GraphController</code> singleton.
	 * 
	 * @param start Id of the start node.
	 * @param end Id of the end node.
	 * @author Anselm Schmidt
	 */
	protected void addEdge(int start, int end) {
		GUITest.write("[GRAPH] addEdge(" + start + ", " + end + ")", true);
		this.getGraphController().addEdge(start, end);
	}
	
	/**
	 * Add a bidirectional edge to the graph using the <code>GraphController</code>
	 * singleton.
	 * 
	 * @param start Id of the start node.
	 * @param end Id of the end node.
	 * @author Anselm Schmidt
	 */
	protected void addDoubleEdge(int start, int end) {
		GUITest.write("[GRAPH] addDoubleEdge(" + start + ", " + end + ")", true);
		this.getGraphController().addDoubleEdge(start, end);
	}

	/**
	 * Start moving a node around.
	 * 
	 * @param node
	 *            The node object.
	 * @author Anselm Schmidt
	 */
	protected void startMovingNode(VisualNode node) {
		// select node and enter edit mode MOVE_NODE
		selectedNode = node;
		setEditMode(EditMode.MOVE_NODE);

		// dragging is possible
		isDraggingPossible = true;
	}

	/**
	 * Start drawing a new edge.
	 * 
	 * @param startNode
	 *            The start node of the new edge.
	 * @author Anselm Schmidt
	 */
	protected void startNewEdge(VisualNode startNode) {
		// select node and enter edit mode DRAW_EDGE
		selectedNode = startNode;
		setEditMode(EditMode.DRAW_EDGE);

		// dragging is possible
		isDraggingPossible = true;
	}

	/**
	 * Start drawing a new bidirectional edge.
	 * 
	 * @param startNode
	 *            The start node of the new bidirectional edge.
	 * @author Anselm Schmidt
	 */
	protected void startNewDoubleEdge(VisualNode startNode) {
		// select node and enter edit mode DRAW_DOUBLE_EDGE
		selectedNode = startNode;
		setEditMode(EditMode.DRAW_DOUBLE_EDGE);

		// dragging is possible
		isDraggingPossible = true;
	}

	/**
	 * End edge drawing at specified position. If a node exists at this
	 * position, the edge will be drawn. Otherwise, it will be cancelled.
	 * 
	 * @param at
	 *            The position where the drawing ends.
	 * @author Anselm Schmidt
	 */
	protected void endNewEdge(Point at) {
		// check, if a node has been hit
		VisualNode node = getNodeAt(at);

		if (node == null) {
			// no node has been hit: cancel the drawing
			cancelDrawingEdge();
		} else {
			// node has been hit: finish new edge
			finishNewEdge(node.getId());
		}
	}

	/**
	 * End bidirectional edge drawing at specified position. If a node exists at
	 * this position, the bidirectional edge will be drawn. Otherwise, it will
	 * be cancelled.
	 * 
	 * @param at
	 *            The position where the drawing ends.
	 * @author Anselm Schmidt
	 */
	protected void endNewDoubleEdge(Point at) {
		// check, if a node has been hit
		VisualNode node = getNodeAt(at);

		if (node == null) {
			// no node has been hit: cancel the drawing
			cancelDrawingDoubleEdge();
		} else {
			// node has been hit: finish new bidirectional edge
			finishNewDoubleEdge(node.getId());
		}
	}

	/**
	 * Finish the moving of a node. The moved node will stay at the last valid
	 * position.
	 * 
	 * @author Anselm Schmidt
	 */
	protected void finishMovingNode() {
		// leave edit mode MOVE_NODE or DRAG_NODE
		setEditMode(EditMode.PUT_NODE);

		// set node focused
		selectedNode.setFocused(true);

		// remove selection
		selectedNode = null;

		// repaint to show changes
		repaint();
		
		// reset mouse cursor
		setCursor(new Cursor(Cursor.HAND_CURSOR));
	}

	/**
	 * Finish a new edge.
	 * 
	 * @param endNode
	 *            Id of the end node of the new edge.
	 * @author Anselm Schmidt
	 */
	protected void finishNewEdge(int endNode) {
		// add edge
		addEdge(selectedNode.getId(), endNode);

		// remove selection and leave edit mode DRAW_EDGE
		selectedNode = null;
		setEditMode(EditMode.START_EDGE);
	}

	/**
	 * Finish a new bidirectional edge.
	 * 
	 * @param endNode
	 *            ID of the end node of the new edge.
	 * @author Anselm Schmidt
	 */
	protected void finishNewDoubleEdge(int endNode) {
		// check if it is a circle edge
		if(selectedNode.getId() == endNode) {
			// there are no bidirectional circle edges, add a simple circle edge
			addEdge(endNode, endNode);
		}
		else {
			// 	add bidirectional edge
			addDoubleEdge(selectedNode.getId(), endNode);
		}

		// remove selection and leave edit mode DRAW_DOUBLE_EDGE
		selectedNode = null;
		setEditMode(EditMode.START_DOUBLE_EDGE);
	}

	/**
	 * Cancel drawing a new edge.
	 * 
	 * @author Anselm Schmidt
	 */
	protected void cancelDrawingEdge() {
		// remove selection and leave edit mode DRAW_EDGE
		selectedNode = null;
		setEditMode(EditMode.START_EDGE);
	}

	/**
	 * Cancel drawing a new bidirectional edge.
	 * 
	 * @author Anselm Schmidt
	 */
	protected void cancelDrawingDoubleEdge() {
		// remove selection and leave edit mode DRAW_DOUBLE_EDGE
		selectedNode = null;
		setEditMode(EditMode.START_DOUBLE_EDGE);
	}

	/**
	 * Try to move the current node to the specified position. If anything is
	 * already there, the node will not be moved.
	 * 
	 * @param point
	 *            Position the node will be moved to.
	 * @author Anselm Schmidt
	 */
	protected void moveNodeTo(Point point) {
		// check if anything else has been hit
		if (!hitNode(selectedNode, point)) {
			// no hit: move node to new position and repaint
			this.getGraphController().moveNode(selectedNode.getId(), point);
		}
	}

	protected void dragNodeTo(Point point) {
		// move node to new position
		moveNodeTo(point);

		// repaint
		repaint();
	}

	/**
	 * Set all graph elements to unfocused and repaint.
	 * 
	 * @author Anselm Schmidt
	 */
	protected void unfocusAll() {
		// unfocus all elements
		for (VisualGraphElement element : elements) {
			element.setFocused(false);

			// check, if the found element is an edge
			if (element instanceof VisualEdge) {
				VisualEdge edge = (VisualEdge) element;

				// edge found: unfocus edge endings, too
				edge.setStartFocused(false);
				edge.setEndFocused(false);
			}
		}

		// repaint to show changed
		repaint();
	}

	/**
	 * Check, if the boundaries of any element have been hit.
	 * 
	 * @param point
	 *            Point to check.
	 * @return <code>True</code>, if the boundaries of an element have been hit,
	 *         <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	protected boolean hitAnything(Point point) {
		// check all elements
		for (VisualGraphElement element : elements) {
			// check boundaries of current element
			if (element.hitBoundaries(point)) {
				// boundaries have been hit
				return true;
			}
		}

		// nothing has been hit
		return false;
	}

	/**
	 * Check, if the boundaries of any node have been hit. If
	 * <code>except</code> is not <code>null</code>, this node will be ignored.
	 * 
	 * @param except
	 *            Node that will be ignored while checking or <code>null</code>,
	 *            if all nodes will be checked.
	 * @param point
	 *            Point to check.
	 * @return <code>True</code>, if the boundaries of a node have been hit,
	 *         <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	protected boolean hitNode(VisualNode except, Point point) {
		// check all other elements
		for (VisualGraphElement element : elements) {
			// checking for nodes only
			if (element instanceof VisualNode) {
				// if nodes are equal, return false
				if (except == null || !except.equals(element)) {
					// check boundaries of current other node
					if (element.hitBoundaries(point)) {
						// other node boundaries have been hit
						return true;
					}
				}
			}
		}

		// nothing else been hit
		return false;
	}

	/**
	 * Return the element at a specified position.
	 * 
	 * @param point
	 *            Position of the element.
	 * @result The hit element or <code>null</code> if no element has been hit.
	 * @author Anselm Schmidt
	 */
	protected VisualGraphElement getElementNear(Point point) {
		// check all elements
		for (VisualGraphElement element : elements) {
			// check, if the element boundaires have been hit
			if (element.hitBoundaries(point)) {
				// return element
				return element;
			}
		}

		return null;
	}

	/**
	 * Return the node at a specified position.
	 * 
	 * @param point
	 *            Position of the node.
	 * @result The hit node or <code>null</code> if no node has been hit.
	 * @author Anselm Schmidt
	 */
	protected VisualNode getNodeAt(Point point) {
		// check all elements
		for (VisualGraphElement element : elements) {
			// check, if the current element is a node
			if (element instanceof VisualNode) {
				// check, if the node has been hit
				if (element.wasHit(point)) {
					// return node
					return (VisualNode) element;
				}
			}
		}

		return null;
	}

	/**
	 * Return the node near a specified position using the node boundaries.
	 * 
	 * @param point
	 *            Position of the node.
	 * @result The hit node or <code>null</code> if no node has been hit.
	 * @author Anselm Schmidt
	 */
	protected VisualNode getNodeNear(Point point) {
		// check all elements
		for (VisualGraphElement element : elements) {
			// check, if the current element is a node
			if (element instanceof VisualNode) {
				// check, if the node boundaries have been hit
				if (element.hitBoundaries(point)) {
					// return node
					return (VisualNode) element;
				}
			}
		}

		return null;
	}

	/**
	 * Return the edge near a specified position.
	 * 
	 * @param point
	 *            Position of the edge.
	 * @result The hit edge or <code>null</code> if no edge has been hit.
	 * @author Anselm Schmidt
	 */
	protected VisualEdge getEdgeNear(Point point) {
		// check all elements
		for (VisualGraphElement element : elements) {
			// check, if the current element is a node
			if (element instanceof VisualEdge) {
				// check, if the node boundaries have been hit
				if (element.hitBoundaries(point)) {
					// return node
					return (VisualEdge) element;
				}
			}
		}

		return null;
	}

	/**
	 * Perform a mouse move in erase mode. Highlights nodes, edges and edge
	 * arrows.
	 * 
	 * @author Anselm Schmidt
	 */
	protected void eraserMouseMove() {
		// set all elements to unfocussed
		unfocusAll();

		// check, if the mouse is over a node
		VisualNode node = getNodeAt(mousePosition);

		if (node == null) {
			// check, if the mouse is over an edge
			VisualEdge edge = getEdgeNear(mousePosition);

			if (edge != null) {
				// handle mouse over edge: check if it is a bidirectional edge
				if (edge.isBidirectional()) {
					// bidirectional edge: check hitting area
					Point end1 = this.getGraphController().getNodePosition(
							edge.getStartNode());
					Point end2 = this.getGraphController().getNodePosition(
							edge.getEndNode());

					short hitArea = GraphCalculation.getBidirectionalEdgeHitArea(end1.x,
							end1.y, end2.x, end2.y, mousePosition, EDGE_ARROW_LENGTH,
							NODE_SIZE);

					// set focus of a bidirectional edge part or the whole edge
					// depending on hitting area
					switch (hitArea) {
					case 1:
						edge.setStartFocused(true);
						break;

					case 2:
						edge.setEndFocused(true);
						break;

					default:
						// focus whole edge
						edge.setStartFocused(true);
						edge.setEndFocused(true);
						edge.setFocused(true);
					}
				}
				else {
					// simple edge: focus it
					edge.setFocused(true);
					edge.setEndFocused(true);
				}
			}
		} else {			
			// handle mouse over node: focus node
			node.setFocused(true);
		}

		// repaint to show changes
		repaint();
	}
	
	/**
	 * Perform a mouse click in erase mode. Removes nodes, edges and directions of
	 * bidirectional edges.
	 * @param position Position of the mouse click.
	 * @author Anselm Schmidt
	 */
	protected void eraserMouseClick(Point position) {
		// check, if node has been hit
		VisualNode node = getNodeAt(position);
		
		if(node != null) {
			// node has been hit: remove it
			GUITest.write("[GRAPH] removeNode(" + node.getId() + ")", true);
			this.getGraphController().removeNode(node.getId());
		}
		else {
			// check, if edge has been hit
			VisualEdge edge = getEdgeNear(position);
			
			if(edge != null)  {
				// edge has been hit: remove it (or a direction of it)
				removeEdgeOrDirection(edge, position);
			}
		}
	}

	/**
	 * Constructor. Sets edit mode to the default value PUT_NODE and adds itself
	 * as MouseListener and as MouseMotionListener to itself. Creates the
	 * different cursors.
	 * 
	 * @author Anselm Schmidt
	 */
	public InteractiveGraphView(GraphController graphController) {
		super(graphController);
		// set default edit mode
		mode = EditMode.PUT_NODE;

		// dragging is not possible in the beginning
		isDraggingPossible = false;

		// add mouse and mouse motion listeners
		addMouseListener(this);
		addMouseMotionListener(this);

		// create cursors
		eraseCursor = Toolkit.getDefaultToolkit().createCustomCursor(
				(new ImageIcon(Messages.getResourceURL("main", "Icon.Clear")))
						.getImage(), new Point(0, 15), "ERASER");
		defaultCursor = new Cursor(Cursor.DEFAULT_CURSOR);

		// get mouse position and transform it into GraphView coordinates
		mousePosition = MouseInfo.getPointerInfo().getLocation();
		mousePosition.x -= getX();
		mousePosition.y -= getY();
	}
	
	/**
	 * Create a start node in the middle of the <code>InteractiveGraphView</code>.
	 * @author Anselm Schmidt
	 */
	public void addStartNode() {
		this.getGraphController().addStartNode(new Point(MAX_GRAPH_WIDTH / 2, MAX_GRAPH_HEIGHT / 2));
	}

	/**
	 * Set the edit mode. NOTE: All elements will be unfocused.
	 * 
	 * @param mode
	 *            New edit mode. Use <code>GUIConstants.PUT_NODE</code> as
	 *            default value.
	 * @author Anselm Schmidt
	 */
	public void setEditMode(EditMode mode) {
		// set new edit mode
		this.mode = mode;

		// set all elements to unfocused
		unfocusAll();

		// set cursor depending on edit mode
		switch (mode) {
		case ERASE:
			this.setCursor(eraseCursor);
			break;

		default:
			this.setCursor(defaultCursor);
		}
	}
	
	//gets the current edit mode
	public EditMode getEditMode() {
		return mode;
	}

	/**
	 * Drawing method. Uses the drawing method of GraphView to paint the graph.
	 * Draws a temporary node or edge, if necessary.
	 * 
	 * @param g
	 *            Used instance of <code>Graphics</code>.
	 * @author Anselm Schmidt
	 */
	public void paint(Graphics g) {
		VisualTempEdge tempEdge = null;
		VisualTempNode tempNode = null;
		VisualNode endNode = null;

		// paint GraphView
		super.paint(g);

		// check edit mode
		switch (mode) {
		case ALGORITHM:
			// draw semi-transparent white rectangle over 
			g.setColor(new Color(255, 255, 255, GRAPHVIEW_ALPHA_VALUE));
			g.fillRect(0, 0, getWidth(), getHeight());
			break;
			
		case PUT_NODE:
			// check, if mouse position hits an existing node
			if (isMouseIn && !hitNode(null, mousePosition)) {
				// no node has been hit: create and paint a temporary node
				tempNode = new VisualTempNode(mousePosition, this.getGraphController());
				tempNode.paint(g);
			}
			break;

		case DRAW_EDGE:
		case DRAG_EDGE:
			// get the end node
			endNode = getNodeAt(mousePosition);

			// create and paint a temporary edge
			tempEdge = new VisualTempEdge(selectedNode, endNode, mousePosition,
					false, this.getGraphController());
			tempEdge.paint(g);
			break;

		case DRAW_DOUBLE_EDGE:
		case DRAG_DOUBLE_EDGE:
			// get the end node
			endNode = getNodeAt(mousePosition);

			// create and paint temporary bidirectional edge
			tempEdge = new VisualTempEdge(selectedNode, endNode, mousePosition,
					true, this.getGraphController());
			tempEdge.paint(g);
			break;
		}
	}

	/**
	 * Event handler for mouse movements. Will be used to focus elements.
	 * 
	 * @param e
	 *            Mouse event.
	 * @author Anselm Schmidt
	 */
	public void mouseMoved(MouseEvent e) {		
		// refresh mouse position
		mousePosition = e.getPoint();

		// check edit mode
		switch (mode) {
		case ALGORITHM:
			// no interactivity during algorithm
			return;
			
		case MOVE_NODE:
			// move node to mouse position if possible
			moveNodeTo(mousePosition);
			return;

		case ERASE:
			eraserMouseMove();
			return;
		}

		// set all elements to unfocussed
		unfocusAll();

		// check, if the mouse is on a node
		VisualNode node = getNodeAt(mousePosition);

		if (node != null) {
			// handle mouse on node: focus node
			node.setFocused(true);
			
			if(mode == EditMode.PUT_NODE) {
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		}
		else if(mode == EditMode.PUT_NODE){
			setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		}

		// repaint GraphView to show changes
		repaint();
	}

	/**
	 * Mouse click handler. The performed action depends on the current edit
	 * mode.
	 * 
	 * @param e
	 *            Mouse event.
	 * @author Anselm Schmidt
	 */
	public void mousePressed(MouseEvent e) {
		VisualNode node = null;

		// perform actions depending on edit mode
		switch (mode) {
		case MOVE_NODE:
			// finish node moving
			finishMovingNode();
			break;

		case PUT_NODE:
			// check, if a node has been hit
			node = getNodeAt(e.getPoint());

			if (node == null) {
				// no node has been hit: add new node
				addNode(e.getPoint());
			} else {
				// node hit: start to move node
				startMovingNode(node);
				setCursor(new Cursor(Cursor.HAND_CURSOR));
			}
			break;

		case START_EDGE:
			// check, if a node has been hit
			node = getNodeAt(e.getPoint());

			if (node != null) {
				// node has been hit: start drawing a new edge
				startNewEdge(node);
			}
			break;

		case START_DOUBLE_EDGE:
			// check, if a node has been hit
			node = getNodeAt(e.getPoint());

			if (node != null) {
				// node has been hit: start drawing a new edge
				startNewDoubleEdge(node);
			}
			break;

		case DRAW_EDGE:
			// user set end point of new edge
			endNewEdge(e.getPoint());
			break;

		case DRAW_DOUBLE_EDGE:
			// check, if a node has been hit
			endNewDoubleEdge(e.getPoint());
			break;

		case ERASE:
			eraserMouseClick(e.getPoint());
			break;
		}

	}

	/**
	 * Mouse drag handler. Allows the user to draw edges by using drag and drop.
	 * If we are in DRAW_EDGE or DRAW_DOUBLE_EDGE mode and dragging is possible,
	 * mode will be changed to DRAG_EDGE or DRAG_DOUBLE_EDGE.
	 * 
	 * @param e
	 *            Mouse event.
	 * @author Anselm Schmidt
	 */
	public void mouseDragged(MouseEvent e) {
		// refresh mouse position
		mousePosition = e.getPoint();

		if (isDraggingPossible) {
			// check edit mode
			switch (mode) {
			case DRAW_EDGE:
				// draw edge by drag & drop
				setEditMode(EditMode.DRAG_EDGE);
				break;
			case DRAG_NODE:
				// move node by drag & drop
				dragNodeTo(mousePosition);
				return;
			case DRAW_DOUBLE_EDGE:
				// draw bidirectional edge by drag & drop
				setEditMode(EditMode.DRAG_DOUBLE_EDGE);
				break;
			case MOVE_NODE:
				// move node by drag & drop
				setEditMode(EditMode.DRAG_NODE);
				setCursor(new Cursor(Cursor.HAND_CURSOR));
				return;
			case DRAG_EDGE:
			case DRAG_DOUBLE_EDGE:
				// perform mouse moving actions below
				break;
			default:
				// no drag & drop: do not perform any further actions here
				return;
			}

			// set all elements to unfocussed
			unfocusAll();

			// check, if the mouse is over a node
			VisualNode node = getNodeAt(mousePosition);

			if (node != null) {
				// handle mouse over node: focus node
				node.setFocused(true);
			}

			// repaint to show changes
			repaint();
		}
	}

	/**
	 * Mouse release handler. If we are in DRAG_EDGE or DRAG_DOUBLE_EDGE mode, a
	 * new edge will be drawn. No new edge will be drawn if the mouse doesn't
	 * point at a node.
	 * 
	 * @param e
	 *            Mouse event.
	 * @author Anselm Schmidt
	 */
	public void mouseReleased(MouseEvent e) {
		// dragging is not longer possible
		isDraggingPossible = false;

		// check, if drag & drop has been performed
		switch (mode) {
		case DRAG_EDGE:
			// new edge by drag & drop
			endNewEdge(e.getPoint());
			break;

		case DRAG_DOUBLE_EDGE:
			// new bidirectional edge by drag & drop
			endNewDoubleEdge(e.getPoint());
			break;

		case DRAG_NODE:
			// finish moving node by drag & drop
			finishMovingNode();
			break;
		}
	}

	/** unused mouse event */
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * Implemented interface method.
	 * Sets the status text and shows the temporary node when the mouse pointer enters
	 * the component.
	 * @author Anselm Schmidt 
	 */
	public void mouseEntered(MouseEvent e) {
		JAlgoGUIConnector.getInstance().setStatusMessage(Messages.getString("bfsdfs",
				"GraphView.scrollPaneStatus"));
		isMouseIn = true;
		repaint();
	}

	/**
	 * Implemented interface method.
	 * Removes the status text and hides the temporary node when the mouse pointer leaves
	 * the component.
	 * @author Anselm Schmidt 
	 */
	public void mouseExited(MouseEvent e) {
		JAlgoGUIConnector.getInstance().setStatusMessage(null);
		isMouseIn = false;
		repaint();
	}
}
