/**
 * 
 */
package org.jalgo.module.c0h0.tests;

import static org.junit.Assert.*;

import org.jalgo.module.c0h0.controller.Controller;
import org.jalgo.module.c0h0.models.ast.ASTModel;
import org.jalgo.module.c0h0.models.h0model.H0CodeModel;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * tests the H0 generating visitor
 * 
 * @author Peter Schwede
 * 
 */
public class H0GeneratingTest{
	private static H0CodeModel model;
	private static ASTModel ast;

	@BeforeClass
	public static void setUpBeforeClass() {
		ast = new ASTModel();
		model = new H0CodeModel(new Controller(null, null, null));
	}

	@Test
	public void emptyCodeTest() {
		ast.create("");
		model.generate();
		if (ast.isValid())
			assertTrue("".equals(model.getCode()));
	}

	@Test
	public void simpleCodeTest() {
		ast.create("#include <stdio.h> " + "int main() {" + "int x1;"
				+ "scanf(\"%i\", &x1);" + "x1 = x1 + 1;" + "x1 = -x1 + 1;"
				+ "x1 = (-3 < 2) * 2;" + "x1 = (3 * -2) - 3;"
				+ "x1 = (2 < 3) > 4;" + "x1 = -(3 - 2) / 3;"
				+ "x1 = -(3 < 2) % 2;" + "printf(\"%d\", x1);" + "return 0;"
				+ "}");
		model.generate();
		if (ast.isValid()) {
			assertTrue(("module Main where\n" + "f1 :: Int -> Int\n"
					+ "f1 x1 = f2 (x1 + 1)\n" + "f2 :: Int -> Int\n"
					+ "f2 x1 = f3 (-x1 + 1)\n" + "f3 :: Int -> Int\n"
					+ "f3 x1 = f4 ((-3 < 2) * 2)\n" + "f4 :: Int -> Int\n"
					+ "f4 x1 = f5 ((3 * -2) - 3)\n" + "f5 :: Int -> Int\n"
					+ "f5 x1 = f6 ((2 < 3) > 4)\n" + "f6 :: Int -> Int\n"
					+ "f6 x1 = f7 (-(3 - 2) div 3)\n" + "f7 :: Int -> Int\n"
					+ "f7 x1 = f8 (-(3 < 2) mod 2)\n" + "f8 :: Int -> Int\n"
					+ "f8 x1 = x1\n" + "main = do x1 <- readLn\n"
					+ "          print (f1 x1)\n").equals(model.getCode()));
		}
	}

	@Test
	public void whileCodeTest() {
		ast.create("#include <stdio.h> " + "int main() {" + "int x1;"
				+ "scanf(\"%i\", &x1);" + "while(x1 == 1) { x1 = x1 - 1; }"
				+ "printf(\"%d\", x1);" + "return 0;" + "}");
		model.generate();
		String code = "module Main where\n" + "f1 :: Int -> Int\n"
				+ "f1 x1 = if (x1 == 1) then f11 x1\n"
				+ "                     else f2 x1\n" + "f11 :: Int -> Int\n"
				+ "f11 x1 = f111 x1\n" + "f111 :: Int -> Int\n"
				+ "f111 x1 = f2 (x1 - 1)\n" + "f2 :: Int -> Int\n"
				+ "f2 x1 = x1\n" + "main = do x1 <- readLn\n"
				+ "          print (f1 x1)\n";
		if (ast.isValid()) {
			assertTrue((code).equals(model.getCode()));
		}
	}

	@Test
	public void ifCodeTest() {
		ast.create("#include <stdio.h> \n" + "int main() {" + "int x1;\n"
				+ "scanf(\"%i\", &x1);\n" + "if(x1 == 1) x1 = 2;\n"
				+ "x1 = 3;\n" + "printf(\"%d\", x1);\n" + "return 0;\n" + "}");
		model.generate();
		String code = "" + "module Main where\n"
				+ "f1 :: Int -> Int\n"
				+ "f1 x1 = if (x1 == 1) then f11 x1\n"
				+ "                     else f2 x1\n"
				+ "f11 :: Int -> Int\n"
				+ "f11 x1 = f111 x1\n"
				+ "f111 :: Int -> Int\n"
				+ "f111 x1 = f2 (2)\n"
				+ "f2 :: Int -> Int\n"
				+ "f2 x1 = f3 (3)\n"
				+ "f3 :: Int -> Int\n" + "f3 x1 = x1\n"
				+ "main = do x1 <- readLn\n" + "          print (f1 x1)\n";
		if (ast.isValid()) {
			assertTrue((code).equals(model.getCode()));
		}
	}

	@Test
	public void moreComplexCodeTest() {
		ast.create("#include <stdio.h> \n" + "int main() {" + "int x1;\n"
				+ "scanf(\"%i\", &x1);\n"
				+ "while(x1 > 3) {\n"
				+ "if(x1 < 7) {\n"
				+ "  x1 = x1 - 2;\n"
				+ "} else x1 = x1 - 1;\n"
				+ "}"
				+ "x1 = x1;"
				+ "printf(\"%d\", x1);\n" + "return 0;\n" + "}");
		model.generate();
		String code = "" + "module Main where\n"
				+ "f1 :: Int -> Int\n"
				+ "f1 x1 = if (x1 > 3) then f11 x1\n"
				+ "                    else f2 x1\n"
				+ "f11 :: Int -> Int\n"
				+ "f11 x1 = f111 x1\n"
				+ "f111 :: Int -> Int\n"
				+ "f111 x1 = if (x1 < 7) then f1111 x1\n"
				+ "                      else f1112 x1\n"
				+ "f1111 :: Int -> Int\n"
				+ "f1111 x1 = f11111 x1\n"
				+ "f11111 :: Int -> Int\n"
				+ "f11111 x1 = f2 (x1 - 2)\n"
				+ "f1112 :: Int -> Int\n"
				+ "f1112 x1 = f11121 x1\n"
				+ "f11121 :: Int -> Int\n"
				+ "f11121 x1 = f2 (x1 - 1)\n"
				+ "f2 :: Int -> Int\n"
				+ "f2 x1 = f3 (x1)\n"
				+ "f3 :: Int -> Int\n"
				+ "f3 x1 = x1\n"
				+ "main = do x1 <- readLn\n"
				+ "          print (f1 x1)\n";
		if (ast.isValid()) {
			assertTrue((code).equals(model.getCode()));
		}
	}
}
