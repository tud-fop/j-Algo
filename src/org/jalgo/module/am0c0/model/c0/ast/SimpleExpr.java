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

/**
 * Abstract AST representation of the syntactic variable {@code
 * SimpleExpression}. Simple expressions can be devided into unary and binary
 * expressions. This abstract class contains AST types for the actual syntactic
 * variants of a {@code SimpleExpression}.
 * 
 * @author Martin Morgenstern
 */
public abstract class SimpleExpr extends C0AST {
	/**
	 * Abstract class for all binary simple expressions, i.e. simple expressions
	 * that have two arguments (a simple expression and a term).
	 * 
	 * @author Martin Morgenstern
	 */
	public static abstract class BinSimpleExpr extends SimpleExpr {
		private final SimpleExpr left;
		private final Term right;

		public BinSimpleExpr(SimpleExpr left, Term right) {
			this.left = left;
			this.right = right;
		}

		/**
		 * Get the left operand, which is a simple expression.
		 * 
		 * @return
		 */
		public SimpleExpr getLeft() {
			return left;
		}

		/**
		 * Get the right operand, which is a term.
		 * 
		 * @return
		 */
		public Term getRight() {
			return right;
		}

		@Override
		protected String getCodeTextInternal() {
			return left.getCodeText() + 
				" " + 
				getOperatorSign() + 
				" " + 
				right.getCodeText();
		}
		
		public abstract String getOperatorSign();
	}

	/**
	 * Represents binary plus expressions.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class PlusExpr extends BinSimpleExpr {
		public PlusExpr(SimpleExpr left, Term right) {
			super(left, right);
		}

		@Override
		public String getOperatorSign() {
			return "+";
		}
	}

	/**
	 * Represents binary minus expressions.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class MinusExpr extends BinSimpleExpr {
		public MinusExpr(SimpleExpr left, Term right) {
			super(left, right);
		}

		@Override
		public String getOperatorSign() {
			return "-";
		}
	}

	/**
	 * Abstract class for all unary simple expressions, i.e. simple expressions
	 * that only have one operand (a term).
	 * 
	 * @author Martin Morgenstern
	 */
	public static abstract class UnarySimpleExpr extends SimpleExpr {
		private final Term term;

		public UnarySimpleExpr(Term t) {
			term = t;
		}

		public Term getTerm() {
			return term;
		}

		@Override
		protected String getCodeTextInternal() {
			return getOperatorSign() + term.getCodeText();
		}

		public abstract String getOperatorSign();
	}

	/**
	 * Represents unary plus expressions.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class UnaryPlusExpr extends UnarySimpleExpr {
		public UnaryPlusExpr(Term t) {
			super(t);
		}

		@Override
		public String getOperatorSign() {
			return "";
		}
	}

	/**
	 * Represents unary minus expressions.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class UnaryMinusExpr extends UnarySimpleExpr {
		public UnaryMinusExpr(Term t) {
			super(t);
		}

		@Override
		public String getOperatorSign() {
			return "-";
		}
	}
}
