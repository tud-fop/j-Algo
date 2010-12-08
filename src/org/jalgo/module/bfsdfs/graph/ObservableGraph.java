package org.jalgo.module.bfsdfs.graph;

import java.awt.Point;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;

/**
 * This class represent the graph of the BFSDFS module. It is observable by {@link GraphObserver}.
 * @author Johannes Siegert
 */
public class ObservableGraph implements Serializable{
	private static final long serialVersionUID = 6560484674663537319L;
	
	/**
	 * Saves the {@link GraphObserver}s of this {@link ObservableGraph}.
	 */
	private Collection<GraphObserver> graphObservers = null;
	/**
	 * Saves the {@link Node}s of this {@link ObservableGraph}.
	 */
	private Collection<Node> nodes = null;
	/**
	 * Saves the {@link Edge}s of this {@link ObservableGraph}.
	 */
	private Collection<Edge> edges = null;
	
	/**
	 * Highest identifier which is currently assigned to a {@link Node}.  
	 */
	private int highestNodeId = 0;
	
	public ObservableGraph(){
		this.graphObservers = new ArrayList<GraphObserver>();
		this.nodes = new TreeSet<Node>();
		this.edges = new ArrayList<Edge>();
	}
	
	/**
	 * Checks whether this graph contains the specified {@link Node}.
	 * @author Thomas G&ouml;rres
	 * @param nodeIndex index of the searched node
	 * @return <code>true</code> if this graph contains that node
	 */
	public boolean containsNode(int nodeIndex){
		return getNodesAsInteger().contains(nodeIndex);
	}
	
	/**
	 * Searches all direct predecessors of the specified {@link Node}.
	 * @author Thomas G&ouml;rres
	 * @param node index of the node who's successors are searched
	 * @return indices of this node's direct successors. In case of an
	 * exception, an empty list is returned
	 */
	public List<Integer> getPredecessors(int node){
		// get all nodes of the graph
		Collection<Integer> allNodes = getNodesAsInteger();
		Collection<Edge> allEdges = getEdges();
		if (null == allNodes)
			return Collections.emptyList();

		// search for edges that connect a predecessor with the specified node
		List<Integer> predecessors = new ArrayList<Integer>();
		for (Edge edge: allEdges)
			if (edge.getEndNode().getId() == node)
				predecessors.add(edge.getStartNode().getId());
		
		return predecessors;
	}
	
	/**
	 * Searches all direct successors of the specified {@link Node}.
	 * @author Thomas G&ouml;rres
	 * @param node index of the node who's successors are searched
	 * @return indices of this node's direct successors, sorted in ascending order. In case of an
	 * exception, an empty list is returned
	 */
	public List<Integer> getSuccessors(int node) throws IllegalArgumentException{
		// get all nodes of the graph
		Collection<Integer> allNodes = getNodesAsInteger();
		Collection<Edge> allEdges = getEdges();
		if (null == allNodes)
			return Collections.emptyList();

		// search for edges that connect the specified node with a successor
		List<Integer> successors = new ArrayList<Integer>();
		for (Edge edge: allEdges)
			if (edge.getStartNode().getId() == node)
				successors.add(edge.getEndNode().getId());
		
		Collections.sort(successors);
		
		return successors;
	}
	
	/**
	 * Increments the identifier of the specified {@link Node}. 
	 * @param node node which identifier is to be incremented
	 * @throws NullPointerException if <code>node</code> is <code>null</code>
	 */
	private void incrementNodeId(Node node) throws NullPointerException{
		if (node==null){
			throw new NullPointerException();
		}
		/*
		 * Save old Node id.
		 */
		int oldNodeId = node.getId();
		/*
		 * Increments the id.
		 */
		node.setId(node.getId()+1);
		/*
		 * Information to GraphObservers.
		 */
		for(GraphObserver graphObserver : this.graphObservers){
			graphObserver.onNodeChanged(oldNodeId,node.getId());
		}
	}
	
