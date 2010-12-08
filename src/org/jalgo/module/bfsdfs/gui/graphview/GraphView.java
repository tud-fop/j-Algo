package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;

import javax.swing.JComponent;
import javax.swing.Timer;

import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.graph.GraphObserver;
import org.jalgo.module.bfsdfs.gui.GUIConstants;
import org.jalgo.module.bfsdfs.gui.GUITest;
import org.jalgo.module.bfsdfs.gui.GraphDrawing;

/**
 * <code>GraphView</code> represents an area where a graph will be drawn
 * 
 * @author Anselm Schmidt
 * 
 */
public class GraphView extends JComponent implements GraphObserver,
		GUIConstants {
	private static final long serialVersionUID = 1L;

	/**
	 * Save the actual graphController.
	 */
	private GraphController graphController = null;

	/**
	 * Return the graphController.
	 */
	protected GraphController getGraphController() {
		return graphController;
	}

	/**
	 * Collection for drawing the graph elements.
	 */
	protected Collection<VisualGraphElement> elements;

	/**
	 * Collection for drawing deleted elements. Needed for delete animations.
	 */
	protected Collection<VisualGraphElement> deletedElements;

	/**
	 * Used drawing strategy for the nodes.
	 */
	private GraphNodeStrategy nodeStrategy;

	/**
	 * Used drawing strategy for the edges.
	 */
	private EdgeDrawingStrategy edgeStrategy;

	/**
	 * <code>True</code>, if animations are enabled, <code>False</code>
	 * otherwise.
	 */
	protected boolean animated;

	/**
	 * Draw all edges.
	 * 
	 * @param g
	 *            Used <code>Graphics</code> instance.
	 * @return <code>True</code>, if the edges have to be redrawn due to
	 *         animations, <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	protected void drawEdges(Graphics g) {
		try {
			// draw deleted edges
			for(VisualGraphElement element : deletedElements) {
				if(element instanceof DeletedEdge) {
					if(element.paint(g, animated)) {
						// repaint deleted edge after a short while
						Repainter repainter = new Repainter(this, element.getRect(g));
						Timer timer = new Timer(ANIMATION_REPAINT_DELAY, repainter);
						timer.setRepeats(false);
						timer.start();
					}
				}
			}

			// draw edges
			for(VisualGraphElement element : elements) {
				if(element instanceof VisualEdge) {
					if(element.paint(g, animated)) {
						// repaint deleted edge after a short while
						Repainter repainter = new Repainter(this, element.getRect(g));
						Timer timer = new Timer(ANIMATION_REPAINT_DELAY, repainter);
						timer.setRepeats(false);
						timer.start();
					}
				}
			}
		}
		catch(ConcurrentModificationException e) {
			// This can occure because of multi-threading - can be ignored.
		}
	}

	/**
	 * Draw all nodes.
	 * 
	 * @param g
	 *            Used <code>Graphics</code> instance.
	 * @return <code>True</code>, if the nodes have to be redrawn due to
	 *         animations, <code>False</code> otherwise.
	 * @author Anselm Schmidt
	 */
	protected void drawNodes(Graphics g) {
		// draw deleted nodes
		for (VisualGraphElement element : deletedElements) {
			if (element instanceof DeletedNode) {
				if (element.paint(g, animated)) {
					// repaint deleted node after a short while
					Repainter repainter = new Repainter(this, element.getRect(g));
					Timer timer = new Timer(ANIMATION_REPAINT_DELAY, repainter);
					timer.setRepeats(false);
					timer.start();
				}
			}
		}

		// draw nodes
		for (VisualGraphElement element : elements) {
			if (element instanceof VisualNode) {
				if (element.paint(g, animated)) {
					// repaint node after a short while
					Repainter repainter = new Repainter(this, element.getRect(g));
					Timer timer = new Timer(ANIMATION_REPAINT_DELAY, repainter);
					timer.setRepeats(false);
					timer.start();
				}
			}
		}
	}

	/**
	 * Remove edge with the specified start end end nodes. Should only be used when
	 * the egde has already been removed by the <code>GraphController</code>.
	 * 
	 * @param start
	 *            Id of the start node.
	 * @param end
	 *            Id of the end node.
	 * @author Anselm Schmidt
	 */
	protected void removeEdge(int start, int end) {
		VisualEdge edge = null;

		// look for existing edges
		for (VisualGraphElement element : elements) {
			if (element instanceof VisualEdge) {
				edge = (VisualEdge) element;

				// check start and end nodes
				if (edge.getStartNode() == start && edge.getEndNode() == end) {
					// specified edge found: remove it
					deletedElements.add(new DeletedEdge(edge, graphController));
					elements.remove(element);
					break;
				}
			}
		}
	}
	
	/**
	 * Removes one direction of an edge. Should only be used when
	 * the direction has already been removed by the <code>GraphController</code>.
	 * @param edge The edge.
	 * @param start Id of the direction's start node.
	 * @param end Id of the direction's end node.
	 * @author Anselm Schmidt
	 */
	protected void removeDirection(VisualEdge edge, int start, int end) {
		deletedElements.add(new DeletedEdge(edge, start, end, graphController));
		edge.removeDirection(start, end);
	}

	/**
	 * Return the node with the specified id.
	 * 
	 * @param id
	 *            Id of the node.
	 * @return The node, if it will be found, <code>null</code> otherwise.
	 */
	protected VisualNode findNode(int id) {
		VisualNode node = null;

		// look for node elements
		for (VisualGraphElement element : elements) {
			if (element instanceof VisualNode) {
				node = (VisualNode) element;

				// check the id of the found node
				if (node.getId() == id) {
					// node found!
					return node;
				}
			}
		}

		// no node with the specified id exists
		return null;
	}

	/**
	 * Return the edge with the specified start and end nodes.
	 * 
	 * @param from
	 *            Id of the start node of the edge.
	 * @param to
	 *            Id of the end node of the edge.
	 * @return The edge, if it will be found, <code>null</code> otherwise.
	 */
	protected VisualEdge findEdge(int from, int to) {
		VisualEdge edge = null;

		// look for edge elements
		for (VisualGraphElement element : elements) {
			if (element instanceof VisualEdge) {
				edge = (VisualEdge) element;

				// check the start and end nodes of the found edge
				if (edge.getStartNode() == from && edge.getEndNode() == to) {
					// edge found!
					return edge;
				}

				// is edge bidirectional?
				if (edge.isBidirectional()) {
					// check other direction, too
					if (edge.getEndNode() == from && edge.getStartNode() == to) {
						// edge found!
						return edge;
					}
				}
			}
		}

		// no edge with the specified start and end nodes exists
		return null;
	}

	/**
	 * Return the bidirectional edge with the specified start and end nodes.
	 * 
	 * @param end1
	 *            Id of the node at one end of the bidirectional edge.
	 * @param end2
	 *            Id of the node at the other end of the bidirectional edge.
	 * @return The bidirectional edge, if it will be found, <code>null</code>
	 *         otherwise.
	 */
	protected VisualEdge findBidirectionalEdge(int end1, int end2) {
		VisualEdge edge = null;

		// look for bidirectional edges
		for (VisualGraphElement element : elements) {
			if (element instanceof VisualEdge) {
				edge = (VisualEdge) element;

				if (edge.isBidirectional()) {
					// check the start and end nodes of the found bidirectional
					// edge
					if ((edge.getStartNode() == end1 && edge.getEndNode() == end2)
							|| edge.getStartNode() == end2
							&& edge.getEndNode() == end1) {
						// bidirectional edge found
						return edge;
					}
				}
			}
		}

		// no bidirectional edge with the specified nodes exists
		return null;
	}

	/**
	 * Constructor. Creates elements collection and drawing strategies. Disables
	 * animations.
	 * 
	 * @author Anselm Schmidt
	 */
	public GraphView(GraphController graphController) {
		this.graphController = graphController;
		// create arrays for graph elements
		elements = new ArrayList<VisualGraphElement>();
		deletedElements = new ArrayList<VisualGraphElement>();

		// create drawing strategies
		nodeStrategy = new GraphNodeStrategy();
		edgeStrategy = new GraphEdgeStrategy();

		// disable animations in the beginning
		animated = false;
	}

	/**
	 * Drawing method. Draws all graph elements with anti-aliasing.
	 * 
	 * @author Anselm Schmidt
	 */
	@Override
	public void paint(Graphics g) {
		// fill white
		g.setColor(GRAPH_VIEW_BACKGROUND);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		// enable anti-aliasing
		GraphDrawing.enableAntiAliasing(g);

		// draw edges first, nodes last
		drawEdges(g);
		drawNodes(g);
	}

	/**
	 * Enables or disables animation
	 * 
	 * @param enabled
	 *            <code>true</code> enables animation, <code>false</code>
	 *            disables it
	 * @author Anselm Schmidt
	 */
	public void setAnimationsEnabled(boolean enabled) {
		// clear deleted elements
		deletedElements.clear();
		
		// set animations enabled or disabled
		animated = enabled;
	}

	/**
	 * Adds the <code>VisualGraphElement</code> for an edge or double edge
	 *  when a new edge was added to the graph.
	 * 
	 * @param startNode
	 *            Id of the start node of the edge.
	 * @param endNode
	 *            Id of the end node of the edge.
	 * @author Anselm Schmidt
	 */
	public void onEdgeAdded(int startNode, int endNode) {
		if (this instanceof InteractiveGraphView) {
			GUITest.write("[GRAPH] > onEdgeAdded(" + startNode + ", " + endNode
					+ ")", true);
		} else if (this instanceof TreeView) {
			GUITest.write("[ALGO] > onEdgeAdded(" + startNode + ", " + endNode
					+ ")", true);
		}

		// stop deleting animations
		deletedElements.clear();

		// look for similar edge in the other direction
		VisualEdge otherDirection = findEdge(endNode, startNode);

		if (otherDirection == null) {
			// no bidirectional edge: add new edge
			elements.add(new VisualEdge(startNode, endNode, false,
					edgeStrategy, this.graphController));

		} else {
			// bidirectional edge: make old edge bidirectional
			otherDirection.makeBidirectional();
		}

		// repaint to show changes
		repaint();
	}

	/**
	 * Removes an edge or double edge <code>VisualGraphElement</code> and repaints when
	 * an edge was deleted from the graph.
	 * 
	 * @param startNode
	 *            Id of the start node of the edge.
	 * @param endNode
	 *            Id of the end node of the edge.
	 * @author Anselm Schmidt
	 */
	public void onEdgeRemoved(int startNode, int endNode) {
		if (this instanceof InteractiveGraphView) {
			GUITest.write("[GRAPH] > onEdgeRemoved(" + startNode + ", "
					+ endNode + ")", true);
		} else if (this instanceof TreeView) {
			GUITest.write("[ALGO] > onEdgeRemoved(" + startNode + ", "
					+ endNode + ")", true);
		}

		// look for bidirectional edge
		VisualEdge edge = findBidirectionalEdge(startNode, endNode);

		if (edge == null) {
			// no bidirectional edge: remove edge
			removeEdge(startNode, endNode);
		} else {
			// bidirectional edge: make old edge bidirectional
			removeDirection(edge, startNode, endNode);
		}

		// repaint to show changes
		repaint();
	}

	/**
	 * Clears the graph if it was reloaded
	 * 
	 * @author Anselm Schmidt
	 */
	public void onGraphLoaded() {
		elements.clear();
	}

	/**
	 * Removes the corresponding <code>VisualGraphElement</code> and repaints when a
	 * node was removed from the graph
	 * 
	 * @param node
	 *            Id of the changed node.
	 * @author Anselm Schmidt
	 */
	public void onNodeRemoved(int node) {
		if (this instanceof InteractiveGraphView) {
			GUITest.write("[GRAPH] > onNodeRemoved(" + node + ")", true);
		} else if (this instanceof TreeView) {
			GUITest.write("[ALGO] > onNodeRemoved(" + node + ")", true);
		}

		// remove node
		VisualNode deletedNode = findNode(node);
		
		if(deletedNode != null) {
			deletedElements.add(new DeletedNode(deletedNode, graphController));
			elements.remove(deletedNode);

			// repaint to show changes
			repaint();
		}
	}

	/**
	 * Adds a node <code>VisualGraphElement</code> and repaints when a
	 * node was added to the graph
	 * 
	 * @param node
	 *            Id of the new node.
	 * @param pos
	 *            Position of the new node.
	 * @author Anselm Schmidt
	 */
	public void onNodeAdded(int node, Point pos) {
		if (this instanceof InteractiveGraphView) {
			GUITest.write("[GRAPH] > onNodeAdded(" + node + ", " + pos + ")",
					true);
		} else if (this instanceof TreeView) {
			GUITest.write("[ALGO] > onNodeAdded(" + node + ", " + pos + ")",
					true);
		}

		// stop deleting animations
		deletedElements.clear();

		// add node
		elements.add(new VisualNode(node, (Point) pos, nodeStrategy,
				this.graphController));

		// repaint
		repaint();
	}

	/**
	 * Moves the corresponding node <code>VisualGraphElement</code> and repaints when a
	 * node moved in the graph
	 * 
	 * @param node
	 *            Id of the changed node.
	 * @param pos
	 *            New position of the node.
	 * @author Anselm Schmidt
	 */
	public void onNodeMoved(int node, Point pos) {
		if (this instanceof InteractiveGraphView) {
			GUITest.write("[GRAPH] > onNodeMoved(" + node + ", " + pos + ")",
					true);
		} else if (this instanceof InteractiveGraphView) {
			GUITest.write("[ALGO] > onNodeMoved(" + node + ", " + pos + ")",
					true);
		}

		findNode(node).setPosition(pos);
		repaint();
	}

	/**
	 * Implemented GraphObserver method. The id of a node has been changed,
	 * reset corresponding element and repaint. Throws an
	 * IllegalArgumentException if the node wasn't found.
	 * 
	 * @param node
	 *            Id of the changed node.
	 * @param pos
	 *            New position of the node.
	 * @author Anselm Schmidt
	 */
	public void onNodeChanged(int oldId, int newId) {
		// find node
		VisualNode node = findNode(oldId);

		if (this instanceof InteractiveGraphView) {
			GUITest.write("[GRAPH] > onNodeChanged(" + oldId + ", " + newId
					+ ")", true);
		} else if (this instanceof TreeView) {
			GUITest.write("[ALGO] > onNodeChanged(" + oldId + ", " + newId
					+ ")", true);
		}

		if (node == null) {
			GUITest.write("FATAL ERROR: node " + oldId + " hasn't been found!",
					true);
			throw new IllegalArgumentException();
		}

		// change node and repaint
		node.changeId(newId);
		repaint();
	}

	/**
	 * Implemented GraphObserver method. The id of an edge has been changed,
	 * reset corresponding element and repaint. Throws an
	 * IllegalArgumentException if the edge wasn't found.
	 * 
	 * @param oldStart
	 *            Old id of the start node.
	 * @param oldEnd
	 *            Old id of the end node.
	 * @param newStart
	 *            New id of the start node.
	 * @param newEnd
	 *            New id of the end node.
	 * @author Anselm Schmidt
	 */
	public void onEdgeChanged(int oldStart, int oldEnd, int newStart, int newEnd) {
		// find edge
		VisualEdge edge = findEdge(oldStart, oldEnd);

		if (this instanceof InteractiveGraphView) {
			GUITest.write("[GRAPH] > onEdgeChanged(" + oldStart + ", " + oldEnd
					+ ", " + newStart + ", " + newEnd + ")", true);
		} else if (this instanceof TreeView) {
			GUITest.write("[ALGO] > onEdgeChanged(" + oldStart + ", " + oldEnd
					+ ", " + newStart + ", " + newEnd + ")", true);
		}

		if (edge == null) {
			GUITest.write("FATAL ERROR: edge " + oldStart + "=>" + oldEnd
					+ " hasn't been found!", true);
			throw new IllegalArgumentException();
		}

		// check for a bidirectional edge
		if (edge.isBidirectional()) {
			// bidirectional edge: change only once per two calls
			edge.changeBidirectionalEdgeOnce(newStart, newEnd);
		} else {
			// no bidirectional edge: just change the edge
			edge.changeEdge(newStart, newEnd);
		}
		repaint();
	}

	/**
	 * Convert graph elements into string.
	 * 
	 * @result A string with one element per line. For detailed information look
	 *         at the <code>toString</code> methods of the element classes.
	 * @author Anselm Schmidt
	 */
	@Override
	public String toString() {
		String string = "";

		for (VisualGraphElement element : elements) {
			string += element.toString() + "\n";
		}

		return string;
	}
}