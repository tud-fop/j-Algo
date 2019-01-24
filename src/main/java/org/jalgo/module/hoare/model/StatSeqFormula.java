package org.jalgo.module.hoare.model;

import java.util.List;

import org.jalgo.module.hoare.constants.Rule;

/**
 * Represents a statement sequence of two statements
 * 
 * @author Thomas, Uwe
 *
 */

public class StatSeqFormula extends VerificationFormula {
	/**
	 * the serial Id of this Object
	 */
	private static final long serialVersionUID = 5144003586344912315L;
	
	/**
	 * Constructs a StatSeqFormula with the given beginning and end of the code
	 * and sets the parent and the source of
	 * the whole <code>VerificationFormula</code> tree.
	 * 
	 * @param parent the parent node in the VerificationFormula tree
	 * @param source the source of the whole VerificationFormula tree
	 * @param codeStart startIndex of the SourceCode
	 * @param codeEnd endIndex of the SourceCode
	 */
	public StatSeqFormula(VerificationFormula parent, String source, int codeStart, int codeEnd) {
		super(parent, source, codeStart, codeEnd);
	}

	/**
	 * @see org.jalgo.module.hoare.model.VerificationFormula#canApply(Rule)
	 */
	public boolean canApply(Rule rule)	{
		return rule.equals(Rule.STATSEQ);
	}
	
	/**
	 * @see org.jalgo.module.hoare.model.VerificationFormula#applyNext(VerificationFormula)
	 */
	boolean applyNext(VerificationFormula parent)
		throws UnsupportedOperationException	{

		if (! parent.hasChildren())	{
			return false;
		}
		List<VerificationFormula> vfList = parent.getChildren();
		VerificationFormula vf = vfList.get(0);

		if (vf.isRuleApplied())	{
			return false;
		} else	{
			parent.appliedRule = Rule.STATSEQ;
			
			/*	left child	*/
			vf.setApplied();
			vf.setChanged(true);
			vf.replacePreAssertion(parent.preAssertion);
			
			/*	right child	*/
			vf = vfList.get(1);
			vf.setApplied();
			vf.setChanged(true);
			vf.replacePostAssertion(parent.postAssertion);

			return true;
		}
	}

	/**
	 * 
	 * @see org.jalgo.module.hoare.model.VerificationFormula#getCode(boolean)
	 */
	@Override
	public String getCode(boolean full) {
		return (full ? getCode(getCode()) : "A1 A2");
	}
	
}
