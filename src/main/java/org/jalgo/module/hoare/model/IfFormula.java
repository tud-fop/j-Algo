/**
 
 */
package org.jalgo.module.hoare.model;



import java.util.List;

import org.jalgo.module.hoare.constants.Rule;

/**
 * Represents an If statement with only on statement (or statement sequence)
 * 
 * @author Thomas, Uwe
 *
 */

public class IfFormula extends VerificationFormula {
	/**
	 * the serial Id of this Object
	 */
	private static final long serialVersionUID = 1852029170756056822L;
	/**
	 * holds the concrete assertion in the boolean expression in the if-statement
	 */
	private ConcreteAssertion boolExp;

	
	
	/**
	 * Constructs a IfFormula with the given beginning and end of the code
	 * and sets the parent, the boolean expression and the source of
	 * the whole <code>VerificationFormula</code> tree.
	 * 
	 * @param parent the parent node in the VerificationFormula tree
	 * @param source the source of the whole VerificationFormula tree
	 * @param codeStart startIndex of the SourceCode
	 * @param codeEnd endIndex of the SourceCode
	 * @param boolExp the boolean Expression of the if-Statement
	 */
	IfFormula(VerificationFormula parent, String source, int codeStart,
			int codeEnd, ConcreteAssertion boolExp) {
		super(parent, source, codeStart, codeEnd);
		this.boolExp=boolExp;
	}
	
	/**
	 * @see org.jalgo.module.hoare.model.VerificationFormula#canApply(Rule)
	 */
	boolean canApply(Rule rule)	{
		return rule.equals(Rule.IF);
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
			parent.appliedRule = Rule.IF;
			
			/*	left child	*/
			vf.setApplied();
			vf.setChanged(true);
			vf.replacePreAssertion(new AndAssertion(parent.preAssertion,
										boolExp));
			vf.replacePostAssertion(parent.postAssertion);
		
			/*	right child	*/
			vf = vfList.get(1);
			vf.setApplied();
			vf.setChanged(true);
			vf.replacePreAssertion(new AndAssertion(parent.preAssertion,
										new NotAssertion(boolExp)));
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
		return (full ? getCode(getCode()) : "if ("+boolExp.toText(true)+")");
	}
	
}
