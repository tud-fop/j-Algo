package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.*;

/**
 * Action that deletes a word wrap.
 * 
 * @author Michael Thiele
 * 
 */
public class DeleteWordWrapAction extends DeleteSynDiaElemAction {

	private WordWrap deletedWordWrap;

	/**
	 * Initializes the Action.
	 * 
	 * @param deletedWordWrap
	 *            the word wrap to delete
	 */
	public DeleteWordWrapAction(WordWrap deletedWordWrap) {
		this.deletedWordWrap = deletedWordWrap;
	}

	public void perform() throws ElementNotFoundException {
		NullElem newNullElem = ((Concatenation) deletedWordWrap.getParent())
				.removeWordWrap(deletedWordWrap);
		index = newNullElem.getIndex();
	}

	public void undo() throws NoNullElemException {
		((Concatenation) deletedWordWrap.getParent()).addWordWrap(index);
	}

}
