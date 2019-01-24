package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.ElementNotFoundException;
import org.jalgo.module.ebnf.model.syndia.NoNullElemException;
import org.jalgo.module.ebnf.model.syndia.NullElem;

/**
 * Action that adds a new terminal symbol at <code>oldNullElem</code>.
 * 
 * @author Michael Thiele
 * 
 */
public class AddTerminalAction extends AddSynDiaElemAction {

	private String label;

	/**
	 * 
	 * @param oldNullElem
	 *            is needed to get the position where to insert
	 * @param label
	 *            label of the terminal symbol
	 */
	public AddTerminalAction(NullElem oldNullElem, String label) {
		super(oldNullElem);
		this.label = label;
	}

	public void perform() throws NoNullElemException {
		if (!undo) {
			parent.addTerminalSymbol(oldIndex, label);
		} else {
			parent.redoAddTerminal(oldIndex);
		}
	}

	public void undo() throws ElementNotFoundException {
		undo = true;
		parent.removeTerminalSymbol(oldIndex + 1);
	}

}