	/**
	 * Decrement the identifier of the specified {@link Node}. 
	 * @param node node which identifier is to be decremented
	 * @throws NullPointerException if <code>node</code> is <code>null</code>
	 */
	private void decrementNodeId(Node node) throws NullPointerException{
		if (node==null){
			throw new NullPointerException();
		}
		/*
		 * Save old Node id.
		 */
		int oldNodeId = node.getId();
		/*
		 * Decrements the id.
		 */
		node.setId(node.getId()-1);
		/*
		 * Information to GraphObservers.
		 */
		for(GraphObserver graphObserver : this.graphObservers){
			graphObserver.onNodeChanged(oldNodeId,node.getId());
		}	
	}

	/**
	 * Autoset the node identifier.
	 * @param node the node identifier of the node which produce the autoset
	 * @param if <code>isNodeAdding</code> it is <code>true</code> the nodes will be increment, otherwise decrement
	 * @throws IllegalArgumentException if <code>node</code> is equal or smaller than <code>0</code>
	 */
	private void autosetNodeIdAndEdges(int node, boolean isNodeAdding) 
	throws IllegalArgumentException{
		if ((node<=0)){
			throw new IllegalArgumentException();
		}
		/*
		 * Create a temporary collection of Edge
		 */
		Collection<Edge> tempEdges = this.edges;
		
		/*
		 * Save the old Edges
		 */
		Object[] edgesOldObject = this.edges.toArray();
		Integer[][] edgesOld = new Integer[this.edges.size()][2];
		for (int i = 0;i<this.edges.size();i++){
				edgesOld[i][0]=((Edge)edgesOldObject[i]).getStartNode().getId();
				edgesOld[i][1]=((Edge)edgesOldObject[i]).getEndNode().getId();
		}
		
		/*
		 * Change the Edge which has the current Node as startNode or endNode
		 */
		for(Node nodeOnGraph:this.nodes){
			if (nodeOnGraph.getId()>=node){
				for (Edge edge : tempEdges){
						if (edge.getStartNode().equals(nodeOnGraph)){
							edge.setStartNode(nodeOnGraph);
						}
						if (edge.getEndNode().equals(nodeOnGraph)){
							edge.setEndNode(nodeOnGraph);
						}
				}
			}
		}
		
		if (isNodeAdding){			
			/*
			 * Increment backward until the list.
			 * At first the nodes which will be increment 
			 * added to the array list nodesToIncrement, 
			 * than all nodes in this list will be 
			 * increment backward.
			 */
			ArrayList<Node> nodesToIncrement = new ArrayList<Node>();
			for (Node nodeOnGraph : this.nodes){
				if (nodeOnGraph.getId()>=node){
					nodesToIncrement.add(nodeOnGraph);
				}	
			}
			
			for (int currentIndex = nodesToIncrement.size();currentIndex>0;currentIndex--){
				this.incrementNodeId(nodesToIncrement.get(currentIndex-1));
			}
		}
		else{
			/*
			 * Decrement until the list.
			 * At first the nodes which will be decrement 
			 * added to the array list nodesToDecrement, 
			 * than all nodes in this list will be 
			 * decrement.
			 */
			ArrayList<Node> nodesToDecrement = new ArrayList<Node>();
			for (Node nodeOnGraph : this.nodes){
				if (nodeOnGraph.getId()>node){
					nodesToDecrement.add(nodeOnGraph);
				}	
			}
			
			for (int currentIndex=0;currentIndex<nodesToDecrement.size();currentIndex++){
				this.decrementNodeId(nodesToDecrement.get(currentIndex));
			}
				
		}
		
		/*
		 * Set this.edges to temporary Edge collection  
		 */
		this.edges = tempEdges;
		
		/*
		 * Create a object array to give back the new edges.
		 */
		Object[] edgesNew = this.edges.toArray();
		
		/*
		 *	Information on GraphObservers. 
		 */
		for (int i = (edgesNew.length-1);i>=0;i--){
			for (GraphObserver graphObserver : this.graphObservers){
				graphObserver.onEdgeChanged(edgesOld[i][0],edgesOld[i][1],
						((Edge)edgesNew[i]).getStartNode().getId(), 
						((Edge)edgesNew[i]).getEndNode().getId());
			}
		}
	}

