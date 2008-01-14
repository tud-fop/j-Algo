package org.jalgo.module.hoare.model;

public class IfVF extends VerificationFormula {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2897813996296096435L;

	public IfVF(Assertion pre, Assertion post, VerificationFormula parent) {
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
		return "alt1";
	}
}
