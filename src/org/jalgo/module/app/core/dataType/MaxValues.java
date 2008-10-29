package org.jalgo.module.app.core.dataType;

/**
 * A class used to store maximum (and minimum) input values for natural and
 * rational numbers.
 * 
 */
public class MaxValues {

	/**
	 * @return the maximum input number for natural numbers.
	 */
	public static int getNaturalNumberMaximum() {
		return 65536;
	}

	/**
	 * @return the maximum input number for rational numbers.
	 */
	public static float getRationalNumberMaximum() {
		return 65536.0f;
	}
	
	/**
	 * @return the minimum input number for rational numbers.
	 */
	public static float getRationalNumberMinimum() {
		return -65536.0f;
	}
}
