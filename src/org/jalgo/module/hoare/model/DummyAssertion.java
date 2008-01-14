package org.jalgo.module.hoare.model;

public class DummyAssertion extends ConcreteAssertion {

	/**
	 * an assertion which holds place for an assertion not yet filled in by the
	 * user
	 */
	private static final long serialVersionUID = 1834644143759004295L;

	static int nextID = 0;

	public DummyAssertion() {
		super("?");
	}

	@Override
	public Assertion copy() {
		return new DummyAssertion();
	}

	@Override
	public boolean containsDummyAssertion() {
		return true;
	};
}
