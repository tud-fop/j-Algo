package org.jalgo.module.c0h0.models.flowchart;


/**
 * an edge which contains two vertices
 * @author Philipp
 *
 */
public class Edge extends Element{
	private Vertex from;
	private Vertex to;
	
	/**
	 * @param label
	 * @param from
	 * @param to
	 * @param style
	 */
	public Edge(String label, Vertex from, Vertex to, String style) {
		super(style, label);
		this.from = from;
		this.to = to;
	}

	/**
	 * returns the start vertex
	 * 
	 * @return the start vertex
	 */
	public Vertex getFrom() {
		return from;
	}

	/**
	 * returns the end vertex
	 * 
	 * @return the end vertex
	 */
	public Vertex getTo() {
		return to;
	}
	
	/**
	 * sets the start vertex
	 * @param from the start vertex
	 */
	public void setFrom(Vertex from) {
		this.from = from;
	}

	/**
	 * sets the end vertex
	 * @param to the end vertex
	 */
	public void setTo(Vertex to) {
		this.to = to;
	}
}
