package org.jalgo.module.hoare.model;

/**
 * Represents an variable assertion which could be edited by the User.
 * It holds the name of the variable and the underlying Assertion
 * 
 * @author Thomas
 *
 */
class VarAssertion extends AbstractAssertion {

	/**
	 * the serial Id of this Object
	 */
	private static final long serialVersionUID = 2976302686359687955L;
	/**
	 * holds the name off this variable
	 */
	private String name;
	/**
	 * holds the underlying assertion tree, by default <code>null</code>
	 */
	private AbstractAssertion child=null;
	/**
	 * holds the original assertion String
	 */
	private String original; 
	
	/**
	 * creates an VarAssertion without a underlying Assertiontree
	 *  
	 * @param name
	 *     the name of the variable
	 */	
	public VarAssertion(String name) {
		this.name=name;
		this.child=null;
		this.original="";
	}
	
	/**
	 * creates an VarAssertion with a underlying Assertiontree
	 *  
	 * @param name
	 *     the name of the variable
	 * @param child
	 *     the child represents the underlying Assertiontree
	 */	
	public VarAssertion(String name,AbstractAssertion child)	{
		this.name=name;
		this.child=child;
		this.original="";
	}

	/**
	 * Returns the name of the variable.
	 * 
	 * @return the name
	 */
	public String getVariableName()	{
		return name;
	}
    
	/**
	 * Sets the underlying assertiontree to the given assertiontree.
	 * If child is null the VarAssertion is set to be empty.
	 * 
	 * @param child
	 *        the root of the assertiontree
	 */
	public void setVariable(AbstractAssertion child)	{
		this.child=child;
	}
    
    
	/**
	 * @see org.jalgo.module.hoare.model.AbstractAssertion#isVariable()
	 */
	public boolean isVariable()	{
		return true;
	}
	
	/**
	 * @see org.jalgo.module.hoare.model.AbstractAssertion#verifiable()
	 */
	@Override
	public boolean verifiable() {
		return child!=null;
	}

	/**
	 * @see org.jalgo.module.hoare.model.AbstractAssertion#toString()
	 */
	@Override
	public String toString() {
		return ((child == null) ? "" : child.toString());
	}

	/** 
	 * If you want the full version of the assertion then
	 * the result becomes the full version of the underlying
	 * assertion tree.
	 * 
	 * @see org.jalgo.module.hoare.model.AbstractAssertion#toText(boolean)
	 */
	public String toText(boolean full) {
		return (full && (child != null)) ? child.toText(true) :  name;
	}
	
	/**
	 * This method sets the original String to the given parameter.
	 * Note: If the parameter is <code>null</code> the  
	 * 
	 * @param value
	 *        the original assertion given
	 */
	public void setOriginal(String value){
		if (value!=null){
		 this.original=value;
		}
	}

	/**
	 * 
	 * @see org.jalgo.module.hoare.model.AbstractAssertion#getOrginal()
	 */
	@Override
	public String getOrginal() {
		return original;
	}

}
