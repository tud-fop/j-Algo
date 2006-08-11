package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.ElementNotFoundException;
import org.jalgo.module.ebnf.model.syndia.NoNullElemException;
import org.jalgo.module.ebnf.model.syndia.NullElem;

/**
 * Action that adds a new repetition from <code>oldNullElem</code> to
 * <code>rightNullElem</code>. If it is removed (by clicking "undo") elements
 * of a potemtial contained concatenation are re-inserted in their original
 * concatenation.
 * 
 * @author Michael Thiele
 * 
 */
public class AddRepetitionAction extends AddSynDiaElemAction {

	private java.util.List containedPath;

	private int rightNullElemIndex;

	/**
	 * @param oldNullElem
	 *            the left NullElem (start of the repetition)
	 * @param rightNullElem
	 *            the right NullElem (end of the repetition)
	 */
	public AddRepetitionAction(NullElem oldNullElem, NullElem rightNullElem) {
		super(oldNullElem);
		this.rightNullElemIndex = rightNullElem.getIndex();
	}

	public void perform() throws IllegalArgumentException, NoNullElemException {
		if (!undo) {
		containedPath = parent
				.addRepetition(oldIndex, rightNullElemIndex);
		} else {
			containedPath = parent
			.redoAddRepetition(oldIndex, rightNullElemIndex);
		}
	}

	public void undo() throws IllegalArgumentException,
			ElementNotFoundException {
		undo = true;
		if (containedPath.size() > 0) {
			parent.removeRepetition(
					oldIndex + 1, true, false, false);
		} else {
			parent.removeRepetition(
					oldIndex + 1, false, false, false);
		}
	}

}
