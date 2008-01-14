package org.jalgo.module.hoare.control;

import org.jalgo.module.hoare.control.edits.StatSeqVFApply;
import org.jalgo.module.hoare.gui.Node;
import org.jalgo.module.hoare.model.VerificationFormula;

/**
 * 
 * @author Gerald
 * 
 */
public class StatementSequenceRule {

	/**
	 * 
	 * @param vf
	 *            the StatSeqVF, which the rule is applied on
	 * @param node
	 *            the GUI-representation node of this vf
	 * @return an UndoableEdit of an statement sequence rule apply
	 */
	public static UndoableEdit getUndoableEdit(VerificationFormula vf, Node node) {
		return new StatSeqVFApply(vf, node);
	}

}
