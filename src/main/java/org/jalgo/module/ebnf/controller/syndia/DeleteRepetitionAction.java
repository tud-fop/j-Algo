package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.*;

/**
 * Action that deletes a repetition.
 * 
 * @author Michael Thiele
 * 
 */
public class DeleteRepetitionAction extends DeleteSynDiaElemAction {

	private Repetition deletedRepetition;

	private boolean left, right;

	private int endLeft;

	/**
	 * Initializes the Action.
	 * 
	 * @param deletedRepetition
	 *            the repetition to delete
	 * @param left
	 *            <code>true</code> if the left (upper) path has to be
	 *            preserved
	 * @param right
	 *            <code>true</code> if the right (lower) path has to be
	 *            preserved
	 */
	public DeleteRepetitionAction(Repetition deletedRepetition, boolean left,
			boolean right) {
		this.deletedRepetition = deletedRepetition;
		this.left = left;
		this.right = right;
		endLeft = deletedRepetition.getLeft().getNumberOfElems() - 1;
	}

	public void perform() throws IllegalArgumentException,
			ElementNotFoundException {
		NullElem newNullElem = ((Concatenation) deletedRepetition.getParent())
				.deleteRepetition(deletedRepetition, left, right);
		index = newNullElem.getIndex();
	}

	public void undo() throws IllegalArgumentException, NoNullElemException {
		if (left == false && right == false) {
			((Concatenation) deletedRepetition.getParent())
					.undoDeleteRepetition(index, index);
		} else {
			if (left == true) {
				((Concatenation) deletedRepetition.getParent())
						.undoDeleteRepetition(index, endLeft + index);
			} else {
				((Concatenation) deletedRepetition.getParent())
						.undoDeleteRepetition(index, index);
				// now delete all elements, that are in the right side of the
				// branch, but also in the parent concatenation
				Concatenation rightSide = (Concatenation) deletedRepetition
						.getRight();
				// do not delete NullElems - that's done in the remove function
				for (int i = 1; i < rightSide.getNumberOfElems(); i = i + 2) {
					SynDiaElem sde = rightSide.getSynDiaElem(i);
					try {
						((Concatenation) deletedRepetition.getParent())
								.removeSynDiaElemCompletely(sde);
					} catch (ElementNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
