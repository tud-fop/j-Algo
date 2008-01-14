package org.jalgo.module.hoare.model;

/**
 * Class of a single Assertion
 * 
 * @author Gerald
 * 
 */
public class ConcreteAssertion extends Assertion {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2737552367891898201L;

	private String assertion;

	public ConcreteAssertion(String assertion) {
		this.assertion = assertion;
	}

	public String getAssertion() {
		return assertion;
	}

	public void setAssertion(String assertion) {
		this.assertion = assertion;
	}

	public String getHTMLString() {
		String html = toString();
		html = html.replaceAll("<=", "&#8804;");
		html = html.replaceAll(">=", "&#8805;");
		html = html.replaceAll("<", "&lt;");
		html = html.replaceAll(">", "&gt;");
		html = html.replaceAll("sqrt", "&#8730;");
		html = html.replaceAll("Sum", "&#8721;");
		html = html.replaceAll("Product", "&#8719;");
		return html;
	}

	public String toString() {

		return "(" + assertion + ")";
	}

	@Override
	public Assertion copy() {
		return new ConcreteAssertion(new String(assertion));
	}

	@Override
	public void replaceTarget(Assertion o, Assertion n) {

	}

	@Override
	public boolean containsDummyAssertion() {
		return false;
	};
}
