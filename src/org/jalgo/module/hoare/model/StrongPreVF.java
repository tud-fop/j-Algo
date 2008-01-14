package org.jalgo.module.hoare.model;

import java.awt.Dimension;

public class StrongPreVF extends VerificationFormula {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7573094365178836555L;

	public StrongPreVF(Assertion pre, Assertion post, VerificationFormula parent) {
		super(pre, post, parent);

	}

	public Class<? extends VerificationFormula> getType() {
		return parent.getType();
	}

	@Override
	public Dimension getCodeStart() {
		return parent.getCodeStart();
	}

	public Dimension getCodeEnd() {
		return parent.getCodeEnd();
	}

	public String toString() {
		return "SV";
	}

	public VerificationFormula pureVF() {
		return parent.pureVF();
	}

}
