/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */

/*
 * Created on 21.05.2005 $Id$
 */
package org.jalgo.module.dijkstra.model;

import java.util.ArrayList;

import org.jalgo.module.dijkstra.gui.Controller;

/**
 * @author Julian Stecklina
 * 
 */
public class State {

	private boolean isMacro;
	private Graph graph;
	private StringBuilder descHTMLCode;

	private ArrayList borderStates;

	/**
	 * Returns the list of {@link BorderState} objects.
	 * 
	 * @return border state objects
	 */
	public ArrayList getBorderStates() {
		return borderStates;
	}

	/**
	 * Returns the path from the given node back to the start node.
	 * 
	 * @return The list of nodes in the right order. (Start node is the first)
	 */
	public ArrayList getPath(Node node) {
		ArrayList<Node> nodes = new ArrayList<Node>();

		for (Node n = node; n != null; n = node.getPredecessor()) {
			// Wow, this is wasteful. :)
			nodes.add(0, n);
		}

		return nodes;
	}

	/**
	 * Appends descr to the end of the current description and set appropriate
	 * styles.
	 * 
	 * @param descr
	 */
	public void addStyledDescription(String descr) {
		descHTMLCode.append("<font color=C80A0A>").append(descr).append("</font>");
	}

	public void addDescription(String descr) {
		descHTMLCode.append(descr);
	}

	/**
	 * @return a coloured version of the decription
	 */
	public String getDescriptionEx() {
		StringBuilder htmlCode = new StringBuilder(descHTMLCode);
		htmlCode.append("</font></b></html>");
		return htmlCode.toString();
	}

	/**
	 * Creates a state object with the given parameters. This constructor does
	 * copy the passed graph. Care must be taken, that only structures are
	 * passed that are never changed again.
	 * 
	 * @param graph The graph
	 * @param macro
	 */
	public State(Graph graph, String descr, boolean macro,
		ArrayList borderStates) {
		this.graph = graph;
		for (Node node : graph.getNodeList()) {
			node.getVisual().updateModel(node);
			node.getVisual().setControllerMode(Controller.MODE_ALGORITHM);
			node.getVisual().update();
		}
		for (Edge edge : graph.getEdgeList()) {
			edge.getVisual().updateModel(edge);
			edge.getVisual().setControllerMode(Controller.MODE_ALGORITHM);
			edge.getVisual().update();
		}

		descHTMLCode = new StringBuilder("<html><b><font size=+1>");
		descHTMLCode.append(descr);
		this.isMacro = macro;
		this.borderStates = borderStates;
	}

	/**
	 * Creates a new state object without discription.
	 * 
	 * @see #State(Graph,String,boolean,ArrayList)
	 */
	public State(Graph graph, boolean macro, ArrayList borderStates) {
		this(graph, "", macro, borderStates); //$NON-NLS-1$
	}

	/**
	 * @return Returns the isMacro.
	 */
	public boolean isMacro() {
		return isMacro;
	}

	/**
	 * Sets the macro property of this state.
	 * 
	 * @param isMacro The value to set.
	 */
	public void setMacro(boolean isMacro) {
		this.isMacro = isMacro;
	}

	/**
	 * @return Returns the graph.
	 */
	public Graph getGraph() {
		return graph;
	}
}