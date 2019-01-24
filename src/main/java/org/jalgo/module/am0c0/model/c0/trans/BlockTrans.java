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

import java.util.ArrayList;
import java.util.List;

import org.jalgo.main.util.Messages;
import org.jalgo.module.am0c0.model.TreeAddress;
import org.jalgo.module.am0c0.model.c0.ast.Block;

/**
 * Implements C0 blocktrans function
 * 
 * @author Felix Schmitt
 * 
 */
public class BlockTrans extends TransformFunction {

	private Block block;

	public BlockTrans(Block token, TreeAddress address) {
		super(token, address);
		this.block = token;

		description = Messages.getString("am0c0", "BlockTrans.0") //$NON-NLS-1$
				+ Messages.getString("am0c0", "BlockTrans.1") //$NON-NLS-1$
				+ Messages.getString("am0c0", "BlockTrans.2") //$NON-NLS-1$
				+ Messages.getString("am0c0", "BlockTrans.3"); //$NON-NLS-1$
	}

	@Override
	/**
	 * see {@link TransformFunction#apply}
	 * updates the symbol table
	 */
	public List<TransformFunction> apply(SymbolTable symbolTable) throws TransException {
		List<TransformFunction> result = new ArrayList<TransformFunction>();

		UpdateTrans updateTrans = new UpdateTrans(block.getDeclaration(), null);
		updateTrans.updateTable(symbolTable);
		// we should have our symbol table ready now
		
		if (block.getStatementSequence() == null) {
			StubTrans stub = new StubTrans(null, new TreeAddress());
			result.add(stub);
			return result;
		}
		
		STSeqTrans stSeqTrans = new STSeqTrans(block.getStatementSequence(), new TreeAddress());

		result.add(stSeqTrans);
		return result;
	}

	/**
	 * special version of apply() for BlockTrans which does not alter the symbol
	 * table
	 * 
	 * @return see {@link BlockTrans#apply()}
	 */
	public List<TransformFunction> applyPreview() {
		List<TransformFunction> result = new ArrayList<TransformFunction>();

		STSeqTrans stSeqTrans = new STSeqTrans(block.getStatementSequence(),
				new TreeAddress());

		result.add(stSeqTrans);
		return result;
	}

	@Override
	/*** see {@link TransformFunction#getCodeText}
	 * 
	 */
	public String getCodeText() {
		return "blocktrans(" + block.getCodeText() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
