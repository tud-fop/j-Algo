/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
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

/*
 * Created on 21.05.2005
 * $Id: DijkstraAlgorithm.java,v 1.2 2005/06/27 20:59:51 mischi Exp $
 */
package org.jalgo.module.dijkstraModule.model;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.swt.graphics.Color;
import org.jalgo.module.dijkstraModule.gui.ColorFactory;

/**
 * @author Julian Stecklina
 */

public class DijkstraAlgorithm {

	/**
	 * <code>INFINITY</code> approximates the numerical infinity. :-)
	 * Special care should be taken not to present this value to the user.
	 */
	public static final int INFINITY = -1;
	
	private ColorFactory colorFactory; 
	
	/**
	 * <code>INVALID</code> means that there is no next index.
	 */
	public static final int INVALID = -1;
	
	private Color color(int r, int g, int b) {
	    return colorFactory.makeColor(r,g,b);
	}
	
	private void addNodeReference(State state, Node node) {
	    state.addStyledDescription("Knoten "+node.getIndex(),
	            					colorFactory.makeColor(200,10,10),
	            					null);
	}
	
	private interface NodeClosure {
		void value(Node node);
	}
	
	private interface EdgeClosure {
		void value(Edge edge);
	}
	
    private Graph graph;
    private Node startNode;
    
    private State currentState;
    private ArrayList stateList;

    private ArrayList borderStates = new ArrayList();

    private Node currentlyChosen;
    
    // I miss optional and keyword parameters... and _real_ closures...
    private void doAllNodes(NodeClosure closure) {
    	ArrayList nodes = graph.getNodeList();
    	
    	doAllNodes(closure,nodes);
    }
    
    /**
     * Something like Lisp's mapc for Java. Maps over all nodes in the given
     * ArrayList.
     * 
     * @param closure
     * @param nodes
     */
    private void doAllNodes(NodeClosure closure, ArrayList nodes) {
    	int len = nodes.size();
    	
    	for (int i = 0; i<len; i++) {
    		closure.value((Node)nodes.get(i));
    	}
    }
    
    /**
     * Maps over all edges in the given array.
     * 
     * @param closure
     * @param edges
     */
    private void doAllEdges(EdgeClosure closure, ArrayList edges) {
    	int len = edges.size();
    	
    	for (int i = 0;i<len;i++) {
    		closure.value((Edge)edges.get(i));
    	}
    }
    
    
    /** Same as doAllEdges with explicit ArrayList parameter, but defaults to
     * all edges. 
     * 
     * @param closure
     */
    private void doAllEdges(EdgeClosure closure) {
    	ArrayList edges = graph.getEdgeList();
    	doAllEdges(closure,edges);
    }
    
    /**
     * @param index The state to go to.
     */
    public void gotoState(int index) {
        currentState = (State)stateList.get(index);
    }
    
    /**
     * Return the current count of states
     */ 
    public int getStateCount()
    {
        return (stateList == null)? 0: stateList.size();
    }
    
  
    
    public State getCurrentState() {
        return currentState;
    }
    
    private State createState(ArrayList border, String descr, boolean macro) {
        return createState(border,descr,macro,null);
    }
    
    private State createState(ArrayList border, String descr, boolean macro, Node activeNode) {
        return createState(border,descr,macro, activeNode, null);
    }
    private State createState(ArrayList border, String descr, boolean macro, Node activeNode, Edge skipEdge) {
    	// Update the border flag
        Graph ngraph = (Graph)graph.clone();
        Iterator it = ngraph.getNodeList().iterator();
        
        
        while (it.hasNext()) {
            Node node = (Node)it.next();
            Node oldNode = graph.findNode(node.getIndex());
            node.setBorder(border.contains(oldNode));
            if ((activeNode == oldNode) ||
                (currentlyChosen == oldNode))
                node.setActive(true);
                
        }
    	
        ArrayList edges = ngraph.getEdgeList();
        Edge nskipEdge = (skipEdge != null) ? ngraph.findEdge(ngraph.findNode(skipEdge.getStartNodeIndex()),
                						 	                  ngraph.findNode(skipEdge.getEndNodeIndex()))
                						 	: null;
    	// Now we need to propagate all flags to the approprate nodes.
        it = ngraph.getNodeList().iterator();
        while (it.hasNext()) {
            Node node = (Node)it.next();
            Node pred = node.getPredecessor();
            // If we have a predeccessor, find the appropriate edge
            if (pred != null) {
                Edge edge = ngraph.findEdge(node,pred);
                // and set the right flags.
                if (edge != nskipEdge) {
                    edge.setFlags(node.getFlags());
                    edge.setReversed(pred != edge.getStartNode());
                }
            }
        }
    	
        State newState = new State(ngraph, descr, macro, (ArrayList)borderStates.clone());
    	stateList.add(newState);
    	
    	return newState;
    }
    
