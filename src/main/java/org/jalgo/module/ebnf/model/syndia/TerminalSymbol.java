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

import org.jalgo.main.util.*;

/**
 * A <code>TerminalSymbol</code> has a label.
 * 
 * @author Michael Thiele
 */
public class TerminalSymbol extends SynDiaElem {

	private static final long serialVersionUID = 1L;

	private String label;

	/**
	 * Constructs a terminal Symbol.
	 * 
	 * @param parent
	 *            parent of the element
	 * @param label
	 *            label of the terminal symbol
	 * @param mySyntaxDiagram
	 *            the syntax diagram this element belongs to
	 */
	public TerminalSymbol(SynDiaElem parent, String label,
			SyntaxDiagram mySyntaxDiagram) {
		super(parent, mySyntaxDiagram);
		if (label == null || label.trim().equals(""))
			throw new IllegalArgumentException(Messages.getString("ebnf",
					"SynDiaModel.EmptyNameException"));
		this.label = label;
	}

	/**
	 * @return Returns the label of the terminal symbol.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label name.
	 * 
	 * @param label
	 *            label of the terminal symbol
	 */
	public void setLabel(String label) {
		if (label == null || label.trim().equals(""))
			throw new IllegalArgumentException(Messages.getString("ebnf",
					"SynDiaModel.EmptyNameException"));
		this.getMySyntaxDiagram().getMySynDiaSystem().removeTerminal(this.label);
		this.label = label;
		this.getMySyntaxDiagram().getMySynDiaSystem().addTerminal(this.label);
		this.getMySyntaxDiagram().getMySynDiaSystem().changed();
	}

	public String toString() {
		return "Terminal: " + label;
	}
}
