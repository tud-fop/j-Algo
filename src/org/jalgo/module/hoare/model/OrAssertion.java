package org.jalgo.module.hoare.model;

public class OrAssertion extends Assertion {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7279471091681226066L;

	private Assertion leftOperand; // TODO: ins EKLDia einf?gen

	private Assertion rightOperand; // TODO: ins EKLDia einf?gen

	// TODO: Konstruktor ins EKLDia einf?gen
	public OrAssertion(Assertion left, Assertion right) {

		leftOperand = left;
		rightOperand = right;
	}

	public Assertion getLeftOperand() {
		return leftOperand;
	}

	public void setLeftOperand(Assertion leftOperand) {
		this.leftOperand = leftOperand;
	}

	public Assertion getRightOperand() {
		return rightOperand;
	}

	public void setRightOperand(Assertion rightOperand) {
		this.rightOperand = rightOperand;
	}

	public String getHTMLString() {
		return "(" + leftOperand.getHTMLString() + "&#8744;"
				+ rightOperand.getHTMLString() + ")";
	}

	public String toString() {
		return "(" + leftOperand.toString() + "||" + rightOperand.toString()
				+ ")";
	}

	@Override
	public Assertion copy() {
		return new OrAssertion(leftOperand.copy(), rightOperand.copy());
	}

	public void replaceTarget(Assertion o, Assertion n) {
		leftOperand.replaceTarget(o, n);
		rightOperand.replaceTarget(o, n);

		if (leftOperand == o)
			leftOperand = n;
		if (rightOperand == o)
			rightOperand = n;

	}

	@Override
	public boolean containsDummyAssertion() {
		return leftOperand.containsDummyAssertion()
				|| rightOperand.containsDummyAssertion();
	}
}