    /**
     * @param border The border represented as ArrayList
     * @param node The node from which we complete the border.
     */
    private void completeBorder(ArrayList border, Node node) {
        Iterator it = graph.getEdgeList().iterator();
        
        border.remove(node);
        
        while (it.hasNext()) {
            Edge edge = (Edge)it.next();
    		Node from = edge.getStartNode();
    		Node to = edge.getEndNode();
    		Node other;
    			
    		// Check whether we got an edge that belongs to node.
    		// other will be set to the 'other' node.
    		if (from == node) {
    		    other = to;
    		} else if (to == node) {
    		    other = from;
    		} else continue;
    			
    			
    		if (other.getChosen() == false) {
    		    if (other.getDistance() == INFINITY) {
    		        
    		    } else if ((node.getDistance() + edge.getWeight())
    		            	< other.getDistance()) {
    		        // Conflict case ?!    		        
    		        other.setConflict(true);
    		        // edge.setConflict(true);
    		        edge.setActive(true);
    		        edge.setBorder(true);
    		        edge.setReversed(other == edge.getStartNode());
    		        State c1 = createState(border, "", false, other,edge);
    		        addNodeReference(c1, other);
    		        c1.addDescription(" ist über ");
    		        addNodeReference(c1, node);
    		        c1.addDescription(" auf einem kürzeren Wege erreichbar.");
    		    
    		        edge.setActive(false);
    		        // edge.setConflict(false);
    		        edge.setBorder(false);
    		        other.setConflict(false);
    		        
    		    } else {
    		        // Conflict case, but nothing to do
    		        other.setConflict(true);
    		        // edge.setConflict(true);
    		        edge.setActive(true);
    		        edge.setBorder(true);
    		        edge.setReversed(other == edge.getStartNode());
    		        State c1 = createState(border, "", false, other,edge);
    		        
    		        addNodeReference(c1,other);
    		        c1.addDescription(" ist \u00FCber den bestehenden Weg k\u00FCrzer zu erreichen.");
    		        
    		        edge.setActive(false);
    		        // edge.setConflict(false);
    		        edge.setBorder(false);
    		        other.setConflict(false);
    		        continue;
    		    }
    		
    			// There is a shorter path to other via node
    		    other.setPredecessor(node);
    		    other.setDistance(node.getDistance() + edge.getWeight());
    		    if (border.contains(other)) {
    			} else border.add(other);

    		    // Create state
    		    State st1 = createState(border, "Neuer Knoten im Rand: ", false, other);
    		    addNodeReference(st1, other);
    		}
    	}
    }
    
    private ArrayList deepClone(ArrayList list) {
        Iterator it = list.iterator();
        ArrayList newList = new ArrayList(list.size());
        
        while (it.hasNext()) 
            newList.add(((Edge)it.next()).clone());
        
        return newList;
    }
 
    private void newBorderState(Node chosen, ArrayList border) {
	    // We want to pass EDGEs not NODEs here!
	    ArrayList borderEdges = new ArrayList();
	    Iterator it = border.iterator();

	    while (it.hasNext()) {
		    Node bnode = (Node) it.next();
		    Node pred = bnode.getPredecessor();
		    if (pred == null) {
			    throw new RuntimeException("Predecessor of border node is null");
		    }
		    if (bnode == pred) {
			    throw new RuntimeException("Node equals predecessor");
		    }
		    
		    Edge edge = graph.findEdge(pred,bnode);
		    borderEdges.add(edge);
	    }
	    
	    borderStates.add(new BorderState((Node)chosen.clone(),deepClone(borderEdges),(Graph)graph.clone()));
    }    

