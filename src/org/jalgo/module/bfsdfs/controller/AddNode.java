package org.jalgo.module.bfsdfs.controller;

import java.awt.Point;

import org.jalgo.module.bfsdfs.graph.GraphObserver;
import org.jalgo.module.bfsdfs.graph.ObservableGraph;
import org.jalgo.module.bfsdfs.undo.Step;
/**
 * A {@link Step} from undo package. It realized that a AddNode step can be undoable.
 * Implements GraphObserver because it need to know what identifier has the added Node,
 * to run undo.
 * @author Johannes Siegert
 */
class AddNode extends DesignStep implements GraphObserver {
	
	private Point position = null;
	private int nodeId = 0;
	
	/**
	 * @param observableGraph
	 * @param pos position of the node on the observableGraph
	 */
	public AddNode(ObservableGraph observableGraph, 
			Point pos,GraphController graphController) {
		super(observableGraph);
		graphController.addGraphObserver(this);
		this.position = pos;
	}
	
	public void execute() {
		if (this.nodeId==0){
			this.getObservableGraph().addNode(this.position);
		}
		else{
			this.getObservableGraph().
			addNode(this.nodeId, this.position);
		}
	}
	
	public void undo() {
		this.getObservableGraph().removeNodeWithAutomaticNodeIdAdaptation(this.nodeId);
	}
	
	public void onNodeAdded(int node, Point pos){
		if (pos.equals(this.position)){
			this.nodeId = node;
			this.position = pos;
		}
	}
	
	public void onNodeMoved(int node, Point pos) {
		if (this.nodeId==node){
			this.position = pos;
		}
	}
	
	public void onNodeChanged(int oldNodeId, int newNodeId) {
		if (this.nodeId==oldNodeId){
			this.nodeId=newNodeId;
		}
	}
	
	/**
	 * Non use method. 
	 */
	public void onGraphLoaded() {}
	
	/**
	 * Non use method. 
	 */
	public void onNodeRemoved(int node) {}
	
	/**
	 * Non use method. 
	 */
	public void onEdgeAdded(int startNode, int endNode) {}

	/**
	 * Non use method. 
	 */
	public void onEdgeRemoved(int startNode, int endNode) {}

	/**
	 * Non use method. 
	 */
	public void onEdgeChanged(int oldStartNode, int oldEndNode,
			int newStartNode, int newEndNode) {}
}
