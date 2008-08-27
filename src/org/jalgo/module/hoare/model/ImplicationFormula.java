package org.jalgo.module.hoare.model;

import org.jalgo.module.hoare.constants.Rule;

/**
 * This is the representation of the most discussed VerificationFormula
 * in this project. It represents an implication considered to a
 * {@link StrongPreFormula} or {@link WeakPostFormula}.<br>
 * This class should be created during createStrongPre and createWeakPost
 * 
 * @author Thomas
 *
 */
public class ImplicationFormula extends VerificationFormula {

	/**
	 * the serial Id of this Object
	 */
	private static final long serialVersionUID = 5126815578070064211L;

	/**
	 * This method creates a new instance of the most discussed
	 * {@link VerificationFormula} in this projekt.
	 * 
	 * @param parent the parent node in the VerificationFormula tree
	 * @param codeStart startIndex of the SourceCode
	 * @param codeEnd endIndex of the SourceCode
	 * @param preAssertion preAssertion of the ImplicationFormula
	 * @param postAssertion postAssertion of the ImplicationFormula
	 */
	public ImplicationFormula(VerificationFormula parent, int codeStart, int codeEnd,
			 AbstractAssertion preAssertion, AbstractAssertion postAssertion) {
		super(parent, "", codeStart, codeEnd);
		this.preAssertion=preAssertion;
		this.postAssertion=postAssertion;
	}

	/**
	 * This method returns <code>false</code> on every call.
	 * 
	 * @see org.jalgo.module.hoare.model.VerificationFormula#applyNext(org.jalgo.module.hoare.model.VerificationFormula)
	 */
	@Override
	boolean applyNext(VerificationFormula parent)
		throws UnsupportedOperationException	{

		return false;
	}

	/**
	 * This method returns <code>false</code> on every call.
	 * 
	 * @see org.jalgo.module.hoare.model.VerificationFormula#canApply(org.jalgo.module.hoare.constants.Rule)
	 */
	@Override
	boolean canApply(Rule rule) {
		return false;
	}

	/**
	 * This method returns <code>null</code> on every call.
	 * 
	 * @see org.jalgo.module.hoare.model.VerificationFormula#getAppliedRule()
	 */
	@Override
	public Rule getAppliedRule() {
		return null;
	}

	/**
	 * This method returns "=>" on every call.
	 * 
	 * @see org.jalgo.module.hoare.model.VerificationFormula#getCode(boolean)
	 */
	@Override
	public String getCode(boolean full) {
		return "=>";
	}

	/**
	 * This method returns <code>false</code> on every call.
	 * 
	 * @see org.jalgo.module.hoare.model.VerificationFormula#createStrongPre(java.lang.String)
	 */
	@Override
	boolean createStrongPre(String varName) {
		return false;
	}

	/**
	 * This method returns <code>false</code> on every call.
	 * 
	 * @see org.jalgo.module.hoare.model.VerificationFormula#createWeakPost(java.lang.String)
	 */
	@Override
	boolean createWeakPost(String varName) {
		return false;
	}

	/**
	 * This method returns <code>true</code> if the pre assertion and
	 * the post assertion is verifiable.<br>
	 * Note: An assertion is verifiable if every {@link VarAssertion} has an
	 * underlying assertion.
	 * 
	 * @see org.jalgo.module.hoare.model.VerificationFormula#verifiable()
	 */
	@Override
	public boolean verifiable() {
		return preAssertion.verifiable() && postAssertion.verifiable();
	}

	/**
	 * @see org.jalgo.module.hoare.model.VerificationFormula#isImplication()
	 */
	@Override
	public boolean isImplication() {
		return true;
	}
	
	
	
	
	
	

}
