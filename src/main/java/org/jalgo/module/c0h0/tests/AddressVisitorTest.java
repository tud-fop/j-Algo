package org.jalgo.module.c0h0.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.jalgo.module.c0h0.models.ast.ASTModel;
import org.jalgo.module.c0h0.models.ast.Statement;
import org.jalgo.module.c0h0.models.ast.Symbol;
import org.jalgo.module.c0h0.models.ast.tools.BFSIterator;
import org.jalgo.module.c0h0.models.ast.tools.Iterable;
import org.jalgo.module.c0h0.models.c0model.AddressVisitor;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * tests the address visitor
 *
 */
public class AddressVisitorTest{
	private static ASTModel ast;
	private static AddressVisitor addressV;
	private static String programHead, programOutput;

	@BeforeClass
	public static void setUpBeforeClass() {
		addressV = new AddressVisitor();
		programHead = "#include <stdio.h> "
				+ "int main() {"
				+ "int x1;"
				+ "scanf(\"%i\", &x1);";
		programOutput = "printf(\"%d\", x1);"
					  + "return 0;"
					  + "}";
		String text = programHead
					+ "while(x1 > 3) {"		// f1		// f11
					+ "if(x1 < 7) {"		// f111		// f1111
					+ "x1 = x1 - 2;"		// f11111	
					+ "}else x1 = x1 - 1;"	// f1112	// f11121
					+ "}"					
					+ "x1 = x1;"			// f2
					+ programOutput;
		//TODO maybe load .c0 file instead?
		ast = new ASTModel();
		ast.create(text);

		// make sure, DFS is used
		BFSIterator it = new BFSIterator(ast); // body only
		while (it.hasNext()) {
			Symbol s = it.next();
			s.accept(addressV);
		}
	}

	/**
	 * Test for Addresses
	 */
	@Test
	public void firstBlockTest() {
		ArrayList<Statement> arr = ast.getProgram().getBlock().getStatementList();
		for(int i=1; i<=arr.size(); i++) {
			assertTrue(("f"+i).equals(arr.get(i-1).getAddress()));
		}
	}
	
	@Test
	public void ifBlockTest() {
		ArrayList<Iterable> arr = ast.getProgram().getBlock().getSequence();
		// go to block
		arr = arr.get(0).getSequence();
		// go to while
		arr = arr.get(0).getSequence();
		// go to if
		arr = arr.get(0).getSequence();
		// test if block
		assertTrue("f1111".equals(((Symbol) arr.get(0)).getAddress()));
	}
	
	@Test
	public void elseBlockTest() {
		ArrayList<Iterable> arr = ast.getProgram().getBlock().getSequence();
		// go to block
		arr = arr.get(0).getSequence();
		// go to while
		arr = arr.get(0).getSequence();
		// go to if
		arr = arr.get(0).getSequence();
		// test else block
		assertTrue("f1112".equals(((Symbol) arr.get(1)).getAddress()));
		// go to assignment in else block
		arr = arr.get(1).getSequence();
		assertTrue("f11121".equals(((Symbol) arr.get(0)).getAddress()));
	}
	
	@Test
	public void advancedIfBlockTest() {
		ArrayList<Iterable> arr = ast.getProgram().getBlock().getSequence();
		// go to block
		arr = arr.get(0).getSequence();
		// go to while
		arr = arr.get(0).getSequence();
		// go to if
		arr = arr.get(0).getSequence();
		// test if block
		assertTrue("f1111".equals(((Symbol) arr.get(0)).getAddress()));
		// go to assignment in if block
		arr = arr.get(0).getSequence();
		assertTrue("f11111".equals(((Symbol) arr.get(0)).getAddress()));
	}
}
