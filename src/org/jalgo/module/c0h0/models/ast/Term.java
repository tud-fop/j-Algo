package org.jalgo.module.c0h0.models.ast;

import java.util.ArrayList;

/**
 * Representing a Term in the AST
 *
 */
public class Term {
	private Term term;
	private UnaryType type;

	public Term() {
		type = UnaryType.UNDEFINED;
	}

	/**
	 * @param term
	 */
	public Term(Term term) {
		this.term = term;
		type = UnaryType.UNDEFINED;
	}

	/**
	 * @param term
	 * @param type
	 */
	public Term(Term term, UnaryType type) {
		this.term = term;
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String text;
		if (type.equals(UnaryType.PLUS)) {
			text = "+(" + term + ")";
		} else if (type.equals(UnaryType.MINUS)) {
			text = "-(" + term + ")";
		} else {
			text = "(" + term + ")";
		}
		return text;
	}

	/**
	 * returns an ArrayList<Var> with all Vars contained in the whole Term
	 * @return list
	 * 			ArrayList<Var> with all Vars contained in the whole Term
	 */
	public ArrayList<Var> getVars() {
		ArrayList<Var> list = new ArrayList<Var>();
		list.addAll(term.getVars());
		return list;
	}
}
