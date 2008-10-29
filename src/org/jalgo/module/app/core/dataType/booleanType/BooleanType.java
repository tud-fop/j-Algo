package org.jalgo.module.app.core.dataType.booleanType;

import java.util.*;

import org.jalgo.module.app.core.dataType.*;

/**
 * A <code>DataType</code>, which represents a boolean value (<code>true</code>,
 * <code>false</code>).
 * 
 */
public class BooleanType extends DataType {

	private static final long serialVersionUID = -545227017196474005L;

	/**
	 * @see DataType#getOperations()
	 */
	public static Set<Operation> getOperations() {
		Set<Operation> operations;

		operations = new HashSet<Operation>();

		operations.add(new Conjunction());
		operations.add(new Disjunction());

		return operations;
	}

	/**
	 * @see DataType#getOperationByID(String)
	 */
	public static Operation getOperationByID(String id) {
		for (Operation op : getOperations()) {
			if (op.getID().equals(id))
				return op;
		}

		return null;
	}

	/**
	 * @see DataType#getName()
	 */
	public static String getName() {
		return "Boolean Type";
	}

	/**
	 * @see DataType#getSymbolicRepresentation()
	 */
	public static String[] getSymbolicRepresentation() {
		String[] returnValue = {"{true, false}", "", ""};
		
		return returnValue;
	}

	private Boolean value;

	/**
	 * Instantiates <code>BooleanType</code> with the default value
	 * <code>true</code>.
	 */
	public BooleanType() {
		this.value = true;
	}

	/**
	 * Instantiates <code>BooleanType</code> with the given <code>value</code>.
	 * 
	 * @param value
	 *            a boolean value (<code>true</code> or <code>false</code>).
	 */
	public BooleanType(Boolean value) {
		this.value = value;
	}

	/**
	 * @see DataType#clone()
	 */
	public BooleanType clone() {
		return new BooleanType(value);
	}

	/**
	 * Sets the <code>BooleanType</code> instance to <code>val</code>
	 * @param val
	 * @return <code>true</code>
	 */
	public boolean setFromBoolean(boolean val) {
		value = val;
		return true;
	}

	/**
	 * Sets <code>BooleanType</code> from <code>Integer</code>. 1 equals
	 * <code>true</code>, 0 equals <code>false</code>.
	 * 
	 * @param val
	 *            the value to be converted to <code>BooleanType</code>.
	 * @return <code>true</code>, if <code>val</code> either 1 or 0.
	 */
	public boolean setFromInt(int val) {
		if (val == 0) {
			value = false;
		} else if (val == 1) {
			value = true;
		} else {
			return false;
		}
		return true;
	}

	/**
	 * Sets <code>BooleanType</code> from <code>String</code>.
	 * 
	 * @param str
	 *            A String, which has to be either 1, true or True for
	 *            <code>true</code> or 0, false or False for
	 *            <code>false</code>.
	 * 
	 * 
	 * @return true, if <code>str</code> was in proper format.
	 */
	@Override
	public boolean setFromString(String str) {
		if (str.equals("1") || str.toLowerCase().equals("t")) {
			value = true;
			return true;
		}
		if (str.equals("0") || str.toLowerCase().equals("f")) {
			value = false;
			return true;
		} 
		if ((str.compareToIgnoreCase("true") != 0)
				&& (str.compareToIgnoreCase("false") != 0)) {
			return false;
		}
		value = Boolean.parseBoolean(str);
		return true;
	}

	/**
	 * 
	 * @return the boolean representation of <code>BooleanType</code>
	 */
	public boolean toBoolean() {
		return value;
	}

	/**
	 * @see DataType#toString()
	 */
	@Override
	public String toString() {
		return Boolean.toString(value);
	}

	/**
	 * Returns 1 if value is true, 0 otherwise.
	 * 
	 * @return 1 if value is true, 0 otherwise.
	 */
	public int toInt() {
		if (value) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * @see DataType#equals(Object)
	 */
	@Override
	public boolean equals(Object ot) {
		BooleanType other;

		if (!(ot instanceof BooleanType))
			return false;

		other = (BooleanType) ot;
		return (value == other.value);
	}
	
	/**
	 * @see DataType#getSpecialCharacter()
	 */
	
	public String[] getSpecialCharacter() {
		return null;
	}
}
