package org.jalgo.module.c0h0.models.ast;

import java.util.ArrayList;

/**
 * Representing a Relation in the AST
 *
 */
public class Relation extends Term {
	private RelationType rel;
	private Term left, right;

	/**
	 * @param left
	 * @param rel
	 * @param right
	 */
	public Relation(Term left, RelationType rel, Term right) {
		this.rel = rel;
		this.left = left;
		this.right = right;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.Term#toString()
	 */
	public String toString() {
		String text = "";
		text += left;
		if (rel.equals(RelationType.EQ)) {
			text += " == ";
		} else if (rel.equals(RelationType.GE)) {
			text += " >= ";
		} else if (rel.equals(RelationType.GT)) {
			text += " > ";
		} else if (rel.equals(RelationType.LE)) {
			text += " <= ";
		} else if (rel.equals(RelationType.LT)) {
			text += " < ";
		} else {
			text += " != ";
		}
		text += right;
		return text;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.Term#getVars()
	 */
	public ArrayList<Var> getVars() {
		ArrayList<Var> list = new ArrayList<Var>();
		list.addAll(left.getVars());
		list.addAll(right.getVars());
		return list;
	}
}
