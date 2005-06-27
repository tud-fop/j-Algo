/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
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
 * Created on 29.04.2004
 */

package org.jalgo.module.synDiaEBNF.synDia;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaSystemFigure;

/**
 * symbolize an syntactical diagram system , which consist of a final set of
 * syntactical diagramms, describing a Language. SynDia=(V,E,S,R), V is a final
 * set of SyntaxVariables E is a final set of Terminalsymbols S is included in
 * V, and is the startdiagramm R is a final set of diagrams with the name v
 * 
 * @author Michael Pradel
 * @author Marco Zimmerling
 * @author Babett Schaliz
 */
public class SynDiaSystem implements Serializable {

	private String label = "SynDia"; //$NON-NLS-1$; // Name of the SDS, mostly SynDia
	private Set synVariables; // Strings
	private Set terminalSymbols; // Strings
	/* the figure including the hole Diagramsystem! */
	private SynDiaSystemFigure gfx; 
	private LinkedList initialDia = new LinkedList(); //diagramms

	private SynDiaInitial startElem; // decorated StartElem

	/**
	 * constructor
	 */
	public SynDiaSystem() {
		startElem = null;
		synVariables = new HashSet();
		terminalSymbols = new HashSet();
	}

	/**
	 * constructor
	 */
	public SynDiaSystem(SynDiaSystemFigure panel) {
		this.gfx = panel;
		startElem = null;
		synVariables = new HashSet();
		terminalSymbols = new HashSet();
	}

	/**
	 * constructor
	 * 
	 * @param startElem
	 *                   pointer of the start element
	 */
	public SynDiaSystem(SynDiaInitial startElem) {
		this.startElem = startElem;
	}

	/**
	 * @return a pointer of the start element
	 */
	public SynDiaInitial getStartElem() {
		return startElem;
	}

	public void setStartElem(SynDiaInitial startElem) {
		this.startElem = startElem;
	}

	/**
	 * @return a list of the syntactical variables
	 */
	public Set getSynVariables() {
		return synVariables;
	}

	public void setSynVariables(Set synVariables) {
		this.synVariables = synVariables;
	}

	public void addSynVariable(SynDiaVariable name) {
		synVariables.add(name);
	}

	/**
	 * @return a list of the terminal symbols
	 */
	public Set getTerminalSymbols() {
		return terminalSymbols;
	}

	public void setTerminalSymbols(Set terminalSymbols) {
		this.terminalSymbols = terminalSymbols;
	}

	public void addTerminalSymbol(String name) {
		terminalSymbols.add(name);
	}

	public boolean removeTerminalSymbol(String termSym) {
		return terminalSymbols.remove(termSym);
	}

	public StyledText getTuple(Composite parent) {
		StyledText widget = new StyledText(parent, SWT.BORDER);
		widget
				.append(label
						+ Messages
								.getString("SynDiaSystem.(_u03A3,_V)_mit_n___u03A3___{_4")); //$NON-NLS-1$
		for (Iterator it = terminalSymbols.iterator(); it.hasNext();) {
			widget.append((String) it.next());
			if (it.hasNext())
				widget.append(", "); //$NON-NLS-1$
		}
		widget.append("}\n  V = {"); //$NON-NLS-1$
		for (Iterator it = synVariables.iterator(); it.hasNext();) {
			widget.append((String) it.next());
			if (it.hasNext())
				widget.append(", "); //$NON-NLS-1$
		}
		widget.append("}"); //$NON-NLS-1$
		return widget;
	}

	/**
	 * @return the Figure including the hole diagram system
	 */
	public SynDiaSystemFigure getGfx() {
		return gfx;
	}

	public void setGfx(SynDiaSystemFigure figure) {
		gfx = figure;
	}

	public LinkedList getInitialDiagrams() {
		return initialDia;
	}

	public SynDiaInitial getInitialDiagram(int i) {
		return (SynDiaInitial) initialDia.get(i);
	}

	public void setInitialDiagrams(LinkedList list) {
		initialDia = list;
	}

	public void addInitialDiagram(SynDiaInitial obj) {
		initialDia.add(obj);
	}
}