	/**
	 * Add the specified {@link GraphObserver} to {@link ObservableGraph}.
	 * @param graphObserver the graph observer which will be added
	 * @throws NullPointerException if <code>graphObserver</code> is <code>null</code>
	 */
	public void addGraphObserver(GraphObserver graphObserver) 
	throws NullPointerException{
		if(graphObserver==null){
			throw new NullPointerException();
		}
		this.graphObservers.add(graphObserver);
	}
	
	/**
	 * Remove the specified {@link GraphObserver} from {@link ObservableGraph}.
	 * @param graphObserver the graph observer which will be removed
	 * @throws NullPointerException if <code>graphObserver</code> is <code>null</code>
	 */
	public void removeGraphObserver(GraphObserver graphObserver) 
	throws NullPointerException{
		if(graphObserver==null){
			throw new NullPointerException();
		}
		this.graphObservers.remove(graphObserver);
	}
	
	/**
	 * Get all {@link GraphObserver} from {@link ObservableGraph}.
	 * @return the graph observers of observable graph.
	 */
	public Collection<GraphObserver> getGraphObservers(){
		return this.graphObservers;
	}
	
	/**
	 * Get a {@link Collection} of {@link Node} from {@link ObservableGraph}.
	 * @return a collection of all nodes on the observable graph.
	 */
	public Collection<Node> getNodes() {
		return this.nodes;
	}
	
	/**
	 * Get a {@link Collection} of node identifiers from all nodes on {@link ObservableGraph}.
	 * @return a collection of node identifiers from all nodes on the observable graph
	 */
	public Collection<Integer> getNodesAsInteger() {
		/*
		 * Adapt the list of nodes to integer list 
		 * with the ids of all nodes. 
		 */
		Collection<Integer> result = new TreeSet<Integer>();
		for (Node node : this.getNodes()){
			result.add(node.getId());
		} 
		return result;
	}
	
	/**
	 * Get a {@link Edge} from {@link ObservableGraph}.
	 * @param node the node identifier from the node which will be searched.
	 * @return if the node exist on the observable graph the node, otherwise <code>null</code>
	 * @throws IllegalArgumentException if <code>node</code> is equal or smaller than <code>0</code>
	 */
	public Node getNode(int node) throws IllegalArgumentException{
		if (node <= 0){
			throw new IllegalArgumentException();
		}
		/*
		 * Search for the node with the right id and return it.
		 */
		for (Node nodeOnGraph: this.nodes){
			if (nodeOnGraph.getId()==node){
				return nodeOnGraph;
			}
		}
		return null;
	}
	
	/**
	 * Add a {@link Node} to {@link ObservableGraph}. If the {@link Node} exists
	 * on {@link ObservableGraph}, only the position is changed.
	 * @param node the node identifier of node which will be added
	 * @param position the position on the observable graph
	 * @throws NullPointerException if <code>position</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>node</code> is equal or smaller than <code>0</code>
	 */
	public void addNode(int node, Point position) throws NullPointerException, IllegalArgumentException{
		if(position==null){
			throw new NullPointerException();
		}
		else if (node<=0){
			throw new IllegalArgumentException();
		}
		
		/*
		 * Add the Node.
		 */
		if (this.getNode(node)!=null){
			this.getNode(node).setPosition(position);
		}
		else{
			this.nodes.add(new Node(node, position));
			/*
			 * If the node is adding after the last Node 
			 * idCounter must incremented
			 */
			if ((this.highestNodeId+1) == node){
				this.highestNodeId++;
			}
		}
		/*
		 * Information to GraphObservers.
		 */
		for (GraphObserver graphObserver: this.graphObservers){
			graphObserver.onNodeAdded(node, position);
		}
	}
	
