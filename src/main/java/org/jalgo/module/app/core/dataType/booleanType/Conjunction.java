package org.jalgo.module.app.core.dataType.booleanType;

import java.util.List;

import org.jalgo.module.app.core.dataType.*;

/**
 * The binary operation <code>Conjunction</code> (or Boolean And), which can
 * be executed on <code>BooleanType</code>.
 * 
 */
public class Conjunction extends Operation {

	private static final long serialVersionUID = 1022585504793412750L;

	/**
	 * @see Operation#getID()
	 */
	public String getID() {
		return "conjunct";
	}

	/**
	 * @see Operation#getName()
	 */
	public String getName() {
		return "Conjunction";
	}

	/**
	 * @see Operation#getSymbolicRepresentation()
	 */
	public String getSymbolicRepresentation() {
		return "\u2227";
	}

	/**
	 * @see Operation#getNotation()
	 */
	public Notation getNotation() {
		return Notation.INFIX;
	}

	/**
	 * @see Operation#getNeutralElement()
	 */
	public DataType getNeutralElement() {
		return new BooleanType(true);
	}
	
	/**
	 * @see Operation#getAbsorbingElement()
	 */
	public DataType getAbsorbingElement() {
		return new BooleanType(false);
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
		Boolean result = ((BooleanType) arg1).toBoolean()
				&& ((BooleanType) arg2).toBoolean();
		return new BooleanType(result);
	}

	/**
	 * @see Operation#op(List)
	 */
	public DataType op(List<DataType> args) {
		boolean result = true;

		for (DataType dt : args) {
			if (!((BooleanType) dt).toBoolean()) {
				result = false;
				break;
			}
		}

		return new BooleanType(result);
	}

	/**
	 * @see Operation#star(DataType, Operation)
	 */
	public DataType star(DataType arg, Operation other) {
		throw new UnsupportedOperationException();
	}

}
