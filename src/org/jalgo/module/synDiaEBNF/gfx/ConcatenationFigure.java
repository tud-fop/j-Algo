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
 * Created on 15.06.2004
 */

package org.jalgo.module.synDiaEBNF.gfx;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * @author Michael Pradel
 * @author Cornelius Hald
 */
public class ConcatenationFigure extends CompositeSynDiaFigure {

	private static final long serialVersionUID = -3624766979324957655L;

	private final int minNumOfInteriorFigures = 0;

	private final int hSpacing = 15;

	private LinkedList<PolylineConnection> innerConn;

	public ConcatenationFigure(int numOfInteriorFigures) {

		/*throws SynDiaException*/{
			if (numOfInteriorFigures < minNumOfInteriorFigures)
				// groesser 2
				//throw new SynDiaException("The number of interior figures to create has to at least 2.");

				this.numOfInteriorFigures = numOfInteriorFigures;
			startGap = endGap = 0;

			initializeLayout();

			// create startFigure
			startFigure = new EmptyFigure();
			interiorFigures.add(startFigure);
			add(startFigure);

			// create clouds
			for (int i = 0; i < numOfInteriorFigures; i++) {
				CloudFigure tC = new CloudFigure();
				interiorFigures.add(tC);
				add(tC);
			}
		}

		// create endFigure
		endFigure = new EmptyFigure();
		interiorFigures.add(endFigure);
		add(endFigure);

		// create connections
		innerConn = new LinkedList<PolylineConnection>();

		// create first connection
		PolylineConnection conIn = new PolylineConnection();
		innerConn.add(conIn);
		connectionsToInteriorFigures.add(conIn);
		add(conIn);

		// create middel connections (thats all connections minus the first and the last)
		for (int i = 0; i + 3 < interiorFigures.size(); i++) {
			PolylineConnection con = new PolylineConnection();
			innerConn.add(con);
			add(con);
		}

		// create last connection
		PolylineConnection conOut = new PolylineConnection();
		innerConn.add(conOut);
		connectionsToInteriorFigures.add(conOut);
		add(conOut);

		reposition();
	}

	public void remove(int index) throws SynDiaException {
		//TODO: implement, but not soooo important	
	}

	public void remove(SynDiaFigure figureToRemove) throws SynDiaException {
		//TODO: implement, but not soooo important
	}

	public void replace(SynDiaFigure newFigure, int index) throws SynDiaException {

		// Adjust index. The startFigure should not count
		index++;

		// test if possible
		if (index < 0 || index > interiorFigures.size() - 2)
			throw new InvalidIndexException(index);

		remove((Figure) interiorFigures.get(index));
		add(newFigure);

		interiorFigures.set(index, newFigure);

		reposition();
	}

	public void replace(SynDiaFigure oldFigure, SynDiaFigure newFigure) throws SynDiaException {

		for (int i = 0; i < interiorFigures.size(); i++) {
			if (interiorFigures.get(i).equals(oldFigure)) {
				replace(newFigure, i - 1);
			}
		}
	}

	public void reposition() {
		setPositions();
		// Recursive reposition of parents
		if (this.getParent() instanceof CompositeSynDiaFigure) {
			((CompositeSynDiaFigure) this.getParent()).reposition();
		}
	}

	private void setPositions() {
		// position the figures

		int x = 0;
		for (SynDiaFigure figure : interiorFigures) {
			// Case it's the endFigure
			if (figure.equals(endFigure))
				x += endGap;
			layout.setConstraint(figure, new Rectangle(x, 0, -1, -1));
			// Case it's the endFigure
			if (figure.equals(startFigure))
				x += startGap;
			x += hSpacing + figure.getPreferredSize().width;
		}

		// update connections
		for (int i = 0; i + 1 < interiorFigures.size(); i++) {
			PolylineConnection con = innerConn.get(i);
			con.setSourceAnchor(interiorFigures.get(i).getRightAnchor());
			con.setTargetAnchor(interiorFigures.get(i + 1).getLeftAnchor());
		}
	}

	public void setStartGap(int startGap) {
		this.startGap = startGap;
		setPositions();
	}

	public void setEndGap(int endGap) {
		this.endGap = endGap;
		setPositions();
	}

	private int width() {
		int width = 0;
		for (SynDiaFigure figure : interiorFigures) {
			width += figure.getPreferredSize().width;
		}
		width += startGap + endGap + (interiorFigures.size() - 1) * hSpacing;
		return width;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int arg0, int arg1) {

		int width = width() + endFigure.getPreferredSize().width;

		int heigth = 0;

		// Find maximal height of interior Figures
		for (SynDiaFigure figure : interiorFigures) {
			if (figure.getPreferredSize().height > heigth) {
				heigth = figure.getPreferredSize().height;
			}
		}
		return new Dimension(width, heigth + 2);
	}

	public List<SynDiaFigure> getInteriorFigures() {
		LinkedList<SynDiaFigure> innerFigures = new LinkedList<SynDiaFigure>();
		
		innerFigures.addAll(interiorFigures);
		// Cut away startFigure and endFigure
		innerFigures.removeFirst();
		innerFigures.removeLast();
		return innerFigures;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#highlight(boolean)
	 */
	public void highlight(boolean highlight) {
	}
}
