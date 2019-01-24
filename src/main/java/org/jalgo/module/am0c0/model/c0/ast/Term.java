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
 * Abstract AST representation for the syntactic variable {@code Term}. Terms
 * have different actual syntactic variants (e.g. {@code MultTerm}) and can also
 * be grouped into binary and unary terms.
 * 
 * @author Martin Morgenstern
 */
public abstract class Term extends C0AST {
	/**
	 * Terms that take two arguments, a term and a factor.
	 * 
	 * @author Martin Morgenstern
	 */
	public static abstract class BinTerm extends Term {
		protected final Term left;
		protected final Factor right;

		protected BinTerm(Term left, Factor right) {
			this.left = left;
			this.right = right;
		}

		/**
		 * @return the term from the left hand side of the term
		 */
		public Term getLeft() {
			return left;
		}

		/**
		 * @return the factor from the right hand side of the term
		 */
		public Factor getRight() {
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
	 * Unary term that contains a factor.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class FactorTerm extends Term {
		public FactorTerm(Factor f) {
			factor = f;
		}

		public Factor getFactor() {
			return factor;
		}

		private final Factor factor;

		@Override
		protected String getCodeTextInternal() {
			return factor.getCodeText();
		}
	}

	/**
	 * Multiplication term.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class MultTerm extends BinTerm {
		public MultTerm(Term left, Factor right) {
			super(left, right);
		}

		@Override
		public String getOperatorSign() {
			return "*";
		}
	}

	/**
	 * Division term.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class DivTerm extends BinTerm {
		public DivTerm(Term left, Factor right) {
			super(left, right);
		}

		@Override
		public String getOperatorSign() {
			return "/";
		}
	}

	/**
	 * Modulo term.
	 * 
	 * @author Martin Morgenstern
	 */
	public static class ModTerm extends BinTerm {
		public ModTerm(Term left, Factor right) {
			super(left, right);
		}

		@Override
		public String getOperatorSign() {
			return "%";
		}
	}
}