	/**
	 * Add a {@link Node} to {@link ObservableGraph}. 
	 * If a {@link Node} with the same node identifier already 
	 * exists on {@link ObservableGraph}, the node identifier 
	 * of this {@link Node} and all {@link Node}s with greater 
	 * identifier will be increment.
	 * @param node the node identifier of the node which will be added
	 * @param position the position on the observable graph
	 * @throws NullPointerException if <code>position</code> is <code>null</code>
	 * @throws IllegalArgumentException if <code>node</code> is equal or smaller than <code>0</code>
	 */
	public void addNodeWithAutomaticNodeIdAdaptation(int node, Point position) 
	throws NullPointerException, IllegalArgumentException {
		if(position == null){
			throw new NullPointerException();
		}
		else if (node<=0){
			throw new IllegalArgumentException();
		}
		
		if (this.getNode(node)!=null){
			this.autosetNodeIdAndEdges(node, true);
		}
		/*
		 * Add the Node.
		 */
		this.nodes.add(new Node(node, position));
		
		this.highestNodeId++;
		/*
		 * Information to GraphObservers.
		 */
		for (GraphObserver graphObserver: this.graphObservers){
			graphObserver.onNodeAdded(node, position);
		}
	}
	
	/**
	 * Add a {@link Node} to {@link ObservableGraph}. 
	 * The node identifier will be publish 
	 * by {@link GraphObserver} method onNodeAdded.
	 * @param position the position of the new node on observable graph
	 * @throws NullPointerException if <code>position</code> is <code>null</code>
	 */
	public void addNode(Point position) throws NullPointerException {
		if (position==null){
			throw new NullPointerException();
		}
		/*
		 * Node id save in node.
		 */
		this.highestNodeId++;
		
		int node = this.highestNodeId;
		
		/*
		 * Adding the node.
		 */
		this.nodes.add(new Node(node, position));
		/*
		 * Information to GraphObservers.
		 */
		for (GraphObserver graphObserver: this.graphObservers){
			graphObserver.onNodeAdded(node, position);
		}
	}

	/**
	 * Add a {@link Node} to {@link ObservableGraph}. If the {@link Node} exists 
	 * on {@link ObservableGraph}, only the position is changed.
	 * @param node the node which will be added to the observable graph
	 * @throws NullPointerException if <code>node</code> is <code>null</code>
	 */
	public void addNode(Node node) throws NullPointerException{
		if (node==null){
			throw new NullPointerException();
		}
		/*
		 * Add the Node.
		 */
		if (this.getNode(node.getId())!=null){
			this.getNode(node.getId()).setPosition(node.getPosition());
		}
		else{
			this.nodes.add(node);
			/*
			 * If the node is adding after the last Node idCounter must 
			 * incremented
			 */
			if ((this.highestNodeId+1) == node.getId()){
				this.highestNodeId++;
			}
		}
		/*
		 * Information to GraphObservers.
		 */
		for (GraphObserver graphObserver: this.graphObservers){
			graphObserver.onNodeAdded(node.getId(), node.getPosition());
		}
	}
	
	/**
	 * Add a {@link Node} to {@link ObservableGraph}. 
	 * If a {@link Node} with the same node identifier already 
	 * exists on {@link ObservableGraph}, the node identifier 
	 * of this {@link Node} and all {@link Node}s with greater 
	 * identifier will be increment.
	 * @param node the node which will be added to the observable graph
	 * @throws NullPointerException if <code>node</code> is <code>null</code>
	 */
	public void addNodeWithAutomaticNodeIdAdaptation(Node node) 
	throws NullPointerException{
		if (node==null){
			throw new NullPointerException();
		}
		/*
		 * Increment the nodes which have a equal or 
		 * greater id and chance the edges according to this. 
		 */
		if (this.getNode(node.getId())!=null){
			this.autosetNodeIdAndEdges(node.getId(), true);	
		}
		
		/*
		 * Add the Node.
		 */
		this.nodes.add(node);
		
		this.highestNodeId++;
		/*
		 * Information to GraphObservers.
		 */
		for (GraphObserver graphObserver: this.graphObservers){
			graphObserver.onNodeAdded(node.getId(), node.getPosition());
		}
	}
	/**
	 * Delete the edges which have the node identifier as start node or as end node.
	 */
	private void removeEdges(int node)throws IllegalArgumentException{
		if (node<=0){
			throw new IllegalArgumentException();
		}
		ArrayList<Edge> edgesToDelete = new ArrayList<Edge>();
		for (Edge edge : this.edges){
			if ((edge.getStartNode().getId()==node)||(edge.getEndNode().getId()==node)){
				edgesToDelete.add(edge);
			}
		}
		for (Edge edge : edgesToDelete){
			this.removeEdge(edge);
		}
	}
	
