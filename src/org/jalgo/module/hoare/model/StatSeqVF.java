package org.jalgo.module.hoare.model;

public class StatSeqVF extends VerificationFormula {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8850495570174264177L;

	public StatSeqVF(Assertion pre, Assertion post, VerificationFormula parent) {
		super(pre, post, parent);
	}

	@Override
	public Class<? extends VerificationFormula> getType() {
		return getClass();
	}

	public String toString() {
		return "sequence";
	}

}
