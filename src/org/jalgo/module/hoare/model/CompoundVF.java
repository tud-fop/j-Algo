package org.jalgo.module.hoare.model;

public class CompoundVF extends VerificationFormula {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6406620576989082553L;

	public CompoundVF(Assertion pre, Assertion post, VerificationFormula parent) {
		super(pre, post, parent);
	}

	@Override
	public Class<? extends VerificationFormula> getType() {
		return getClass();
	}

	public String toString() {
		return "comp";
	}
}
