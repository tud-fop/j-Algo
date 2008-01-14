package org.jalgo.module.hoare.control;

import org.jalgo.module.hoare.control.edits.IfElseVFApply;
import org.jalgo.module.hoare.gui.Node;
import org.jalgo.module.hoare.model.VerificationFormula;

/**
 * 
 * @author Gerald
 * 
 */
public class IfElseRule {

	/**
	 * 
	 * @param vf
	 *            the IfElseVF, which the rule is applied on
	 * @param node
	 *            the GUI-representation node of this vf
	 * @return an UndoableEdit of an ifelserule apply
	 */
	public static UndoableEdit getUndoableEdit(VerificationFormula vf, Node node) {
		return new IfElseVFApply(vf, node);
	}

}
