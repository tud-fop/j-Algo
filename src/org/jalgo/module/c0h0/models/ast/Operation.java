package org.jalgo.module.c0h0.models.ast;

import java.util.ArrayList;

/**
 * Representing an Operation in the AST
 *
 */
public class Operation extends Term {
	private OperationType op;
	private Term left, right;

	/**
	 * @param left
	 * @param op
	 * @param right
	 */
	public Operation(Term left, OperationType op, Term right) {
		this.op = op;
		this.left = left;
		this.right = right;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.c0h0.models.ast.Term#toString()
	 */
	public String toString() {
		String text = "";
		text += left;
		if (op.equals(OperationType.ADD)) {
			text += " + ";
		} else if (op.equals(OperationType.SUB)) {
			text += " - ";
		} else if (op.equals(OperationType.MUL)) {
			text += " * ";
		} else if (op.equals(OperationType.DIV)) {
			text += " / ";
		} else if (op.equals(OperationType.MOD)) {
			text += " % ";
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
