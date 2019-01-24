package org.jalgo.module.app.core.dataType.booleanType;

import java.util.List;

import org.jalgo.module.app.core.dataType.*;

/**
 * The binary operation <code>Disjunction</code> (or Boolean Or), which can
 * be executed on <code>BooleanType</code>.
 * 
 */
public class Disjunction extends Operation {
	
	private static final long serialVersionUID = -6252134530049745634L;

	/**
	 * @see Operation#getID()
	 */
	public String getID() {
		return "disjunct";
	}

	/**
	 * @see Operation#getName()
	 */
	public String getName() {
		return "Disjunction";
	}

	/**
	 * @see Operation#getSymbolicRepresentation()
	 */
	public String getSymbolicRepresentation() {
		return "\u2228";
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
		return new BooleanType(false);
	}

	/**
	 * @see Operation#getAbsorbingElement()
	 */
	public DataType getAbsorbingElement() {
		return new BooleanType(true);
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
		Boolean result = ((BooleanType) arg1).toBoolean() || ((BooleanType) arg2).toBoolean();
		return new BooleanType(result);
	}

	/**
	 * @see Operation#op(List)
	 */
	public DataType op(List<DataType> args) {
		boolean result = false;
		
		for (DataType dt : args) {
			if (((BooleanType) dt).toBoolean()) {
				result = true;
				break;
			}
		}
		
		return new BooleanType(result);
	}

	/**
	 * @see Operation#star(DataType, Operation)
	 */
	public DataType star(DataType arg, Operation other) {
		return new BooleanType(true);
	}

}
