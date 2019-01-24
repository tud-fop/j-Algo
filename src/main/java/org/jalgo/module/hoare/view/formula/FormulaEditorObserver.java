package org.jalgo.module.hoare.view.formula;

/**
 * Observer for <code>FormulaEditor</code>s.
 * @author Antje
 *
 */
public interface FormulaEditorObserver {
	
	/**
	 * Is called when the formula is changed.
	 * @param editor <code>FormulaEditor</code> where the change happened.
	 */
	void formulaChanged(FormulaEditor editor);
	
	/**
	 * Is called when the <code>FormulaEditor</code> receives a parse message from the parser.
	 * @param editor the <code>FormulaEditor</code> that received the parse message
	 * @param message the parse message that is received.
	 */
	void receiveParseMessage(FormulaEditor editor, String message);
	
	/**
	 * Is called when a change of the formula should be applied.
	 * @param editor the <code>FormulaEditor</code> that requested the application.
	 */
	void applyFormulaChange(FormulaEditor editor);
	
	/**
	 * Is called when a <code>FormulaEditor</code> is closed.
	 * @param editor the <code>FormulaEditor</code> that is closed.
	 */
	void formulaEditorClosed(FormulaEditor editor);
	
}
