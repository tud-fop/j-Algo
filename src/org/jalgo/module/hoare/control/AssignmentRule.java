package org.jalgo.module.hoare.control;

import org.jalgo.module.hoare.control.edits.AssignVFApply;
import org.jalgo.module.hoare.model.Assertion;
import org.jalgo.module.hoare.model.VerificationFormula;

/**
 * 
 * @author Gerald
 * 
 */
public class AssignmentRule {

	/**
	 * 
	 * @param vf
	 *            the AssignVF to evaluate
	 * @param pre
	 *            the postassertion of the VF
	 * @param post
	 *            the preassertion of the VF
	 * @param code
	 *            the associated code segment of the VF
	 * @return an UndoableEdit of an assignmentrule apply
	 */
	public static UndoableEdit getUndoableEdit(VerificationFormula vf,
			Assertion pre, Assertion post, String code,
			ProgramControl programControl) {
		return new AssignVFApply(vf, pre, post, code, programControl);
	}

}
