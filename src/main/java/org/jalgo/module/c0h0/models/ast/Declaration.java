package org.jalgo.module.c0h0.models.ast;

import java.util.ArrayList;

import org.jalgo.module.c0h0.models.ast.tools.Iterable;

/**
 * Contains all declarated Vars
 *
 */
public class Declaration extends Symbol implements Iterable {
	private ArrayList<Var> vars;

	/**
	 * @param b
	 */
	public Declaration(Var b) {
		vars = new ArrayList<Var>();
		vars.add(b);
	}

	/**
	 * adds a variable which has to be added to the Declaration
	 * @param b
	 * 		Var which has to be added to the Declaration
	 */
	public void addVariable(Var b) {
		vars.add(b);
	}

	/**
	 * Returns the specific Var with the wanted index
	 * 
	 * @param index
	 * 			index of the wanted Var
	 * @return Var
	 * 			returns the Var
	 */
	public Var getVariable(int index) {
		return vars.get(index);
	}

	/**
	 * Returns all Vars which got a Scanf-Statement
	 * 
	 * @return scanfs
	 * 			ArrayList<Var> with all Variables in the sequence
	 */
	public ArrayList<Var> getVariableList() {
		return vars;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.tools.Iterable#getSequence()
	 */
	public ArrayList<Iterable> getSequence() {
		return new ArrayList<Iterable>(vars);
	}
}
