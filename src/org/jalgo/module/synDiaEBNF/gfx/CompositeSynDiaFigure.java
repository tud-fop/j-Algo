/*
 * Created on 15.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gfx;

import java.util.LinkedList;

/**
 * This class extends SynDiaFigure by adding some methods to work with composites. 
 * 
 * @author Marco Zimmerling
 */
public abstract class CompositeSynDiaFigure extends SynDiaFigure {

	protected int numOfInteriorFigures;																	// number of interior figures
	protected LinkedList interiorFigures = new LinkedList();									// figures inside 
	protected LinkedList connectionsToInteriorFigures = new LinkedList();		// connections from startFigure to interior ones
	protected LinkedList connectionsFromInteriorFigures = new LinkedList();	// connections from interior figures to endFigure
	
	// *** abstract methods ***
	
	/**
	 * Replaces oldFigure by newFigure if oldFigure is one of the interior figures. 
	 * 
	 * @param oldFigure 					figure to replace
	 * @param newFigure					figure instead of oldFigure
	 * @throws SynDiaException		oldFigure is not one of the interior figures
	 */
	public abstract void replace(SynDiaFigure oldFigure, SynDiaFigure newFigure) throws SynDiaException;
	
	/**
	 * Replaces the figure at the passed index (interiorFigures[index]) by newFigure in case of a valid index. 
	 * 
	 * @param newFigure					figure instead of interiorFigure[index]
	 * @param index							figure at this position will be replaced 
	 * @throws SynDiaException		oldFigure is not one of the interior figures
	 */
	public abstract void replace(SynDiaFigure newFigure, int index) throws SynDiaException;

	/**
	 * Removes interiorFigures[index].
	 *  
	 * @param index							figure at this position will be removed
	 * @throws SynDiaException		invalid index
	 */
	public abstract void remove(int index) throws SynDiaException;
	
	// *** implemented methods ***
	
	public void highlightConnectionTo(SynDiaFigure targetFigure, boolean selection) throws SynDiaException {
		
	}
	
	public void highlightConnectionTo(int index, boolean selection) throws SynDiaException {
		
	}
	
	public void highlightConnectionFrom(SynDiaFigure sourceFigure, boolean selection) throws SynDiaException {
		
	}
	
	public void highlightConnectionFrom(int index, boolean selection) throws SynDiaException {
		
	}
	
	/**
	 * This method is for laying out the children.
	 */
	public abstract void reposition();
	
	/*public void remove() throws SynDiaException {
		((CompositeSynDiaFigure)getParent()).remove(this);
	}*/
}
