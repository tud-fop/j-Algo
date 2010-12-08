package org.jalgo.module.bfsdfs.controller;

import java.awt.Point;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jalgo.main.AbstractModuleConnector.SaveStatus;
import org.jalgo.main.gui.JAlgoGUIConnector;
import org.jalgo.main.util.Messages;
import org.jalgo.module.bfsdfs.ModuleConnector;
import org.jalgo.module.bfsdfs.graph.Edge;
import org.jalgo.module.bfsdfs.graph.GraphObserver;
import org.jalgo.module.bfsdfs.graph.Node;
import org.jalgo.module.bfsdfs.graph.ObservableGraph;
import org.jalgo.module.bfsdfs.undo.Step;
import org.jalgo.module.bfsdfs.undo.Undoable;

/**
 * This class controls the access to the {@link ObservableGraph}.
 * @author Johannes Siegert
 */
public class GraphController extends Undoable{
	
	/**
	 * The graph which is control by {@link GraphController}.
	 */
	private ObservableGraph observableGraph = null;
	private ModuleConnector moduleConnector = null;
	
	public GraphController(){
		this.observableGraph = new ObservableGraph();
	}
	
	/**
	 * @param moduleConnector the moduleConnector which works together with the graph controller
	 */
	public GraphController(ModuleConnector moduleConnector){
		this.observableGraph = new ObservableGraph();
		this.moduleConnector = moduleConnector;
	}
	
	/**
	 * Searches all direct predecessors of the specified node
	 * @author Thomas G&ouml;rres
	 * @param node index of the node who's successors are searched
	 * @return indices of this node's direct successors. In case of an
	 * exception, an empty list is returned
	 */
	public List<Integer> getPredecessors(int node) {
		return observableGraph.getPredecessors(node);
	}
	
	/**
	 * Searches all direct successors of the specified node
	 * @author Thomas G&ouml;rres
	 * @param node index of the node who's successors are searched
	 * @return indices of this node's direct successors. In case of an
	 * exception, an empty list is returned
	 */
	public List<Integer> getSuccessors(int node) {
		return observableGraph.getSuccessors(node);
	}
	
	/**
	 * Add a {@link Node} to {@link ObservableGraph}.
	 * @param pos position on observable graph
	 */
	public void addNode(Point pos){
		this.makeUndoableStep(new AddNode(this.observableGraph,pos,this));
	}
	
	/**
	 * Add the start node, which isn't undoable to {@link ObservableGraph}.
	 * @param pos position on observable graph
	 */
	public void addStartNode(Point pos) throws IllegalArgumentException {
		// just executes the step; no undoability
		// important for loading a saved graph
		this.makeStep(new AddNode(this.observableGraph,pos,this));
	}
	
	/**
	 * Remove {@link Node} from {@link ObservableGraph}. 
	 * @param node identifier of the node which will be removed.
	 */
	public void removeNode(int node){
			/*
			 * Edges, which have the node as startNode or endNode must be deleted.
			 */
			ArrayList<Edge> removeEdges = new ArrayList<Edge>();
			for (Edge edge : this.getEdges()){
				if ((edge.getStartNode().equals(this.observableGraph.getNode(node)))
						||(edge.getEndNode().equals(this.observableGraph.getNode(node)))){	
					removeEdges.add(edge);
				}
			}
			
			/*
			 *  The removal of the node and the associated edges becomes a 
			 *  complex step
			 */
			ArrayList<Step> steps = new ArrayList<Step>();
			/*
			 *  Firstly the creation of the instances "RemoveEdge" for 
			 *  each edge in the list "removeEdges".
			 */
			while (!(removeEdges.isEmpty())){
				steps.add(new RemoveEdge(this.observableGraph, 
						removeEdges.get(0).getStartNode().getId(), 
						removeEdges.get(0).getEndNode().getId()));
				removeEdges.remove(removeEdges.get(0));
			}
			/*
			 * Secondly the creation of the instance "RemoveNode" 
			 * for the Node which has to be deleted.
			 */
			steps.add(new RemoveNode(this.observableGraph,node));
			this.makeUndoableStep(new ComplexStep(this.observableGraph, steps));
	}
	
	/**
	 * Add {@link Edge} to {@link ObservableGraph} if the node not exists on the {@link ObservableGraph}.
	 * @param startNode start node of the edge
	 * @param endNode end node of the edge
	 */
	public void addEdge(int startNode, int endNode){
		if (this.observableGraph.getEdge(startNode, endNode)==null){
			this.makeUndoableStep(new AddEdge(this.observableGraph,startNode,endNode));
		}
	}
	
	/**
	 * Remove {@link Edge} from {@link ObservableGraph}.
	 * @param startNode start node of the edge
	 * @param endNode end node of the edge
	 */
	public void removeEdge(int startNode, int endNode){
		this.makeUndoableStep(new RemoveEdge(this.observableGraph,startNode,endNode));	
	}
	
	/**
	 * Add two {@link Edge}'s to {@link ObservableGraph}. (<code>startNode</code>, <code>endNode</code>) and (<code>endNode</code>,<code>startNode</code>),
	 * if maximal one node exists on {@link ObservableGraph}.
	 * @param startNode start node of the edge
	 * @param endNode end node of the edge
	 */
	public void addDoubleEdge(int startNode, int endNode){
		if ((this.observableGraph.getEdge(startNode, endNode)!=null)&&((this.observableGraph.getEdge(endNode, startNode)!=null))){
			return;
		}
		this.makeUndoableStep(new AddDoubleEdge(this.observableGraph,startNode,endNode,this));
	}
	
