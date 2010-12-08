package org.jalgo.module.bfsdfs.graph;

import java.io.Serializable;

/**
 * This class represent an Edge on {@link ObservableGraph}.
 * @author Johannes Siegert
 *
 */
public class Edge implements Serializable {

	private static final long serialVersionUID = 540764027708479944L;

	private Node startNode = null;
	private Node endNode = null;
	
	/**
	 * @param startNode start node of the new edge.
	 * @param endNode end node of the new edge.
	 * @throws NullPointerException if <code>startNode</code> or <code>endNode</code> is <code>null</code>
	 */
	public Edge (Node startNode, Node endNode) throws NullPointerException{
		if ((startNode==null)||(endNode==null)){
			throw new NullPointerException();
		}
		this.startNode = startNode;
		this.endNode = endNode;
	}
	
	public Node getStartNode() {
		return startNode;
	}

	/**
	 * @param startNode node to set as start node.
	 * @throws NullPointerException if <code>startNode</code> is <code>null</code>
	 */
	public void setStartNode(Node startNode) throws NullPointerException{
		if (startNode==null){
			throw new NullPointerException();
		}
		this.startNode = startNode;
	}

	public Node getEndNode() {
		return endNode;
	}

	/**
	 * @param endNode node to set as end node.
	 * @throws NullPointerException if <code>endNode</code> is <code>null</code>
	 */
	public void setEndNode(Node endNode) throws NullPointerException {
		if (endNode==null){
			throw new NullPointerException();
		}
		this.endNode = endNode;
	}
	
	/**
	 *	@return <code>true</code> if start node identifier and end node identifier are equal
	 *	@throws NullPointerException if <code>obj</code> is <code>null</code>
	 */
	@Override
	public boolean equals(Object obj) throws IllegalArgumentException{
		if (obj==null){
			throw new IllegalArgumentException();
		}
		else{
			Edge edge = (Edge) obj;
			if ((edge.getStartNode().getId()==this.startNode.getId())&&(edge.getEndNode().getId()==this.endNode.getId())){
				return true;
			}
			else{
				return false;
			}
		}
	}
	
	@Override
	public String toString() {
		return(startNode.getId() + "->"+endNode.getId());
	}
	
}

