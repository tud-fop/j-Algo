package org.jalgo.module.hoare.model;

public class IterationVF extends VerificationFormula {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4268436839319044898L;

	public IterationVF(Assertion pre, Assertion post, VerificationFormula parent) {
		super(pre, post, parent);
	}

	private Assertion pi;

	public Assertion getPi() {
		return pi;
	}

	public void setPi(Assertion pi) {
		this.pi = pi;
	}

	@Override
	public Class<? extends VerificationFormula> getType() {
		return getClass();
	}

	public String toString() {
		return "iter";
	}


}
