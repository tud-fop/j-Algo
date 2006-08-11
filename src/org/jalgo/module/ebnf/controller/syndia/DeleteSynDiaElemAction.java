package org.jalgo.module.ebnf.controller.syndia;

import org.jalgo.module.ebnf.util.IAction;

/**
 * An abstract class for all actions that have to delete elements from a
 * concatenation.
 * 
 * @author Michael Thiele
 * 
 */
public abstract class DeleteSynDiaElemAction implements IAction {

	/**
	 * Is saved to re-insert at the right place.
	 */
	protected int index;
	
}
