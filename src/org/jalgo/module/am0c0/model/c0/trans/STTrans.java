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
import org.jalgo.module.am0c0.model.c0.ast.*;
import org.jalgo.module.am0c0.model.c0.ast.Statement.*;
import org.jalgo.module.am0c0.model.c0.trans.AtomicTrans.AtomicType;
import org.jalgo.module.am0c0.model.c0.trans.Symbol.SymbolType;

/**
 * Implements C0 sttrans function
 * 
 * @author Felix Schmitt
 * 
 */
public class STTrans extends TransformFunction {

	private Statement statement;

	public STTrans(Statement token, TreeAddress address) {
		super(token, address);
		this.statement = token;

		description = Messages.getString("am0c0", "STTrans.0"); //$NON-NLS-1$

		if (statement instanceof CompStatement)
			description += Messages.getString("am0c0", "STTrans.1") //$NON-NLS-1$
					+ Messages.getString("am0c0", "STTrans.2") //$NON-NLS-1$
					+ Messages.getString("am0c0", "STTrans.3"); //$NON-NLS-1$

		if (statement instanceof AssignmentStatement)
			description += Messages.getString("am0c0", "STTrans.4") //$NON-NLS-1$
					+ Messages.getString("am0c0", "STTrans.5") //$NON-NLS-1$
					+ Messages.getString("am0c0", "STTrans.6"); //$NON-NLS-1$

		if (statement instanceof ScanfStatement)
			description += Messages.getString("am0c0", "STTrans.7") //$NON-NLS-1$
					+ Messages.getString("am0c0", "STTrans.8") //$NON-NLS-1$
					+ Messages.getString("am0c0", "STTrans.9"); //$NON-NLS-1$

		if (statement instanceof PrintfStatement)
			description += Messages.getString("am0c0", "STTrans.10") //$NON-NLS-1$
					+ Messages.getString("am0c0", "STTrans.11") //$NON-NLS-1$
					+ Messages.getString("am0c0", "STTrans.12"); //$NON-NLS-1$

		if (statement instanceof IfStatement)
			description += Messages.getString("am0c0", "STTrans.13") + Messages.getString("am0c0", "STTrans.14") //$NON-NLS-1$ //$NON-NLS-2$
					+ Messages.getString("am0c0", "STTrans.15") + Messages.getString("am0c0", "STTrans.16") + Messages.getString("am0c0", "STTrans.17") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ Messages.getString("am0c0", "STTrans.18") //$NON-NLS-1$
					+ Messages.getString("am0c0", "STTrans.19"); //$NON-NLS-1$

		if (statement instanceof IfElseStatement)
			description += Messages.getString("am0c0", "STTrans.20") //$NON-NLS-1$
					+ Messages.getString("am0c0", "STTrans.21") + Messages.getString("am0c0", "STTrans.22") //$NON-NLS-1$ //$NON-NLS-2$
					+ Messages.getString("am0c0", "STTrans.23") + Messages.getString("am0c0", "STTrans.24") //$NON-NLS-1$ //$NON-NLS-2$
					+ Messages.getString("am0c0", "STTrans.25") + Messages.getString("am0c0", "STTrans.26") //$NON-NLS-1$ //$NON-NLS-2$
					+ Messages.getString("am0c0", "STTrans.27") //$NON-NLS-1$
					+ Messages.getString("am0c0", "STTrans.28"); //$NON-NLS-1$

		if (statement instanceof WhileStatement)
			description += Messages.getString("am0c0", "STTrans.29") //$NON-NLS-1$
					+ Messages.getString("am0c0", "STTrans.30") + Messages.getString("am0c0", "STTrans.31") //$NON-NLS-1$ //$NON-NLS-2$
					+ Messages.getString("am0c0", "STTrans.32") + Messages.getString("am0c0", "STTrans.33") + Messages.getString("am0c0", "STTrans.34") //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					+ Messages.getString("am0c0", "STTrans.35") //$NON-NLS-1$
					+ Messages.getString("am0c0", "STTrans.36"); //$NON-NLS-1$
	}

