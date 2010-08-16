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
 * AST part that represents the syntactic variable {@code Block}. A block
 * contains a {@code Declaration} and, optionally, a {@code StatementSequence}.
 * 
 * @author Martin Morgenstern
 */
public class Block extends C0AST {
	private final Declaration declaration;
	private final StatementSequence statementSequence;
	private final String codeText;

	/**
	 * Construct a new {@code Block} with given a given {@code Declaration} and
	 * an optional {@code StatementSequence}.
	 * 
	 * @param declaration
	 *            the declaration
	 * @param statementSequence
	 *            the statement sequence or {@code null}
	 * @throws NullPointerException
	 *             if declaration is {@code null}
	 */
	public Block(final Declaration declaration, final StatementSequence statementSequence) {
		if (null == declaration) {
			throw new NullPointerException();
		}

		this.declaration = declaration;
		this.statementSequence = statementSequence;
		codeText = initCodeText();
	}

	/**
	 * @return the declaration part of the block
	 */
	public Declaration getDeclaration() {
		return declaration;
	}

	/**
	 * @return the statement sequence or {@code null} if the block does not
	 *         contain a statement sequence
	 */
	public StatementSequence getStatementSequence() {
		return statementSequence;
	}

	@Override
	protected String getCodeTextInternal() {
		return codeText;
	}

	private String initCodeText() {
		assert declaration != null;

		final StringBuilder result = new StringBuilder();

		result.append("{\n");
		result.append(declaration.getCodeText());

		if (null != statementSequence) {
			result.append(statementSequence.getCodeText());
		}

		result.append("return 0;\n}");

		return result.toString();
	}
}
