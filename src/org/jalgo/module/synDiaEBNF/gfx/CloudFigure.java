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
 * Created on Jun 11, 2004
 */
 
package org.jalgo.module.synDiaEBNF.gfx;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.PolylineConnection;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.jalgo.main.gfx.ClickListener;
import org.jalgo.main.gfx.FixPointAnchor;

/**
 * @author Cornelius Hald
 * @author Marco Zimmerling
 */
public class CloudFigure extends SynDiaFigure {

	private Ellipse interiorFigure;

	public CloudFigure() {

		initializeLayout();
		setOpaque(true);

		startGap = 0;
		endGap = 0;

		startFigure = new EmptyFigure();
		layout.setConstraint(startFigure, new Rectangle(0, 0, -1, -1));
		add(startFigure);

		interiorFigure = new Ellipse();
		interiorFigure.setLineStyle(Graphics.LINE_DOT);
		interiorFigure.setOpaque(true);
		layout.setConstraint(
			interiorFigure,
			new Rectangle(
				startFigure.getPreferredSize().width + startGap,
				0,
				-1,
				-1));
		add(interiorFigure);
		// setting size to fix value
		

		endFigure = new EmptyFigure();
		layout.setConstraint(
			endFigure,
			new Rectangle(
				startFigure.getPreferredSize().width
					+ startGap
					+ interiorFigure.getPreferredSize().width
					+ endGap,
				0,
				-1,
				-1));
		add(endFigure);

		//create connections
		PolylineConnection conIn = new PolylineConnection();
		//setting no connection router for direct connection
		conIn.setSourceAnchor(new FixPointAnchor(startFigure, SWT.RIGHT));
		conIn.setTargetAnchor(new FixPointAnchor(interiorFigure, SWT.LEFT));
		add(conIn);

		PolylineConnection conOut = new PolylineConnection();
		//setting no connection router for direct connection
		conOut.setSourceAnchor(new FixPointAnchor(interiorFigure, SWT.RIGHT));
		conOut.setTargetAnchor(new FixPointAnchor(endFigure, SWT.LEFT));
		add(conOut);

		new ClickListener(interiorFigure);
	}

	public void setStartGap(int startGap) {
		int divGap = startGap - this.startGap;
		this.startGap = startGap;

		Rectangle oldConstraint =
			(Rectangle) layout.getConstraint(interiorFigure);
		layout.setConstraint(
			interiorFigure,
			oldConstraint.setLocation(
				oldConstraint.x + divGap,
				oldConstraint.y));

		oldConstraint = (Rectangle) layout.getConstraint(endFigure);
		layout.setConstraint(
			endFigure,
			oldConstraint.setLocation(
				oldConstraint.x + divGap,
				oldConstraint.y));
	}

	public void setEndGap(int endGap) {
		int divGap = endGap - this.endGap;
		this.endGap = endGap;

		Rectangle oldConstraint = (Rectangle) layout.getConstraint(endFigure);
		layout.setConstraint(
			endFigure,
			oldConstraint.setLocation(
				oldConstraint.x + divGap,
				oldConstraint.y));
	}

	/**
	 * @see org.eclipse.draw2d.Figure#getPreferredSize(int, int)
	 */
	public Dimension getPreferredSize(int arg0, int arg1) {
		int width =
			startFigure.getPreferredSize().width
				+ startGap
				+ interiorFigure.getPreferredSize().width
				+ endGap
				+ endFigure.getPreferredSize().width
				+ 2;

		int height = interiorFigure.getPreferredSize().height;

		return new Dimension(width, height);
	}

	public void highlight(boolean highlight) {
		if (highlight) {
			interiorFigure.setForegroundColor(
				SynDiaColors.highlightEntireFigure);
		} else {
			interiorFigure.setForegroundColor(SynDiaColors.normal);
		}
	}

	/**
	 * @see org.eclipse.draw2d.Figure#setBackgroundColor(org.eclipse.swt.graphics.Color)
	 */
	public void setBackgroundColor(Color arg0) {
		interiorFigure.setBackgroundColor(arg0);
	}
}
