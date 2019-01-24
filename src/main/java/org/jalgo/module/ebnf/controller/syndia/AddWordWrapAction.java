package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.ElementNotFoundException;
import org.jalgo.module.ebnf.model.syndia.NoNullElemException;
import org.jalgo.module.ebnf.model.syndia.NullElem;

/**
 * Action that adds a new word wrap at <code>oldNullElem</code>.
 * 
 * @author Michael Thiele
 * 
 */
public class AddWordWrapAction extends AddSynDiaElemAction {

	/**
	 * 
	 * @param oldNullElem
	 *            is needed to get the position where to insert
	 */
	public AddWordWrapAction(NullElem oldNullElem) {
		super(oldNullElem);
	}

	public void perform() throws NoNullElemException {
		parent.addWordWrap(oldIndex);
	}

	public void undo() throws ElementNotFoundException {
		parent.removeWordWrap(oldIndex + 1);
	}

}
