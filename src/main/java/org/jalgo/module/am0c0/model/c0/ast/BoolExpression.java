/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2010 j-Algo-Team, j-algo-development@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jalgo.module.am0c0.model.c0.ast;

import org.jalgo.module.am0c0.model.c0.trans.AtomicTrans.AtomicType;

/**
 * AST part that represents the syntactic variable {@code BoolExpression}.
 * 
 * @author Martin Morgenstern
 */
public class BoolExpression extends C0AST {
	private final SimpleExpr left;
	private final AtomicType rel;
	private final SimpleExpr right;
	private final String codeText;

	/**
	 * @param left
	 *            the simple expression on the left hand side of the relation
	 * @param rel
	 *            the relation
	 * @param right
	 *            the simple expression on the right hand side of the relation
	 */
	public BoolExpression(final SimpleExpr left, final AtomicType rel,
			final SimpleExpr right) {
		this.left = left;
		this.rel = rel;
		this.right = right;
		codeText = initCodeText();
	}

	/**
	 * @return the relation type of the boolean expression.
	 */
	public AtomicType getRelation() {
		return rel;
	}

	/**
	 * @return the {@code SimpleExpr} on the left hand side of the relation.
	 */
	public SimpleExpr getSimpleExp1() {
		return left;
	}

	/**
	 * @return the {@code SimpleExpr} on the right hand side of the relation.
	 */
	public SimpleExpr getSimpleExp2() {
		return right;
	}

	/**
	 * Gets the cached C0 code representation of this object, e.g.
	 * {@code a >= b} .
	 * 
	 * @return C0 code representation of this object
	 */
	@Override
	protected String getCodeTextInternal() {
		return codeText;
	}

	/**
	 * Construct the C0 code representation for this object. This method should
	 * only be invoked when all private fields of this object are initialized
	 * (this is checked using assertions).
	 * 
	 * @return C0 code representation of this object
	 */
	private String initCodeText() {
		assert rel != null;
		assert left != null;
		assert right != null;

		String relation;

		switch (rel) {
		case EQ:
			relation = "==";
			break;
		case NE:
			relation = "!=";
			break;
		case LT:
			relation = "<";
			break;
		case GT:
			relation = ">";
			break;
		case LE:
			relation = "<=";
			break;
		case GE:
			relation = ">=";
			break;
		default:
			throw new AssertionError(rel); // should not happen
		}

		return left.getCodeText() + " " + relation + " " + right.getCodeText();
	}
}
