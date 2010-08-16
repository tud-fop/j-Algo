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
import org.jalgo.module.am0c0.model.c0.ast.BoolExpression;

/**
 * Implements C0 boolexptrans function
 * @author Felix Schmitt
 *
 */
public class BoolExpTrans extends TransformFunction {

	private BoolExpression boolExpression;

	public BoolExpTrans(BoolExpression token,
			TreeAddress address) {
		super(token, address);
		this.boolExpression = token;
		
		description = Messages.getString("am0c0", "BoolExpTrans.0") +  //$NON-NLS-1$
			Messages.getString("am0c0", "BoolExpTrans.1") +  //$NON-NLS-1$
            Messages.getString("am0c0", "BoolExpTrans.2") +  //$NON-NLS-1$
            Messages.getString("am0c0", "BoolExpTrans.3") +  //$NON-NLS-1$
            Messages.getString("am0c0", "BoolExpTrans.4") +  //$NON-NLS-1$
            Messages.getString("am0c0", "BoolExpTrans.5"); //$NON-NLS-1$
	}

	@Override
	public List<TransformFunction> apply(SymbolTable symbolTable) {
		List<TransformFunction> result = new ArrayList<TransformFunction>();

		SimpleExpTrans simpleExpTrans1 = new SimpleExpTrans(boolExpression
				.getSimpleExp1(), null);
		SimpleExpTrans simpleExpTrans2 = new SimpleExpTrans(boolExpression
				.getSimpleExp2(), null);

		AtomicTrans atomicTrans = new AtomicTrans(null,
				null, boolExpression.getRelation(), -1);

		result.add(simpleExpTrans1);
		result.add(simpleExpTrans2);
		result.add(atomicTrans);
		
		return result;
	}

	@Override
	public String getCodeText() {
		return "boolexptrans(" + boolExpression.getCodeText() + ", tab)"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
