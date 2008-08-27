package org.jalgo.module.hoare.model;

/**
 * Represents an Not in the assertiontree. It could only have one child assertion as operand.
 * 
 * @author Thomas
 *
 */

class NotAssertion extends AbstractAssertion {

	private static final long serialVersionUID = -4594430915140006774L;
	private AbstractAssertion operand;
	
	/**
	 * creates an NotAssertion with one Assertion tree
	 *  
	 * @param operand
	 *     the child assertion to be negated
	 */
	public NotAssertion(AbstractAssertion operand) {
		this.operand = operand;
	}

	/**
	 * @see org.jalgo.module.hoare.model.AbstractAssertion#includesVariable()
	 */
	@Override
	public boolean verifiable() {
		return operand.verifiable();
	}

	/**
	 * @see org.jalgo.module.hoare.model.AbstractAssertion#toString()
	 */
	@Override
	public String toString() {
		return "!"+operand.toString();
	}

	/**
	 * @see org.jalgo.module.hoare.model.AbstractAssertion#toText(boolean)
	 */
	@Override
	public String toText(boolean full) {
		return "\u00ac(" + operand.toText(full)+")";
	}

}
