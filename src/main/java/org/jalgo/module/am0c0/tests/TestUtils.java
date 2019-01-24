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

public class TestUtils {
	public static final String C0_VALID = "#include<stdio.h> int main(){return 0;}";
	public static final String C0_VALID_FULL = "#include <stdio.h> int main() "
			+ "{ int i, n, s; scanf(\"%i\", &n); " + "i = 1; s = 0; "
			+ "while (i <= n){s = s+i*i; i = i+1;} "
			+ "printf(\"%d\", s); return 0;}";
	public static final String C0_INVALID = "invalid";

	public static final String AM0_VALID = "READ 1\nLOAD 1\nLIT 2\nADD\nSTORE 2\nWRITE 2";
	public static final String AM0_VALID_FULL = "READ 2;\nLIT 1;\nSTORE 1;\n"
			+ "LIT 0;\nSTORE 3;\nLOAD 1;\n" + "LOAD 2;\nLE;\nJMC 21;\n"
			+ "LOAD 3;\nLOAD 1;\nLOAD 1;\n" + "MUL;\nADD;\nSTORE 3;\n"
			+ "LOAD 1;\nLIT 1;\nADD;\n" + "STORE 1;\nJMP 6;\nWRITE 3;\n";
	public static final String AM0_INVALID = "invalid";
}
