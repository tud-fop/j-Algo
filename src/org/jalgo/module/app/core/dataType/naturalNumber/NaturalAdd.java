package org.jalgo.module.app.core.dataType.naturalNumber;

import java.util.List;

import org.jalgo.module.app.core.dataType.*;

/**
 * Represents the addition in the natural numbers.
 *
 */
public class NaturalAdd extends Operation {
	
	private static final long serialVersionUID = -4126828819528924337L;

	public String getID() {
		return "add";
	}

	public String getName() {
		return "Addition";
	}

	public String getSymbolicRepresentation() {
		return "+";
	}
	
	public Notation getNotation() {
		return Notation.INFIX;
	}

	public DataType getNeutralElement() {
		return new NaturalNumber(0);
	}

	public DataType getAbsorbingElement() {
		return new NaturalNumber(Infinity.POSITIVE_INFINITE);
	}

	public boolean isAssociative() {
		return true;
	}

	public boolean isCommutative() {
		return true;
	}

	public DataType op(DataType arg1, DataType arg2) {
		int value;

		if (((NaturalNumber)arg1).getInfinity() != Infinity.REAL || ((NaturalNumber)arg2).getInfinity() != Infinity.REAL)
			return getAbsorbingElement();

		value = ((NaturalNumber)arg1).toInt();
		value += ((NaturalNumber)arg2).toInt();

		return new NaturalNumber(value);
	}

	public DataType op(List<DataType> args){
		int value;

		value = 0;
		for(DataType dt : args) {
			if (((NaturalNumber)dt).getInfinity() != Infinity.REAL)
				return getAbsorbingElement();

			value += ((NaturalNumber)dt).toInt();
		}

		return new NaturalNumber(value);
	}

	public DataType star(DataType arg, Operation other) {
		return getAbsorbingElement();
	}
}