	/**
	 * Remove a {@link Node} from {@link ObservableGraph}.
	 * @param node the node identifier from the node which will be removed
	 * @throws IllegalArgumentException if <code>node</code> is equal or smaller than <code>0</code>
	 * @throws NoSuchElementException if <code>node</code> not exists on observeable graph
	 */
	public void removeNode(int node) throws IllegalArgumentException, NoSuchElementException{
		if (node <= 0){
			throw new IllegalArgumentException();
		}
		else if(this.getNode(node)==null){
			throw new NoSuchElementException();
		}
		
		this.removeEdges(node);
		
		Node nodeToDelete = this.getNode(node);
		this.nodes.remove(nodeToDelete);
		/*
		 * Information to GraphObservers
		 */
		for (GraphObserver graphObserver: this.graphObservers){
			graphObserver.onNodeRemoved(node);
		}
	}
	
	
	/**
	 * Remove a {@link Node} from {@link ObservableGraph} and 
	 * increment the node identifier of {@link Node}s on 
	 * {@link ObservableGraph}, which has greather node identifier.
	 * @param node the node identifier from the node which will be removed
	 * @throws IllegalArgumentException if <code>node</code> is equal or smaller than <code>0</code>
	 * @throws NoSuchElementException if <code>node</code> not exists on observeable graph
	 */
	public void removeNodeWithAutomaticNodeIdAdaptation(int node) 
	throws IllegalArgumentException, NoSuchElementException{
		if (node <= 0){
			throw new IllegalArgumentException();
		}
		else if (this.getNode(node)==null){
			throw new NoSuchElementException();
		}

		this.removeEdges(node);
		
		/*
		 * Delete the Node if the Node was the Node with the highest Id,
		 * it can be only removed. Else all Nodes, which have an greater Id,
		 * their Id must decremented after removing the Node.
		 */
		Node nodeToDelete = this.getNode(node);
		this.nodes.remove(nodeToDelete);
		
		/*
		 * Information to GraphObservers
		 */
		for (GraphObserver graphObserver: this.graphObservers){
			graphObserver.onNodeRemoved(node);
		}
		/*
		 * Chance the greater nodes and edges.
		 */
		if (node < this.highestNodeId + 1){
			this.autosetNodeIdAndEdges(node, false);
		}
		this.highestNodeId--;
	}
	
	
	/**
	 * Move position of a {@link Node} on {@link ObservableGraph}.
	 * @param node the node identifier of node to move
	 * @param position the new position on the observable graph
	 * @throws NullPointerException if <code>position</code> is <code>null</code> 
	 * @throws IllegalArgumentException if <code>node</code> is equal or smaller than <code>0</code>
	 * @throws NoSuchElementException if <code>node</code> not exists on observable graph
	 */
	public void moveNode(int node, Point position)
	throws NullPointerException, IllegalArgumentException, NoSuchElementException{
		if (position==null){
			throw new NullPointerException();
		}
		else if (node <= 0){
			throw new IllegalArgumentException();
		}
		else if (this.getNode(node)==null){
			throw new NoSuchElementException();
		}
		/*
		 * Move the position.
		 */
		this.getNode(node).setPosition(position);
		/*
		 * Information to GraphObservers.
		 */
		for (GraphObserver graphObserver : this.graphObservers){
			graphObserver.onNodeMoved(node, this.getNode(node).getPosition());
		}
	} 
	
