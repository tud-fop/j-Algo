package org.jalgo.module.bfsdfs.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;

/**
 * This class represent the result graph of the algorithm.
 * It extends {@link ObservableGraph} by methods to calculate the distance to startNode.
 * @author Johannes Siegert
 *
 */
public class AlgoGraph extends ObservableGraph {
	
	private static final long serialVersionUID = 1309990567480132436L;
	
	private Collection<AlgoGraphObserver> algoGraphObservers = null;
	
	/**
	 * This map save the distance from a node to the startNode.
	 * The key is the identifier of the node and the value is the distance to startNode.
	 */
	private Map<Integer,Integer> distances = null;
	
	public AlgoGraph(){
		super();
		this.algoGraphObservers = new ArrayList<AlgoGraphObserver>();
		this.distances = new TreeMap<Integer,Integer>();
	}
	
	/**
	 * @param node identifier of the node which distance will be set
	 * @param distance distance from <code>node</code> to startNode
	 * @throws IllegalArgumentException if <code>node</code> or <code>distance</code> is equal or smaller than <code>0</code>
	 * @throws NoSuchElementException if the <code>node</code> not exists on observeable graph
	 */
	public void setNodeDistance(int node,int distance) 
	throws IllegalArgumentException, NoSuchElementException{
		if((node<=0)||(distance<0)){
			throw new IllegalArgumentException();
		} 
		else if(this.getNode(node)==null){
			throw new NoSuchElementException();
		}
		this.distances.put(node, distance);
		for (AlgoGraphObserver aGO:this.algoGraphObservers){
			aGO.onDistanceChanged(node, distance);
		}
	}
	
	/**
	 * @param node identifier of the node which distance will be searched
	 * @return distance from <code>node</code> to startNode if distance is set, or <code>infinite</code>
	 * @throws IllegalArgumentException if <code>node</code> is equal or smaller than <code>0</code>
	 */
	public int getNodeDistance(int node) throws IllegalArgumentException{
		if (node<=0){
			throw new IllegalArgumentException();
		}
		if ((distances == null) || (!(this.distances.containsKey(node)))) {
			return Integer.MAX_VALUE;
		}
		else {
			return this.distances.get(node);
		}
	}
	
	/**
	 * Add <code>algoGraphObserver</code> to <code>algoGraphObservers</code>.
	 @param algoGraphObserver the algoGraph observer which will be added.
	 * @throws NullPointerException if <code>algoGraphObserver</code> is <code>null</code>
	 */
	public void addAlgoGraphObserver(AlgoGraphObserver algoGraphObserver) 
	throws NullPointerException{
		if (algoGraphObserver==null){
			throw new NullPointerException();
		}
		this.addGraphObserver(algoGraphObserver);
		this.algoGraphObservers.add(algoGraphObserver);
	}
	
	/**
	 * Remove <code>algoGraphObserver</code> to <code>algoGraphObservers</code>.
	 * @param algoGraphObserver the algoGraph observer which will be removed.
	 * @throws NullPointerException if <code>algoGraphObserver</code> is <code>null</code>
	 */
	public void removeAlgoGraphObserver(AlgoGraphObserver algoGraphObserver) 
	throws NullPointerException{
		if (algoGraphObserver==null){
			throw new NullPointerException();
		}
		this.removeGraphObserver(algoGraphObserver);
		this.algoGraphObservers.remove(algoGraphObserver);
	}
	
	public void removeNode(int node){
		super.removeNode(node);
		this.distances.remove(node);
	}
	
	public void removeNodeWithAutomaticNodeIdAdaptation(int node){
		super.removeNodeWithAutomaticNodeIdAdaptation(node);
		this.distances.remove(node);
	}
}
