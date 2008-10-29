package org.jalgo.module.app.core.dataType.naturalNumber;

import java.util.List;

import org.jalgo.module.app.core.dataType.DataType;
import org.jalgo.module.app.core.dataType.Infinity;
import org.jalgo.module.app.core.dataType.Operation;

/**
 * Represents the maximum operation in the natural numbers (e.g. max{2,3} = 3).
 * 
 */
public class NaturalMaximum extends Operation {

	private static final long serialVersionUID = 6775657970133861189L;

	/**
	 * @see Operation#getID()
	 */
	public String getID() {
		return "max";
	}

	/**
	 * @see Operation#getName()
	 */
	public String getName() {
		return "Maximum";
	}

	/**
	 * @see Operation#getSymbolicRepresentation()
	 */
	public String getSymbolicRepresentation() {
		return "max";
	}

	/**
	 * @see Operation#getNotation()
	 */
	public Notation getNotation() {
		return Notation.PREFIX;
	}

	/**
	 * @see Operation#getNeutralElement()
	 */
	public DataType getNeutralElement() {
		return new NaturalNumber(0);
	}

	/**
	 * @see Operation#getAbsorbingElement()
	 */
	public DataType getAbsorbingElement() {
		return new NaturalNumber(Infinity.POSITIVE_INFINITE);
	}

	/**
	 * @see Operation#isAssociative()
	 */
	public boolean isAssociative() {
		return true;
	}

	/**
	 * @see Operation#isCommutative()
	 */
	public boolean isCommutative() {
		return true;
	}

	/**
	 * @see Operation#op(DataType, DataType)
	 */
	public DataType op(DataType arg1, DataType arg2) {
		int value;

		if (((NaturalNumber) arg1).getInfinity() != Infinity.REAL
				|| ((NaturalNumber) arg2).getInfinity() != Infinity.REAL)
			return getAbsorbingElement();

		value = Math.max(((NaturalNumber) arg1).toInt(), ((NaturalNumber) arg2)
				.toInt());
		return new NaturalNumber(value);
	}

	/**
	 * @see Operation#op(List)
	 */
	public DataType op(List<DataType> args) {
		int value;

		value = 0;
		for (DataType dt : args) {
			if (((NaturalNumber) dt).getInfinity() != Infinity.REAL)
				return getAbsorbingElement();

			value = Math.max(value, ((NaturalNumber) dt).toInt());
		}

		return new NaturalNumber(value);
	}

	/**
	 * @see Operation#star(DataType, Operation)
	 */
	public DataType star(DataType arg, Operation other) {
		return getAbsorbingElement();
	}
}