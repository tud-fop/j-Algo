package org.jalgo.module.app.core.dataType.rationalNumber;

import java.util.HashSet;
import java.util.Set;

import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.Operation;


public class PositiveRationalNumber extends RationalNumber {

	/*
	 * (non-Javadoc) CLASS METHODS
	 */
	
	private static final long serialVersionUID = -3314801881292434525L;

	public static Set<Operation> getOperations() {
		Set<Operation> operations;
		
		operations = new HashSet<Operation>();

		operations.add(new RationalAdd(PositiveRationalNumber.class));
		operations.add(new RationalMultiply(PositiveRationalNumber.class));
		operations.add(new RationalMinimum(PositiveRationalNumber.class));
		operations.add(new RationalMaximum(PositiveRationalNumber.class));

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
		return "Positive Rational Number";
	}

	public static String[] getSymbolicRepresentation() {
		String[] returnValue = {"\u211d","+", "\u221e"};
		
		return returnValue;
	}
	
	@Override
	protected float minimumValue() {
		return 0f;
	}
	
	public PositiveRationalNumber() {
		super();
	}

	public PositiveRationalNumber(float val) {
		super(val);
	}

	public PositiveRationalNumber(Infinity inf) {
		super(inf);
	}
}
