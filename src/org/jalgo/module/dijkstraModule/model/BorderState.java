// $Id: BorderState.java,v 1.1 2005/06/23 10:08:27 jalgosequoia Exp $

package org.jalgo.module.dijkstraModule.model;

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
	 * Gets the Border as ArrayList of {@link edge}s.
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
	    ArrayList newBorder = new ArrayList(border.size());
	    Iterator it = border.iterator();
	    
	    while (it.hasNext()) {
	        newBorder.add(it.next());
	    }
	    
	    return new BorderState((Node)chosen.clone(), newBorder, graph);
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
