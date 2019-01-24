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
 * AST representation for the syntactic variable {@code ConstDeclaration}. Note
 * that at this stage <strong>no semantic check</strong> is done, i.e. this
 * representation explicitly allows multiple constants with the same name.
 * 
 * @author Martin Morgenstern
 */
public class ConstDeclaration extends C0AST {
	private final List<ConstIdent> constants = new ArrayList<ConstIdent>();

	/**
	 * Initialize a constant declaration with the initial element {@code ident}.
	 * 
	 * @param ident
	 *            the constant identifier that is to be added to the declaration
	 */
	public ConstDeclaration(ConstIdent ident) {
		constants.add(ident);
	}

	/**
	 * Add constants to the declaration. <em>Note:</em> multiple identifiers
	 * with the same name are allowed.
	 * 
	 * @param ident
	 *            the constant identifier that is to be added to the declaration
	 */
	public void addConstant(ConstIdent ident) {
		constants.add(ident);
	}

	/**
	 * Get a list containing values of the type {@link ConstIdent}. A defensive
	 * copy is returned to ensure that the internal list can only be changed by
	 * the method {@code addConstant}.
	 * 
	 * @return a list containing all constant identifiers of this declaration
	 */
	public List<ConstIdent> getConstants() {
		return new ArrayList<ConstIdent>(constants);
	}
	
	@Override
	protected String getCodeTextInternal() {
		StringBuilder codeText = new StringBuilder("const ");

		for (int i = 0; i < constants.size(); i++) {
			codeText.append(constants.get(i).getName());
			codeText.append(" = ");
			codeText.append(constants.get(i).getValue());

			if (i < constants.size() - 1) {
				codeText.append(", ");
			}
		}

		codeText.append(";\n");
		return codeText.toString();
	}
}
