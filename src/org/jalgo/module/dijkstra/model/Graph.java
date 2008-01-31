/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and
 * platform independent. j-Algo is developed with the help of Dresden
 * University of Technology.
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
 * Created on 07.05.2005
 *
 */
package org.jalgo.module.dijkstra.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Hannes Strass, Martin Winter
 * 
 * Represents a set of nodes and a set of edges.
 *
 */
public class Graph implements Serializable, Cloneable {

	private static final long serialVersionUID = -5343300138161726312L;

	private List<Node> nodeList;

	private List<Edge> edgeList;

	/**
	 *  creates a new Graph with new, empty ArrayLists
	 */
	public Graph() {
		nodeList = new ArrayList<Node>();
		edgeList = new ArrayList<Edge>();
	}

	/**
	 * creates a Graph using given ArrayLists
	 * @param nodes the Node list
	 * @param edges the Edge list
	 */
	public Graph(List<Node> nodes, List<Edge> edges) {
		nodeList = nodes;
		edgeList = edges;
	}

	/** Finds the edge between two nodes.
	 * @param from
	 * @param to
	 * @return the node
	 */
	public Edge findEdge(Node from, Node to) {
		for (int i = 0; i < edgeList.size(); i++) {
			Edge edge = edgeList.get(i);
			if (((edge.getStartNode().getIndex() == from.getIndex()) &&
					(edge.getEndNode().getIndex() == to.getIndex())) ||
				((edge.getStartNode().getIndex() == to.getIndex()) &&
					(edge.getEndNode().getIndex() == from.getIndex())))
				return edge;
		}
		throw new RuntimeException("Graph structure is broken."); //$NON-NLS-1$
	}

