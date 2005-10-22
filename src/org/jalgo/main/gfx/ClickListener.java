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
 * Created on 13.06.2004
 *
 */
package org.jalgo.main.gfx;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.jalgo.main.util.ClickCollector;
import org.jalgo.main.util.GfxUtil;

/**
 * A ClickListener informs the ClickCollector that its Figure has been clicked.
 * It also reports the click-position to the ClickCollector.
 * 
 * Example: 
 * new ClickListener(mySWTfigure);
 * 
 * @author Hauke Menges
 */
public class ClickListener
	extends MouseMotionListener.Stub
	implements MouseListener {

	private Figure myFigure;
	private Point lastPoint;
	private Color oldColor;

	public ClickListener(IFigure figure) {
		figure.addMouseMotionListener(this);
		figure.addMouseListener(this);
		this.myFigure = (Figure) figure;
	}

	public Figure getFigure() {
		return myFigure;
	}

	public Point getLastPoint() {
		return lastPoint;
	}

	public void mousePressed(MouseEvent event) {
		lastPoint = event.getLocation();
		ClickCollector.addItem(this);
		if (!ClickCollector.isCollecting()){
			 mouseExited(event); 
		}
	}

	@Override
	public void mouseEntered(MouseEvent event) {
		oldColor = this.myFigure.getBackgroundColor();
		if (ClickCollector.isCollecting()) {
			GfxUtil.getAppShell().setCursor(
				new Cursor(
					GfxUtil.getAppShell().getDisplay(),
					SWT.CURSOR_UPARROW));
			
			// maybe change this back later on...
			//this.myFigure.setBackgroundColor(ColorConstants.lightBlue);
		}
	}

	@Override
	public void mouseExited(MouseEvent event) {
		GfxUtil.getAppShell().setCursor(
			new Cursor(
				GfxUtil.getAppShell().getDisplay(),
				SWT.CURSOR_ARROW));
		this.myFigure.setBackgroundColor(oldColor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.MouseListener#mouseReleased(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseReleased(MouseEvent event) {
	// this method has no effect
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.MouseListener#mouseDoubleClicked(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseDoubleClicked(MouseEvent event) {
	// this method has no effect
	}
}