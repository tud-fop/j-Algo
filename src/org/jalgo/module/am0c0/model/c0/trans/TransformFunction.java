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

import org.apache.commons.lang.StringUtils;
import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.model.CodeObject;
import org.jalgo.module.am0c0.model.TreeAddress;
import org.jalgo.module.am0c0.model.c0.ast.C0AST;

public abstract class TransformFunction extends CodeObject implements Cloneable {
	protected String description;
	
	/**
	 * a C0-to-AM0 transformation function
	 * @param token the C0 token for this function
	 * @param address the basic address of this function, which is shown in the JEditor
	 */
	public TransformFunction(C0AST token, TreeAddress address) {
		super(address);
		description = Messages.getString("am0c0", "TransformFunction.0"); //$NON-NLS-1$
	}

	@Override
	public TransformFunction clone() {
		try {
			TransformFunction clone = (TransformFunction) super.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e); // never happens
		}
	}

	/**
	 * returns the description of this TransformFunction
	 * @return a String representing which describes this TransformFunction
	 */
	public String getDescription() {
		return description;
	}

	@Override
	public int getLinesCount() {
		return StringUtils.countMatches(getCodeText(), "\n") + 1; //$NON-NLS-1$
	}

	@Override
	public TreeAddress getAddress() {
		return (TreeAddress)(super.getAddress());
	}
	
	/**
	 * Applies this TransformFunction and returns a List of resulting TransformFunctions
	 * @param symbolTable the current SymbolTable
	 * @return a List of generated TransformFunctions
	 * @throws TransException
	 */
	public abstract List<TransformFunction> apply(SymbolTable symbolTable) throws TransException;
}
