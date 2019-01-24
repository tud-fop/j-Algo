package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.*;

/**
 * Action that deletes a branch.
 * 
 * @author Michael Thiele
 * 
 */
public class DeleteBranchAction extends DeleteSynDiaElemAction {

	private Branch deletedBranch;

	private boolean left, right;

	private int endLeft;

	/**
	 * Initializes the Action.
	 * 
	 * @param deletedBranch
	 *            the branch to delete
	 * @param left
	 *            <code>true</code> if the left (upper) path has to be
	 *            preserved
	 * @param right
	 *            <code>true</code> if the right (lower) path has to be
	 *            preserved
	 */
	public DeleteBranchAction(Branch deletedBranch, boolean left, boolean right) {
		this.deletedBranch = deletedBranch;
		this.left = left;
		this.right = right;
		// pre-initialize endLeft, because elements in the branch could be
		// deleted
		endLeft = deletedBranch.getLeft().getNumberOfElems() - 1;
	}

	public void perform() throws IllegalArgumentException,
			ElementNotFoundException {
		NullElem newNullElem = ((Concatenation) deletedBranch.getParent())
				.deleteBranch(deletedBranch, left, right);
		index = newNullElem.getIndex();
	}

	public void undo() throws IllegalArgumentException, NoNullElemException {
		if (left == false && right == false) {
			((Concatenation) deletedBranch.getParent()).undoDeleteBranch(index,
					index);
		} else {
			if (left == true) {
				((Concatenation) deletedBranch.getParent()).undoDeleteBranch(
						index, endLeft + index);
			} else {
				((Concatenation) deletedBranch.getParent()).undoDeleteBranch(
						index, index);
				// now delete all elements, that are in the right side of the
				// branch, but also in the parent concatenation
				Concatenation rightSide = (Concatenation) deletedBranch
						.getRight();
				// do not delete NullElems - that's done in the remove function
				for (int i = 1; i < rightSide.getNumberOfElems(); i = i + 2) {
					SynDiaElem sde = rightSide.getSynDiaElem(i);
					try {
						((Concatenation) deletedBranch.getParent())
								.removeSynDiaElemCompletely(sde);
					} catch (ElementNotFoundException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