	/**
	 * Remove two {@link Edge}'s to {@link ObservableGraph}. (<code>startNode</code>, <code>endNode</code>) and (<code>endNode</code>,<code>startNode</code>).
	 * @param startNode start node of the edge
	 * @param endNode end node of the edge
	 */
	public void removeDoubleEdge(int startNode, int endNode){
		this.makeUndoableStep(new RemoveDoubleEdge(this.observableGraph,startNode,endNode));
	}
	
	/**
	 * Add {@link GraphObserver} to {@link ObservableGraph}.
	 * @param graphObserver the graph observer which will be added
	 * @throws NullPointerException if <code>graphObserver</code> is <code>null</code>
	 */
	public void addGraphObserver(GraphObserver graphObserver) 
	throws NullPointerException{
		if (graphObserver==null){
			throw new NullPointerException();
		}
		else{
			this.observableGraph.addGraphObserver(graphObserver);
		}
	}
	
	/**
	 * Remove {@link GraphObserver} from {@link ObservableGraph}.
	 * @param graphObserver the graph observer which will be removed
	 * @throws NullPointerException if <code>graphObserver</code> is <code>null</code>
	 */
	public void removeGraphObserver(GraphObserver graphObserver) 
	throws NullPointerException{
		if (graphObserver==null){
			throw new NullPointerException();
		}
		else{
			this.observableGraph.removeGraphObserver(graphObserver);
		}
	}
	
	/**
	 * Update the position of the {@link Node} on {@link ObservableGraph}.
	 * @param node identifier of the node which will be moved
	 * @param pos new position of the node
	 */
	public void moveNode(int node,Point pos){
			this.observableGraph.moveNode(node, pos);
			/*
			 * After moving a node the save button must be enabled.
			 */
			this.moduleConnector.setSaveStatus(SaveStatus.CHANGES_TO_SAVE);
	}
	
	/**
	 * @return collection of all nodes on observable graph
	 */
	public Collection<Integer> getNodes(){
		return this.observableGraph.getNodesAsInteger();
	}
	
	/**
	 * @return collection of all edges on observable graph
	 */
	public Collection<Edge> getEdges(){
		return this.observableGraph.getEdges();
	}
	
	/**
	 * @param node identifier from node which position is searched
	 * @return position on observable graph
	 */
	public Point getNodePosition(int node){
		return this.observableGraph.getNode(node).getPosition();
	}
	
	/**
	 * @return all positions of all nodes on observable graph
	 */
	public Map<Integer, Point> getNodePositions(){
		Map<Integer, Point> result = new TreeMap<Integer, Point>();
		for (Node node:this.observableGraph.getNodes()){
			result.put(node.getId(), node.getPosition());
		}
		return result;
	}	
	
	/**
	 * This method set a ByteArrayOutputStream <br />
	 * to save the actually {@link ObservableGraph} into a file.
	 * @return ByteArrayOutputStream
	 */
	public ByteArrayOutputStream getDataForFile(){
		ByteArrayOutputStream byteArrayOutputStream = 
			new ByteArrayOutputStream();
		try{
			ObjectOutputStream objectOutputStream = 
				new ObjectOutputStream(byteArrayOutputStream);
			Collection<Node> nodes = this.observableGraph.getNodes();
			Collection<Edge> edges = this.observableGraph.getEdges();
			List<Collection<? extends Object>> result =
				new LinkedList<Collection<? extends Object>>();
			result.add(nodes);
			result.add(edges);
			objectOutputStream.writeObject(result);
			objectOutputStream.close();
		}
		catch (IOException ex) {
		ex.printStackTrace();
		}
		return byteArrayOutputStream;
	}
	
	/**
	 * Load the {@link ObservableGraph} from inputData.
	 * @param inputData the content which was read from file.
	 * @throws NullPointerException if <code>inputData</code> is <code>null</code>
	 */
	@SuppressWarnings("unchecked")
	public void setDataFromFile(ByteArrayInputStream inputData) 
	throws NullPointerException{
		if (inputData==null){
			throw new NullPointerException();
		}
		else{
			try {
				ObjectInputStream objectInputStream = 
					new ObjectInputStream(inputData);
				List<Collection<? extends Object>> list = 
					((List<Collection<? extends Object>>)objectInputStream.
							readObject());
				ModuleConnector.getGuiController().installStandardLayout();
				this.observableGraph.loadGraph(list); 
			}
			catch (IOException ex) {
				JAlgoGUIConnector.getInstance().showErrorMessage
				(Messages.getString(
					"bfsdfs", "ModuleConnector.No_valid_BFSDFS_file"));
			}
			catch (ClassNotFoundException ex) {
				JAlgoGUIConnector.getInstance().showErrorMessage
				(Messages.getString(
					"bfsdfs", "ModuleConnector.Loading_error") +
					System.getProperty("line.separator") +
					Messages.getString("bfsdfs", "ModuleConnector.File_damaged"));
			}
		}
	}
	
	/**
	 * @return next free node identifier to allocate
	 */
	public int getNextNodeId(){
		return this.observableGraph.getNextNodeId();
	}
}

