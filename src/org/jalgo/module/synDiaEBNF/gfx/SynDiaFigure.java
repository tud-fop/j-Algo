/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
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
 * Created on 15.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gfx;

import java.io.Serializable;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.XYLayout;
import org.eclipse.swt.SWT;
import org.jalgo.main.gfx.FixPointAnchor;

/**
 * This abstract class provides basic functionalities of components which can appear in a syntactical diagram.
 * These components are grafical representations of abstract objects and extend the Figure-class of Draw2d.
 * There are StandAloneFigures and CompositeSynDiaFigures which extend this class, too. 
 * 
 * @author Marco Zimmerling
 * @author Cornelius Hald
 */
public abstract class SynDiaFigure
	extends Figure
	implements SynDiaColors, Serializable {

	protected int startGap;
	protected int endGap;
	protected EmptyFigure startFigure;
	protected EmptyFigure endFigure;
	protected XYLayout layout; // layout used to organize interior figures

	// *** abstract methods ***

	public abstract void setStartGap(int startGap);

	public abstract void setEndGap(int endGap);

	/**
	 * Removes the entire figure.
	 */
	// soll die sich wirklich selbst entfernen? oder nicht besser vom parent entfernen lassen?
	//public abstract void remove() throws SynDiaException;

	// *** implemented methods ***

	public int getStartGap() {
		return startGap;
	}

	public int getEndGap() {
		return endGap;
	}

	/**
	 * Performs some basic initialization things concerning the used XYLayout.
	 */
	protected void initializeLayout() {
		layout = new XYLayout();
		setLayoutManager(layout);
		setOpaque(true);
	}

	/**
	 * Returns incoming anchor at the left side of the figure.
	 * @return incoming anchor
	 */
	public FixPointAnchor getLeftAnchor() {
		return new FixPointAnchor(startFigure, SWT.LEFT);
	}

	/**
	 * Returns exiting anchor at the right side of the figure.
	 * @return exiting anchor
	 */
	public FixPointAnchor getRightAnchor() {
		return new FixPointAnchor(endFigure, SWT.RIGHT);
	}

	/**
	 * Hightlights the entire figure via a colored outline.
	 */
	public abstract void highlight(boolean highlight);
}
