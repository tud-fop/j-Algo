package org.jalgo.module.bfsdfs.gui.graphview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Calendar;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.Timer;

import org.jalgo.module.bfsdfs.algorithms.Algo;
import org.jalgo.module.bfsdfs.algorithms.stack.NodeStatus;
import org.jalgo.module.bfsdfs.algorithms.stack.StackObserver;
import org.jalgo.module.bfsdfs.controller.GraphController;
import org.jalgo.module.bfsdfs.graph.AlgoGraphObserver;
import org.jalgo.module.bfsdfs.graph.GraphObserver;
import org.jalgo.module.bfsdfs.gui.GUITest;
import org.jalgo.module.bfsdfs.gui.GraphAnimations;
import org.jalgo.module.bfsdfs.gui.GraphDrawing;

/**
 * <code>TreeView</code> extends <code>GraphView</code> and paints the result
 * tree over the already drawn graph using transparency.
 * 
 * @author Anselm Schmidt
 * 
 */
public class TreeView extends GraphView implements StackObserver, AlgoGraphObserver {
	private static final long serialVersionUID = 1L;

	/**
	 * Used drawing strategy for the tree nodes.
	 */
	protected AlgoNodeStrategy nodeStrategy;

	/**
	 * Used drawing strategy for the finished tree nodes.
	 */
	protected AlgoFinishedNodeStrategy finishedStrategy;

	/**
	 * Used drawing strategy for the tree edges.
	 */
	protected AlgoEdgeStrategy edgeStrategy;
	
	/**
	 * Id of the current node. Will be shown animated.
	 */
	protected int currentNode;
	
	/**
	 * Start time of the node cursor animation.
	 * Will be used to perform the hiding animation of the last node, too.
	 */
	protected long cursorAnimationTime;
	
	/**
	 * Alpha value of the last node when it changed to the current node.
	 */
	protected int lastAlphaValue;
	
	/**
	 * Position of the current node.
	 */
	protected Point currentNodePos;
	
	/**
	 * Position of the last node.
	 */
	protected Point lastNodePos;
	
	/**
	 * Used <code>GraphController</code> instance.
	 */
	GraphController graphController;

	/**
	 * Constructor. Creates the element array and the drawing strategy. Adds
	 * itself as <code>AlgoGraphObserver</code> and <code>StackObserver</code>
	 * to the corresponding algorithm. Disables animations.
	 * 
	 * @param agorithm
	 *            Algorithm to show in the view.
	 * @author Anselm Schmidt
	 */
	public TreeView(Algo algorithm, GraphController graphController) {
		super(graphController);

		// create drawing strategies
		nodeStrategy = new AlgoNodeStrategy();
		edgeStrategy = new AlgoEdgeStrategy();
		finishedStrategy = new AlgoFinishedNodeStrategy();

		// disable animations in the beginning
		animated = false;

		// add observers
		algorithm.addTreeObserver(this);
		algorithm.addStackObserver(this);
		graphController.addGraphObserver(new InputGraphObserver());
		
		// save graph controller instance
		this.graphController = graphController;
	}

