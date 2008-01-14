package org.jalgo.module.hoare.model;

public class NotAssertion extends Assertion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4542358001110709809L;

	private Assertion operand; // TODO: ins EKLDia einf?gen

	// TODO: Konstruktor ins EKLDia einf?gen
	public NotAssertion(Assertion operand) {

		this.operand = operand;
	}

	public Assertion getOperand() {
		return operand;
	}

	public void setOperand(Assertion operand) {
		this.operand = operand;
	}

	public String getHTMLString() {
		return "&#0172" + operand.getHTMLString();
	}

	public String toString() {
		return "!" + operand.toString(); // ASCII: 170
	}

	@Override
	public Assertion copy() {
		return new NotAssertion(operand.copy());
	}

	@Override
	public void replaceTarget(Assertion o, Assertion n) {
		operand.replaceTarget(o, n);

		if (operand == o)
			operand = n;

	}

	@Override
	public boolean containsDummyAssertion() {
		return operand.containsDummyAssertion();
	}
}
