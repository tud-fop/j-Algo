package org.jalgo.module.c0h0.tests;

import static org.junit.Assert.*;

import java.util.LinkedList;

import org.jalgo.module.c0h0.models.ast.*;
import org.jalgo.module.c0h0.models.ast.tools.DFSIterator;
import org.jalgo.module.c0h0.models.c0model.C0GeneratingVisitor;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * tests the c0 generating visitor
 * 
 */
public class C0GeneratingVisitorTest {
	private static ASTModel ast;
	private static C0GeneratingVisitor visitor;
	private static DFSIterator iterator;
	private static String programHead, programField, programOutput;

	@BeforeClass
	public static void setUpBeforeClass() {
		ast = new ASTModel();
		visitor = new C0GeneratingVisitor();
		programHead = "#include <stdio.h> " + "int main() {" + "int x1;"
				+ "scanf(\"%i\", &x1);";
		programField = "";
		programOutput = "printf(\"%d\", x1);" + "return 0;" + "}";
	}

	/**
	 * Spezielle Spezialfaelle speziell testen
	 */

	private void createAST() {
		ast.create(programHead + programField + programOutput);
		iterator = new DFSIterator(ast);
		while (iterator.hasNext()) {
			iterator.next().accept(visitor);
		}
	}

	@Test
	public void testAddressCount() {
		programField = "x1 = x1 + 1;" + "x1 = -x1 + 1;" + "x1 = (-3 < 2) * 2;"
				+ "x1 = (3 * -2) - 3;" + "x1 = (2 < 3) > 4;"
				+ "x1 = -(3 - 2) / 3;" + "x1 = -(3 < 2) % 2;";
		createAST();
		assertEquals(7, visitor.getAddressCount());
	}

	@Test
	public void testResultingAddresssList() {
		programField = "if(x1 > 1) x1 = 1;";
		createAST();
		LinkedList<String> prefResult = new LinkedList<String>();
		prefResult.add("f1");
		prefResult.add("f11");
		assertTrue(visitor.getAddressList().containsAll(prefResult));
	}

	@Test
	public void testResultingCode() {
		programField = "while(x1 > 7){x1 = x1 - 2;}x1 = x1 + 2;";
		createAST();
		String result = "<table><tr><td class=\"f1\"><table><tr><td>&nbsp;&nbsp;</td><td><a class=\"token\" name=\"f1\" href=\"f1\">while</a></td><td><a href=\"f1\">&nbsp;(x1 > 7)</a></td></tr></table></td></tr><tr><td class=\"f11\"><table><tr><td><table><tr><td>&nbsp;&nbsp;</td><td><a name=\"f11\" href=\"f11\">{</a></td></tr></table></td></tr><tr><td class=\"f111\"><table><tr><td>&nbsp;&nbsp;&nbsp;&nbsp;</td><td><a name=\"f111\" href=\"f111\">x1 = x1 - 2;</a></td></tr></table></td></tr><tr><td><table><tr><td>&nbsp;&nbsp;</td><td>}</td></tr></table></td></tr></table></td></tr><tr><td class=\"f2\"><table><tr><td>&nbsp;&nbsp;</td><td><a name=\"f2\" href=\"f2\">x1 = x1 + 2;</a></td></tr></table></td></tr></table>";
		assertFalse(visitor.getCode().equals(result));
	}
}
