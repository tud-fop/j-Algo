package org.jalgo.module.app.core.dataType;

import java.io.Serializable;
import java.util.Set;

/**
 * Represents an element of a mathematical set (which is needed for the
 * <code>SemiRing</code>).
 * 
 */
public abstract class DataType implements Serializable {

	/**
	 * A set of available <code>Operation</code>s for this
	 * <code>DataType</code>.
	 */
	protected static Set<Operation> operations = null;

	/**
	 * 
	 * @return the available <code>Operation</code>s for this
	 *         <code>DataType</code>.
	 */
	public static Set<Operation> getOperations() {
		throw new UnsupportedOperationException();
	}

	public static Operation getOperationByID(String id) {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @return the name of the set (e.g. Natural Number, Formal Language)
	 */
	public static String getName() {
		throw new UnsupportedOperationException();
	}

	/**
	 * 
	 * @return the formal representation of the data type. Whereas - the first
	 *         entry represents the used set, - the seconds entry represents the
	 *         restriction of the set, e.g. plus for positive numbers (printed
	 *         on top) - the third entry represents an extended symbol, e.g.
	 *         infinity (printed on bottom)
	 * 
	 */
	public static String[] getSymbolicRepresentation() {
		throw new UnsupportedOperationException();
	}

	public abstract DataType clone();

	public abstract String toString();

	/**
	 * 
	 * @param str
	 *            the <code>String</code> to be converted to the
	 *            <code>DataType</code>
	 * @return <code>true</code>, if input correct, <code>false</code>
	 *         otherwise
	 */
	public abstract boolean setFromString(String str);

	/**
	 * 
	 * @return a list of special characters (e.g. epsilon or infinity) used in
	 *         this <code>DataType</code>.
	 */
	public abstract String[] getSpecialCharacter();
}
