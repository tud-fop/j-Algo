package org.jalgo.module.hoare.model;

/**
 * Class of an AndAssertion
 * 
 * @author Gerald
 * 
 */
public class AndAssertion extends Assertion {

	private static final long serialVersionUID = -5921376751437067348L;

	/**
	 * the left Operand "x&&...
	 */
	private Assertion leftOperand;

	/**
	 * the right Operand "...&x"
	 */
	private Assertion rightOperand;

	public AndAssertion(Assertion left, Assertion right) {

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

	/**
	 * @return a HTML representation form
	 */
	public String getHTMLString() {
		return "(" + leftOperand.getHTMLString() + "&#8743;"
				+ rightOperand.getHTMLString() + ")";
	}

	/**
	 * @return a string in the intern form
	 */
	public String toString() {
		return "(" + leftOperand.toString() + "&&" + rightOperand.toString()
				+ ")";
	}

	@Override
	public Assertion copy() {
		return new AndAssertion(leftOperand.copy(), rightOperand.copy());
	}

	@Override
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
