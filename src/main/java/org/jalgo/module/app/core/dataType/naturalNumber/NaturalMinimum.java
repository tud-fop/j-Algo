package org.jalgo.module.app.core.dataType.naturalNumber;

import java.util.List;

import org.jalgo.module.app.core.dataType.*;

/**
 * Represents the minimum operation in the natural numbers (e.g. min{2,3} = 2).
 *
 */
public class NaturalMinimum extends Operation {
	
	private static final long serialVersionUID = 1671869633291013726L;

	public String getID() {
		return "min";
	}

	public String getName() {
		return "Minimum";
	}

	public String getSymbolicRepresentation() {
		return "min";
	}

	public Notation getNotation() {
		return Notation.PREFIX;
	}

	public DataType getNeutralElement() {
		return new NaturalNumber(Infinity.POSITIVE_INFINITE);
	}

	public DataType getAbsorbingElement() {
		return new NaturalNumber(0);
	}

	public boolean isAssociative() {
		return true;
	}

	public boolean isCommutative() {
		return true;
	}

	public DataType op(DataType arg1, DataType arg2) {
		int value;

		if (((NaturalNumber)arg1).getInfinity() != Infinity.REAL)
			return arg2;
		if (((NaturalNumber)arg2).getInfinity() != Infinity.REAL)
			return arg1;

		value = Math.min(((NaturalNumber)arg1).toInt(), ((NaturalNumber)arg2).toInt());
		return new NaturalNumber(value);
	}

	public DataType op(List<DataType> args){
		int value;

		value = Integer.MAX_VALUE;
		for(DataType dt : args) {
			if (((NaturalNumber)dt).getInfinity() == Infinity.REAL)
				value = Math.min(value, ((NaturalNumber)dt).toInt());
		}

		return new NaturalNumber(value);
	}


	public DataType star(DataType arg, Operation other) {
		return getAbsorbingElement();
	}
}
