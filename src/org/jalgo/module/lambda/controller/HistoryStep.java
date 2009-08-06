/**
 * Class Step
 * 
 * @author Tobias Reiher
 * @version 1.0
 */
package org.jalgo.module.lambda.controller;

public class HistoryStep {

	private String term;
	private String operation;
	private int stepNumber;
	
	/**
	 * initiates a step
	 * 
	 * @param t term
	 * @param o applied operation
	 * @param n number of steps
	 */
	public HistoryStep(String t, String o, int n) {
		term = t;
		operation = o;
		stepNumber = n;
	}

	/**
	 * returns the term
	 * 
	 * @return term
	 */
	public String getTerm() {
		return term;
	}

	/**
	 * returns the applied operation
	 * 
	 * @return applied operation
	 */
	public String getOperation() {
		return operation;
	}

	/**
	 * returns the number of step
	 * 
	 * @return number of step
	 */
	public int getStepNumber() {
		return stepNumber;
	}
	
}
