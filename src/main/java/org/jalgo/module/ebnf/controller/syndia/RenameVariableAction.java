package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.Variable;

/**
 * Action that renames a variable.
 * 
 * @author Michael Thiele
 * 
 */
public class RenameVariableAction extends RenameElementAction {

	private Variable variable;

	/**
	 * 
	 * @param variable
	 *            the variable to rename
	 * @param newLabel
	 *            the new label for this variable
	 */
	public RenameVariableAction(Variable variable, String newLabel) {
		super(newLabel);
		this.variable = variable;
		oldLabel = variable.getLabel();
	}

	public void perform() throws Exception {
		variable.setLabel(newLabel);
		variable.getMySyntaxDiagram().getMySynDiaSystem().addVariable(newLabel);
		variable.getMySyntaxDiagram().getMySynDiaSystem().removeVariable(
				oldLabel);
	}

	public void undo() throws Exception {
		variable.setLabel(oldLabel);
		variable.getMySyntaxDiagram().getMySynDiaSystem().removeVariable(
				newLabel);
		variable.getMySyntaxDiagram().getMySynDiaSystem().addVariable(oldLabel);
	}

}
