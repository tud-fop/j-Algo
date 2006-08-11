package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.util.IAction;

/**
 * Abstract class for renaming elements in a syntax diagram system.
 * 
 * @author Michael Thiele
 *
 */
public abstract class RenameElementAction implements IAction {

	protected String newLabel;

	protected String oldLabel;

	/**
	 * 
	 * @param newLabel
	 *            the new label for the syntax diagram element
	 */
	public RenameElementAction(String newLabel) {
		this.newLabel = newLabel;
	}

}
