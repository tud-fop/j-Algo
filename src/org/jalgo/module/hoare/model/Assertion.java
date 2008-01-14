package org.jalgo.module.hoare.model;

import java.io.Serializable;

/**
 * abstract class of an Assertion
 * 
 * @author Gerald
 * 
 */
public abstract class Assertion implements Serializable {

	/**
	 * true if the Assertion is a loop invariant
	 */
	private boolean isSIV;

	public Assertion() {
		isSIV = false;
	}

	public boolean isSIV() {
		return isSIV;
	}

	public void setSIV(boolean isSIV) {
		this.isSIV = isSIV;
	}

	public String getHTMLString() {
		return null;
	}

	public String toString() {
		return null;
	}

	public abstract Assertion copy();

	public abstract void replaceTarget(Assertion o, Assertion n);

	public abstract boolean containsDummyAssertion();

}
