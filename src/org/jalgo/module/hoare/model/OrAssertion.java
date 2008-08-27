package org.jalgo.module.hoare.model;

/**
 * Represents an Or in the assertiontree. It must have two children assertion as operands.
 *  
 * @author Thomas
 **/
class OrAssertion extends AbstractAssertion {

	/**
	 * the serial Id of this Object
	 */
	private static final long serialVersionUID = -4867430960518614L;
	/**
	 * the left side of the assertion tree under the or
	 */
	private AbstractAssertion left;
	/**
	 * the right side of the assertion tree under the or
	 */
	private AbstractAssertion right;
	
	/**
	 * creates an OrAssertion with an left and right Assertion tree
	 *  
	 * @param left
	 *     the left child assertion compared with Or
	 * @param left
	 *     the right child assertion compared with Or
	 **/	
	public OrAssertion(AbstractAssertion left,AbstractAssertion right)	{
		this.left=left;
		this.right=right;
	}
	
	/**
	 * @see org.jalgo.module.hoare.model.AbstractAssertion#verifiable()
	 */
	@Override
	public boolean verifiable() {
		return left.verifiable() && right.verifiable();
	}

	/**
	 * @see org.jalgo.module.hoare.model.AbstractAssertion#toString()
	 */
	@Override
	public String toString() {
		return "("+left.toString()+" || "+right.toString()+")";
	}

	/**
	 * @see org.jalgo.module.hoare.model.AbstractAssertion#toText(boolean)
	 */
	@Override
	public String toText(boolean full) {
		return "("+left.toText(full) + " \u2228 " + right.toText(full)+")";
	}
	
}
