package org.jalgo.module.app.core.dataType.formalLanguage;

import java.io.Serializable;

abstract class FormalLanguageObject implements Comparable<FormalLanguageObject>, Serializable {
	/**
	 * Returns a string representation of the language.
	 */
	public abstract String toString();

	/**
	 * Sets the value of the object to the language represented by
	 * <code>str</code>.
	 * 
	 * Throws a InvalidArgumentException if language does not match the object's
	 * language type.
	 * 
	 * @param str
	 *            the string to set the object value from
	 */
	public abstract void setFromString(String str);

	/**
	 * Try to simplify the language object. Actual implementation might give
	 * more information about the things actually done.
	 * 
	 * The returned object might be the same as the object that was called, but
	 * it might also be a replacement or <code>null</code> if the object has
	 * become empty.
	 * 
	 * @return the simplified object
	 */
	public abstract FormalLanguageObject simplify();

	/**
	 * @see Object.clone
	 */
	public abstract FormalLanguageObject clone();

	/**
	 * @see Object.equals
	 */
	public abstract boolean equals(Object other);

	/**
	 * @see Comparable.compareTo
	 */
	public int compareTo(FormalLanguageObject other) {
		return this.toString().compareTo(other.toString());
	}
}
