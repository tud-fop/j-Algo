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
 * AST representation of the syntactic variable {@code Declaration}. It contains
 * a {@code ConstDeclaration} and a {@code VarDeclaration} (both optional).
 * 
 * @author Martin Morgenstern
 */
public class Declaration extends C0AST {
	private final ConstDeclaration constDecl;
	private final VarDeclaration varDecl;
	private final String codeText;

	/**
	 * @param constDecl
	 *            the optional {@code ConstDeclaration}
	 * @param varDecl
	 *            the optional {@code VarDeclaration}
	 */
	public Declaration(ConstDeclaration constDecl, VarDeclaration varDecl) {
		this.constDecl = constDecl;
		this.varDecl = varDecl;
		codeText = initCodeText();
	}

	/**
	 * @return the {@code ConstDeclaration} or {@code null}
	 */
	public ConstDeclaration getConstDecl() {
		return constDecl;
	}

	/**
	 * @return the {@code VarDeclaration} or {@code null}
	 */
	public VarDeclaration getVarDecl() {
		return varDecl;
	}

	@Override
	protected String getCodeTextInternal() {
		return codeText;
	}

	private String initCodeText() {
		StringBuilder result = new StringBuilder();

		if (null != constDecl) {
			result.append(constDecl.getCodeText());
		}

		if (null != varDecl) {
			result.append(varDecl.getCodeText());
		}

		return result.toString();
	}
}
