/*
 * j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer science. It is written in Java and platform
 * independent. j-Algo is developed with the help of Dresden University of
 * Technology.
 * 
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
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

/* Created on 05.05.2005 */
package org.jalgo.module.kmp;

/**
 * The interface <code>Constants</code> is a collection of several constant
 * values used in the core implementation of the KMP module.
 * 
 * @author Danilo Lisske
 */
public interface Constants {

	/*----------------------------valid key range----------------------------*/
	/** The minimum integer value a key can have. */
	public static final int MAX_PAT_LENGTH = 10;
	/** The size of the alphabet for the random pattern. */
	public static final int ALPHABET_SIZE = 10; 
	/** The length of the random generated searchtext. */
	public static final int RANDOM_SEARCHTEXT_LENGTH = 30;
	/** The newline-character depending on the operating system. */
	public static final String SEPARATOR = System.getProperty("line.separator");
	/** The file seperator depending on the Operating System. */
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	/** The code of phase one (build shiftingtable). */
	public static final String CODE_PHASE_ONE = "Tabelle[0] = -1;" + SEPARATOR + 
	"VglInd = 0;" + SEPARATOR + 
	"for (PatPos = 1; PatPos <= PatternLength - 1; PatPos = PatPos + 1) {" + SEPARATOR + 
	"    if (Pattern[PatPos] == Pattern[VglInd])" + SEPARATOR + 
	"        Tabelle[PatPos] = Tabelle[VglInd];" + SEPARATOR + 
	"    else Tabelle[PatPos] = VglInd;" + SEPARATOR + 
	"    while ((VglInd >= 0) && (Pattern[PatPos] != Pattern[VglInd]))" + SEPARATOR + 
	"        VglInd = Tabelle[VglInd];" + SEPARATOR + 
	"    VglInd = VglInd + 1;" + SEPARATOR + 
	"}";
	/** The code of phase two (find pattern). */
	public static final String CODE_PHASE_TWO = "TextPos = 0; PatternPos = 0;" + SEPARATOR + 
	"while ((PatternPos < Patternlength) && (TextPos < TextLength)) {" + SEPARATOR + 
	"    while ((PatternPos >=0 ) && (Text[TextPos] != Pattern[PatternPos]))" + SEPARATOR + 
	"        PatternPos = Tabelle[PatternPos];"  + SEPARATOR + 
	"    TextPos = TextPos + 1;" + SEPARATOR + 
	"    PatternPos = PatternPos + 1;" + SEPARATOR + 
	"}" + SEPARATOR + 
	"if (PatternPos == Patternlength)" + SEPARATOR + 
	"    printf(\"Pattern was found at position : %d\", TextPos - Patternlength);" + SEPARATOR + 
	"else printf(\"Pattern was not found\");";
}