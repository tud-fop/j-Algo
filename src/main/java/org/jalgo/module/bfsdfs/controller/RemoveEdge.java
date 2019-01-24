package org.jalgo.module.bfsdfs.controller;

import org.jalgo.module.bfsdfs.graph.ObservableGraph;
import org.jalgo.module.bfsdfs.undo.Step;
/**
 * A {@link Step} from undo package it realized that a 
 * RemoveEdge step can be undoable.
 * @author Johannes Siegert
 */
class RemoveEdge extends DesignStep {
	private int startNode = 0;
	private int endNode = 0;
	
	/**
	 * @param observableGraph
	 * @param startNode the start node of the edge
	 * @param endNode the end node of the edge
	 */
	public RemoveEdge(ObservableGraph observableGraph, int startNode, int endNode){
		super(observableGraph);
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public void execute() {
		this.getObservableGraph().removeEdge(this.startNode, this.endNode);
	}

	public void undo() {
		this.getObservableGraph().addEdge(this.startNode, this.endNode);
	}
}
