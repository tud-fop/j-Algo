package org.jalgo.module.app.core.dataType.naturalNumber;

import java.util.*;

import org.jalgo.module.app.core.dataType.*;

/**
 * Represents a natural number (an <code>int</code> >= 0).
 *
 */
public class NaturalNumber extends NumericDataType {

	private static final long serialVersionUID = 5634981613906809570L;

	public static Set<Operation> getOperations() {
		Set<Operation> operations;
		
		operations = new HashSet<Operation>();

		operations.add(new NaturalAdd());
		operations.add(new NaturalMultiply());
		operations.add(new NaturalMinimum());
		operations.add(new NaturalMaximum());
		
		return operations;
	}
	
	public static Operation getOperationByID(String id) {
		for (Operation op : getOperations()) {
			if (op.getID().equals(id))
				return op;
		}
		
		return null;
	}

	public static String getName() {
		return "Natural Number";
	}

	public static String[] getSymbolicRepresentation() {
		String[] returnValue = {"\u2115", "", "\u221e"};
		
		return returnValue;
	}

	private int value;

	/**
	 * Instantiates a new natural number.
	 */
	public NaturalNumber() {
		value = 0;
	}

	/**
	 * Instantiates a new natural number with the value <code>val</code>
	 * @param val the start value of the instance
	 */
	public NaturalNumber(int val) {
		if (val >= 0) {
			value = val;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Instantiates a new natural number with a kind of <code>Infinity</code>.
	 * @param inf the kind of {@link Infinity}
	 */
	public NaturalNumber(Infinity inf) {
		setInfinity(inf);
	}
	
	public NaturalNumber clone() {
		NaturalNumber num;
		
		num = new NaturalNumber();
		num.setFromNumeric(this);
		
		return num;
	}

	@Override
	public boolean setFromFloat(float val) {
		if (val >= 0) {
			value = (int) val;
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean setFromInt(int val) {
		if (val >= 0) {
			value = val;
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public boolean setFromNumeric(NumericDataType num) {
		Infinity inf;
		int val;

		val = num.toInt();
		inf = num.getInfinity();

		if (inf != Infinity.REAL) {
			return setInfinity(inf);
		}
		if (val >= 0) {
			value = val;
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public float toFloat() {
		return value;
	}

	@Override
	public int toInt() {
		return value;
	}

	@Override
	public boolean setFromString(String str) {
		int val;

		// Input is infinite
		if (str.equals("\u221e")) {
			setInfinity(Infinity.POSITIVE_INFINITE);
			return true;
		}

		// Input is real
		try {
			val = Integer.parseInt(str);
		}
		catch (NumberFormatException ex) {
			return false;
		}

		if (val >= 0 && val <= MaxValues.getNaturalNumberMaximum()) {
			value = val;
			return true;
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		if (value != Integer.MAX_VALUE) {
			return String.valueOf(value);
		} else {
			return "\u221e";
		}
	}
	
	@Override
	public Infinity getInfinity() {
		if (value != Integer.MAX_VALUE)
			return Infinity.REAL;
		else
			return Infinity.POSITIVE_INFINITE;
	}

	@Override
	public boolean setInfinity(Infinity inf) {
		if (inf == Infinity.POSITIVE_INFINITE)
			value = Integer.MAX_VALUE;
		else if (inf == Infinity.REAL)
			value = 0;
		else
			return false;

		return true;
	}

	public String[] getSpecialCharacter() {
		String[] out = {"\u221e"};
		
		return out;
	}		
}
