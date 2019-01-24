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

import java.util.ArrayList;
import java.util.List;

/**
 * AST representation for the syntactic variable {@code VarDeclaration}.
 * <em>Note:</em> no semantic checks are made, i.e. multiple variables with the
 * same names can be added to the declaration.
 * 
 * @author Martin Morgenstern
 */
public class VarDeclaration extends C0AST {
	private final List<Ident> vars = new ArrayList<Ident>();

	/**
	 * @param firstIdent
	 *            the first identifier to be added to the declaration list
	 */
	public VarDeclaration(Ident firstIdent) {
		vars.add(firstIdent);
	}

	/**
	 * @param ident
	 *            the identifier to be added to the declaration list
	 */
	public void addVariable(Ident ident) {
		vars.add(ident);
	}

	/**
	 * Get a copy of the list of identifiers. A defensive copy is returned to
	 * ensure that the internal list can only be changed by the method {@code
	 * addVariable}.
	 * 
	 * @return list of identifiers
	 */
	public List<Ident> getDeclaredVars() {
		return new ArrayList<Ident>(vars);
	}

	@Override
	protected String getCodeTextInternal() {
		StringBuilder codeText = new StringBuilder("int ");

		for (int i = 0; i < vars.size(); i++) {
			codeText.append(vars.get(i).getName());

			if (i < vars.size() - 1) {
				codeText.append(", ");
			}
		}

		codeText.append("\n");
		return codeText.toString();
	}
}
