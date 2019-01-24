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

/**
 * Exception is thrown if you want to add a new syntax diagram element at a
 * position of a concatenation, where no <code>NullElem</code> is located.
 * 
 *  @author Michael Thiele
 */
package org.jalgo.module.ebnf.model.syndia;

/**
 * Exception that is thrown if you try to insert an element at a position that
 * has no NullElement.
 * 
 * @author Michael Thiele
 * 
 */
public class NoNullElemException extends Exception {

	private static final long serialVersionUID = -6819655720873059669L;

	/**
	 * 
	 *
	 */
	public NoNullElemException() {
		super();
	}

	/**
	 * 
	 * @param message
	 */
	public NoNullElemException(String message) {
		super(message);
	}
}
