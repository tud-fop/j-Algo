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
package org.jalgo.module.am0c0.model.c0.trans;

import java.util.List;

import org.jalgo.module.am0c0.model.TreeAddress;
import org.jalgo.module.am0c0.model.c0.ast.Declaration;
import org.jalgo.module.am0c0.model.c0.ast.C0AST.*;

/**
 * Implementation of the C0 {@code updatetrans} function.
 * 
 * @author Felix Schmitt
 * @author Martin Morgenstern
 */
public class UpdateTrans extends TransformFunction {
	/**
	 * Contains the declaration part of the abstract syntax tree.
	 */
	private final Declaration declaration;

	public UpdateTrans(Declaration token, TreeAddress address) throws TransException {
		super(token, address);
		this.declaration = token;
	}

	/**
	 * Runs the actual update function on symbolTable
	 * 
	 * @param symbolTable
	 *            the {@link SymbolTable} which has be to updated
	 * @throws TransException
	 */
	public void updateTable(SymbolTable symbolTable) throws TransException {
		try {
			updateConst(symbolTable);
			updateVar(symbolTable);
		} catch (SymbolException e) {
			symbolTable.clear();
			throw new TransException(e.getMessage());
		}
	}

	/**
	 * Fills the symbol table with the declared constants.
	 * 
	 * @param symbolTable
	 *            the {@link SymbolTable} which has be to updated
	 * @throws SymbolException
	 */
	private void updateConst(SymbolTable symbolTable) throws SymbolException {
		if (declaration.getConstDecl() != null) {
			List<ConstIdent> constants = declaration.getConstDecl().getConstants();

			for (ConstIdent ident : constants) {
				symbolTable.add(Symbol.constSymbol(ident.getName(), ident.getValue()));
			}
		}
	}

	/**
	 * Fills the symbol table with the declared variables.
	 * 
	 * @param symbolTable
	 *            the {@link SymbolTable} which has be to updated
	 * @throws SymbolException
	 */
	private void updateVar(SymbolTable symbolTable) throws SymbolException {
		if (declaration.getVarDecl() != null) {
			List<Ident> vars = declaration.getVarDecl().getDeclaredVars();

			int addressCounter = 1;

			for (Ident ident : vars) {
				symbolTable.add(Symbol.varSymbol(ident.getName(), addressCounter));
				++addressCounter;
			}
		}
	}

	@Override
	public List<TransformFunction> apply(SymbolTable symbolTable) {
		throw new IllegalAccessError("Applying UpdateTrans is not allowed." //$NON-NLS-1$
				+ " Its constructor implicitly applies."); //$NON-NLS-1$
	}

	@Override
	public String getCodeText() {
		return "update(" + declaration.getCodeText() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
