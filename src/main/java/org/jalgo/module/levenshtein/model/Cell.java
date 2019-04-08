package org.jalgo.module.levenshtein.model;


public class Cell {

	public static int INDELETION = 1;
	public static int ININSERTION = 2;
	public static int INSUBSTITUTION = 4;
	public static int INIDENTITY = 8;
	public static int OUTDELETION = 16;
	public static int OUTINSERTION = 32;
	public static int OUTSUBSTITUTION = 64;
	public static int OUTIDENTITY = 128;
	
	
	// the value of the cell
	private int value;
	
	// how do we get in and out of the cell
	private int inAndOut;
	
	public Cell(int value)
	{
		this(value, 0);
	}
	
	public Cell(int value, int inAndOut) {
		this.value = value;
		this.inAndOut = inAndOut;
	}
	
	public int getValue() {
		return value;
	}
	
	public void addDirection(int d) {
		inAndOut |= d;
	}
	
	public boolean inDeletion() {
		return (inAndOut & INDELETION) == INDELETION;
	}
	
	public boolean inInsertion() {
		return (inAndOut & ININSERTION) == ININSERTION;
	}
	
	public boolean inSubstitution() {
		return (inAndOut & INSUBSTITUTION) == INSUBSTITUTION;
	}
	
	public boolean inIdentity() {
		return (inAndOut & INIDENTITY) == INIDENTITY;
	}
	
	public boolean outDeletion() {
		return (inAndOut & OUTDELETION) == OUTDELETION;
	}
	
	public boolean outInsertion() {
		return (inAndOut & OUTINSERTION) == OUTINSERTION;
	}
	
	public boolean outSubstitution() {
		return (inAndOut & OUTSUBSTITUTION) == OUTSUBSTITUTION;
	}
	
	public boolean outIdentity() {
		return (inAndOut & OUTIDENTITY) == OUTIDENTITY;
	}
}
