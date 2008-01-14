package org.jalgo.module.hoare.control;

import org.jalgo.module.hoare.control.edits.WeakPostVFApply;
import org.jalgo.module.hoare.model.VerificationFormula;

/**
 * 
 * @author Gerald
 * 
 */
public class WeakPostRule {

	/**
	 * 
	 * @param vf
	 *            the VF, which the weak postcondition rule is applied on
	 * @param prgCtrl
	 *            the programcontrol
	 * @return an UndoableEdit of a weak postcondition rule apply
	 */
	static public UndoableEdit getUndoableEdit(VerificationFormula vf,
			ProgramControl prgCtrl) {
		return new WeakPostVFApply(vf, prgCtrl);
	}
}
