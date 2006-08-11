package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.model.syndia.Concatenation;
import org.jalgo.module.ebnf.model.syndia.NullElem;
import org.jalgo.module.ebnf.util.IAction;

/**
 * An abstract class for all Actions that have to add elements in a
 * concatenation. All actions can be undone by implementing the
 * <code>undo</code> function from <code>IAction</code>.
 * 
 * @author Michael Thiele
 * 
 */
public abstract class AddSynDiaElemAction implements IAction {

	protected int oldIndex;
	
	protected Concatenation parent;

	protected boolean undo;

	/**
	 * 
	 * @param oldNullElem
	 *            this <code>NullElem</code> is used to define where the new
	 *            syntax diagram element has to be added.
	 */
	public AddSynDiaElemAction(NullElem oldNullElem) {
		this.oldIndex = oldNullElem.getIndex();
		parent = (Concatenation) oldNullElem.getParent();
		undo = false;
	}
}
