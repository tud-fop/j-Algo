package org.jalgo.module.c0h0.models.ast;

import java.util.ArrayList;

import org.jalgo.module.c0h0.models.ast.tools.Iterable;

/**
 * Representing an If-statement in the AST
 *
 */
public class Var extends Term implements Iterable {
	private int index;
	private UnaryType type;

	/**
	 * @param str
	 */
	public Var(String str) {
		str = str.substring(1);
		index = Integer.parseInt(str);
		type = UnaryType.UNDEFINED;
	}

	/**
	 * @param str
	 * @param type
	 */
	public Var(String str, UnaryType type) {
		str = str.substring(1);
		index = Integer.parseInt(str);
		this.type = type;
	}

	/**
	 * returns the integer of the Var's index#
	 * @return index
	 * 			integer of the Var's index
	 */
	public int getIndex() {
		return index;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.Term#toString()
	 */
	public String toString() {
		String text;
		if (type.equals(UnaryType.PLUS)) {
			text = "+x" + String.valueOf(index);
		} else if (type.equals(UnaryType.MINUS)) {
			text = "-x" + String.valueOf(index);
		} else {
			text = "x" + String.valueOf(index);
		}
		return text;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.Term#getVars()
	 */
	public ArrayList<Var> getVars() {
		ArrayList<Var> list = new ArrayList<Var>();
		list.add(this);
		return list;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.tools.Iterable#getSequence()
	 */
	public ArrayList<Iterable> getSequence() {
		return new ArrayList<Iterable>();
	}
}
