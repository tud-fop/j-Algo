package org.jalgo.module.c0h0.tests;

import org.jalgo.module.c0h0.models.ast.ASTModel;
import org.jalgo.module.c0h0.models.ast.PrintfStatement;
import org.jalgo.module.c0h0.models.ast.Var;
import org.jalgo.module.c0h0.models.ast.tools.DFSIterator;
import org.jalgo.module.c0h0.models.flowchart.FcGeneratingVisitor;
import org.junit.Test;
import static org.junit.Assert.*;


/**
 * tests the flow chart visitor
 *
 */
public class FcVisitorTest {
	private static FcGeneratingVisitor fcVisitor;
	private static ASTModel ast;
	
	@Test
	public void testStepNumber(){
		
		String programHead = "#include <stdio.h> "
			+ "int main() {"
			+ "int x1;"
			+ "scanf(\"%i\", &x1);";
		String programOutput = "printf(\"%d\", x1);"
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
		ast = new ASTModel();
		ast.create(text);
		
		fcVisitor = new FcGeneratingVisitor();
		DFSIterator iter = new DFSIterator(ast);
		while (iter.hasNext()) {
			iter.next().accept(fcVisitor);
		}
		
		PrintfStatement p = new PrintfStatement(new Var("x1"));
		p.setAddress("f3");
		assertTrue(fcVisitor.getElementList(p).size()==8);
	}
	
	@Test
	public void testStepNumberIf(){
		
		String programHead = "#include <stdio.h> "
			+ "int main() {"
			+ "int x1;"
			+ "scanf(\"%i\", &x1);";
		String programOutput = "printf(\"%d\", x1);"
					  + "return 0;"
					  + "}";
		String text = programHead
					+ "if(x1 > 1) x1 = 1;"
					+ "if(x1 == 2) x1 = 1;"
					+ "if(x1 <= 3) { x1 = 2; }"
					+ "if(x1 >= 4) { x1 = 3; }else{ x1 = 4; }"
					+ "if(x1 != 5) x1 = 5; else x1 = 6;"
					+ "if(x1 < 6) { x1 = 7; }else x1 = 8;"
					+ "if((3 % 5) < 2) x1 = 9; else{ x1 = 10;}"
					+ programOutput;
		ast = new ASTModel();
		ast.create(text);
		
		fcVisitor = new FcGeneratingVisitor();
		DFSIterator iter = new DFSIterator(ast);
		while (iter.hasNext()) {
			iter.next().accept(fcVisitor);
		}
		
		PrintfStatement p = new PrintfStatement(new Var("x1"));
		p.setAddress("f3");
		assertTrue(fcVisitor.getElementList(p).size()==24);
	}
}
