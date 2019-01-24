package org.jalgo.module.app.core.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import org.jalgo.module.app.core.dataType.DataType;

/**
 * Represents the graph model. This consists of nodes (of type <code>Node</code>)
 * and edges (of type <code>Edge</code>).
 * 
 */
public class Graph implements Serializable {

	private static final long serialVersionUID = 7342120321239695018L;
	private Set<Node> nodes;
	private Set<Edge> edges;

	/**
	 * Creates an empty set for edges an one for nodes.
	 */
	public Graph() {
		// FIXME: why in try-catch?
		try {
			nodes = new HashSet<Node>();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
		edges = new HashSet<Edge>();
	}

	/**
	 * Returns the set of all nodes in this graph.
	 * 
	 * @return the whole set of nodes.
	 */
	public Set<Node> getNodes() {
		return nodes;
	}

	/**
	 * Returns the Node with the given ID.
	 * 
	 * @param id
	 *            the ID of an existing node of the graph.
	 * @return The node with the given ID.
	 */
	public Node getNode(int id) {
		for (Node n : nodes) {
			if (n.getId() == id)
				return n;
		}

		return null;
	}

	/**
	 * Generates a new node with the number of nodes as ID, adding it to the
	 * node list.
	 * 
	 * @return the added node.
	 */
	public Node newNode() {
		return newNode(true);
	}

	/**
	 * Generates a new node with the number of nodes as ID, adding it to the
	 * node list, if <code>add</code> is <code>true</code>.
	 * 
	 * @param add
	 * 
	 * @return The added node.
	 */
	public Node newNode(boolean add) {
		Node node = new Node(nodes.size());

		if (add) {
			nodes.add(node);
		}

		return node;
	}

	public int getBiggestID() {
		return nodes.size() - 1;
	}

	// private int getNextID() {
	// int nextID = 0;
	// for (Node node : nodes) {
	// if (node.getId() >= nextID) {
	// nextID = node.getId() + 1;
	// }
	// }
	// return nextID;
	// }

	/**
	 * Adds the given node to the set of nodes.
	 * 
	 * @param node The node to add.
	 */
	public void addNode(Node node) {
		nodes.add(node);
	}

	/**
	 * Adds a node and adjust the node ids of the existing nodes.
	 * 
	 * @param node
	 *            The node to add after remove.
	 */
	public void addUndoNode(Node node) {
		int id = node.getId();

		for (Node graphNode : nodes) {
			if (graphNode.getId() >= id) {
				graphNode.setId(graphNode.getId() + 1);
			}
		}

		nodes.add(node);
	}

	/**
	 * Generate a list of new nodes. Does not add these nodes to the graph.
	 * 
	 * @param count
	 *            The number of new created nodes.
	 * @return A list with new nodes.
	 */
	public List<Node> newNodes(int count) {
		List<Node> newNodes;

		newNodes = new ArrayList<Node>(count);
		for (int i = 0; i < count; i++) {
			newNodes.add(newNode());
		}

		return newNodes;
	}

	/**
	 * Removes the given node from the graph.
	 * 
	 * @param node
	 *            The node to delete.
	 */
	public void removeNode(Node node) {
		if (node == null) {
			throw new NullPointerException();
		} else if (!(nodes.contains(node))) {
			throw new NoSuchElementException();
		}

		int id = node.getId();
		nodes.remove(node);

		// correct id of nodes
		for (Node n : nodes) {
			if (n.getId() > id)
				n.setId(n.getId() - 1);
		}

		// delete all edges on the removed node
		for (Edge e : new LinkedList<Edge>(edges)) {
			if (e.getBegin() == node || e.getEnd() == node) {
				edges.remove(e);
			}
		}
	}

	/**
	 * Gets the set of alle edges in this graph.
	 * 
	 * @return The whole set of edges.
	 */
	public Set<Edge> getEdges() {
		return edges;
	}

	/**
	 * @param begin
	 *            The ID of the start node.
	 * @param end
	 *            The ID of the end node.
	 * @return the edge with the given start and end node.
	 */
	public Edge getEdge(int begin, int end) {
		return getEdge(getNode(begin), getNode(end));
	}

	/**
	 * @param begin
	 *            The start node.
	 * @param end
	 *            The end node.
	 * @return the edge with the given start and end node.
	 */
	public Edge getEdge(Node begin, Node end) {
		for (Edge e : edges) {
			if (e.getBegin() == begin && e.getEnd() == end)
				return e;
		}

		return null;
	}

	/**
	 * Adds a new edge to the graph, based on the IDs of start and end node. The
	 * weight is set to <code>null</code>.
	 * 
	 * @param begin
	 *            The ID of the start node.
	 * @param end
	 *            The ID of the end node.
	 * @return The new edge.
	 */
	public Edge addEdge(int begin, int end) {
		return addEdge(getNode(begin), getNode(end));
	}

	/**
	 * Adds a new edge to the graph with the given weight, based on the IDs of
	 * start and end node.
	 * 
	 * @param begin
	 *            The ID of the start node.
	 * @param end
	 *            The ID of the end node.
	 * @param weight
	 *            The weight of the new edge.
	 * @return The new edge.
	 */
	public Edge addEdge(int begin, int end, DataType weight) {
		return addEdge(getNode(begin), getNode(end), weight);
	}

	/**
	 * Adds a new edge to the graph, based on the given start and end node. The
	 * weight is set to <code>null</code>.
	 * 
	 * @param begin
	 *            The start node.
	 * @param end
	 *            The end node.
	 * @return The new edge.
	 */
	public Edge addEdge(Node begin, Node end) {
		return addEdge(begin, end, null);
	}

	/**
	 * Adds a new edge to the graph with the given weight, based on the given
	 * start and end node.
	 * 
	 * @param begin
	 *            The start node.
	 * @param end
	 *            The end node.
	 * @param weight
	 *            The weight of the new edge.
	 * @return The new edge.
	 */
	public Edge addEdge(Node begin, Node end, DataType weight) {
		return newEdge(begin, end, weight, true);
	}

	/**
	 * Adds a new edge to the graph.
	 * 
	 * @param edge
	 *            the given edge.
	 */
	public void addEdge(Edge edge) {
		edges.add(edge);
	}

	/**
	 * Creates a new edge, and adds it to the graph at the same time, if
	 * <code>add</code> is <code>true</code>.
	 * 
	 * @param begin
	 *            the start node.
	 * @param end
	 *            the end node.
	 * @param weight
	 *            the weight of the edge
	 * @param add
	 *            if <code>true</code>, add the edge to the graph.
	 * @return the created edge.
	 */
	public Edge newEdge(Node begin, Node end, DataType weight, boolean add) {
		if (begin == null || end == null) {
			throw new NullPointerException();
		}
		if (begin == end) {
			throw new IllegalArgumentException();
		}
		if (getEdge(begin, end) != null) {
			throw new IllegalArgumentException();
		}

		Edge edge = new Edge(begin, end);
		edge.setWeight(weight);
		if (add)
			edges.add(edge);

		return edge;
	}

	/**
	 * Removes the given edge from the graph.
	 * 
	 * @param edge
	 *            The edge to remove.
	 */
	public void removeEdge(Edge edge) {
		edges.remove(edge);
	}
}