    /**
     * This is the implementation of Dijkstra's algorithm to find
     * the shortest paths in a graph as given in Vogler's script as
     * of 2003.
     * 
     * @param start the start node
     */
    public void generateStates(Node start) {
        // Tabula rasa
        
        stateList = new ArrayList();
        borderStates = new ArrayList();
        currentlyChosen = start;
        
        // Initial states
        start.setStart(true);
       
        // Initialization:
        // All nodes except start are not reachable.
        Iterator it = graph.getNodeList().iterator();
        while (it.hasNext()) {
            Node node = (Node)it.next();
        	if (node != start) {
    			node.setStart(false);
    			node.setPredecessor(null);
    			node.setChosen(false);
    			node.setDistance(INFINITY);        			
    		}
        }
        
        // start is chosen
        start.setPredecessor(null);
        start.setDistance(0);
        start.setChosen(true);
        
        // All nodes adjacent to start form the initial border.
        // We do not use the border flag of the nodes, because that 
        // would be unwieldy.
        ArrayList border = new ArrayList();
        currentState = createState(border, "", true, start);
        addNodeReference(currentState,start);
        currentState.addDescription(" als Startknoten gew\u00E4hlt.");
        
        completeBorder(border,start);
        newBorderState(start,border);
    	createState(border, "Randknotenmenge f\u00FCr Startknoten komplett.", true);
        
        // Compute paths from start
        while(!border.isEmpty()) {
        	// We need to make this an array because Java is braindead. 
        	final Node[] minimum = new Node[1];
        	minimum[0] = null;
        	
        	NodeClosure minimize = new NodeClosure() {

				public void value(Node node) {
					if ((minimum[0] == null) || 
						(minimum[0].getDistance()>node.getDistance())) {
						minimum[0] = node;
					}
					
				}        		
        	};
        	
        	// Find v with minimal distance
        	doAllNodes(minimize,border);
        	Node v = minimum[0];
        
        	v.setChosen(true);
        	currentlyChosen = v;
        	
        	State state1 = createState(border,"Neuer gew\u00E4hlter ", true, v);
        	addNodeReference(state1,v);
        	
        	completeBorder(border,v);
        	newBorderState(v,border);
        	
        	createState(border, "Randknotenmenge komplett", true);
        	currentlyChosen = null;
        	border.remove(v);
        }
        
        createState(border, "Algorithmus beendet",true);
        
        // dumpResult(start);
    }
    
    private void dumpResult(Node start) {
        Iterator it = graph.getNodeList().iterator();

        System.out.println("Dumping results for Node " + start.getIndex());
        
        while (it.hasNext()) {
            Node node = (Node)it.next();
            
            System.out.println("Node " + node.getIndex() + " distance = " + node.getDistance());
            for (Node pred = node; pred != null; pred = pred.getPredecessor())
                System.out.print("> Node " + pred.getIndex());
            System.out.println();
        }
    }
    
    /** This constructor creates a new DijkstraAlgorithm object that will
     * compute the shortest paths in the given graph.
     * 
     * @param graph The graph we want our algorithm to operate on.
     * @param fact The Algorithm needs a ColorFactory in order to create Color objects without a device.
     */
    public DijkstraAlgorithm(ColorFactory fact, Graph graph) {
        // Do we want to copy this graph?
        this.graph = graph;
        this.stateList = new ArrayList();
        this.colorFactory = fact;
    }
    
  
    /** Returns the index of the current state.
     * @return index of the current state
     */
    public int getCurrentStateIndex()
    {
        return stateList.indexOf(currentState);
    }
    
    /** Returns the index of the next macro step.
     * @return next index of macro state
     */
    public int getNextMacroStepIndex()
    {
        int pos = INVALID;
        
        for (int i = getCurrentStateIndex() + 1; i < stateList.size(); i++) {
            if (((State)stateList.get(i)).isMacro())
                return i;
        }
        return INVALID;
    }
   
    /** Returns the index of the previous macro step.
     * @return previous index of macro state
     */
    public int getPrevMacroStepIndex()
    {
        int pos = INVALID;
        
        for (int i = getCurrentStateIndex() - 1; i >= 0; i--) {
            if (((State)stateList.get(i)).isMacro())
                return i;
        }
        return INVALID;
    }

    /** Returns the index of the next state.
     * @return next index
     */
    public int getNextStepIndex()
    {
        int cur = getCurrentStateIndex();
        
        if (cur + 1 == stateList.size())
            return INVALID;
        else
            return getCurrentStateIndex() + 1;
    }
    
    /** Returns the index of the previous state.
     * @return previous index
     */
    public int getPrevStepIndex()
    {
        int cur = getCurrentStateIndex();
        
        return (cur <= 0) ? INVALID : cur - 1;
    }    
    
    /** Returns the start node.
      * @return start node
     */
    public Node getStartNode()
    {
        return startNode;
    }
}