	/** Finds the node with the given index or returns null.
	 * @param index
	 * @return a node or null
	 */
	public Node findNode(int index) {
		Iterator it = nodeList.iterator();

		while (it.hasNext()) {
			Node node = (Node) it.next();

			if (node.getIndex() == index)
				return node;
		}

		return null;

	}


	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Graph)) return false;
		if (obj == null) return false;
		Graph other = (Graph)obj;
		return nodeList.equals(other.nodeList) && edgeList.equals(other.edgeList);
	}


	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 * @author Julian Stecklina
	 */
	public Object clone() {
		Graph newGraph = new Graph();

		for (int i = 0; i < nodeList.size(); i++)
			newGraph.addNode(nodeList.get(i).clone());

		for (int i = 0; i < nodeList.size(); i++) {
			Node n = newGraph.nodeList.get(i);
			Node pred = n.getPredecessor();

			// Deep copy means we must fix references!
			if (pred != null)
				n.setPredecessor(newGraph.findNode(pred.getIndex()));
		}

		for (int i = 0; i < edgeList.size(); i++)
			newGraph.addEdge(edgeList.get(i).clone());
		for (int i = 0; i < edgeList.size(); i++) {
			Edge e = newGraph.edgeList.get(i);
			Node from = e.getStartNode();
			Node to = e.getEndNode();

			// Fix references again
			e.setStartNode(newGraph.findNode(from.getIndex()));
			e.setEndNode(newGraph.findNode(to.getIndex()));
		}

		return newGraph;
	}


	public String toString() {
		return "Graph: NodeList: " + getNodeListText() + "; EdgeList: " + getEdgeListText() + "."; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
	}


	/** Return next available index for a new node.
	 * 
	 * @return next available index
	 */
	public int getNextNodeIndex() {
		// Node indices are 1-based.
		return nodeList.size() + 1;
	}


	/**
	 * adds a Node to the NodeList if it doesn't exist yet
	 * @param node the Node to add
	 * @return true, if Node has been added
	 */
	public boolean addNode(Node node) {
		for (int i = 0; i < nodeList.size(); i++) {
			if (nodeList.get(i).getIndex() == node.getIndex()) {
				return false;
			}
		}
		boolean returnValue = nodeList.add(node);
		Collections.sort(nodeList);
		return returnValue;
	}


	/**
	 * adds an Edge to the EdgeList if it doesn't exist yet
	 * @param edge the Edge to add
	 * @return true, if Edge has been added
	 */
	public boolean addEdge(Edge edge) {
		Edge currentEdge;

		if (edge.getEndNodeIndex() == edge.getStartNodeIndex()) {
			addNode(edge.getStartNode());
			return false;
		}

		for (int i = 0; i < edgeList.size(); i++) // check if edge already exists
		{
			currentEdge = edgeList.get(i);
			if (currentEdge.equals(edge)) {
				int newWeight = edge.getWeight();
				if (newWeight == currentEdge.getWeight())
					return false;
				currentEdge.setWeight(newWeight);
				currentEdge.setChanged(true);
				return true;
			}
		}

		for (int i = 0; i < nodeList.size(); i++) // check for duplicate Nodes
		{
			if (edge.getStartNode().getIndex() == nodeList.get(i).getIndex()) {
				edge.setStartNode(nodeList.get(i));
			}
			if (edge.getEndNode().getIndex() == nodeList.get(i).getIndex()) {
				edge.setEndNode(nodeList.get(i));
			}
		}

		boolean returnValue = edgeList.add(edge);
		Collections.sort(edgeList);

		return returnValue;
	}


	/**
	 * @return the Edge list according to Prof. Vogler's script without leading and trailing braces
	 */
	public String getEdgeListText() {
		StringBuffer returnValue = new StringBuffer();

		if (edgeList.size() > 0) {
			returnValue.append(edgeList.get(0).getText());
		}
		for (int i = 1; i < edgeList.size(); i++) {
			returnValue.append(", ").append(edgeList.get(i).getText()); //$NON-NLS-1$
		}

		return returnValue.toString();
	}


	/**
	 * @return the Node list according to Prof. Vogler's script  without leading and trailing braces
	 */
	public String getNodeListText() {
		StringBuffer returnValue = new StringBuffer();

		if (nodeList.size() > 0) {
			returnValue.append(nodeList.get(0).getIndex());
		}
		for (int i = 1; i < nodeList.size(); i++) {
			returnValue.append(", ").append(nodeList.get(i).getIndex()); //$NON-NLS-1$
		}

		return returnValue.toString();
	}


	/** Automaticly changes Node Positions and arranges Nodes in a circle with their indexes ascending clockwise.
	 * 
	 *  Call after replaceMissingNodes(). 
	 * 
	 * @return rescaled Graph (this)
	 */
	public Graph rescale() {
		/*
		 * count nodes, calculate new positions, change positions
		 */
		double nodeAngle, newWorldX, newWorldY, addValue;
		int numberOfNodes = nodeList.size();
		Position newPosition;

		addValue = ((numberOfNodes % 2) == 0) ?
			((Math.PI / numberOfNodes) + (Math.PI / 2)) :
			(Math.PI / 2);

		if (numberOfNodes == 1) {
			// Put single node in the center.
			nodeList.get(0).setPosition(new Position(0.0, 0.0));
		}
		else {
			for (int i = 0; i < numberOfNodes; i++) {
				// Arrange nodes clockwise, starting with node 1 top middle //left.
				nodeAngle = i * ((-2) * Math.PI) / numberOfNodes + addValue;
				newWorldX = Math.cos(nodeAngle) * 0.8; // Offset from edge of screen.
				newWorldY = Math.sin(nodeAngle) * 0.8;
				newPosition = new Position(newWorldX, newWorldY);
				nodeList.get(i).setPosition(newPosition);
			}
		}

		return this;
	}


	/**
	 *   The current implementation of the distance matrix editing composite does not
	 *    support arbitrary Node indexes in the Node list, eg. you can't have a Node 1 and a Node 3 but no Node 2.
	 *  
	 *   This method checks the Nodes and if their indexes (and thus labels) differ from their position in the
	 *   Node list, it changes the indexes.
	 *   
	 *   After calling this method you will have n Nodes with indexes from 1 to n in your Node list.
	 *   
	 *   Should be called before rescale().
	 *
	 */
	public void replaceMissingNodes() {
		int startNodeIndex, endNodeIndex;
		Node currentNode;
		Edge currentEdge;
		ArrayList<Node> newNodes = new ArrayList<Node>(10);
		ArrayList<Node> badNodes = new ArrayList<Node>(10);

		for (int i = 0; i < 10; i++) {
			newNodes.add(null);
			badNodes.add(null);
		}

		for (int i = 0; i < nodeList.size(); i++) {
			currentNode = nodeList.get(i);
			if (currentNode.getIndex() == i + 1) {
				// Node has correct index, do nothing
			} else {
				// wrong index, map Node to new index
				badNodes.set(i, currentNode);
				newNodes.set(i, new Node((i + 1), currentNode.getPosition()));
			}
		}

		for (int i = 0; i < newNodes.size(); i++) {
			if (newNodes.get(i) != null) {
				nodeList.set(i, newNodes.get(i));
			}
		}

		for (int i = 0; i < edgeList.size(); i++) {
			currentEdge = edgeList.get(i);

			startNodeIndex = badNodes.indexOf(currentEdge.getStartNode());
			if (startNodeIndex != -1) {
				currentEdge.setStartNode(newNodes.get(startNodeIndex));
			}

			endNodeIndex = badNodes.indexOf(currentEdge.getEndNode());
			if (endNodeIndex != -1) {
				currentEdge.setEndNode(newNodes.get(endNodeIndex));
			}
		}
	}


	/**
	 * resets the Graph -- Node list and Edge list are cleared
	 */
	public void reset() {
		nodeList.clear();
		edgeList.clear();
	}


	/**
	 * @return the Node list
	 */
	public List<Node> getNodeList() {
		return nodeList;
	}


	/**
	 * @return The start node of the graph or null
	 */
	public Node getStartNode() {
		Iterator iter = getNodeList().iterator();
		while (iter.hasNext()) {
			Node node = (Node) iter.next();
			if (node.isStart())
				return node;
		}
		return null;
	}


	/**
	 * @return the Edge list
	 */
	public List<Edge> getEdgeList() {
		return edgeList;
	}


	/**	removes a Node from the Node list and all adjacent Edges from the Edge list
	 * @param nodeToDelete the Node to delete
	 * @return true, if Node actually existed in Node list and has been deleted. nothing is removed otherwise
	 */
	public boolean deleteNode(Node nodeToDelete) {
		for (int i = 0; i <= nodeList.size(); i++) {
			if (nodeList.get(i).getIndex() == nodeToDelete.getIndex()) {
				/*
				 * FSt
				 *	nodeList.remove(nodeToDelete);
				 * because somtimes the indices are equal, but the object pointers are different
				 */
				nodeList.remove(nodeList.get(i));
				//~FSt

				boolean stillHasEdges = true;
				Edge currentEdge = null;

				//if nodeToDelete has adjacent Edges, delete them
				while (stillHasEdges) {
					stillHasEdges = false;
					for (int j = 0; j < edgeList.size(); j++) {
						if ((nodeToDelete.getIndex() == edgeList.get(j).getStartNode()
								.getIndex())
								|| (nodeToDelete.getIndex() == edgeList.get(j)
										.getEndNode().getIndex())) {
							currentEdge = edgeList.get(j);
							stillHasEdges = true;
						}
					}
					if (stillHasEdges)
						this.deleteEdge(currentEdge);
				}
				return true;
			}
		}

		return false;
	}


	/**
	 * deletes an Edge from the Edge list. Note: after removing an Edge there might be a Node that is not connected to the Graph any more
	 * 
	 * @param edgeToDelete the Edge to delete
	 * @return true, if Edge actually existed in Edge list and has been deleted
	 */
	public boolean deleteEdge(Edge edgeToDelete) {
		for (int i = 0; i <= edgeList.size(); i++) {
			if (edgeList.get(i).equals(edgeToDelete)) {
				//edgeList.remove(edgeToDelete);
				//FSt see deleteNode() for more information about this change
				edgeList.remove(edgeList.get(i));
				return true;
			}
		}
		return false;
	}


	/**
	 * sets changed-flags of all Nodes and all Edges false. call this before adding new Nodes and Edges
	 */
	public void setAllChangedFlagsFalse() {
		for (Node node : nodeList) {
			node.setChanged(false);
			node.getVisual().update();
		}
		for (Edge edge : edgeList) {
			edge.setChanged(false);
			edge.getVisual().update();
		}
	}


	/**
	 * Synchronizes this with anotherGraph -- ie. elements that are in anotherGraph
	 * but not in this, are added. Elements that are in this, but not in
	 * anotherGraph, are deleted from this. This method is basically needed because 
	 * you can't create Edges in a Node list and new elements always carry the
	 * changed-flag, which should only be true for recently added elements.
	 * @param anotherGraph graph to synchronize with
	 * @param deleteNodes determines wether Nodes that are in this, but not in
	 * anotherGraph, shall be deleted
	 */
	public void synchronizeWith(Graph anotherGraph, boolean deleteNodes) {
		/* direction 1: 
		 * 				copy all Elements from anotherGraph into this.
		 * 				already existing elements should not be overwritten (changed-flag)
		 */

		List<Node> anotherNodeList = anotherGraph.getNodeList(); // NOT ANOTHER NODE LIST! (coming to cinemas soon)
		List<Edge> anotherEdgeList = anotherGraph.getEdgeList();

		for (int i = 0; i < anotherNodeList.size(); i++) {
			this.addNode(anotherNodeList.get(i));
		}
		for (int i = 0; i < anotherEdgeList.size(); i++) {
			this.addEdge(anotherEdgeList.get(i));
		}

		/* direction 2:
		 * 				check if this contains elements that anotherGraph
		 * 				does not contain; if found, delete them
		 */

		boolean deleteIt = true;
		Node currentNode;
		Edge currentEdge;
		ArrayList<Node> nodesToDelete = new ArrayList<Node>();
		ArrayList<Edge> edgesToDelete = new ArrayList<Edge>();

		for (int i = 0; i < nodeList.size(); i++) {
			currentNode = nodeList.get(i); // get an element from this Graph

			for (int j = 0; j < anotherNodeList.size(); j++) {
				if (currentNode.getIndex() == anotherNodeList.get(j).getIndex())
					deleteIt = false;
			}
			if (deleteIt)
				nodesToDelete.add(currentNode);
			//this.deleteNode(currentNode);

			deleteIt = true;
		}

		for (int i = 0; i < edgeList.size(); i++) {
			currentEdge = edgeList.get(i); // get an element from this Graph

			for (int j = 0; j < anotherEdgeList.size(); j++) {
				if (currentEdge.equals(anotherEdgeList.get(j)))
					deleteIt = false;
			}
			if (deleteIt)
				edgesToDelete.add(currentEdge);
			//this.deleteEdge(currentEdge);

			deleteIt = true;
		}

		for (int i = 0; i < edgesToDelete.size(); i++) {
			this.deleteEdge(edgesToDelete.get(i));
		}

		if (deleteNodes) {
			for (int i = 0; i < nodesToDelete.size(); i++) {
				this.deleteNode(nodesToDelete.get(i));
			}
		}
	} // (1,1,3),(3,2,5),(5,3,2),(2,4,4),(4,5,1)}
}