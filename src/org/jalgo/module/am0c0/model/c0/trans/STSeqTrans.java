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
import org.jalgo.module.am0c0.model.c0.ast.Statement;
import org.jalgo.module.am0c0.model.c0.ast.StatementSequence;

/**
 * Implements C0 stseqtrans function
 * 
 * @author Felix Schmitt
 */
public class STSeqTrans extends TransformFunction {
	/**
	 * The statement sequence, which may be {@code null}.
	 */
	private StatementSequence statementSequence;

	public STSeqTrans(StatementSequence token,
			TreeAddress address) {
		super(token, address);
		this.statementSequence = token;

		description = Messages.getString("am0c0", "STSeqTrans.0") //$NON-NLS-1$
				+ Messages.getString("am0c0", "STSeqTrans.1") //$NON-NLS-1$
				+ Messages.getString("am0c0", "STSeqTrans.2") //$NON-NLS-1$
				+ Messages.getString("am0c0", "STSeqTrans.3") //$NON-NLS-1$
				+ Messages.getString("am0c0", "STSeqTrans.4") //$NON-NLS-1$
				+ Messages.getString("am0c0", "STSeqTrans.5") //$NON-NLS-1$
				+ Messages.getString("am0c0", "STSeqTrans.6"); //$NON-NLS-1$
	}

	@Override
	public List<TransformFunction> apply(SymbolTable symbolTable) throws TransException {
		List<TransformFunction> result = new ArrayList<TransformFunction>();

		if (null == statementSequence || statementSequence.getStatements().size() == 0) {
			throw new TransException(Messages.getString("am0c0", "STSeqTrans.7")); //$NON-NLS-1$
		}

		TreeAddress baseAddr = new TreeAddress(getAddress());
		baseAddr.extend();
		
		for (int i = 0; i < statementSequence.getStatements().size(); i++) {
			Statement statement = statementSequence.getStatements().get(i);

			STTrans stTrans = new STTrans(statement,
					new TreeAddress(baseAddr));
			result.add(stTrans);

			baseAddr.increase();
		}

		return result;
	}

	@Override
	public String getCodeText() {
		StringBuilder result = new StringBuilder();
		result.append("stseqtrans(");

		if (null != statementSequence) {
			result.append(statementSequence.getCodeText());
		}

		result.append(", tab, " + address + ")");

		return result.toString();
	}
}
