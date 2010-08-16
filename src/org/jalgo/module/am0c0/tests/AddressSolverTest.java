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

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jalgo.module.am0c0.model.LineAddress;
import org.jalgo.module.am0c0.model.TreeAddress;
import org.jalgo.module.am0c0.model.c0.trans.AddressSolver;
import org.jalgo.module.am0c0.model.c0.trans.AtomicTrans;
import org.jalgo.module.am0c0.model.c0.trans.StubTrans;
import org.jalgo.module.am0c0.model.c0.trans.TransformFunction;
import org.jalgo.module.am0c0.model.c0.trans.AtomicTrans.AtomicType;
import org.junit.Test;

public class AddressSolverTest {

	@Test
	public void testSolve() {
		List<TransformFunction> functions = new ArrayList<TransformFunction>();
		
		functions.add(new AtomicTrans(null, null, AtomicType.LIT, 1));
		functions.add(new AtomicTrans(null, null, AtomicType.GT, -1));
		
		TreeAddress stubAddress = new TreeAddress();
		stubAddress.extend();
		functions.add(new StubTrans(null, stubAddress));
		
		TreeAddress jmpAddress = new TreeAddress(stubAddress);
		jmpAddress.increase();
		functions.add(new AtomicTrans(null, jmpAddress, AtomicType.JMP, -1, stubAddress));
		
		TreeAddress stubAddress2 = new TreeAddress(jmpAddress);
		stubAddress2.increase();
		functions.add(new StubTrans(null, stubAddress2));
		
		TreeAddress addAddress = new TreeAddress();
		addAddress.increase();
		functions.add(new AtomicTrans(null, addAddress, AtomicType.GT, -1));
		
		Map<String, LineAddress> solved = AddressSolver.solve(functions);
	
		assertTrue(solved.get(stubAddress.toString()).getLine() == 3);
		assertTrue(solved.get(jmpAddress.toString()).getLine() == 3);
		assertTrue(solved.get(stubAddress2.toString()).getLine() == 4);
		assertTrue(solved.get(addAddress.toString()).getLine() == 4);
	}

}
