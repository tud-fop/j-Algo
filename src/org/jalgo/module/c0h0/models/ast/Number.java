package org.jalgo.module.c0h0.models.ast;

import java.util.ArrayList;

/**
 * Representing a Number in the AST
 *
 */
public class Number extends Term {
	private int number;
	private UnaryType type;

	/**
	 * @param number
	 */
	public Number(int number) {
		this.number = number;
		type = UnaryType.UNDEFINED;
	}

	/**
	 * @param number
	 * @param type
	 */
	public Number(int number, UnaryType type) {
		this.number = number;
		this.type = type;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.Term#toString()
	 */
	public String toString() {
		String text;
		if (type.equals(UnaryType.PLUS)) {
			text = "+" + String.valueOf(number);
		} else if (type.equals(UnaryType.MINUS)) {
			text = "-" + String.valueOf(number);
		} else {
			text = String.valueOf(number);
		}
		return text;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.Term#getVars()
	 */
	public ArrayList<Var> getVars() {
		return new ArrayList<Var>();
	}
}