	@Override
	/** Drawing method. Draws the result tree over the graph using transparency.
	 * @param g Used <code>Graphics</code> instance.
	 * @author Anselm Schmidt
	 */
	public void paint(Graphics g) {		
		// draw semi-transparent white rectangle
		g.setColor(new Color(255, 255, 255, TREEVIEW_ALPHA_VALUE));
		g.fillRect(0, 0, getWidth(), getHeight());

		// enable anti-aliasing
		GraphDrawing.enableAntiAliasing(g);
		
		try {
			// draw deleted tree elements
			for(VisualGraphElement element : deletedElements) {
				if(element.paint(g, animated)) {
					// repaint element after a short while
					Repainter repainter = new Repainter(this, element.getRect(g));
					Timer timer = new Timer(ANIMATION_REPAINT_DELAY, repainter);
					timer.setRepeats(false);
					timer.start();
				}
			}
			
			// draw tree elements
			for (VisualGraphElement element : elements) {
				if(element.paint(g, animated)) {
					// repaint element after a short while
					Repainter repainter = new Repainter(this, element.getRect(g));
					Timer timer = new Timer(ANIMATION_REPAINT_DELAY, repainter);
					timer.setRepeats(false);
					timer.start();
				}
			}
		}
		catch(ConcurrentModificationException e) {
			// This can be happen because of multi-threading - can be ignored.
		}
		
		if(currentNode > 0) {
			// draw and animate the current node cursor			
			if(animated) {
				int alpha = GraphAnimations.animateNodeCursor(ALPHA_100_PERCENT,
						Calendar.getInstance().getTimeInMillis() - cursorAnimationTime,
						NODE_CURSOR_ANIMATION_TIME);
			
				Rectangle rect = GraphDrawing.drawNodeCursor(g, currentNodePos,
						NODE_BEAMER_SIZE, NODE_CURSOR_SCALE_FACTOR,
						NODE_BEAMER_BORDER_SIZE, NODE_CURSOR_COLOR, alpha);
			
				// repaint node cursor after a short while
				Repainter repainter = new Repainter(this, rect);
				Timer timer = new Timer(ANIMATION_REPAINT_DELAY, repainter);
				timer.setRepeats(false);
				timer.start();
			}
			else {
				GraphDrawing.drawNodeCursor(g, currentNodePos,
						NODE_BEAMER_SIZE, NODE_CURSOR_SCALE_FACTOR,
						NODE_BEAMER_BORDER_SIZE, NODE_CURSOR_COLOR, 255);
			}
		}
	}

	/**
	 * Observer method. Not used.
	 * 
	 * @author Anselm Schmidt
	 */
	public void onAllNodesFinished() {
	}

	/**
	 * Observer method. Not used.
	 * 
	 * @author Anselm Schmidt
	 */
	public void onAllQueuesRemoved() {
	}

	/**
	 * Observer method. Not used.
	 * 
	 * @author Anselm Schmidt
	 */
	public void onFirstQueueAdded(int owner) {
	}

	/**
	 * Observer method. Not used.
	 * 
	 * @author Anselm Schmidt
	 */
	public void onNodesAdded(List<Integer> nodes) {
	}

	/**
	 * Observer method. Not used.
	 * 
	 * @author Anselm Schmidt
	 */
	public void onQueueAdded(int owner) {
	}

	/**
	 * Observer method. Changes status of a node.
	 * 
	 * @param node
	 *            Id of the node.
	 * @param newStatus
	 *            New status of the node.
	 * @author Anselm Schmidt
	 */
	public void onStatusChanged(int node, NodeStatus newStatus) {
		// logging
		GUITest.write("[ALGO] onStatusChanged(" + node + ", " + newStatus + ")", true);

		// find node in the tree
		VisualNode visualNode = findNode(node);

		if (visualNode != null) {
			// focus and finish node depending on new node status
			switch (newStatus) {
			case UNTOUCHED:
				// untouched node: unfocus and unfinish node
				visualNode.setFocused(false);
				visualNode.changeDrawingStrategy(nodeStrategy);
				break;

			case WAITING:
				// waiting node: focus node and unfinish node
				visualNode.setFocused(true);
				visualNode.changeDrawingStrategy(nodeStrategy);
				break;

			case FINISHED:
				// finished node: unfocus node and finish node
				visualNode.setFocused(false);
				visualNode.changeDrawingStrategy(finishedStrategy);		
				break;
			}
			
			repaint(visualNode.getRect(getGraphics()));
		}
	}

	/**
	 * Observer method. Not used.
	 * 
	 * @author Anselm Schmidt
	 */
	public void onTopQueueRemoved() {
	}

	/**
	 * Observer method. Not used.
	 * 
	 * @author Anselm Schmidt
	 */
	public void onUntouchedReplaced(List<Integer> oldNodes,
			List<Integer> newNodes) {
	}