	/**
	 * Get a {@link Collection} of {@link Edge} from {@link ObservableGraph}.
	 * @return collection of all edges on the observable graph
	 */	
	public Collection<Edge> getEdges(){
		return this.edges;
	}
	
	
	/**
	 * Get an {@link Edge} from {@link ObservableGraph}.
	 * @param startNode the start node of the edge
	 * @param endNode the end node of the edge
	 * @return if the edge exist on the observable graph the edge, otherwise <code>null</code>
	 * @throws IllegalArgumentException if <code>startNode</code> or <code>endNode</code> is equal or smaller than <code>0</code>
	 */
	public Edge getEdge(int startNode, int endNode) 
	throws IllegalArgumentException{
		if ((startNode<=0)||(endNode<=0)){
			throw new IllegalArgumentException();
		}
		for (Edge edge:this.edges){
			if ((edge.getStartNode().equals(this.getNode(startNode)))
					&&(edge.getEndNode().equals(this.getNode(endNode)))){
				return edge;
			}
		}
		return null;
	}
	
	
	/**
	 * Get an {@link Edge} from {@link ObservableGraph}.
	 * @param startNode the start node of the edge
	 * @param endNode the end node of the edge
	 * @return if the edge exist on the observable graph the edge, otherwise <code>null</code>
	 * @throws NullPointerException if <code>startNode</code> or <code>endNode</code> is <code>null</code>
	 */
	public Edge getEdge(Node startNode, Node endNode) 
	throws NullPointerException{
		if ((startNode==null)||(endNode==null)){
			throw new NullPointerException();
		}
		for(Edge edge: this.edges){
			if ((edge.getStartNode().equals(startNode))&&
					(edge.getEndNode().equals(endNode))){
				return edge;
			}
		}
		return null;
	}
		
	
	/**
	 * Add an {@link Edge} to {@link ObservableGraph}.
	 * @param startNode the start node of the edge
	 * @param endNode the end node of the edge
	 * @throws IllegalArgumentException if <code>startNode</code> or <code>endNode</code> is equal or smaller than <code>0</code>
	 * @throws NoSuchElementException if <code>startNode</code> or <code>endNode</code> not exist on the observable graph
	 */
	public void addEdge(int startNode, int endNode) 
	throws IllegalArgumentException, NoSuchElementException{
		if ((startNode<=0)||(endNode<=0)){
			throw new IllegalArgumentException();
		}
		else if (((this.getNode(startNode)==null)||(this.getNode(endNode)==null))){
			throw new NoSuchElementException();
		}
		
		if(this.getEdge(startNode, endNode)==null){
			this.edges.add(new Edge(this.getNode(startNode),this.getNode(endNode)));
			/*
			 * Information to GraphObservers
			 */
			for (GraphObserver graphObserver:this.graphObservers){
				graphObserver.onEdgeAdded(startNode, endNode);
			}
		}
	}
	
	
	/**
	 * Add an {@link Edge} to {@link ObservableGraph}.
	 * @param startNode the start node of the edge
	 * @param endNode the end node of the edge
	 * @throws NullPointerException if <code>startNode</code> or <code>endNode</code> is <code>null</code>
	 * @throws NoSuchElementException if <code>startNode</code> or <code>endNode</code> not exist on the observable graph
	 */
	public void addEdge(Node startNode, Node endNode)
	throws NullPointerException, NoSuchElementException{
		if (((startNode==null)||(endNode==null))){
			throw new NullPointerException();
		}
		else if((!this.nodes.contains(endNode))||(!(this.nodes.contains(startNode)))){
			throw new NoSuchElementException();
		}
		if(this.getEdge(startNode, endNode)==null){
			this.edges.add(new Edge(startNode,endNode));
			/*
		 	* Information to GraphObservers.
		 	*/
			for (GraphObserver graphObserver:this.graphObservers){
				graphObserver.onEdgeAdded(startNode.getId(), endNode.getId());
			}
		}
	}
	
	
	/**
	 * Add an {@link Edge} to {@link ObservableGraph}.
	 * @param edge the edge which will be added
	 * @throws NullPointerException if <code>edge</code> is <code>null</code>
	 * @throws NoSuchElementException if the start node or the end node not exist on the observable graph
	 */
	public void addEdge(Edge edge)throws NullPointerException, NoSuchElementException{
		if(edge==null){
			throw new NullPointerException();
		}
		else if (((this.getNode(edge.getStartNode().getId())==null)||
				(this.getNode(edge.getEndNode().getId())==null))){
			throw new NoSuchElementException();
		}
		if (this.getEdge(edge.getStartNode(),edge.getEndNode())==null){
			this.edges.add(edge);
			/*
			 * Information to GraphObservers.
			 */
			for (GraphObserver graphObserver:this.graphObservers){
				graphObserver.onEdgeAdded(edge.getStartNode().getId(), 
						edge.getEndNode().getId());
			}
		}
	}

	
	/**
	 * Remove an {@link Edge} from {@link ObservableGraph}.
	 * @param startNode the start node of the edge
	 * @param endNode the end node of the edge
	 * @throws IllegalArgumentException if <code>startNode</code> or <code>endNode</code> is equal or smaller than <code>0</code>
	 * @throws NoSuchElementException if the edge, <code>startNode</code> or <code>endNode</code> not exist on the observable graph
	 */
	public void removeEdge(int startNode,int endNode)
	throws IllegalArgumentException, NoSuchElementException{
		if ((startNode<=0)||(endNode<=0)){
			throw new IllegalArgumentException();
		}		
		else if((this.getNode(startNode)==null)||(this.getNode(endNode)==null)||
				(this.getEdge(startNode, endNode)==null)){
			throw new NoSuchElementException();
		}
		this.edges.remove(this.getEdge(startNode, endNode));
		/*
		 * Information to GraphObservers.
		 */
		for(GraphObserver graphObserver:this.graphObservers){
			graphObserver.onEdgeRemoved(startNode, endNode);
		}
	}
	
	
	/**
	 * Remove an {@link Edge} from {@link ObservableGraph}.
	 * @param startNode the start node of the edge
	 * @param endNode the end node of the edge
	 * @throws NullPointerException if <code>startNode</code> or <code>endNode</code> is <code>null</code>
	 * @throws NoSuchElementException if the edge, <code>startNode</code> or <code>endNode</code> not exist on the observable graph
	 */
	public void removeEdge(Node startNode,Node endNode) 
	throws NullPointerException, NoSuchElementException{
		if(((startNode==null)||(endNode==null))){
			throw new NullPointerException();
		}
		else if (((!(this.nodes.contains(startNode)))||
						((!(this.nodes.contains(endNode)))))||
							(this.getEdge(startNode, endNode)==null)){
			throw new NoSuchElementException();
		}
		this.edges.remove(this.getEdge(startNode, endNode));
		/*
		 * Information to GraphObservers
		 */
		for(GraphObserver graphObserver:this.graphObservers){
			graphObserver.onEdgeRemoved(startNode.getId(), endNode.getId());
		}
	}
	
	
	/**
	 * Remove an {@link Edge} from {@link ObservableGraph}.
	 * @param edge an edge which will be removed 
	 * @throws NullPointerException if <code>edge</code> is <code>null</code> 
	 * @throws NoSuchElementException if <code>edge</code> , the start node or the end node not exist on the observable graph
	 */
	public void removeEdge(Edge edge)throws NullPointerException, NoSuchElementException{
		if ((edge==null)){
			throw new NullPointerException();
		}
		else if (((!(this.nodes.contains(edge.getStartNode())))||
						((!(this.nodes.contains(edge.getEndNode())))))||
						(!(this.edges.contains(edge)))){
			throw new NoSuchElementException();
		}
		this.edges.remove(edge);
		/*
		 * Information to GraphObservers.
		 */
		for(GraphObserver graphObserver:this.graphObservers){
			graphObserver.onEdgeRemoved(edge.getStartNode().getId(),
					edge.getEndNode().getId());
		}
	}
	
	
	/**
	 * Load the structure of {@link ObservableGraph}.
	 * @param list a list which contain a collection of node and a collection of edge
	 * @throws NullPointerException if <code>list</code> is <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public void loadGraph(List<? extends Object> list)
	throws NullPointerException{
		if (list == null){
			throw new NullPointerException();
		}
		
		for (int i = 1; i<=this.highestNodeId;i++){
			this.removeNode(i);
		}
		
		Collection<Node> nodes= (Collection<Node>)list.get(0);
		Collection<Edge> edges= (Collection<Edge>)list.get(1);
		
		/*
		 * Add the nodes.
		 */
		for (Node node : nodes){
			this.addNode(node);
		}
		/*
		 * Add the edges.
		 */
		for (Edge edge : edges){
			this.addEdge(edge);
		}
	}
	
	
	/**
	 * @return next free node identifier
	 */
	public int getNextNodeId(){
		return this.highestNodeId +1;
	}
}