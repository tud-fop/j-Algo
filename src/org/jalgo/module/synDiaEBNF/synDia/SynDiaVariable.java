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

import org.eclipse.swt.graphics.Font;
import org.jalgo.module.synDiaEBNF.gfx.SynDiaColors;
import org.jalgo.module.synDiaEBNF.gfx.VariableFigure;

/**
 * @author Benjamin Scholz
 * @author Babett Schaliz
 * @author Michael Pradel
 * @author Marco Zimmerling
 */
public class SynDiaVariable extends SynDiaElement implements SynDiaColors,
		Serializable {

	private static final long serialVersionUID = -5791017043768225083L;

	//Rectangle in with the SynDiaVar is shown on the Screen
	private VariableFigure rectangle;

	private int backtrackingLabel;

	private static int maxBacktrackingLabel = 1;

	private SynDiaInitial startElem = null;

	/* for Backtracking is set there */
	private SynDiaVariableBack helpCopy = null;

	public SynDiaVariable() {
		backtrackingLabel = maxBacktrackingLabel++;
	}

	public SynDiaVariable(VariableFigure rectangle) {
		this.rectangle = rectangle;
		backtrackingLabel = maxBacktrackingLabel++;
	}

	public SynDiaVariable(String label) {
		/*
		 * the cast is necessary, thus the compiler is able to choose
		 * the right constructor
		 */
		this(label, (SynDiaInitial) null);
	}

	public SynDiaVariable(String label, SynDiaInitial startElem) {
		this.rectangle = new VariableFigure(label);
		this.startElem = startElem;
		backtrackingLabel = maxBacktrackingLabel++;
		if (rectangle != null) {
			rectangle.setIndexText("" + backtrackingLabel);} //$NON-NLS-1$
	}

	public SynDiaVariable(String label, SynDiaInitial startElem, Font font) {
		this.rectangle = new VariableFigure(label, font);
		this.startElem = startElem;
		backtrackingLabel = maxBacktrackingLabel++;
		if (rectangle != null) {
			rectangle.setIndexVisible(false);
		}
	}

	public SynDiaVariable(String label, Font font) {
		this(label, null, font);
	}

	public String getLabel() {
		return rectangle.getLabel();
	}

	public int getBacktrackingLabel() {
		return backtrackingLabel;
	}

	public SynDiaInitial getStartElem() {
		return startElem;
	}

	public void setStartElem(SynDiaInitial startElem) {
		this.startElem = startElem;
	}

	public void markLastConnection(boolean marked) {
		rectangle.highlightIncomingConnection(marked);
	}

	public void markNextConnection(boolean marked) {
		rectangle.highlightExitingConnection(marked);
	}

	public void highlightObject(boolean marked) {
		rectangle.highlight(marked);
	}

	public void markObject() {
		rectangle.setBackgroundColor(currentFigure);
	}

	public void unmarkObject(boolean bool) {
		rectangle.setBackgroundColor(null);
		highlightObject(bool);
	}

	public VariableFigure getGfx() {
		return rectangle;
	}

	public void setGfx(VariableFigure figure) {
		rectangle = figure;
		if (figure != null) {
			rectangle.setIndexText("" + backtrackingLabel); //$NON-NLS-1$
		}
	}

	public SynDiaVariableBack getHelpCopy() {
		return helpCopy;
	}

	/**
	 * Sets the <code>SynDiaVariableBack</code>, which saves the
	 * information where to jump back to after the diagram it is pointing to
	 * was executed.
	 * 
	 * @param back
	 */
	public void setHelpCopy(SynDiaVariableBack back) {
		helpCopy = back;
	}
}