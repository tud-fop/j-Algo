package org.jalgo.module.hoare.model;

public class AssignVF extends VerificationFormula {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1414518083638166254L;

	public AssignVF(Assertion pre, Assertion post, VerificationFormula parent) {
		super(pre, post, parent);
	}

	@Override
	public Class<? extends VerificationFormula> getType() {
		return getClass();
	}

	/**
	 * @return a string representation of the class' function
	 */
	public String toString() {
		return "assign";
	}

}
