package org.jalgo.module.hoare.model;

import org.jalgo.module.hoare.constants.Rule;

/**
 * Represents a strong pre verification formula
 *  
 * @author Thomas, Uwe
 *
 */

public class StrongPreFormula extends VerificationFormula {
	/**
	 * the serial Id of this Object
	 */
	private static final long serialVersionUID = 6793878960166641496L;

	/**
	 * Constructs a StrongPreFormula with the given beginning and end of the code
	 * and sets the parent, preAssertion, postAssertion and the source
	 * of the whole <code>VerificationFormula</code> tree.
	 * 
	 * @param parent the parent node in the VerificationFormula tree
	 * @param source the source of the whole VerificationFormula tree
	 * @param codeStart startIndex of the SourceCode
	 * @param codeEnd endIndex of the SourceCode
	 * @param preAssertion preAssertion of the StrongPreFormula
	 * @param postAssertion postAssertion of the StrongPreFormula
	 */
	StrongPreFormula(VerificationFormula parent, String source, int codeStart, int codeEnd,
			AbstractAssertion preAssertion, AbstractAssertion postAssertion) {
		super(parent, source, codeStart, codeEnd);
		this.preAssertion=preAssertion;
		this.postAssertion=postAssertion;
		this.setChanged(true);
		this.setApplied();
		parent.appliedRule = Rule.STRONGPRE;
	}

	/**
	 * @see org.jalgo.module.hoare.model.VerificationFormula#canApply(Rule)
	 */
	boolean canApply(Rule rule)	{
		return parent.canApply(rule);
	}
	
	/**
	 * @see org.jalgo.module.hoare.model.VerificationFormula#applyNext(VerificationFormula)
	 */
	boolean applyNext(VerificationFormula parent)
		throws UnsupportedOperationException	{

		return this.parent.applyNext(parent);
	}
	
	/**
	 * @see org.jalgo.module.hoare.model.VerificationFormula#check(VerificationFormula)
	 */
	@Override
	protected boolean check(VerificationFormula formula) {
		return parent.check(formula);
	}

	/**
	 * 
	 * @see org.jalgo.module.hoare.model.VerificationFormula#getCode(boolean)
	 */
	public String getCode(boolean full) {
		return parent.getCode(full);		
	}

	
}
