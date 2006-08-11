package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.TerminalSymbol;

/**
 * Action that renames a terminal symbol.
 * 
 * @author Michael Thiele
 * 
 */
public class RenameTerminalAction extends RenameElementAction {

	private TerminalSymbol terminalSymbol;

	/**
	 * 
	 * @param terminalSymbol
	 *            the terminal symbol to rename
	 * @param newLabel
	 *            the new label for this terminal symbol
	 */
	public RenameTerminalAction(TerminalSymbol terminalSymbol, String newLabel) {
		super(newLabel);
		this.terminalSymbol = terminalSymbol;
		oldLabel = terminalSymbol.getLabel();
	}

	public void perform() throws Exception {
		terminalSymbol.setLabel(newLabel);
	}

	public void undo() throws Exception {
		terminalSymbol.setLabel(oldLabel);
	}

}
