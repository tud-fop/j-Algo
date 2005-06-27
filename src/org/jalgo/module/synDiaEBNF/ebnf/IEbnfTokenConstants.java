/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on 29.05.2004
 */
 
package org.jalgo.module.synDiaEBNF.ebnf;

/**
 * @author Stephan Creutz
 */
public interface IEbnfTokenConstants {
	final static Integer SYMBOL = new Integer(1);
	final static Integer WHITESPACE = new Integer(2);
	final static Integer PIPE = new Integer(3);
	final static Integer LEFT_CURLY_BRACKET = new Integer(4);
	final static Integer RIGHT_CURLY_BRACKET = new Integer(5);
	final static Integer LEFT_SQUARED_BRACKET = new Integer(6);
	final static Integer RIGHT_SQUARED_BRACKET = new Integer(7);
	final static Integer LEFT_BRACKET = new Integer(8);
	final static Integer RIGHT_BRACKET = new Integer(9);
	final static Integer DOT = new Integer(10);
}
