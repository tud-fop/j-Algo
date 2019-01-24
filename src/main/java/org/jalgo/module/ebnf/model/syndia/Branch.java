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
 * This class is the model of a branch. It consists of two concatenations: left
 * and right.
 * 
 * @author Michael Thiele
 */
public class Branch extends SynDiaElem {

	private static final long serialVersionUID = 1L;

	private Concatenation left;

	private Concatenation right;

	/**
	 * 
	 * @param parent
	 *            parent of the element
	 * @param mySyntaxDiagram
	 *            the syntax diagram this element belongs to
	 */
	public Branch(SynDiaElem parent, SyntaxDiagram mySyntaxDiagram) {
		super(parent, mySyntaxDiagram);
		left = new Concatenation(this, mySyntaxDiagram);
		right = new Concatenation(this, mySyntaxDiagram);
	}

	/**
	 * @return The left concatenation, which is the upper one when rendered.
	 */
	public Concatenation getLeft() {
		return left;
	}

	/**
	 * @return The right concatenation, which is the lower one when rendered.
	 */
	public Concatenation getRight() {
		return right;
	}

	/**
	 * Removes all <code>NullElem</code>s from the Branch.
	 * 
	 * @return <code>true</code> if <code>NullElem</code>s were removed,
	 *         <code>false</code> if nothing had to be removed
	 */
	public boolean removeNullElems() {
		boolean retVal = false;
		retVal = left.removeNullElems();
		retVal |= right.removeNullElems();
		return retVal;
	}

	/**
	 * Fills the Branch with <code>NullElem</code>s.
	 * 
	 * @return <code>true</code> if <code>NullElem</code>s were added,
	 *         <code>false</code> if nothing had to be added
	 */
	public boolean fillWithNullElems() {
		boolean retVal = false;
		retVal = left.fillWithNullElems();
		retVal |= right.fillWithNullElems();
		return retVal;
	}

	public String toString() {
		return "[ Branch: " + left.toString() + ": " + right.toString() + "]";
	}

}
