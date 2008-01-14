package org.jalgo.module.hoare.control;

import org.jalgo.module.hoare.control.edits.IterationVFApply;
import org.jalgo.module.hoare.gui.Node;
import org.jalgo.module.hoare.model.VerificationFormula;

/**
 * 
 * @author Gerald
 * 
 */
public class IterationRule {

	/**
	 * 
	 * @param vf
	 *            the IterationVF, which the rule is applied on
	 * @param node
	 *            the GUI-representation node of this vf
	 * @return an UndoableEdit of an iterationrule apply
	 */
	public static UndoableEdit getUndoableEdit(VerificationFormula vf, Node node) {
		return new IterationVFApply(vf, node);
	}

}
