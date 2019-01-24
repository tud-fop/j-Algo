package org.jalgo.module.bfsdfs.controller;

import java.awt.Point;

import org.jalgo.module.bfsdfs.graph.ObservableGraph;
import org.jalgo.module.bfsdfs.undo.Step;
/**
 * A {@link Step} from undo package it realized 
 * that a RemoveNode step can be undoable.
 * @author Johannes Siegert
 */
class RemoveNode extends DesignStep{
	private int nodeId = 0;
	private Point position = null;
	

	public RemoveNode(ObservableGraph observableGraph,int node) {
		super(observableGraph);
		this.position = this.getObservableGraph().getNode(node).getPosition();
		this.nodeId = node;
	}
	
	public void execute() {
		this.getObservableGraph().removeNodeWithAutomaticNodeIdAdaptation(this.nodeId);
	}
	
	public void undo() {
		this.getObservableGraph().addNodeWithAutomaticNodeIdAdaptation(this.nodeId, this.position);
	}
}
