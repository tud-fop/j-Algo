/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for
 * students and lecturers of computer sience. It is written in Java and
 * platform independant. j-Algo is developed with the help of Dresden
 * University of Technology.
 *
 * Copyright (C) 2004 j-Algo-Team, j-algo-development@lists.sourceforge.net
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
import org.jalgo.module.synDiaEBNF.gfx.TerminalFigure;

/**
 * @author Michael Pradel
 * @author Babett Schaliz
 */
public class SynDiaTerminal extends SynDiaElement
		implements
			SynDiaColors,
			Serializable {

	private TerminalFigure ellipse;
	//Ellipse in with the SynDiaTerminal is shown on the Screen

	public SynDiaTerminal(String label, Font font) {
		this.ellipse = new TerminalFigure(label, font);
	}

	public SynDiaTerminal(String label) {
		this.ellipse = new TerminalFigure(label);
	}

	public SynDiaTerminal(TerminalFigure figure) {
		this.ellipse = figure;
	}

	public String getLabel() {
		return ellipse.getLabel();
	}
	public void markLastConnection(boolean marked) {
		ellipse.highlightIncomingConnection(marked);
	}

	public void markNextConnection(boolean marked) {
		ellipse.highlightExitingConnection(marked);
	}

	public void markObjekt(boolean marked) {
		ellipse.highlight(marked);
	}

	public void markObject() {
		ellipse.setBackgroundColor(currentFigure);
	}

	public void unmarkObject(boolean bool) {
		ellipse.setBackgroundColor(null);
		markObjekt(bool);
	}

	/**
	 * @return
	 */
	public TerminalFigure getGfx() {
		return ellipse;
	}

	/**
	 * @param figure
	 */
	public void setGfx(TerminalFigure figure) {
		ellipse = figure;
	}

}