package org.jalgo.module.hoare.model;

import java.awt.Dimension;

public class WeakPostVF extends VerificationFormula {

	/**
	 * 
	 */
	private static final long serialVersionUID = -903381525699105983L;

	public WeakPostVF(Assertion pre, Assertion post, VerificationFormula parent) {
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
		return "SN";
	}

	public VerificationFormula pureVF() {
		return parent.pureVF();
	}
}
