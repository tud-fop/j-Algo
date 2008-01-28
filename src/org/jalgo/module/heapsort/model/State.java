package org.jalgo.module.heapsort.model;

//import java.util.List;
import java.util.Map;

/**
 * The interface State is used to represent a state in the computation of the algorithm.
 * Runs can be constructed by using the method <code>computeSuccessors</code>. Note
 * that this restricts us to a finite branching factor, which excludes e. g. algorithms
 * guessing natural numbers.
 * 
 * @author mbue
 *
 */
public interface State {
	/**
	 * Returns the detail level of the state. The program will allow the user to automatically
	 * derive up to the next state which has at most a certain detail level.
	 * @return
	 */
	int getDetailLevel();
	
	/**
	 * Computes a list of successor states.
	 * 
	 * @return
	 */
	Map<State,Action> computeSuccessors();
}
