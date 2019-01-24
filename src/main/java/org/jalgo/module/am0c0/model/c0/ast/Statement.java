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
 * AST representation of the syntactic variable {@code Statement}. There are six
 * actual variants of statements which are implemented as static inner classes.
 * Additionally, similar statements are grouped using static abstract inner
 * classes.
 * 
 * @author Martin Morgenstern
 */
public abstract class Statement extends C0AST {
	/**
	 * Abstract super class for {@code PrintfStatement} and
	 * {@code ScanfStatement}, as they share the same type of content (an
	 * identifier).
	 * 
	 * @author Martin Morgenstern
	 */
	public static abstract class IOStatement extends Statement {
		protected final String ident;

		/**
		 * @param ident
		 *            the identifier that is the source/destination of the I/O
		 *            operation
		 */
		public IOStatement(final String ident) {
			this.ident = ident;
		}

		/**
		 * @return the identifier that is the source/destination of the I/O
		 *         operation
		 */
		public String getIdent() {
			return ident;
		}
	}

	/**
	 * Represents the output operation "printf".
	 * 
	 * @author Martin Morgenstern
	 */
	public static class PrintfStatement extends IOStatement {
		private final String codeText;

		public PrintfStatement(final String ident) {
			super(ident);
			codeText = "printf(\"%i\", " + ident + ");";
		}

		@Override
		protected String getCodeTextInternal() {
			return codeText;
		}
	}

	/**
	 * Represents the input operation "scanf".
	 * 
	 * @author Martin Morgenstern
	 */
	public static class ScanfStatement extends IOStatement {
		private final String codeText;

		public ScanfStatement(final String ident) {
			super(ident);
			codeText = "scanf(\"%i\", &" + ident + ");";
		}

		@Override
		protected String getCodeTextInternal() {
			return codeText;
		}
	}

	/**
	 * Represents an if statement that has a {@code BoolExpression} and a
	 * {@code Statement}.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class IfStatement extends Statement {
		private final BoolExpression expr;
		private final Statement statement;
		private final String codeText;

		public IfStatement(final BoolExpression expr, final Statement statement) {
			this.expr = expr;
			this.statement = statement;
			codeText = "if (" + expr.getCodeText() + ")\n"
					+ statement.getCodeText();
		}

		public BoolExpression getBoolExpr() {
			return expr;
		}

		/**
		 * Alias for the method {@code getFirstStatement}.
		 * 
		 * @return
		 */
		public Statement getStatement() {
			return statement;
		}

		/**
		 * Gets the first statement (for if and while statements, this is the
		 * only statement).
		 * 
		 * @return the statement that immediately follows the head of the
		 *         conditional or loop statement
		 */
		public Statement getFirstStatement() {
			return statement;
		}

		@Override
		protected String getCodeTextInternal() {
			return codeText;
		}
	}

	/**
	 * Represents if-else statements. They differ from {@code IfStatement} only
	 * in one point: they contain an additional statement for the else-branch.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class IfElseStatement extends Statement {
		private final BoolExpression expr;
		private final Statement statement;
		private final Statement secondStatement;
		private final String codeText;

		public IfElseStatement(final BoolExpression expr,
				final Statement firstStatement, final Statement secondStatement) {
			this.expr = expr;
			this.statement = firstStatement;
			this.secondStatement = secondStatement;
			codeText = "if (" + expr.getCodeText() + ")\n"
					+ statement.getCodeText() + "\nelse\n"
					+ secondStatement.getCodeText();
		}

		public BoolExpression getBoolExpr() {
			return expr;
		}

		public Statement getFirstStatement() {
			return statement;
		}

		/**
		 * @return the statement that belongs to the else-branch
		 */
		public Statement getSecondStatement() {
			return secondStatement;
		}

		@Override
		protected String getCodeTextInternal() {
			return codeText;
		}
	}

	/**
	 * Representation of while statements.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class WhileStatement extends Statement {
		private final BoolExpression expr;
		private final Statement statement;
		private final String codeText;

		public WhileStatement(BoolExpression expr, Statement statement) {
			this.expr = expr;
			this.statement = statement;
			codeText = "while (" + expr.getCodeText() + ")\n"
					+ statement.getCodeText();
		}

		public BoolExpression getBoolExpr() {
			return expr;
		}

		public Statement getFirstStatement() {
			return statement;
		}

		@Override
		protected String getCodeTextInternal() {
			return codeText;
		}
	}

	/**
	 * Represents assignments of simple expressions to variables.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class AssignmentStatement extends Statement {
		private final String ident;
		private final SimpleExpr expr;
		private final String codeText;

		public AssignmentStatement(final String ident, final SimpleExpr expr) {
			this.ident = ident;
			this.expr = expr;
			codeText = ident + " = " + expr.getCodeText() + ";";
		}

		/**
		 * @return the name to which the result of the evaluation of the
		 *         expression is assigned
		 */
		public String getIdent() {
			return ident;
		}

		/**
		 * @return the expression on the right hand side of the assignment
		 */
		public SimpleExpr getExpr() {
			return expr;
		}

		@Override
		protected String getCodeTextInternal() {
			return codeText;
		}
	}

	/**
	 * Represents compound statements, i.e. statements that contain a statement
	 * sequence in parentheses.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class CompStatement extends Statement {
		private final StatementSequence seq;
		private final String codeText;

		public CompStatement(final StatementSequence seq) {
			this.seq = seq;
			codeText = "{\n" + seq.getCodeText() + "}";
		}

		/**
		 * @return the inner statement sequence
		 */
		public StatementSequence getStatementSequence() {
			return seq;
		}

		@Override
		protected String getCodeTextInternal() {
			return codeText;
		}
	}
}
