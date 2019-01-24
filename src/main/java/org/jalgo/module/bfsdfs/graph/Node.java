package org.jalgo.module.bfsdfs.graph;

import java.awt.Point;
import java.io.Serializable;

/**
 * This class represent a node on {@link ObservableGraph}.
 * @author Johannes Siegert
 *
 */
public class Node implements Serializable, Comparable<Node> {

	private static final long serialVersionUID = 7243160548381678774L;
	/**
	 * The identifier of {@link Node}.
	 */
	private int id = 0;
	/**
	 * The current position of {@link Node}.
	 */
	private Point position = null;
	
	/**
	 * @param id identifier of the new node
	 * @param pos position of the new node
	 * @throws NullPointerException if <code>pos</code> is <code>null</code>
	 * @throws IllegalArgumentException if the <code>id</code> equals or smaller than <code>0</code>
	 */
	public Node(int id,Point pos) throws NullPointerException,IllegalArgumentException{
		if(pos==null){
			throw new NullPointerException();
		}
		else if (id<=0){
			throw new IllegalArgumentException();
		}
		this.id = id;
		this.position = pos;
	}
	
	/**
	 * This constructor set the position to Point(0,0).
	 * @param id identifier of the new node
	 * @throws IllegalArgumentException if the <code>id</code> equals or smaller than <code>0</code>
	 */
	public Node(int id) throws IllegalArgumentException{
		if (id<=0){
			throw new IllegalArgumentException();
		}
		this.id = id;
		this.position = new Point(0,0);
	}
	
	public int getId() {
		return id;
	}

	/**
	 * @param id new identifier for this node
	 * @throws IllegalArgumentException if the <code>id</code> equals or smaller than <code>0</code>
	 */
	public void setId(int id) throws IllegalArgumentException {
		if(id<=0){
			throw new IllegalArgumentException();
		}
		this.id = id;
	}

	public Point getPosition() {
		return position;
	}

	/**
	 * @param position new position for this node
	 * @throws NullPointerException if the argument is <code>null</code>
	 */
	public void setPosition(Point position) throws NullPointerException{
		if (position==null){
			throw new NullPointerException();
		}
		this.position = position;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Node node) throws NullPointerException {
		if(node==null){
			throw new NullPointerException();
		}
		if (node.getId()==this.id){
			return 0;
		}
		else if (node.getId()>this.id){
			return -1;
		}
		else{
			return 1;
		}
	}	
	
	/**
	 * @param obj object to compare
	 * @returns <code>true</code> if the specified object is equal to this {@link Node} with an identifier
	 * @throws NullPointerException if <code>obj</code> is <code>null</code>
	 */
	@Override
	public boolean equals(Object obj) throws NullPointerException{
		if (obj==null){
			throw new NullPointerException();
		}
		Node node = (Node)obj;
		if (node.compareTo(this)==0){
			return true;
		}
		else{
			return false;
		}
	}
}

