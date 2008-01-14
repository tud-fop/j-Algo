package org.jalgo.module.hoare.control;

import org.jalgo.module.hoare.control.edits.IfVFApply;
import org.jalgo.module.hoare.gui.Node;
import org.jalgo.module.hoare.model.VerificationFormula;

/**
 * 
 * @author Gerald
 * 
 */
public class IfRule {

	/**
	 * 
	 * @param vf
	 *            the IfVF, which the rule is applied on
	 * @param node
	 *            the GUI-representation node of this vf
	 * @return an UndoableEdit of an ifrule apply
	 */
	public static UndoableEdit getUndoableEdit(VerificationFormula vf,
			Node node, ProgramControl control) {
		return new IfVFApply(vf, node, control);
	}

}
