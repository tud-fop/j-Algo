package org.jalgo.module.hoare.model;

/**
 * Represents an And in the assertiontree. It must have two children assertion as operands.
 * 
 * @author Thomas, Uwe
 **/
class AndAssertion extends AbstractAssertion {

	/**
	 * the serial Id of this Object
	 */
	private static final long serialVersionUID = -7327027897881901603L;
	/**
	 * the left side of the assertion tree under the and
	 */
	private AbstractAssertion left;
	/**
	 * the right side of the assertion tree under the and
	 */
	private AbstractAssertion right;
	
	/**
	 * creates an AndAssertion with an left and right assertion tree 
	 *  
	 * @param left
	 *     the left child assertion compared with And
	 * @param left
	 *     the right child assertion compared with And
	 **/	
	public AndAssertion(AbstractAssertion left,AbstractAssertion right)	{
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
		return "("+left.toString()+" && "+right.toString()+")";
	}

	/**
	 * @see org.jalgo.module.hoare.model.AbstractAssertion#toText(boolean)
	 */
	@Override
	public String toText(boolean full) {
		return "("+left.toText(full) + " \u2227 " + right.toText(full) + ")";
	}

}
