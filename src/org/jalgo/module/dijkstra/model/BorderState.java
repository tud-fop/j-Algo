/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer science. It is written in Java and platform independent. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

// $Id$

package org.jalgo.module.dijkstra.model;

import java.util.ArrayList;
import java.util.Iterator;



/**
 * <code>BorderState</code> is a container that associates a chosen
 * node with a border list.
 *
 * @see DijkstraAlgorithm
 * @see State
 * @author <a href="mailto:der_julian@web.de">Julian Stecklina</a>
 */
public class BorderState implements Cloneable {
	
	private ArrayList border;
	private Node chosen;
	private Graph graph;

    /**
     * @return Returns the graph.
     */
    public Graph getGraph() {
        return graph;
    }
	/**
	 * Gets the Border as ArrayList of {@link Edge}s.
	 *  
	 * @return the Border value.
	 */
	public ArrayList getBorder() {
		return border;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
	    ArrayList<Object> newBorder = new ArrayList<Object>(border.size());
	    Iterator it = border.iterator();
	    
	    while (it.hasNext()) {
	        newBorder.add(it.next());
	    }
	    
	    return new BorderState(chosen.clone(), newBorder, graph);
	}

	/**
	 * Gets the chosen Node.
	 * 
	 * @return the Node
	 */
	public Node getChosen() {
		return chosen;
	}
	

	/** Creates a BorderState object with the given contents.
	 * @param chosen the chosen node
	 * @param border an ArrayList of edges.
	 */
	BorderState(Node chosen, ArrayList border, Graph graph) {
		this.border = border;
		this.chosen = chosen;
		this.graph = graph;
	}
}
