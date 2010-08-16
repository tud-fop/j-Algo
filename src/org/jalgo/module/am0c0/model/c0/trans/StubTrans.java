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
import org.jalgo.module.am0c0.model.c0.ast.C0AST;

/**
 * Stub TransformFunction used for address merging
 * @author Felix Schmitt
 *
 */
public class StubTrans extends TransformFunction {

	public StubTrans(C0AST token, TreeAddress address) {
		super(token, address);
	}

	@Override
	public List<TransformFunction> apply(SymbolTable symbolTable) {
		throw new IllegalAccessError("Applying StubTrans is not allowed.");
	}

	@Override
	public String getCodeText() {
		return "";
	}

}
