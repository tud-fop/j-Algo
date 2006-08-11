package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.ElementNotFoundException;
import org.jalgo.module.ebnf.model.syndia.NoNullElemException;
import org.jalgo.module.ebnf.model.syndia.NullElem;

/**
 * Action that adds a new branch from <code>oldNullElem</code> to
 * <code>rightNullElem</code>. If it is removed (by clicking "undo") elements
 * of a potemtial contained concatenation are re-inserted in their original
 * concatenation.
 * 
 * @author Michael Thiele
 * 
 */
public class AddBranchAction extends AddSynDiaElemAction {

	private java.util.List containedPath;

	private int rightNullElemIndex;

	/**
	 * @param oldNullElem
	 *            the left NullElem (start of the branch)
	 * @param rightNullElem
	 *            the right NullElem (end of the branch)
	 */
	public AddBranchAction(NullElem oldNullElem, NullElem rightNullElem) {
		super(oldNullElem);
		this.rightNullElemIndex = rightNullElem.getIndex();
	}

	public void perform() throws IllegalArgumentException, NoNullElemException {
		if (!undo) {
			containedPath = parent.addBranch(oldIndex, rightNullElemIndex);
		} else {

			containedPath = parent.redoAddBranch(oldIndex, rightNullElemIndex);
		}

	}

	public void undo() throws IllegalArgumentException,
			ElementNotFoundException {
		undo = true;
		if (containedPath.size() > 1) {
			parent.removeBranch(oldIndex + 1, true, false, false);
		} else {
			parent.removeBranch(oldIndex + 1, false, false, false);
		}
	}

}
