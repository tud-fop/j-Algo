package org.jalgo.module.app.core.dataType.rationalNumber;

import java.util.List;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.Operation;


public class RationalMinimum extends RationalOperation {

	private static final long serialVersionUID = -8101248622263077182L;

	public RationalMinimum(Class<? extends RationalNumber> aClass) {
		super(aClass);
	}
	
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
		return newNumber(Infinity.POSITIVE_INFINITE);
	}

	public DataType getAbsorbingElement() {
		return newNumber(Infinity.NEGATIVE_INFINITE);
	}

	public DataType op(DataType arg1, DataType arg2) {
		float value = Math.min(((RationalNumber)arg1).toFloat(), ((RationalNumber)arg2).toFloat());
		return newNumber(value);
	}

	public DataType op(List<DataType> args) {
		float value;

		value = ((RationalNumber)getNeutralElement()).toFloat();
		for (DataType dt : args)
			value = Math.min(value, ((RationalNumber)dt).toFloat());

		return newNumber(value);
	}
}
