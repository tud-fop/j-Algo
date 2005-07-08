/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
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
 * Created on 15.06.2004
 */

package org.jalgo.module.synDiaEBNF.gfx;

import java.util.LinkedList;

import org.eclipse.draw2d.PolylineConnection;

/**
 * This class extends SynDiaFigure by adding some methods to work with composites. 
 * 
 * @author Marco Zimmerling
 */
public abstract class CompositeSynDiaFigure extends SynDiaFigure {

	protected int numOfInteriorFigures; // number of interior figures
	// figures inside
	protected LinkedList<SynDiaFigure> interiorFigures = new LinkedList<SynDiaFigure>(); 
	// connections from startFigure to interior ones
	protected LinkedList<PolylineConnection> connectionsToInteriorFigures = new LinkedList<PolylineConnection>();
	// connections from interior figures to endFigure
	protected LinkedList<PolylineConnection> connectionsFromInteriorFigures = new LinkedList<PolylineConnection>();

	// *** abstract methods ***

	/**
	 * Replaces oldFigure by newFigure if oldFigure is one of the interior figures. 
	 * 
	 * @param oldFigure 					figure to replace
	 * @param newFigure					figure instead of oldFigure
	 * @throws SynDiaException		oldFigure is not one of the interior figures
	 */
	public abstract void replace(SynDiaFigure oldFigure, SynDiaFigure newFigure) throws SynDiaException;

	/**
	 * Replaces the figure at the passed index (interiorFigures[index]) by newFigure in case of a valid index. 
	 * 
	 * @param newFigure					figure instead of interiorFigure[index]
	 * @param index							figure at this position will be replaced 
	 * @throws SynDiaException		oldFigure is not one of the interior figures
	 */
	public abstract void replace(SynDiaFigure newFigure, int index) throws SynDiaException;

	/**
	 * Removes interiorFigures[index].
	 *  
	 * @param index							figure at this position will be removed
	 * @throws SynDiaException		invalid index
	 */
	public abstract void remove(int index) throws SynDiaException;

	// *** implemented methods ***

	public void highlightConnectionTo(SynDiaFigure targetFigure, boolean selection) throws SynDiaException {

	}

	public void highlightConnectionTo(int index, boolean selection) throws SynDiaException {

	}

	public void highlightConnectionFrom(SynDiaFigure sourceFigure, boolean selection) throws SynDiaException {

	}

	public void highlightConnectionFrom(int index, boolean selection) throws SynDiaException {

	}

	/**
	 * This method is for laying out the children.
	 */
	public abstract void reposition();

	/*public void remove() throws SynDiaException {
	 ((CompositeSynDiaFigure)getParent()).remove(this);
	 }*/
}
