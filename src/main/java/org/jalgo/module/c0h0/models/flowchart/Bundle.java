package org.jalgo.module.c0h0.models.flowchart;

import java.util.List;

/**
 * bundles edges and vertizes for each step
 * 
 * @author Philipp Geissler
 *
 */
public class Bundle {
	private List<Vertex> vertexes;
	private List<Edge> edges;
	
	private String address;

	/**
	 * @param vertexes
	 * @param edges
	 * @param address
	 */
	public Bundle(List<Vertex> vertexes, List<Edge> edges, String address) {
		this.vertexes = vertexes;
		this.edges = edges;
		this.address = address;
	}

	/**
	 * returns all vertices of one step
	 * @return the vertices
	 */
	public List<Vertex> getVertexes() {
		return vertexes;
	}

	/**
	 * returns all edges of one step
	 * @return the edges
	 */
	public List<Edge> getEdges() {
		return edges;
	}

	/**
	 * returns the address of the step
	 * 
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}
	
	
	
	
}
