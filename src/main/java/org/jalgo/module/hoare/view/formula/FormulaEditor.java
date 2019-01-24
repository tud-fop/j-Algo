package org.jalgo.module.hoare.view.formula;

/**
 * Editor for formulas.
 * @author Antje
 *
 */
public interface FormulaEditor {
	
	/**
	 * Standard parse message: No error occured while parsing.
	 */
	static final String MESSAGE_OKAY = "okay";
	/**
	 * Standard parse message: An error occured while parsing.
	 */
	static final String MESSAGE_ERROR = "error";
	
	/**
	 * Returns the current formula.
	 * @return the current formula
	 */
	String getFormula();
	
	/**
	 * Accepts a message from the parser.
	 * The message received may be one of the standard messages <code>MESSAGE_OKAY</code> and <code>MESSAGE_ERROR</code>.
	 * @param message the message from the parser.
	 */
	void receiveParseMessage(String message);
	
	/**
	 * Displays the specified message
	 * @param message the parse message to display.
	 */
	void setParseMessage(String message);
	
	/**
	 * Removes the display of the parse message if any is displayed.
	 *
	 */
	void removeParseMessage();
	
	/**
	 * Adds a <code>FormulaEditorObserver</code>.
	 * @param observer new observer of the <code>FormulaEditor</code>
	 */
	void addFormulaEditorObserver(FormulaEditorObserver observer);
	
	/**
	 * Removes a <code>FormulaEditorObserver</code>.
	 * @param observer the <code>FormulaEditorObserver</code> to be removed
	 */
	void removeFormulaEditorObserver(FormulaEditorObserver observer);
	
}
