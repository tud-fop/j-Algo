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

package org.jalgo.module.ebnf.model.syndia;

/**
 * The class <code>NullElem</code> represents a location in the syntax
 * diagram, where new elements can be added. Use the getIndex() function to
 * determine where to add new elements in a <code>Concatenation</code>.
 * 
 * @author Michael Thiele
 */
public class NullElem extends SynDiaElem {

	private static final long serialVersionUID = 1L;

	private int line;

	private int index;

	/**
	 * Constructs a NullElem.
	 * 
	 * @param parent
	 *            parent of the element
	 * @param line
	 *            the number of the concatenation
	 * @param index
	 *            the position inside the concatenation
	 * @param mySyntaxDiagram
	 *            the syntax diagram this element belongs to
	 */
	public NullElem(SynDiaElem parent, int line, int index,
			SyntaxDiagram mySyntaxDiagram) {
		super(parent, mySyntaxDiagram);
		this.line = line;
		this.index = index;

	}

	/**
	 * @return Returns the number of the <code>Concatenation</code> the
	 *         <code>NullElem</code> is in.
	 */
	public int getLine() {
		return line;
	}

	/**
	 * @return Returns the index inside of a <code>Concatenation</code>.
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * @param index
	 *            the new index
	 */
	public void setIndex(int index) {
		this.index = index;
	}

	/**
	 * @param line
	 *            the new line
	 */
	public void setLine(int line) {
		this.line = line;
	}

	public String toString() {
		return "NullElem";
	}
}
