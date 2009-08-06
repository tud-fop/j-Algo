/**
 * Class TermHistory
 * 
 * @author Tobias Reiher
 * @version 1.0
 */
package org.jalgo.module.lambda.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class TermHistory extends Observable {

	private List<HistoryTerm> termList;
	private int currentTerm;
	private int currentStep;

	/**
	 * initializes attributes and add observer
	 * 
	 * @param o observer
	 */
	public TermHistory(Observer o) {
		this.addObserver(o);
		termList = new LinkedList<HistoryTerm>();
		currentTerm = -1;
		currentStep = -1;
	}

	/**
	 * creates a new empty HistoryTerm and deletes all further steps
	 */
	public void createTerm() {
		if (currentTerm > -1) {
			termList.get(currentTerm).setSubList(0, currentStep+1);
			termList = termList.subList(0, currentTerm+1);
		}
		if (currentTerm == -1 || !termList.get(currentTerm).isEmpty()) {
			currentStep = -1;
			termList.add(new HistoryTerm());
			currentTerm++;
		}
	}

	/**
	 * adds step to current term
	 * 
	 * @param term term
	 * @param operation operation as unicode character
	 * @param i step number
	 */
	public void addStep(String term, String operation, int i) {
		termList.get(currentTerm).setSubList(0, currentStep+1);
		termList = termList.subList(0, currentTerm+1);
		termList.get(currentTerm).add(new HistoryStep(term, operation, getCurrentStepNumber()+i));
		currentStep++;
		setChanged();
		notifyObservers();
	}

	/**
	 * @return true if undo step is possible
	 */
	public boolean isUndoStepPossible() {
		if (currentTerm > 0 || currentStep > 0)
			return true;
		return false;
	}

	/**
	 * @return true if redo step is possible
	 */
	public boolean isRedoStepPossible() {
		if (termList.size() > 0 && (currentTerm < termList.size()-1 || currentStep < termList.get(currentTerm).size()-1))
			return true;
		return false;
	}

	/**
	 * undo last step
	 */
	public void undoStep() {
		currentStep--;
		if (currentStep < 0) {
			currentTerm--;
			currentStep = termList.get(currentTerm).size()-1;
		}
		setChanged();
		notifyObservers(termList.get(currentTerm).get(0).getTerm());
	}

	/**
	 * undo all steps
	 */
	public void undoAllSteps() {
		currentTerm = 0;
		currentStep = 0;
		setChanged();
		notifyObservers(termList.get(currentTerm).get(0).getTerm());
	}

	/** 
	 * redo step
	 */
	public void redoStep() {
		currentStep++;
		if (currentStep >= termList.get(currentTerm).size()) {
			currentTerm++;
			currentStep = 0;
		}
		setChanged();
		notifyObservers(termList.get(currentTerm).get(0).getTerm());
	}

	/**
	 * redo all steps
	 */
	public void redoAllSteps() {
		currentTerm = termList.size()-1;
		currentStep = termList.get(currentTerm).size()-1;
		setChanged();
		notifyObservers(termList.get(currentTerm).get(0).getTerm());
	}

	/**
	 * returns a concatenated list of strings: performed operation as unicode character + term
	 * 
	 * @return list of HistoryTerm steps from first to current step, null if current step is lower equal 0
	 */
	public List<String> getCurrentTermSteps() {
		List<String> list = new LinkedList<String>();
		if (currentStep <= 0) {
			return null;
		}
		for (int i = 0; i <= currentStep-1; i++) {
			list.add(termList.get(currentTerm).get(i).getOperation() + termList.get(currentTerm).get(i).getTerm());
		}
		return list;
	}

	/** 
	 * @return number of current step
	 */
	public int getCurrentStepNumber() {
		if (currentTerm >= 0 && currentStep >= 0)
			return termList.get(currentTerm).get(currentStep).getStepNumber();
		return 0;
	}

	/** 
	 * @return term of the first step of the current term
	 */
	public String getFirstTerm() {
		return termList.get(currentTerm).get(0).getTerm();
	}

	/** 
	 * @return term of the current step of the current term
	 */
	public String getCurrentTerm() {
		return termList.get(currentTerm).get(currentStep).getTerm();
	}

	/** 
	 * @return operation of the current step of the current term
	 */
	public String getCurrentOperation() {
		return termList.get(currentTerm).get(currentStep).getOperation();
	}

}
