package org.jalgo.module.app.core.dataType.rationalNumber;

import java.util.HashSet;
import java.util.Set;

import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.Operation;


public class PercentageType extends RationalNumber {

	private static final long serialVersionUID = 6885280182754231240L;

	public static Set<Operation> getOperations() {
		Set<Operation> operations;
		
		operations = new HashSet<Operation>();

		operations.add(new RationalAdd(PercentageType.class));
		operations.add(new RationalMultiply(PercentageType.class));
		operations.add(new RationalMinimum(PercentageType.class));
		operations.add(new RationalMaximum(PercentageType.class));

		return operations;
	}

	public static String getName() {
		return "Percentage";
	}
	
	public static Operation getOperationByID(String id) {
		for (Operation op : getOperations()) {
			if (op.getID().equals(id))
				return op;
		}
		
		return null;
	}

	public static String[] getSymbolicRepresentation() {
		String[] returnValue = {"[0,1]","", ""};
		
		return returnValue;
	}

	@Override
	protected float minimumValue() {
		return 0f;
	}

	@Override
	protected float maximumValue() {
		return 1f;
	}
	
	public PercentageType() {
		super();
	}

	public PercentageType(float val) {
		super(val);
	}

	public PercentageType(Infinity inf) {
		super(inf);
	}

	public String[] getSpecialCharacter() {
		return null;
	}		
}
