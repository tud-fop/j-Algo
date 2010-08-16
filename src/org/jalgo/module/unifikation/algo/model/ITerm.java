package org.jalgo.module.unifikation.algo.model;

import java.util.List;

/**
 * the interface for all terms (variables+functions)
 */
public interface ITerm extends IModelFormat{
	/**
	 * returns the (un-)formated Name of the Term
	 * @param doFormat true if formating should be done
	 * @return (un-)formated Name of the Term
	 */
	public String getName(final boolean doFormat);

	/**
	 * returns the formated representation of the term
	 * @param recDepth the depth of recursion (for conditional formating)
	 * @return formated representation of the term
	 */
	public String getFormatText(final int recDepth);

	/**
	 * returns the formated representation of the term
	 * @return formated representation of the term
	 */
	public String getFormatText();

	/**
	 * adds a parameter to the term and returns whether it was successful
	 * @param param
	 * @return successfully added
	 */
	public boolean addParameter(final ITerm param);

	/**
	 * Gets a list of the parameters
	 * @return list of parameters
	 */
	public List<ITerm> getParameters();

	/**
	 * Checks if this term contains (or IS) a specified variable
	 * @return True if variable is in term
	 */
	public boolean containsVar(Variable var);
	
	/**
	 * Checks if this and other term have the same name
	 * @param other Other term
	 * @return True if name is the same
	 */
	public boolean equalsName(ITerm other);
	
	/**
	 * Substitutes varFrom to termTo and returns the resulting term
	 * @param varFrom Variable to replace
	 * @param termTo Term that is replaced for varFrom
	 * @return Resulting term (Can be self, if nothing was done)
	 */
	public ITerm substitute(Variable varFrom, ITerm termTo);
}
