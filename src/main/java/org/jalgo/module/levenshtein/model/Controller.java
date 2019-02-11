package org.jalgo.module.levenshtein.model;

import java.util.ArrayList;
import java.util.List;

public class Controller {

	private String source;
	private String target;
	
	private int deletion;
	private int insertion;
	private int substitution;
	private int identity;
	
	private Cell[][] levenshteinArray;
	
	/**
	 * initializes the controller
	 * @param source, the source word
	 * @param target, the target word
	 * @param deletion, the costs for deleting a character
	 * @param insertion, the costs for inserting a character
	 * @param substitution, the costs for substituting a character
	 * @param identity, the costs for doing nothing with the character
	 */
	public void init(
			String source    , 
			String target    ,
			int deletion     ,
			int insertion    ,
			int substitution ,
			int identity     ) 
	{
		this.source = source;
		this.target = target;
		
		this.deletion = deletion;
		this.insertion = insertion;
		this.substitution = substitution;
		this.identity = identity;
		
		// initialize the array
		levenshteinArray = new Cell[source.length()+1][target.length()+1];
		
		// fill the array
		fillArray();
	}
	
	/**
	 * fills the array with levenshtein values
	 */
	private void fillArray() {
		// creating the first row and column
		levenshteinArray[0][0] = new Cell(0);
		
		for (int j = 1; j <= source.length(); j++) {
			levenshteinArray[j][0] = new Cell(j, Cell.INDELETION);
			levenshteinArray[j-1][0].addDirection(Cell.OUTDELETION);
		}
		
		for (int i = 1; i <= target.length(); i++) {
			levenshteinArray[0][i] = new Cell(i, Cell.ININSERTION);
			levenshteinArray[0][i-1].addDirection(Cell.OUTINSERTION);
		}
		
		
		// filling the rest of the array
		for (int j = 1; j <= source.length(); j++) {
			for (int i = 1; i <= target.length(); i++) {
				// compare the two current character of the source and target word
				// with each other
				boolean sameChar = source.charAt(j-1) == target.charAt(i-1);
				
				// calculate the different values for deletion, insertion, 
				// substitution and identity
				int del = levenshteinArray[j-1][i].getValue() + deletion;
				int ins = levenshteinArray[j][i-1].getValue() + insertion;
				int subs = levenshteinArray[j-1][i-1].getValue() + substitution;
				int id = levenshteinArray[j-1][i-1].getValue() + identity;
				
				// calculate the minimum value
				int min = Math.min(del, ins);
				// check if it is possible to use identity
				if (sameChar) {
					min = Math.min(min, id);
				} else {
					min = Math.min(min, subs);
				}
				
				// collect the directions via which we can reach the current cell
				// and add the directions as outDirections to the according cell
				int inDirections = 0;
				
				if (del == min) {
					inDirections |= Cell.INDELETION;
					levenshteinArray[j-1][i].addDirection(Cell.OUTDELETION);
				}
				if (ins == min) {
					inDirections |= Cell.ININSERTION;
					levenshteinArray[j][i-1].addDirection(Cell.OUTINSERTION);
				}
				if (sameChar && id == min) {
					inDirections |= Cell.INIDENTITY;
					levenshteinArray[j-1][i-1].addDirection(Cell.OUTIDENTITY);
				}
				if (!sameChar && subs == min) {
					inDirections |= Cell.INSUBSTITUTION;
					levenshteinArray[j-1][i-1].addDirection(Cell.OUTSUBSTITUTION);
				}
				
				// create the new cell and save it in the array
				levenshteinArray[j][i] = new Cell(min, inDirections);
			}
		}
	}
	
	/**
	 * 
	 * @return the levenshtein array
	 */
	public Cell[][] getLevenshteinArray() {
		return levenshteinArray;
	}
	
	/**
	 * 
	 * @param j, the index of the source word
	 * @param i, the index of the target word
	 * @return the cell in the levenshtein array
	 */
	public Cell getCell(int j, int i) {	
		return levenshteinArray[j][i];
	}
	
	/**
	 * calculates the alignments with target cell (j,i)
	 * @param j, the index of the source word
	 * @param i, the index of the target word
	 * @return the alignments
	 */
	public List<List<Action>> getAlignments(int j, int i) {
		return getAlignments(0, 0, j, i);
	}
	
	/**
	 * calculates the alignments from cell (startJ,startI) to cell (targetJ,targetI)
	 * @param startJ, the start index of the source word
	 * @param startI, the start index of the target word
	 * @param targetJ, the target index of the source word
	 * @param targetI, the target index of the target word
	 * @return
	 */
	private List<List<Action>> getAlignments(int startJ, int startI, 
			int targetJ, int targetI) {
		
		if (startJ < 0 || startJ > source.length() ||
				startI < 0 || startI > target.length() ||
				targetJ < 0 || targetJ > source.length() ||
				targetI < 0 || targetI > target.length()) {
			throw new ArrayIndexOutOfBoundsException();
		}
		
		// create a list with all the result alignments
		List<List<Action>> result = new ArrayList<List<Action>>();
		
		// if we reached the target, we add an empty list, since all possible
		// alignments are only one empty alignment
		if(startJ == targetJ && startI == targetI) {
			result.add(new ArrayList<Action>());
			return result;
		}
			
		Cell cell = getCell(startJ, startI);
		
		// checking if we can go further by deleting a character
		if (startJ < targetJ && cell.outDeletion()) {
			// recursively get all alignments from the next indices
			List<List<Action>> deletions = 
					getAlignments(startJ+1,startI,targetJ,targetI);
			
			// add the deletion step to the alignment
			for(List<Action> alignment : deletions) {
				alignment.add(0, Action.DELETION);
			}
			
			// add all the alignments to the result list
			result.addAll(deletions);
		}
		
		// checking if we can go further by inserting a character
		if (startI < targetI && cell.outInsertion()) {
			// recursively get all alignments from the next indices
			List<List<Action>> insertions =
					getAlignments(startJ, startI+1, targetJ, targetI);
			
			// add the insertion step to the alignments
			for(List<Action> alignment: insertions) {
				alignment.add(0, Action.INSERTION);
			}
			
			// add all the alignments to the result list
			result.addAll(insertions);
		}
		
		// checking if we can go further using substitution or the identity
		if (startJ < targetJ && startI < targetI && 
				(cell.outIdentity() || cell.outSubstitution())) {
			// deciding if its substitution or identity
			Action action = cell.outIdentity() ? 
					Action.IDENTITY : Action.SUBSTITUTION;
			
			// recursively get all alignments from the next indices
			List<List<Action>> substitutions = 
					getAlignments(startJ+1, startI+1, targetJ, targetI);
			
			// add the determined action to the alignments
			for(List<Action> alignment : substitutions) {
				alignment.add(0, action);
			}
			
			// add all the alignments to the result list
			result.addAll(substitutions);
		}
		
		return result;
	}
	
	public int getDeletion() {
		return deletion;
	}
	
	public int getInsertion() {
		return insertion;
	}
	
	public int getSubstitution() {
		return substitution;
	}
	
	public int getIdentity() {
		return identity;
	}
	
	public boolean sameCharAt(int j, int i) {
		return source.charAt(j-1) == target.charAt(i-1);
	}
}
