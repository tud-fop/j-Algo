package org.jalgo.module.hoare.control;

import org.jalgo.module.hoare.control.edits.CompoundVFApply;
import org.jalgo.module.hoare.gui.Node;
import org.jalgo.module.hoare.model.VerificationFormula;

/**
 * 
 * @author Gerald
 * 
 */
public class CompoundRule {

	/**
	 * 
	 * @param vf
	 *            the CompoundVF, which the rule is applied on
	 * @param node
	 *            the GUI-representation Node of this VF
	 * @return an UndoableEdit of a compoundrule apply
	 */
	public static UndoableEdit getUndoableEdit(VerificationFormula vf, Node node) {
		return new CompoundVFApply(vf, node);
	}

}
