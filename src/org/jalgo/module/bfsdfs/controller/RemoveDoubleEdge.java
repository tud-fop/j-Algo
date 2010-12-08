package org.jalgo.module.bfsdfs.controller;

import org.jalgo.module.bfsdfs.graph.ObservableGraph;
import org.jalgo.module.bfsdfs.undo.Step;
/**
 * A {@link Step} from undo package it realized that a 
 * RemoveDoubleEdge step can be undoable.
 * @author Johannes Siegert
 */
class RemoveDoubleEdge extends DesignStep {
	private int startNode = 0;
	private int endNode = 0;

	/**
	 * @param observableGraph
	 * @param startNode the start node of the edge
	 * @param endNode the end node of the edge
	 */
	public RemoveDoubleEdge(ObservableGraph observableGraph, int startNode, int endNode) {
		super(observableGraph);
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public void execute() {
		this.getObservableGraph().removeEdge(this.startNode, this.endNode);
		this.getObservableGraph().removeEdge(this.endNode, this.startNode);
	}

	public void undo() {
		this.getObservableGraph().addEdge(this.startNode, this.endNode);
		this.getObservableGraph().addEdge(this.endNode, this.startNode);
	}
}
