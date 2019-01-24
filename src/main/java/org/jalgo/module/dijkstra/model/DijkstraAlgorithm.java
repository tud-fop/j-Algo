/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/*
 * Created on 21.05.2005 $Id$
 */
package org.jalgo.module.dijkstra.model;

import java.util.ArrayList;
import java.util.List;

import org.jalgo.main.util.Messages;

/**
 * @author Julian Stecklina
 */
public class DijkstraAlgorithm {

	/**
	 * <code>INFINITY</code> approximates the numerical infinity. :-) Special
	 * care should be taken not to present this value to the user.
	 */
	private static final int INFINITY = -1;
	private Graph graph;
	private Node startNode;
	private State currentState;
	private ArrayList<State> stateList;
	private ArrayList<BorderState> borderStates = new ArrayList<BorderState>();
	private Node currentlyChosen;
	/**
	 * <code>INVALID</code> means that there is no next index.
	 */
	public static final int INVALID = -1;

	private void addNodeReference(State state, Node node) {
		state.addStyledDescription(Messages.getString(
			"dijkstra", "DijkstraAlgorithm.Node") + node.getIndex()); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private interface NodeClosure {
		void value(Node node);
	}

	private interface EdgeClosure {
		void value(Edge edge);
	}

	// I miss optional and keyword parameters... and _real_ closures...
	private void doAllNodes(NodeClosure closure) {
		doAllNodes(closure, graph.getNodeList());
	}

	/**
	 * Something like Lisp's mapc for Java. Maps over all nodes in the given
	 * ArrayList.
	 * 
	 * @param closure
	 * @param nodes
	 */
	private void doAllNodes(NodeClosure closure, List nodes) {
		for (int i = 0; i < nodes.size(); i++)
			closure.value((Node)nodes.get(i));
	}

	/**
	 * Maps over all edges in the given array.
	 * 
	 * @param closure
	 * @param edges
	 */
	private void doAllEdges(EdgeClosure closure, List<Edge> edges) {
		for (Edge edge : edges)
			closure.value(edge);
	}

	/**
	 * Same as doAllEdges with explicit ArrayList parameter, but defaults to all
	 * edges.
	 * 
	 * @param closure
	 */
	private void doAllEdges(EdgeClosure closure) {
		doAllEdges(closure, graph.getEdgeList());
	}

	/**
	 * @param index The state to go to.
	 */
	public void gotoState(int index) {
		currentState = stateList.get(index);
	}

	/**
	 * Return the current count of states
	 */
	public int getStateCount() {
		return (stateList == null) ? 0 : stateList.size();
	}

	public State getCurrentState() {
		return currentState;
	}

	private State createState(ArrayList border, String descr, boolean macro) {
		return createState(border, descr, macro, null);
	}

	private State createState(ArrayList border, String descr, boolean macro,
		Node activeNode) {
		return createState(border, descr, macro, activeNode, null);
	}

	private State createState(ArrayList border, String descr, boolean macro,
		Node activeNode, Edge skipEdge) {
		// Update the border flag
		Graph ngraph = (Graph)graph.clone();

		for (Node node : ngraph.getNodeList()) {
			Node oldNode = graph.findNode(node.getIndex());
			node.setBorder(border.contains(oldNode));
			if ((activeNode == oldNode) || (currentlyChosen == oldNode))
				node.setActive(true);
		}

		Edge nskipEdge = (skipEdge != null) ?
			ngraph.findEdge(ngraph.findNode(skipEdge.getStartNodeIndex()),
				ngraph.findNode(skipEdge.getEndNodeIndex())) : null;
		// Now we need to propagate all flags to the approprate nodes.

		for (Node node : ngraph.getNodeList()) {
			Node pred = node.getPredecessor();
			// If we have a predeccessor, find the appropriate edge
			if (pred != null) {
				Edge edge = ngraph.findEdge(node, pred);
				// and set the right flags.
				if (edge != nskipEdge) {
					edge.setFlags(node.getFlags());
					edge.setReversed(pred != edge.getStartNode());
				}
			}
		}

		State newState = new State(ngraph, descr, macro,
			(ArrayList)borderStates.clone());
		stateList.add(newState);

		return newState;
	}

	/**
	 * @param border The border represented as ArrayList
	 * @param node The node from which we complete the border.
	 */
	private void completeBorder(ArrayList<Node> border, Node node) {
		border.remove(node);

		for (Edge edge : graph.getEdgeList()) {
			Node from = edge.getStartNode();
			Node to = edge.getEndNode();
			Node other;

			// Check whether we got an edge that belongs to node.
			// other will be set to the 'other' node.
			if (from == node) {
				other = to;
			}
			else if (to == node) {
				other = from;
			}
			else continue;

			if (other.isChosen() == false) {
				if (other.getDistance() == INFINITY) {
					// do nothing
				}
				else if ((node.getDistance() + edge.getWeight()) <
					other.getDistance()) {
					// Conflict case ?!
					other.setConflict(true);
					// edge.setConflict(true);
					edge.setActive(true);
					edge.setBorder(true);
					edge.setReversed(other == edge.getStartNode());
					State c1 = createState(border, "", //$NON-NLS-1$
						false, other, edge);
					addNodeReference(c1, other);
					c1.addDescription(Messages.getString(
						"dijkstra", "DijkstraAlgorithm.Is_over")); //$NON-NLS-1$ //$NON-NLS-2$
					addNodeReference(c1, node);
					c1.addDescription(Messages.getString(
						"dijkstra", "DijkstraAlgorithm.Reachable_on_shorter_path")); //$NON-NLS-1$ //$NON-NLS-2$

					edge.setActive(false);
					// edge.setConflict(false);
					edge.setBorder(false);
					other.setConflict(false);

				}
				else {
					// Conflict case, but nothing to do
					other.setConflict(true);
					// edge.setConflict(true);
					edge.setActive(true);
					edge.setBorder(true);
					edge.setReversed(other == edge.getStartNode());
					State c1 = createState(border, "", //$NON-NLS-1$
						false, other, edge);

					addNodeReference(c1, other);
					c1.addDescription(Messages.getString(
						"dijkstra", "DijkstraAlgorithm.Reachable_over_existing_path_shorter")); //$NON-NLS-1$ //$NON-NLS-2$

					edge.setActive(false);
					// edge.setConflict(false);
					edge.setBorder(false);
					other.setConflict(false);
					continue;
				}

				// There is a shorter path to other via node
				other.setPredecessor(node);
				other.setDistance(node.getDistance() + edge.getWeight());
				if (!border.contains(other)) border.add(other);

				// Create state
				State st1 = createState(border, Messages.getString(
					"dijkstra", "DijkstraAlgorithm.New_node"), //$NON-NLS-1$ //$NON-NLS-2$
					false, other);
				addNodeReference(st1, other);
			}
		}
	}

	private ArrayList deepClone(ArrayList<Edge> list) {
		ArrayList<Object> newList = new ArrayList<Object>(list.size());

		for (Edge edge : list)
			newList.add(edge.clone());

		return newList;
	}

	private void newBorderState(Node chosen, ArrayList<Node> border) {
		// We want to pass EDGEs not NODEs here!
		ArrayList<Edge> borderEdges = new ArrayList<Edge>();

		for (Node bnode : border) {
			Node pred = bnode.getPredecessor();
			if (pred == null) { throw new RuntimeException(Messages.getString(
				"dijkstra", "DijkstraAlgorithm.Error_1")); //$NON-NLS-1$ //$NON-NLS-2$
			}
			if (bnode == pred) { throw new RuntimeException(Messages.getString(
				"dijkstra", "DijkstraAlgorithm.Error_2")); //$NON-NLS-1$ //$NON-NLS-2$
			}

			Edge edge = graph.findEdge(pred, bnode);
			borderEdges.add(edge);
		}

		borderStates.add(new BorderState(chosen.clone(),
			deepClone(borderEdges), (Graph)graph.clone()));
	}

	/**
	 * This is the implementation of Dijkstra's algorithm to find the shortest
	 * paths in a graph as given in Vogler's script as of 2003.
	 * 
	 * @param start the start node
	 */
	public void generateStates(Node start) {
		// Tabula rasa

		stateList = new ArrayList<State>();
		borderStates = new ArrayList<BorderState>();
		currentlyChosen = start;

		// Initial states
		start.setStart(true);

		// Initialization:
		// All nodes except start are not reachable.
		for (Node node : graph.getNodeList()) {
			if (node != start) {
				node.setStart(false);
				node.setPredecessor(null);
				node.setChosen(false);
				node.setDistance(INFINITY);
			}
		}

		// start is chosen
		start.setPredecessor(null);
		start.setDistance(0);
		start.setChosen(true);

		// All nodes adjacent to start form the initial border.
		// We do not use the border flag of the nodes, because that
		// would be unwieldy.
		ArrayList<Node> border = new ArrayList<Node>();
		currentState = createState(border, "", true, start); //$NON-NLS-1$
		addNodeReference(currentState, start);
		currentState.addDescription(Messages.getString(
			"dijkstra", "DijkstraAlgorithm.Choosed_as_start_node")); //$NON-NLS-1$ //$NON-NLS-2$

		completeBorder(border, start);
		newBorderState(start, border);
		createState(border, Messages.getString(
			"dijkstra", "DijkstraAlgorithm.Fringe_complete_for_start_node"), //$NON-NLS-1$ //$NON-NLS-2$
			true);

		// Compute paths from start
		while (!border.isEmpty()) {
			// We need to make this an array because Java is braindead.
			final Node[] minimum = new Node[1];
			minimum[0] = null;

			NodeClosure minimize = new NodeClosure() {

				public void value(Node node) {
					if ((minimum[0] == null)
						|| (minimum[0].getDistance() > node.getDistance())) {
						minimum[0] = node;
					}

				}
			};

			// Find v with minimal distance
			doAllNodes(minimize, border);
			Node v = minimum[0];

			v.setChosen(true);
			currentlyChosen = v;

			State state1 = createState(border, Messages.getString(
				"dijkstra", "DijkstraAlgorithm.New_choosen"), true, v); //$NON-NLS-1$ //$NON-NLS-2$
			addNodeReference(state1, v);

			completeBorder(border, v);
			newBorderState(v, border);

			createState(border, Messages.getString(
				"dijkstra", "DijkstraAlgorithm.Fringe_complete"), true); //$NON-NLS-1$ //$NON-NLS-2$
			currentlyChosen = null;
			border.remove(v);
		}

		createState(border, Messages.getString(
			"dijkstra", "DijkstraAlgorithm.Algorithm_finished"), true); //$NON-NLS-1$ //$NON-NLS-2$

		// dumpResult(start);
	}

	private void dumpResult(Node start) {
		System.out.println("Dumping results for Node " //$NON-NLS-1$
			+ start.getIndex());

		for (Node node : graph.getNodeList()) {
			System.out.println("Node " + node.getIndex() //$NON-NLS-1$
				+ " distance = " + node.getDistance()); //$NON-NLS-1$
			for (Node pred = node; pred != null; pred = pred.getPredecessor())
				System.out.print("> Node " + pred.getIndex()); //$NON-NLS-1$
			System.out.println();
		}
	}

	/**
	 * This constructor creates a new DijkstraAlgorithm object that will compute
	 * the shortest paths in the given graph.
	 * 
	 * @param graph The graph we want our algorithm to operate on.
	 */
	public DijkstraAlgorithm(Graph graph) {
		// Do we want to copy this graph?
		this.graph = graph;
		this.stateList = new ArrayList<State>();
	}

	/**
	 * Returns the index of the current state.
	 * 
	 * @return index of the current state
	 */
	public int getCurrentStateIndex() {
		return stateList.indexOf(currentState);
	}

	/**
	 * Returns the index of the next macro step.
	 * 
	 * @return next index of macro state
	 */
	public int getNextMacroStepIndex() {
		for (int i = getCurrentStateIndex() + 1; i < stateList.size(); i++) {
			if (stateList.get(i).isMacro()) return i;
		}
		return INVALID;
	}

	/**
	 * Returns the index of the previous macro step.
	 * 
	 * @return previous index of macro state
	 */
	public int getPrevMacroStepIndex() {
		for (int i = getCurrentStateIndex() - 1; i >= 0; i--) {
			if (stateList.get(i).isMacro()) return i;
		}
		return INVALID;
	}

	/**
	 * Returns the index of the next state.
	 * 
	 * @return next index
	 */
	public int getNextStepIndex() {
		int cur = getCurrentStateIndex();

		if (cur + 1 == stateList.size()) return INVALID;
		return getCurrentStateIndex() + 1;
	}

	/**
	 * Returns the index of the previous state.
	 * 
	 * @return previous index
	 */
	public int getPrevStepIndex() {
		int cur = getCurrentStateIndex();

		return (cur <= 0) ? INVALID : cur - 1;
	}

	/**
	 * Returns the start node.
	 * 
	 * @return start node
	 */
	public Node getStartNode() {
		return startNode;
	}
}
