package org.jalgo.module.app.core.dataType;

/**
 * An abstract subclass of <code>DataType</code>, which represents numbers.
 * Numbers can be converted into each other.
 */
public abstract class NumericDataType extends DataType {

	/**
	 * Converts the <code>NumericDataType</code> instance to an
	 * <code>Integer</code> representation.
	 * 
	 * @return the Integer representation.
	 */
	public abstract int toInt();

	/**
	 * Converts the <code>NumericDataType</code> instance to a
	 * <code>Float</code> representation.
	 * 
	 * @return the Float representation.
	 */
	public abstract float toFloat();

	/**
	 * Sets the <code>NumericDataType</code> object to an integer value.
	 * 
	 * @param value
	 *            the integer value to set the <code>NumericDataType</code>
	 *            instance to.
	 * @return <code>true</code> if <code>value</code> correct,
	 *         <code>false</code> otherwise
	 */
	public abstract boolean setFromInt(int value);

	/**
	 * Sets the <code>NumericDataType</code> object to an floating-point
	 * value.
	 * 
	 * @param value
	 *            the floating-point value to set the
	 *            <code>NumericDataType</code> instance to.
	 * @return true if <code>value</code> correct, false otherwise
	 */
	public abstract boolean setFromFloat(float value);

	/**
	 * Sets the <code>NumericDataType</code> object to the generic value
	 * <code>numeric</code>. value.
	 * 
	 * @param numeric
	 * @return true if <code>numeric</code> correct, false otherwise
	 */
	public abstract boolean setFromNumeric(NumericDataType numeric);

	/**
	 * Returns the kind of <code>Infinity</code> of this number.
	 * 
	 * @return the kind of <code>Infinity</code> of this number.
	 */
	public abstract Infinity getInfinity();

	/**
	 * Sets the Infinity to <code>inf</code>. If <code>inf</code> is
	 * <code>Infinity.Real</code>, then the number is set to its default
	 * value.
	 * 
	 * @param inf
	 * @return <code>true</code>, if setting <code>Infinity</code> was
	 *         sucessful, otherwise <code>false</code>.
	 */
	public abstract boolean setInfinity(Infinity inf);

	public boolean equals(Object ot) {
		NumericDataType other;

		if (!(ot instanceof NumericDataType))
			return false;

		other = (NumericDataType) ot;
		return (getInfinity() == other.getInfinity() && (getInfinity() != Infinity.REAL || toFloat() == other
				.toFloat()));
	}
}