	@Override
	public List<TransformFunction> apply(SymbolTable symbolTable) throws TransException {
		List<TransformFunction> result = new ArrayList<TransformFunction>();

		if (statement instanceof CompStatement) {
			StatementSequence statementSequence = ((CompStatement) statement)
					.getStatementSequence();

			STSeqTrans stSeqTrans = new STSeqTrans(statementSequence, getAddress());
			result.add(stSeqTrans);
			return result;
		}

		if (statement instanceof AssignmentStatement) {
			String id = ((AssignmentStatement) statement).getIdent();
			try {
				if (symbolTable.exists(id) && symbolTable.getType(id) == SymbolType.ST_VAR) {
					SimpleExpTrans simpleExpTrans = new SimpleExpTrans(
							((AssignmentStatement) statement).getExpr(), null);
					AtomicTrans atomicTrans = new AtomicTrans(null, null, AtomicType.STORE,
							symbolTable.getAddress(id));

					result.add(simpleExpTrans);
					result.add(atomicTrans);
					return result;

				} else {
					throw new TransException(Messages.getString("am0c0", "STTrans.AssignError")); //$NON-NLS-1$
				}
			} catch (SymbolException e) {
				throw new TransException(e.getMessage());
			}
		}

		if (statement instanceof ScanfStatement) {
			String id = ((ScanfStatement) statement).getIdent();

			try {
				if (symbolTable.exists(id) && symbolTable.getType(id) == SymbolType.ST_VAR) {

					AtomicTrans atomicTrans = new AtomicTrans(null, null, AtomicType.READ,
							symbolTable.getAddress(id));

					result.add(atomicTrans);
					return result;
				} else {
					throw new TransException(Messages.getString("am0c0", "STTrans.ScanfError")); //$NON-NLS-1$
				}
			} catch (SymbolException e) {
				throw new TransException(e.getMessage());
			}
		}

		if (statement instanceof PrintfStatement) {
			String id = ((PrintfStatement) statement).getIdent();

			try {
				if (symbolTable.exists(id) && symbolTable.getType(id) == SymbolType.ST_VAR) {

					AtomicTrans atomicTrans = new AtomicTrans(null, null, AtomicType.WRITE,
							symbolTable.getAddress(id));

					result.add(atomicTrans);
					return result;
				} else {
					throw new TransException(Messages.getString("am0c0", "STTrans.PrintfError")); //$NON-NLS-1$
				}
			} catch (SymbolException e) {
				throw new TransException(e.getMessage());
			}
		}

		if (statement instanceof IfStatement) {
			BoolExpTrans boolExpTrans = new BoolExpTrans(((IfStatement) statement).getBoolExpr(),
					null);

			TreeAddress jmcTargetAddress = new TreeAddress(getAddress());
			jmcTargetAddress.extend();
			AtomicTrans atomicTrans = new AtomicTrans(null, null, AtomicType.JMC, 0,
					jmcTargetAddress);

			TreeAddress sttransAddress = new TreeAddress(jmcTargetAddress);
			sttransAddress.increase();
			STTrans stTrans = new STTrans(((IfStatement) statement).getFirstStatement(),
					sttransAddress);

			TreeAddress stubAddress = new TreeAddress(jmcTargetAddress, true);
			StubTrans stubTrans = new StubTrans(null, stubAddress);

			result.add(boolExpTrans);
			result.add(atomicTrans);
			result.add(stTrans);
			result.add(stubTrans);
			return result;
		}

		if (statement instanceof IfElseStatement) {
			BoolExpTrans boolExpTrans = new BoolExpTrans(((IfElseStatement) statement)
					.getBoolExpr(), null);

			TreeAddress jmcTargetAddress = new TreeAddress(getAddress());
			jmcTargetAddress.extend();
			AtomicTrans atomicTrans1 = new AtomicTrans(null, null, AtomicType.JMC, 0,
					jmcTargetAddress);

			TreeAddress sttransAddress1 = new TreeAddress(jmcTargetAddress);
			sttransAddress1.increase();
			STTrans stTrans1 = new STTrans(((IfElseStatement) statement).getFirstStatement(),
					sttransAddress1);

			TreeAddress jmpTargetAddress = new TreeAddress(sttransAddress1);
			jmpTargetAddress.increase();
			AtomicTrans atomicTrans2 = new AtomicTrans(null, null, AtomicType.JMP, 0,
					jmpTargetAddress);

			TreeAddress stubAddress1 = new TreeAddress(jmcTargetAddress, true);
			StubTrans stubTrans1 = new StubTrans(null, stubAddress1);

			TreeAddress sttransAddress2 = new TreeAddress(jmpTargetAddress);
			sttransAddress2.increase();
			STTrans stTrans2 = new STTrans(((IfElseStatement) statement).getSecondStatement(),
					sttransAddress2);

			TreeAddress stubAddress2 = new TreeAddress(jmpTargetAddress, true);
			StubTrans stubTrans2 = new StubTrans(null, stubAddress2);

			result.add(boolExpTrans);
			result.add(atomicTrans1);
			result.add(stTrans1);
			result.add(atomicTrans2);
			result.add(stubTrans1);
			result.add(stTrans2);
			result.add(stubTrans2);
			return result;
		}

		if (statement instanceof WhileStatement) {

			TreeAddress boolStubAddress = new TreeAddress(getAddress(), true);
			boolStubAddress.extend();

			StubTrans boolStub = new StubTrans(null,  boolStubAddress);

			BoolExpTrans boolExpTrans = new BoolExpTrans(
					((WhileStatement) statement).getBoolExpr(), null);

			TreeAddress jmcTargetAddress = new TreeAddress(boolStubAddress);
			jmcTargetAddress.increase();
			AtomicTrans atomicTrans1 = new AtomicTrans(null, null, AtomicType.JMC, 0,
					jmcTargetAddress);

			TreeAddress sttransAddress = new TreeAddress(jmcTargetAddress);
			sttransAddress.increase();
			STTrans stTrans = new STTrans(((WhileStatement) statement).getFirstStatement(),
					sttransAddress);

			TreeAddress jmpTargetAddress = new TreeAddress(boolStubAddress);
			AtomicTrans atomicTrans2 = new AtomicTrans(null, null, AtomicType.JMP, 0,
					jmpTargetAddress);

			TreeAddress stubAddress = new TreeAddress(jmcTargetAddress, true);
			StubTrans stubTrans = new StubTrans(null,  stubAddress);

			result.add(boolStub);
			result.add(boolExpTrans);
			result.add(atomicTrans1);
			result.add(stTrans);
			result.add(atomicTrans2);
			result.add(stubTrans);
			return result;
		}

		throw new IllegalStateException(
				"The correct instance of this Statement could not be determined."); //$NON-NLS-1$
	}

	@Override
	public String getCodeText() {
		return "sttrans(" + statement.getCodeText() + ", tab, " + address +  ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}

}
