package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.*;

/**
 * Action that deletes a terminal symbol.
 * 
 * @author Michael Thiele
 * 
 */
public class DeleteTerminalAction extends DeleteSynDiaElemAction {

	private TerminalSymbol deletedTerminal;

	/**
	 * Initializes the Action.
	 * 
	 * @param deletedTerminal
	 *            the terminal symbol to delete
	 */
	public DeleteTerminalAction(TerminalSymbol deletedTerminal) {
		this.deletedTerminal = deletedTerminal;
	}

	public void perform() throws ElementNotFoundException {
		NullElem newNullElem = ((Concatenation) deletedTerminal.getParent())
				.deleteTerminal(deletedTerminal);
		index = newNullElem.getIndex();
	}

	public void undo() throws NoNullElemException {
		((Concatenation) deletedTerminal.getParent())
				.undoDeleteTerminal(index);
	}

}
