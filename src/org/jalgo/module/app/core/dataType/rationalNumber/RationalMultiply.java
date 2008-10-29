package org.jalgo.module.app.core.dataType.rationalNumber;

import java.util.List;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.Operation;


public class RationalMultiply extends RationalOperation {

	private static final long serialVersionUID = 3133209844705037342L;

	public RationalMultiply(Class<? extends RationalNumber> aClass) {
		super(aClass);
	}
	
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
		return newNumber(1.f);
	}

	public DataType getAbsorbingElement() {
		return newNumber(Infinity.POSITIVE_INFINITE);
	}

	public DataType op(DataType arg1, DataType arg2) {
		Infinity inf1, inf2;
		float value;

		// This extra test is necessary as we don't want exceptions
		inf1 = ((RationalNumber)arg1).getInfinity();
		inf2 = ((RationalNumber)arg2).getInfinity();
		
		if (inf1 != Infinity.REAL)
			return newNumber(inf1);
		if (inf2 != Infinity.REAL)
			return newNumber(inf2);

		value = ((RationalNumber)arg1).toFloat();
		value *= ((RationalNumber)arg2).toFloat();

		return newNumber(value);
	}

	public DataType op(List<DataType> args){
		Infinity inf;
		float value;

		value = 1.f;
		for (DataType dt : args) {
			// This extra test is necessary as we don't want exceptions
			inf = ((RationalNumber)dt).getInfinity();
			if (inf != Infinity.REAL)
				return newNumber(inf);

			value *= ((RationalNumber)dt).toFloat();
		}

		return newNumber(value);
	}
}
