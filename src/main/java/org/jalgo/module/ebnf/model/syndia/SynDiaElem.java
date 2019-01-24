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
 * <code>SynDiaElem</code> is the super class of all syntax diagram model
 * classes. It provides the knowledge of its parent.
 * 
 * @author Michael Thiele
 */
public abstract class SynDiaElem implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private SynDiaElem parent;

	private SyntaxDiagram mySyntaxDiagram;

	/**
	 * Constructs an abstract SynDiaElem.
	 * 
	 * @param parent
	 *            parent of the element
	 * @param mySyntaxDiagram
	 *            the syntax diagram the element belongs to
	 */
	public SynDiaElem(SynDiaElem parent, SyntaxDiagram mySyntaxDiagram) {
		this.parent = parent;
		this.mySyntaxDiagram = mySyntaxDiagram;
	}

	/**
	 * @return Returns the parent of the element.
	 */
	public SynDiaElem getParent() {
		return parent;
	}

	/**
	 * Sets the new parent for the syntax diagram element
	 * 
	 * @param parent
	 *            new parent
	 */
	public void setParent(SynDiaElem parent) {
		this.parent = parent;
	}

	/**
	 * 
	 * @return the syntax diagram the element belongs to
	 */
	public SyntaxDiagram getMySyntaxDiagram() {
		return mySyntaxDiagram;
	}

	public abstract String toString();
}
