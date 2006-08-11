package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.*;

/**
 * Action that deletes a variable.
 * 
 * @author Michael Thiele
 * 
 */
public class DeleteVariableAction extends DeleteSynDiaElemAction {

	private Variable deletedVariable;

	private SynDiaSystem sds;

	/**
	 * Initializes the Action.
	 * 
	 * @param deletedVariable
	 *            the variable to delete
	 * @param sds
	 *            a syntax diagram system
	 */
	public DeleteVariableAction(Variable deletedVariable, SynDiaSystem sds) {
		this.deletedVariable = deletedVariable;
		this.sds = sds;
	}

	public void perform() throws ElementNotFoundException {
		NullElem newNullElem = ((Concatenation) deletedVariable.getParent())
				.deleteVariable(deletedVariable);
		index = newNullElem.getIndex();
		sds.removeVariable(deletedVariable.getLabel());
	}

	public void undo() throws NoNullElemException {
		((Concatenation) deletedVariable.getParent())
				.undoDeleteVariable(index);
		sds.addVariable(deletedVariable.getLabel());
	}

}
