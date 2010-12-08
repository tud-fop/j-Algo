package org.jalgo.module.bfsdfs.controller;

import org.jalgo.module.bfsdfs.graph.ObservableGraph;
import org.jalgo.module.bfsdfs.undo.Step;

/**
 * A {@link Step} from undo package it realized that a 
 * AddDoubleEdge step can be undoable.
 * @author Johannes Siegert
 *
 */
class AddDoubleEdge extends DesignStep {
	
	private int startNode = 0;
	private int endNode = 0;
	
	private GraphController graphController = null;
	
	/**
	 * @param observableGraph
	 * @param startNode the start node of the edge
	 * @param endNode the end node of the edge
	 */
	public AddDoubleEdge(ObservableGraph observableGraph, 
			int startNode, int endNode, GraphController graphController){
		super(observableGraph);
		this.graphController = graphController;
		this.startNode = startNode;
		this.endNode = endNode;
	}

	public void execute() {
		this.getObservableGraph().addEdge(this.startNode, this.endNode);
		this.getObservableGraph().addEdge(this.endNode, this.startNode);
	}

	public void undo() {
		boolean firstEdgeRemove = true;
		boolean secondEdgeRemove = true;
		for (Step step : this.graphController.getUndoableSteps()){
			if (step instanceof AddEdge){
				int edgeStartNode = ((AddEdge)step).getStartNode();
				int edgeEndNode = ((AddEdge)step).getEndNode();
				if ((this.startNode == edgeStartNode)&&(this.endNode == edgeEndNode)){
					firstEdgeRemove = false;
				}
				if((this.startNode == edgeEndNode)&&(this.endNode == edgeStartNode)){
					secondEdgeRemove = false;
				}
			}
		}
		
		if (firstEdgeRemove){
			this.getObservableGraph().removeEdge(this.startNode, this.endNode);
		}
		if (secondEdgeRemove){
			this.getObservableGraph().removeEdge(this.endNode, this.startNode);
		}
	}
}
