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
 * Created on 16.06.2004
 */

package org.jalgo.module.synDiaEBNF.gfx;

import java.util.List;

import org.eclipse.draw2d.Label;
import org.eclipse.draw2d.ToolbarLayout;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * Represents an abstact syntactical diagram, including its name and the
 * diagram.
 * 
 * @author Marco Zimmerling
 * @author Michael Pradel
 * @author Cornelius Hald
 */
public class InitialFigure extends CompositeSynDiaFigure {

	private static final long serialVersionUID = 7398349169950981383L;
	private ConcatenationFigure concat;
	private Label label;

	private boolean startFigure; // auszeichnung, das dieses
												// Startdiagramm

	public InitialFigure(String label) {
		ToolbarLayout tbl = new ToolbarLayout(true);
		tbl.setSpacing(10);
		setLayoutManager(tbl);
		concat = new ConcatenationFigure(1);
		this.label = new Label(label);
		add(this.label);
		add(concat);

		startFigure = false;
	}

	public String getLabel() {
		return label.getText();
	}

	public void setLabel(String label) {
		this.label.setText(label);
	}

	public SynDiaFigure getSynDia() {
		return (SynDiaFigure) concat.getInteriorFigures().get(0);
	}

	public void setStartFigure() {
		startFigure = true;
	}

	public boolean isStartFigure() {
		return startFigure;
	}

	public void replace(SynDiaFigure newFigure, int index) {
		concat.replace(newFigure, index);
	}

	public void replace(SynDiaFigure oldFigure, SynDiaFigure newFigure) {
		concat.replace(oldFigure, newFigure);
	}

	public void reposition() {
		// do nothing, because InitialFigure has no parent
	}

	public void setStartGap(int startGap) {
		concat.setStartGap(startGap);
	}

	public void setEndGap(int endGap) {
		concat.setEndGap(endGap);
	}

	public Dimension getPreferredSize(int arg0, int arg1) {
		return concat.getPreferredSize(arg0, arg1);
	}

	public List getInteriorFigures() {
		return concat.getInteriorFigures();
	}

	public void highlightConnectionTo(SynDiaFigure targetFigure,
			boolean selection) throws SynDiaException {

	}

	public void highlightConnectionTo(int index, boolean selection)
			throws SynDiaException {

	}

	public void highlightConnectionFrom(SynDiaFigure sourceFigure,
			boolean selection) throws SynDiaException {

	}

	public void highlightConnectionFrom(int index, boolean selection)
			throws SynDiaException {

	}

	public void remove(int index) throws SynDiaException {

	}

	public void highlight(boolean highlight) {

	}
}