	/**
	 * Implemented observer method. Changes the distance of a tree node.
	 * 
	 * @param node
	 *            Id of the node.
	 * @author Anselm Schmidt
	 */
	public void onDistanceChanged(int node, int newDistance) {
		// logging
		GUITest.write("[ALGO] > onDistanceChanged(" + node + ", " + newDistance
				+ ")", true);

		// find node in the tree
		VisualNode visualNode = findNode(node);

		if (visualNode != null) {
			// change distance
			visualNode.changeDistance(newDistance);
		}
	}

	/**
	 * Implemented observer method. Node has been added. Use the corresponding
	 * <code>GraphView</code> method, but change the drawing strategy.
	 * 
	 * @param node
	 *            Id of the new node.-
	 * @param pos
	 *            Position of the new node.
	 * @author Anselm Schmidt
	 */
	@Override
	public void onNodeAdded(int node, Point pos) {
		// add node
		super.onNodeAdded(node, pos);

		// change drawing strategy if the node has been added
		VisualNode visualNode = findNode(node);

		if (visualNode != null) {
			visualNode.changeDrawingStrategy(nodeStrategy);
		}
	}

	/**
	 * Implemented observer method. Edge has been added. Use the corresponding
	 * <code>GraphView</code> method, but change the drawing strategy.
	 * 
	 * @param startNode
	 *            Id of the start node.-
	 * @param endNode
	 *            Id of the end node.
	 * @author Anselm Schmidt
	 */
	@Override
	public void onEdgeAdded(int startNode, int endNode) {
		// add edge
		super.onEdgeAdded(startNode, endNode);

		// change drawing strategy if the edge has been added
		VisualEdge edge = findEdge(startNode, endNode);

		if (edge != null) {
			edge.changeDrawingStrategy(edgeStrategy);
		}
	}

	/**
	 * Implemented observer method. Changes the current node.
	 * @param newCurrentNode New id of the current node.
	 * @author Anselm Schmidt 
	 */
	public void onCurrentNodeChanged(int newCurrentNode) {
		if (!graphController.getNodes().contains(newCurrentNode))
			return;
		
		// save the current alpha value of the old cursor to perform the hiding animation
		if (graphController.getNodes().contains(currentNode)) {
			lastAlphaValue = GraphAnimations.animateNodeCursor(ALPHA_100_PERCENT, 
					System.currentTimeMillis(), NODE_CURSOR_ANIMATION_TIME);
			lastNodePos = graphController.getNodePosition(currentNode);
		}
				
		// change ids
		currentNode = newCurrentNode;
		
		// start animation of the new node's cursor
		currentNodePos = graphController.getNodePosition(currentNode);
		cursorAnimationTime = Calendar.getInstance().getTimeInMillis();
	}

	/**
	 * Observer for the original graph (<em>not</em> for the tree the algorithm generates)
	 * @author Thomas G&ouml;rres
	 *
	 */
	private class InputGraphObserver implements GraphObserver {
		/**
		 * Corrects the node cursor's position if a node was moved
		 * @author Thomas G&ouml;rres
		 */
		public void onNodeMoved(int node, Point pos) {
			if (node == currentNode)
				currentNodePos = pos;		
		}
		
		/**
		 * Hides the node cursor if the last node was removed
		 */
		public void onNodeRemoved(int node) {
			if (1 == node)
				currentNode = 0;
		}
		
		/**
		 * Shows the node cursor if the first node was added
		 */
		public void onNodeAdded(int node, Point pos) {
			if (1 == node)
				currentNode = 1;
			
		}

		public void onEdgeAdded(int startNode, int endNode) {}
		public void onEdgeChanged(int oldStartNode, int oldEndNode, int newStartNode, int newEndNode) {}
		public void onEdgeRemoved(int startNode, int endNode) {}
		public void onNodeChanged(int oldNodeId, int newNodeId) {}
	}
}
