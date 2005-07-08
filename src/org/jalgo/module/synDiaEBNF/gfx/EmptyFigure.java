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
 * Created on 10.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gfx;

import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.swt.SWT;
import org.jalgo.main.gfx.FixPointAnchor;

/**
 * @author Michael Pradel
 * @author Marco Zimmerling
 * @author Cornelius Hald
 */
public class EmptyFigure extends SynDiaFigure {

	private static final long serialVersionUID = 1933204442022083350L;

	public EmptyFigure() {
		
		// Add a inner connection cause width is >= 2
		PolylineConnection con = new PolylineConnection();
		con.setSourceAnchor(new FixPointAnchor(this, SWT.LEFT));
		con.setTargetAnchor(new FixPointAnchor(this, SWT.RIGHT));
		add(con);
	}

	public FixPointAnchor getLeftAnchor() {
		return new FixPointAnchor(this, SWT.LEFT);
	}

	public FixPointAnchor getRightAnchor() {
		return new FixPointAnchor(this, SWT.RIGHT);
	}

	/**
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int arg0, int arg1) {
		if ((startGap + endGap) < 2 ) {
			return new Dimension(2, 36);
		}
		return new Dimension(startGap + endGap, 36);
	}

	/**
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#setStartGap(int)
	 */
	public void setStartGap(int startGap) {
		this.startGap = startGap;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.synDiaEBNF.gfx.SynDiaFigure#setEndGap(int)
	 */
	public void setEndGap(int endGap) {
		this.endGap = endGap;
	}

	public void highlight(boolean highlight) {
	}
}
