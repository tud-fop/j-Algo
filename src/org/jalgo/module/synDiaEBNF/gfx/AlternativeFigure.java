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
 * Created on Jun 22, 2004
 */

package org.jalgo.module.synDiaEBNF.gfx;

import java.util.List;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.jalgo.main.gfx.RoundedManhattanConnectionRouter;

/**
 * @author Cornelius Hald
 */
public class AlternativeFigure extends CompositeSynDiaFigure {

	private static final long serialVersionUID = 1038968063975627939L;

	private final int vSpacing = 15, hSpacing = 20;

	private final int minNumOfInteriorFigures = 2;

	private int maxWidth;

	public AlternativeFigure(int numOfInteriorFigures) throws SynDiaException {

		if (numOfInteriorFigures < minNumOfInteriorFigures)
			// valid passed number of interior figures to create?
			throw new SynDiaException("The number of interior figures to create has to at least 2."); //$NON-NLS-1$

		this.numOfInteriorFigures = numOfInteriorFigures;
		// store for further proceedings

		initializeLayout();

		// Create startFigure
		startFigure = new EmptyFigure();
		add(startFigure);

		// Create interiorFigures
		CloudFigure tCF;
		for (int i = 0; i < this.numOfInteriorFigures; i++) {
			tCF = new CloudFigure();
			interiorFigures.add(tCF);
			add(tCF);
		}

		// Create endFigure
		endFigure = new EmptyFigure();
		add(endFigure);

		// *** create connections ***
		PolylineConnection tPC;
		for (int i = 0; i < this.numOfInteriorFigures; i++) {
			// connection from startFigure to interiorFigures[i]
			tPC = new PolylineConnection();
			tPC.setConnectionRouter(new RoundedManhattanConnectionRouter());
			tPC.setSourceAnchor(startFigure.getRightAnchor());
			tPC.setTargetAnchor(interiorFigures.get(i).getLeftAnchor());
			add(tPC);
			connectionsToInteriorFigures.add(tPC);

			// connection from interiorFigures[i] to endFigure
			tPC = new PolylineConnection();
			tPC.setConnectionRouter(new RoundedManhattanConnectionRouter());
			tPC.setSourceAnchor(interiorFigures.get(i).getRightAnchor());

			tPC.setTargetAnchor(endFigure.getLeftAnchor());
			add(tPC);
			connectionsFromInteriorFigures.add(tPC);
		}
		reposition();
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.CompositeSynDiaFigure#reposition()
	 */
	public void reposition() {
		setPositions();

		// Recursive reposition of parents
		if (this.getParent() instanceof CompositeSynDiaFigure) {
			((CompositeSynDiaFigure) this.getParent()).reposition();
		}
	}

	private void setPositions() {

		// Layout startFigure
		layout.setConstraint(startFigure, new Rectangle(0, 0, -1, -1));

		// get die widest interiorFigure
		maxWidth = 0;
		int width = 0;
		
		for (SynDiaFigure figure : interiorFigures) {
			width = figure.getPreferredSize().width;
			if (width > maxWidth)
				maxWidth = width;
		}

		// Layout interiorFigures
		int x = 0;
		int y = 0;
		
		for (SynDiaFigure figure : interiorFigures) {
			Rectangle rect = new Rectangle(x, y, -1, -1);

			if (figure.getPreferredSize().width < maxWidth) {
				rect.x = hSpacing + startGap;
				// Set Gaps to fit width
				int diff = maxWidth - figure.getPreferredSize().width;
				figure.setStartGap(figure.getStartGap() + (diff / 2));
				figure.setEndGap(figure.getEndGap() + (diff / 2));

			} else {
				rect.x = hSpacing + startGap;
			}

			layout.setConstraint(figure, rect);

			y += vSpacing + figure.getPreferredSize().height;
		}

		// Layout endFigure
		x = startGap + maxWidth + 2 * hSpacing + endGap;
		layout.setConstraint(endFigure, new Rectangle(x, 0, -1, -1));
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.CompositeSynDiaFigure#replace(org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure, org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure)
	 */
	public void replace(SynDiaFigure oldFigure, SynDiaFigure newFigure) throws SynDiaException {
		for (int i = 0; i < interiorFigures.size(); i++) {
			if (interiorFigures.get(i).equals(oldFigure)) {
				replace(newFigure, i);
			}
		}
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.CompositeSynDiaFigure#replace(org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure, int)
	 */
	public void replace(SynDiaFigure newFigure, int index) throws SynDiaException {
		// Update Figures
		Figure oldFigure = interiorFigures.get(index);
		interiorFigures.set(index, newFigure);
		remove(oldFigure);
		add(newFigure);

		// Update connections
		(connectionsToInteriorFigures.get(index)).setTargetAnchor(interiorFigures.get(index).getLeftAnchor());
		(connectionsFromInteriorFigures.get(index)).setSourceAnchor(interiorFigures.get(index).getRightAnchor());

		reposition();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int arg0, int arg1) {
		int width = 0;
		int heigth = 0;

		width = maxWidth + startGap + 2 * hSpacing + startFigure.getPreferredSize().width
				+ endFigure.getPreferredSize().width + endGap;

		// Get total heigth
		for (SynDiaFigure figure : interiorFigures) {
			heigth += vSpacing + figure.getPreferredSize().height;
		}
		// Correct the heigth
		heigth -= vSpacing;

		return new Dimension(width, heigth + 2);
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.CompositeSynDiaFigure#remove(int)
	 */
	public void remove(int index) throws SynDiaException {
		// TODO Implement removing of interior Figures

	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#setStartGap(int)
	 */
	public void setStartGap(int startGap) {
		this.startGap = startGap;
		setPositions();
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#setEndGap(int)
	 */
	public void setEndGap(int endGap) {
		this.endGap = endGap;
		setPositions();
	}

	public List<SynDiaFigure> getInteriorFigures() {
		return interiorFigures;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#highlight(boolean)
	 */
	public void highlight(boolean highlight) {
	}
}
