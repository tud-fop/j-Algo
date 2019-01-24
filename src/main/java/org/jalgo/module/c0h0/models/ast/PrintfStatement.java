package org.jalgo.module.c0h0.models.ast;

import java.util.ArrayList;

import org.jalgo.module.c0h0.models.ast.tools.Iterable;

/**
 * Representing the PrintfStatement in the AST
 *
 */
public class PrintfStatement extends Symbol implements Iterable {
	private Var var;

	/**
	 * @param var
	 */
	public PrintfStatement(Var var) {
		this.var = var;
	}

	/**
	 * Returns the Var of the PrintfStatement
	 * 
	 * @return var
	 * 			Var of the PrintfStatement
	 */
	public Var getVar() {
		return var;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.tools.Iterable#getSequence()
	 */
	public ArrayList<Iterable> getSequence() {
		ArrayList<Iterable> arr = new ArrayList<Iterable>();
		arr.add(var);
		return arr;
	}

}
