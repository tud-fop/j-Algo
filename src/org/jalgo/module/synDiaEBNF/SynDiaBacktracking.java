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
 * Created on 30.04.2004
 */

package org.jalgo.module.synDiaEBNF;

import java.io.Serializable;
import java.util.LinkedList;

import org.jalgo.module.synDiaEBNF.synDia.SynDiaInitial;
import org.jalgo.module.synDiaEBNF.synDia.SynDiaSystem;

/**
 * Basic class to manage similar methods of the algorithmus to generate a Word
 * or to recognize a word by a syntax diagram system
 * 
 * @author Michael Pradel
 * @author Babett Schaliz
 * @author Benjamin Scholz
 */
public abstract class SynDiaBacktracking implements IAlgorithm, Serializable {

	/**
	 * Constructor
	 */
	public SynDiaBacktracking() {
	}

	/**
	 * @return true, if there is a previous element, so you can go a step
	 *               back in history; false if not
	 */
	public boolean hasPreviousHistStep() {
		return true;
	}

	/**
	 * @return true, if there is a next element, so you can go a step
	 *               forward in history; false if not
	 */
	public boolean hasNextHistStep() {
		return true;
	}

	/**
	 * @return true, if you can perform the next step in the algorithm, so
	 *               you can call performNextStep() false if not
	 */
	public boolean hasNextStep() {
		return true;
	}

	/**
	 * this method is called if the "do algorithm" button on the GUI is
	 * pushed realise the backtrackingAlgorithm has to find the next element
	 * in the syntactical diagram and...
	 * 
	 * @exception IndexOutOfBound
	 *                            if there is now further step to go
	 */
	public void performNextStep() throws IndexOutOfBoundsException {
	}

	/**
	 * this method is called if the forwardButton on the GUI is pushed and
	 * should restore the next saved step of the visualisation
	 * 
	 * @exception IndexOutOfBound
	 *                            if there is now previous step to go
	 */
	public void nextHistStep() throws IndexOutOfBoundsException {
	}

	/**
	 * this method is called if the backwardButton on the GUI is pushed and
	 * should restore the last saved step of the visualisation
	 * 
	 * @exception IndexOutOfBound
	 *                            if there is now previous step to go
	 */
	public void previousHistStep() throws IndexOutOfBoundsException {
	}

	/**
	 * Checks and corrects the reading order of all diagrams of the system
	 * <code>synDiaDef</code>.
	 * 
	 * @param synDiaDef
	 *                       The <code>SynDiaSystem</code> to check.
	 */
	protected void checkReadingOrder(SynDiaSystem synDiaDef) {
		LinkedList synDiaInitials = synDiaDef.getInitialDiagrams();
		for (int i = 0; i < synDiaInitials.size(); i++) {
			((SynDiaInitial) synDiaInitials.get(i)).getInnerElem()
					.checkReadingOrder(0);
		}
	}

}