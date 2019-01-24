package org.jalgo.module.c0h0.tests;

import static org.junit.Assert.*;

import org.jalgo.module.c0h0.parser.C00Parser;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * tests the c00 parser
 *
 */
public class C00ParserTest{
	private static C00Parser parser;
	private static String programHead, programOutput;
	
	@BeforeClass
	public static void setUpBeforeClass(){
		parser = new C00Parser();
		programHead = "#include <stdio.h> "
					+ "int main() {"
					+ "int x1;"
					+ "scanf(\"%i\", &x1);";
		programOutput = "printf(\"%d\", x1);"
					+ "return 0;"
					+ "}";
	}
	
	/**
	 * Spezielle Spezialfaelle speziell testen
	 */
	
	@Test
	public void testValidStandardForm(){
		String text = programHead + programOutput;
		assertTrue(parser.parse(text));
	}
	
	@Test
	public void testAssignmentTerms(){
		String text = programHead
					+ "x1 = x1 + 1;"
					+ "x1 = -x1 + 1;"
					+ "x1 = (-3 < 2) * 2;"
					+ "x1 = (3 * -2) - 3;"
					+ "x1 = (2 < 3) > 4;"
					+ "x1 = -(3 - 2) / 3;"
					+ "x1 = -(3 < 2) % 2;"
					+ programOutput;
		assertTrue(parser.parse(text));
	}

	@Test
	public void testIfStatement(){
		String text = programHead
					+ "if(x1 > 1) x1 = 1;"
					+ "if(x1 == 2) ;"
					+ "if(x1 <= 3) { x1 = 2; }"
					+ "if(x1 >= 4) { x1 = 3; }else{ x1 = 4; }"
					+ "if(x1 != 5) x1 = 5; else x1 = 6;"
					+ "if(x1 < 6) { x1 = 7; }else x1 = 8;"
					+ "if((3 % 5) < 2) x1 = 9; else{ x1 = 10;}"
					+ programOutput;
		assertTrue(parser.parse(text));
	}

	@Test
	public void testWhileStatement(){
		String text = programHead
					+ "while(x1 > 1) x1 = 1;"
					+ "while(x1 == 2) ;"
					+ "while(x1 <= 3) { x1 = 2; }"
					+ programOutput;
		assertTrue(parser.parse(text));
	}
	
	@Test
	public void testStatement(){
		String text = programHead
					+ "while(x1 > 1) x1 = 1;"
					+ "while(x1 == 2) ;"
					+ "while(x1 <= 3) { x1 = 2; }"
					+ programOutput;
		assertTrue(parser.parse(text));
	}
	
	@Test
	public void testInvalidCode() {
		String text = programHead
					+ "x1 = 1 (/3); "
					+ programOutput;
		assertFalse(parser.parse(text));
		
		text = programHead
			+ "x12 = 1 (+3); "
			+ programOutput;
		assertFalse(parser.parse(text));
		
		text = programHead
			+ "x1b = 1 / 3; "
			+ programOutput;
		assertFalse(parser.parse(text));
	}
}


	


