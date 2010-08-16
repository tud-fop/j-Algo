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
 * Abstract AST representation type for the syntactic variable {@code Factor}.
 * It is used as a supertype for its inner classes that represent the actual
 * syntactic variants of a {@code Factor}.
 * 
 * @author Martin Morgenstern
 */
public abstract class Factor extends C0AST {
	/**
	 * A {@code Factor} that contains a {@code SimpleExpr} in parentheses.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class CompExprFactor extends Factor {
		private final SimpleExpr expr;
		private final String codeText;

		/**
		 * @param expr
		 *            the contained {@code SimpleExpr}
		 */
		public CompExprFactor(final SimpleExpr expr) {
			this.expr = expr;
			codeText = "(" + expr.getCodeText() + ")";
		}

		/**
		 * @return the contained {@code SimpleExpr}
		 */
		public SimpleExpr getSimpleExpr() {
			return expr;
		}

		@Override
		protected String getCodeTextInternal() {
			return codeText;
		}
	}

	/**
	 * A {@code Factor} that is an identifier.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class IdentFactor extends Factor {
		private final String ident;

		public IdentFactor(final String ident) {
			if (null == ident) {
				this.ident = "";
			} else {
				this.ident = ident;
			}
		}

		public String getIdentName() {
			return ident;
		}

		@Override
		protected String getCodeTextInternal() {
			return ident;
		}
	}

	/**
	 * A {@code Factor} that is a number.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class NumberFactor extends Factor {
		public NumberFactor(final int num) {
			this.num = num;
		}

		public int getNumber() {
			return num;
		}

		private final int num;

		@Override
		protected String getCodeTextInternal() {
			return Integer.toString(num);
		}
	}
}
