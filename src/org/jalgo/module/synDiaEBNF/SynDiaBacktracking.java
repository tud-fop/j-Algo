/*
 * Created on 30.04.2004
 */
 
package org.jalgo.module.synDiaEBNF;

import java.io.Serializable;

/**
 * Basicclass to manage similar methods of the algorithmus to generate a Word or
 * to recognize a word by a syntax diagram system
 * 
 * @author Michael Pradel
 * @author Babett Schaliz
 * @author Benjamin Scholz
 */
public abstract class SynDiaBacktracking implements IAlgorithm, Serializable {

	/**
	* Constructor
	*/
	public SynDiaBacktracking() {
	}


   /**
   * @return 	true, if there is a previous element, so you can go a step back in history; 
   * 			false if not
   */
   public boolean hasPreviousHistStep() {
	   return true;
   }

	/**
	* @return 	true, if there is a next element, so you can go a step forward in history; 
	* 			false if not
	*/
	public boolean hasNextHistStep() {
		return true;
	}

	/**
	* @return 	true, if you can perform the next step in the algorithm, so you can call performNextStep()
	* 			false if not
	*/
	public boolean hasNextStep() {
		return true;
	}

	/**
	 * this method is called if the "do algorithm" button on the GUI is pushed
	 * realise the backtrackingAlgorithm
	 * has to find the next element in the syntactical diagram and...
	 * 
	 * @exception IndexOutOfBound   if there is now further step to go
	 */
	public void performNextStep() throws IndexOutOfBoundsException {
	}

	/**
	* this method is called if the forwardButton on the GUI is pushed
	* and should restore the next saved step of the visualisation
	* 
	* @exception IndexOutOfBound   if there is now previous step to go
	*/
	public void nextHistStep() throws IndexOutOfBoundsException {
	}

	/**
	* this method is called if the backwardButton on the GUI is pushed
	* and should restore the last saved step of the visualisation
	* 
	* @exception IndexOutOfBound   if there is now previous step to go
	*/
	public void previousHistStep() throws IndexOutOfBoundsException {
	}
	
}