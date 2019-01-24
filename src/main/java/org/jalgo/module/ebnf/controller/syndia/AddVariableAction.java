package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.ElementNotFoundException;
import org.jalgo.module.ebnf.model.syndia.NoNullElemException;
import org.jalgo.module.ebnf.model.syndia.NullElem;

/**
 * Action that adds a new variable at <code>oldNullElem</code>.
 * 
 * @author Michael Thiele
 * 
 */
public class AddVariableAction extends AddSynDiaElemAction {

	private String label;

	private NullElem oldNullElem;

	/**
	 * 
	 * @param oldNullElem
	 *            is needed to get the position where to insert
	 * @param label
	 *            label of the variable
	 */
	public AddVariableAction(NullElem oldNullElem, String label) {
		super(oldNullElem);
		this.oldNullElem = oldNullElem;
		this.label = label;
	}

	public void perform() throws NoNullElemException {
		if (!undo) {
			parent.addVariable(oldIndex, label);
		} else {
			parent.redoAddVariable(oldIndex);
		}
		oldNullElem.getMySyntaxDiagram().getMySynDiaSystem().addVariable(label);
	}

	public void undo() throws ElementNotFoundException {
		undo = true;
		parent.removeVariable(oldIndex + 1);
		oldNullElem.getMySyntaxDiagram().getMySynDiaSystem().removeVariable(
				label);
	}

}
