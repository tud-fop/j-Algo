package org.jalgo.module.hoare.control;

import org.jalgo.module.hoare.control.edits.StrongPreVFApply;
import org.jalgo.module.hoare.model.VerificationFormula;

/**
 * 
 * @author Gerald
 * 
 */
public class StrongPreRule {

	/**
	 * 
	 * @param vf
	 *            the VF, which the strongprecondition rule is applied on
	 * @param prgCtrl
	 *            the programcontrol
	 * @return an UndoableEdit of a strong precondition rule apply
	 */
	static public UndoableEdit getUndoableEdit(VerificationFormula vf,
			ProgramControl prgCtrl) {
		return new StrongPreVFApply(vf, prgCtrl);
	}
}
