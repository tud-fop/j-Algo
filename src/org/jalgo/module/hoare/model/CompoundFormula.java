package org.jalgo.module.hoare.model;



import org.jalgo.module.hoare.constants.Rule;

/**
 * Represents a compound statement 
 * 
 * @author Thomas, Uwe
 *
 */

public class CompoundFormula extends VerificationFormula {

	/**
	 * the serial Id of this Object
	 */
	private static final long serialVersionUID = 5031680894960284107L;
	
	/**
	 * Constructs a CompoundFormula with the given beginning and end of the code
	 * and sets the parent and the source of
	 * the whole <code>VerificationFormula</code> tree.
	 * 
	 * @param parent the parent node in the VerificationFormula tree
	 * @param source the source of the whole VerificationFormula tree
	 * @param codeStart startIndex of the SourceCode
	 * @param codeEnd endIndex of the SourceCode
	 */
	CompoundFormula(VerificationFormula parent, String source, int codeStart, int codeEnd) {
		super(parent, source, codeStart, codeEnd);
	}
	
	/**
	 * @see org.jalgo.module.hoare.model.VerificationFormula#canApply(Rule)
	 */
	boolean canApply(Rule rule)	{
		return rule.equals(Rule.COMPOUND);
	}
	
	/**
	 * @see org.jalgo.module.hoare.model.VerificationFormula#applyNext(VerificationFormula)
	 */
	boolean applyNext(VerificationFormula parent)
		throws UnsupportedOperationException	{

		if (! parent.hasChildren())	{
			return false;
		}
		VerificationFormula vf = parent.getChildren().get(0);

		if (vf.isRuleApplied())	{
			return false;
		} else	{
			parent.appliedRule = Rule.COMPOUND;
			
			vf.setApplied();
			vf.setChanged(true);
			vf.replacePreAssertion(parent.preAssertion);
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
		return (full ? getCode(getCode()) :"{...}");
	}

}
