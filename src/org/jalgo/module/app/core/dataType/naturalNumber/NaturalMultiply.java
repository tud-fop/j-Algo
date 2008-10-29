package org.jalgo.module.app.core.dataType.naturalNumber;

import java.util.List;

import org.jalgo.module.app.core.dataType.*;

/**
 * Represents the multiply operation on natural numbers.
 *
 */
public class NaturalMultiply extends Operation {
	
	private static final long serialVersionUID = 4281150139970963974L;

	public String getID() {
		return "mult";
	}

	public String getName() {
		return "Multiplication";
	}

	public String getSymbolicRepresentation() {
		return "\u2219";
	}
	
	public Notation getNotation() {
		return Notation.INFIX;
	}

	public DataType getNeutralElement() {
		return new NaturalNumber(1);
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
		value *= ((NaturalNumber)arg2).toInt();

		return new NaturalNumber(value);
	}

	public DataType op(List<DataType> args){
		int value;

		value = 1;
		for(DataType dt : args) {
			if (((NaturalNumber)dt).getInfinity() != Infinity.REAL)
				return getAbsorbingElement();

			value *= ((NaturalNumber)dt).toInt();
		}

		return new NaturalNumber(value);
	}

	public DataType star(DataType arg, Operation other) {
		return getAbsorbingElement();
	}
}
