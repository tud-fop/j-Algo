package org.jalgo.module.app.core.dataType.rationalNumber;

import java.util.List;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.Operation;


public class RationalAdd extends RationalOperation {
	
	private static final long serialVersionUID = -4062506241253643485L;

	public RationalAdd(Class<? extends RationalNumber> aClass) {
		super(aClass);
	}
	
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
		return newNumber(0.0f);
	}

	public DataType getAbsorbingElement() {
		return newNumber(Infinity.POSITIVE_INFINITE);
	}

	public DataType op(DataType arg1, DataType arg2) {
		float value = ((RationalNumber)arg1).toFloat() + ((RationalNumber)arg2).toFloat();
		return newNumber(value);
	}

	public DataType op(List<DataType> args) {
		float value;

		value = 0.f;
		for (DataType dt : args) {
			if (((RationalNumber)dt).getInfinity() != Infinity.REAL)
				return dt;
			
			value += ((RationalNumber)dt).toFloat();
		}

		return newNumber(value);
	}
}