/**
 * Class CTerm
 * 
 * @author Tobias Reiher
 * @version 1.0
 */
package org.jalgo.module.lambda.controller;

import java.util.ArrayList;
import java.util.List;

public class HistoryTerm {
	
	private List<HistoryStep> list;
	
	public HistoryTerm() {
		list = new ArrayList<HistoryStep>();
	}
	
	/**
	 * appends the step to the end of this list
	 * 
	 * @param s step to be appended to this list
	 */	
	public void add(HistoryStep s) {
		list.add(s);
	}	
	
	/**
	 * removes the step at the specified position in this list
	 * 
	 * @param index index of the step to be removed
	 */	
	public void remove(int index) {
		list.remove(index);
	}

	/**
	 * returns the step at the specified position in this list
	 * 
	 * @param index index of the step to return
	 * @return step at the specified position in this list
	 */
	public HistoryStep get(int index) {
		return list.get(index);
	}

	/**
	 * sets a view of the portion of this list between the specified 
	 * fromIndex, inclusive, and toIndex, exclusive
	 * 
	 * @param fromIndex low endpoint (inclusive) of the subList
	 * @param toIndex high endpoint (exclusive) of the subList
	 */
	public void setSubList(int fromIndex, int toIndex) {
		list = list.subList(fromIndex, toIndex);		
	}
	
	/**
	 * returns the number of steps in this list
	 * 
	 * @return number of steps in this list
	 */
	public int size() {
		return list.size();
	}	
	
	/**
	 * returns true if this list contains no steps
	 * 
	 * @return true if this list contains no steps
	 */
	public boolean isEmpty() {
		return list.isEmpty();
	}
	
}
