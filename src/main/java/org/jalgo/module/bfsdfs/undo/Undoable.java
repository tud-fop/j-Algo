/**
 * class: 			Undoable - an abstract class to make the undo-redo functionality possible
 * 						for the classes "Algo" and "GraphController"
 * creation date: 	08.05.09
 * completion date:	26.05.09
 * author: 			Ephraim Zimmer
 */

package org.jalgo.module.bfsdfs.undo;

import java.util.ArrayList;
import java.util.LinkedList;

public abstract class Undoable { 
	// LinedList as implementation of the interface Queue
	private LinkedList<Step> undoableSteps;
	// ArrayList to have index-assisted access
	private ArrayList<UndoableObserver> myUndoableObservers;
	// If a step is revoked, it should not be removed from the list "undoableSteps" to
	// make a redo possible, but the next step has to be available to be revoked.
	private int actualStepIndex;
	
	//private Logger logger;
	
	public Undoable() {
		// initialize the attributes
		
		this.undoableSteps = new LinkedList<Step>();
		this.myUndoableObservers = new ArrayList<UndoableObserver>(); 	
		// as there is no actual step available
		this.actualStepIndex = -1;
		
		//logger = Logger.getLogger(this.getClass().getName());
	}
	
	
	/**
	 * Adds a step to the queue of undoable steps, and executes it.
	 * @param step
	 */
	protected void makeUndoableStep(Step step) {
				
		if (step != null) {
			
			// execution of the added step
			step.execute();

			// increment the index.
			actualStepIndex++;
			
			if (actualStepIndex == undoableSteps.size()) {
				undoableSteps.add(actualStepIndex, step);
				
			} else if (actualStepIndex < undoableSteps.size()){ 
				// means a step has been done after one or more undo-actions
				undoableSteps.set(actualStepIndex, step);
				int j = (undoableSteps.size()-1) - actualStepIndex;
				// all steps which were undone have to be deleted to avoid a redo-action
				for (int i = 0; i < j; i++) {
					undoableSteps.remove(actualStepIndex + 1);
				}
				
				// update all ovservers for redo is disabled
				for (UndoableObserver uo : myUndoableObservers) {
					uo.onRedoDisabled();
					//logger.info("onRedoDisabled");
				}
				
			}
			
			// update all ovservers for undo is enabled
			if (actualStepIndex == 0) {
				for (UndoableObserver uo : myUndoableObservers) {
					uo.onUndoEnabled();
					//logger.info("onUndoEnabled");
				}
			}
			
		} else {
			// Exception-Treatment
			//logger.info("The argument \"step\" is null.");
		}
	}
	
	/**
	 * Executes a step, which should not be undoable
	 * @param step
	 */
	public void makeStep(Step step) {
		// if the first, already awailable step shouldn't be undoable
		step.execute();
	}
	
	/**
	 * Revokes a step.
	 */
	public void undo() {
		// by calling the method "undo()" of the last added Step of 
		// the queue "undoableSteps" and decrements the index.
		//logger.info("undo-action");
		if (isUndoPossible()) {
			undoableSteps.get(actualStepIndex).undo();
			actualStepIndex--;
			
			// update all ovservers for redo is enabled
			if (actualStepIndex == undoableSteps.size()-2) {
				for (UndoableObserver uo : myUndoableObservers) {
					uo.onRedoEnabled();
					//logger.info("onRedoEnabled");
				}
			}
			
			if (actualStepIndex == -1) {
				
				// update all ovservers for undo is disabled
				for (UndoableObserver uo : myUndoableObservers) {
					uo.onUndoDisabled();
					//logger.info("onUndoDisabled");
				}
				
			}
		}
	}
	
	/**
	 * Redoes the pre-revoked step.
	 */
	public void redo() {
		// after incrementing the index to choose the right one
		// from the list "undoableSteps".
		//logger.info("redo-action");
		if (isRedoPossible()) {
			actualStepIndex++;
			undoableSteps.get(actualStepIndex).execute();
			
			// update all ovservers for undo is enabled
			if (actualStepIndex == 0)
			for (UndoableObserver uo : myUndoableObservers) {
				uo.onUndoEnabled();
				//logger.info("onUndoEnabled");
			}
			
		}
		
		if (actualStepIndex >= undoableSteps.size()-1) {
			
			// update all ovservers for redo is disabled
			for (UndoableObserver uo : myUndoableObservers) {
				uo.onRedoDisabled();
				//logger.info("onRedoDisabled");
			}
			
		}
	}
	
	/**
	 * Revokes all steps done so far, with the ability to redo them.
	 */
	public void undoAll() {
		
		if (isUndoPossible()) {
			
			for (; actualStepIndex >= 0; actualStepIndex--) {
				undoableSteps.get(actualStepIndex).undo();
			}
			
			// update all ovservers for undo is disabled
			for (UndoableObserver uo : myUndoableObservers) {
				uo.onUndoDisabled();
				//logger.info("onUndoDisabled");
			}
			
			// update all ovservers for redo is enabled
			for (UndoableObserver uo : myUndoableObservers) {
				uo.onRedoEnabled();
				//logger.info("onRedoEnabled");
			}
		}
//		
//		while (isUndoPossible()) {
//			undo();
//		}
		
	}
	
	/**
	 * Returns true, if a redo is allowed.
	 * @return boolean
	 */
	public boolean isRedoPossible() {		
		return (actualStepIndex < undoableSteps.size()-1);
	}
	
	/**
	 * Returns true, if an undo is allowed.
	 * @return boolean
	 */
	public boolean isUndoPossible() {	
		return (actualStepIndex >= 0);
	}
	
	/**
	 * Adds an UndoableOvserver.
	 */
	public void addUndoableObserver(UndoableObserver uo) {
		// to the list "myUndoableObservers".
		
		this.myUndoableObservers.add(uo);
	}
	
	/**
	 * Removes an UndoableObserver.
	 */
	public void removeUndoableObserver(UndoableObserver uo) {
		// from the list "myUndoableObservers"
		
		this.myUndoableObservers.remove(uo);
	}
	
	
	/**
	 * @return the undoableSteps
	 */
	public LinkedList<Step> getUndoableSteps() {
		return undoableSteps;
	}


	/**
	 * @return the myUndoableObservers
	 */
	public ArrayList<UndoableObserver> getMyUndoableObservers() {
		return myUndoableObservers;
	}


	/**
	 * @return the actualStepIndex
	 */
	public int getActualStepIndex() {
		return actualStepIndex;
	}
}
