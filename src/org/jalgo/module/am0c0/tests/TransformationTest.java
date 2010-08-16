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
package org.jalgo.module.am0c0.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.jalgo.module.am0c0.model.AddressException;
import org.jalgo.module.am0c0.model.LineAddress;
import org.jalgo.module.am0c0.model.am0.AM0Program;
import org.jalgo.module.am0c0.model.am0.SimulationStatement;
import org.jalgo.module.am0c0.model.c0.C0Program;
import org.jalgo.module.am0c0.model.c0.ast.Program;
import org.jalgo.module.am0c0.model.c0.trans.AddressSolver;
import org.jalgo.module.am0c0.model.c0.trans.AtomicTrans;
import org.jalgo.module.am0c0.model.c0.trans.StubTrans;
import org.jalgo.module.am0c0.model.c0.trans.SymbolTable;
import org.jalgo.module.am0c0.model.c0.trans.Trans;
import org.jalgo.module.am0c0.model.c0.trans.TransException;
import org.jalgo.module.am0c0.model.c0.trans.TransformFunction;
import org.jalgo.module.am0c0.parser.c0.C0Parser;

/**
 * This is the complete transformation test
 * @author Felix Schmitt
 *
 */
public class TransformationTest extends TestCase {
	private List<TransformFunction> functions;
	private SymbolTable symbolTable;
	
	protected List<TransformFunction> getApplyFunc(int index) throws TransException {
		TransformFunction func = functions.get(index);

		if (func instanceof AtomicTrans || func instanceof StubTrans)
			throw new IllegalArgumentException(
					"This index points to a TransformFunctions which cannot be applied");

		return func.apply(symbolTable);
	}
	
	private void applyFunc(int index) throws IllegalStateException, TransException {
		List<TransformFunction> resultFunctions;

		try {
			resultFunctions = getApplyFunc(index);
			/**
			 * TODO: make history copy here
			 */
			functions.remove(index);
			functions.addAll(index, resultFunctions);
			/**
			 * TODO: update model here
			 */
		} catch (IllegalArgumentException e) {
			throw new IllegalStateException(e.getMessage());
		}
	}
	
	private boolean internalTranslateStatement() throws TransException {
		int nextIndex = -1;
		
		for (int i = 0; i < functions.size(); i++) {
			TransformFunction func = functions.get(i);
			if (!(func instanceof AtomicTrans) && !(func instanceof StubTrans)) {
				nextIndex = i;
				break;
			}
		}

		if (nextIndex == -1) {
			return false;
		} else {
			try {
				applyFunc(nextIndex);
				return true;
			} catch (IllegalStateException e) {
				System.out.println("An error occured during transformation: " + e.getMessage());
				return false;
			}
		}
	}
	
	public void testTransformation() throws TransException {
		C0Parser parser = new C0Parser();
		assertTrue(parser.parse(TestUtils.C0_VALID_FULL));
		
		C0Program c0program = parser.getProgram();
		assertNotNull(c0program);
		
		functions = new ArrayList<TransformFunction>();
		assertTrue(c0program.size() == 1);
		assertTrue(c0program.get(0) instanceof Program);
		symbolTable = new SymbolTable();
		functions.add(new Trans((Program)c0program.get(0), null));
		
		while(internalTranslateStatement()) {};
		assertTrue(functions.size() > 0);
		
		Map<String, LineAddress> addresses = AddressSolver.solve(functions);
		
		AM0Program am0program = new AM0Program();
		
		int line = 1;
		for (TransformFunction func : functions) {
			if (!(func instanceof StubTrans)) {
				assertTrue(func instanceof AtomicTrans);
				AtomicTrans atomFunc = (AtomicTrans)func;
				try {
					am0program.add(atomFunc.getStatement(new LineAddress(line), addresses));
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				line++;
			}
		}
		
		assertTrue(am0program.size() > 0);
		
		String am0ProgramCode = "";
		for (SimulationStatement statement : am0program)
			am0ProgramCode += statement.getCodeText() + "\n";
		
		assertEquals(TestUtils.AM0_VALID_FULL, am0ProgramCode);
	}
}

