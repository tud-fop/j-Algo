/**
 * class: 			ComplexStep - offers the ability to merge a number of steps to one step, which can
 * 						be undone and redone in common
 * creation date: 	28.05.09
 * completion date:	28.05.09
 * author: 			Ephraim Zimmer
 */

package org.jalgo.module.bfsdfs.controller;

import java.util.ArrayList;
import java.util.List;

import org.jalgo.module.bfsdfs.graph.ObservableGraph;
import org.jalgo.module.bfsdfs.undo.Step;

public class ComplexStep extends DesignStep {
	private List<Step> mySteps;
	
	public ComplexStep(ObservableGraph observableGraph, List<Step> steps)
			throws NullPointerException {		
		super(observableGraph);
		if (steps==null){
			throw new NullPointerException();
		}
		this.mySteps = steps;
	}
	
	public ComplexStep(ObservableGraph observableGraph)
	throws IllegalArgumentException {
		super(observableGraph);
		this.mySteps = new ArrayList<Step>();
	}
	
	/**
	 * Adds a step to the List of steps which should be merged as one complex step.
	 * @param step
	 * @return Boolean
	 * @throws NullPointerException if <code>step</code> is <code>null</code>
	 */
	public boolean addStep(Step step) throws NullPointerException {
		if (step == null) {
			throw new NullPointerException();
		}
		return mySteps.add(step);
	}
	
	/**
	 * Removes a step from the List of steps which should be merged as one complex step.
	 * @param step
	 * @return Boolean
	 * @throws NullPointerException if <code>step</code> is <code>null</code>
	 */
	public boolean removeStep(Step step) throws NullPointerException {
		if (step == null) {
			throw new NullPointerException();
		}
		return mySteps.remove(step);
	}
	
	/**
	 * Executes all the steps represented by this complex step.
	 */
	public void execute() {
		// the order of treatise is important; the first step in the list has to be executed first
		// e.g. deletion of a node with edges: firstly the removal of the edges, than the removal of the node
		for (Step s : mySteps) {
			s.execute();
		}
	}

	/**
	 * Undoes all the steps represented by this complex step.
	 */
	public void undo() {
		// the order of treatise is important; the last step of the list has to be undone first
		// e.g. undo of the deletion described above: firstly the recreation of the node, than the recreation of the edges
		for (int i = mySteps.size()-1; i >= 0; i--) {
			mySteps.get(i).undo();
		}
	}

}
