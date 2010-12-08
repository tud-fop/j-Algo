package org.jalgo.module.bfsdfs.controller;

import org.jalgo.module.bfsdfs.graph.ObservableGraph;
import org.jalgo.module.bfsdfs.undo.Step;

/**
 * A {@link Step} from undo package it realized that a 
 * AddEdge step can be undoable.
 * @author Johannes Siegert
 *
 */
class AddEdge extends DesignStep{
	private int startNode = 0;
	private int endNode = 0;
	
	/**
	 * @param observableGraph
	 * @param startNode the start node of the edge
	 * @param endNode the end node of the edge
	 */
	public AddEdge(ObservableGraph observableGraph,int startNode, int endNode){
		super(observableGraph);
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public void execute() {
		this.getObservableGraph().addEdge(this.startNode,this.endNode);
	}
	
	public void undo() {
		this.getObservableGraph().removeEdge(this.startNode, this.endNode);
	}

	public int getStartNode() {
		return startNode;
	}

	public int getEndNode() {
		return endNode;
	}
}
