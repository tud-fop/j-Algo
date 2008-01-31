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

import java.io.Serializable;

/**
 * A <code>SyntaxDiagram</code>. It can be strict, so that all
 * <code>Concatenation</code>s have to be binary.
 * 
 * @author Michael Thiele
 */
public class SyntaxDiagram implements Serializable {

	private static final long serialVersionUID = 1L;

	private boolean isStrict;

	private String name;

	private Concatenation root;

	private SynDiaSystem mySynDiaSystem;

	/**
	 * Constructs a syntax diagram that is non strict.
	 * 
	 * @param name
	 *            the name of this syntax diagram
	 * @param sds
	 *            the syntax diagram system this syntax diagram belongs to
	 */
	public SyntaxDiagram(String name, SynDiaSystem sds) {
		isStrict = false;
		root = new Concatenation(null, this);
		this.name = name;
		mySynDiaSystem = sds;
	}

	/**
	 * @return Returns the root element of the syntax diagram.
	 */
	public Concatenation getRoot() {
		return root;
	}

	/**
	 * @return Returns the start variable of the syntax diagram.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the new name of the syntax diagram.
	 * 
	 * @param name
	 *            the new name
	 */
	public void setName(String name) {
		this.name = name;
		mySynDiaSystem.changed();
	}

	/**
	 * @return Returns <code>true</code>, if the parentheses are correct
	 *         (left-associative). If concatenations can have more than 2 items
	 *         it returns <code>false</code>
	 */
	public boolean isStrict() {
		return isStrict;
	}

	/**
	 * Fills the syntax diagram with <code>NullElem</code>s. Has to be called
	 * when a syntax diagram is loaded and then edited.
	 * 
	 * @return <code>true</code> if <code>NullElem</code>s were added,
	 *         <code>false</code> if nothing had to be added
	 */
	public boolean fillWithNullElems() {
		return root.fillWithNullElems();
	}

	/**
	 * Removes all <code>NullElem</code>s from the syntax diagram. Has to be
	 * called when editor mode is over.
	 * 
	 * @return <code>true</code> if <code>NullElem</code>s were removed,
	 *         <code>false</code> if nothing had to be removed
	 */
	public boolean removeNullElems() {
		return root.removeNullElems();
	}

	/**
	 * 
	 * @return the syntax diagram system this syntax diagram belongs to
	 */
	public SynDiaSystem getMySynDiaSystem() {
		return mySynDiaSystem;
	}

	public String toString() {
		return root.toString();
	}

	/**
	 * Only for test purposes: prints NullElems.
	 * 
	 * @return NullElems
	 */
	public String printNullElems() {
		return root.printNullElems();
	}
}
