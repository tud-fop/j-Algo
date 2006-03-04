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
 * Created on May 11, 2004
 */
package org.jalgo.main.gfx;

import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.MouseMotionListener;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;

/**
 * @author Cornelius Hald
 * @author Malte Blumberg
 */
public class Dragger extends MouseMotionListener.Stub implements MouseListener {
	public Dragger(IFigure figure) {
		figure.addMouseMotionListener(this);
		figure.addMouseListener(this);
	}
	
	private Point last;
	private Figure dragFigure;
	
	public void mouseClicked(MouseEvent e) {
		// this method has no effect
	}
	
	public void mouseDoubleClicked(MouseEvent e) {
		// this method has no effect
	}
	
	public void mousePressed(MouseEvent e) {
		last = new Point(Math.round((float) e.getLocation().x/10)*10,Math.round((float) e.getLocation().y/10)*10);
		dragFigure=((Figure) e.getSource());
	}
	
	public void mouseReleased(MouseEvent e) {
		dragFigure=null;
	}
	
	public void mouseDragged(MouseEvent e) {
		if (dragFigure != null) {
			Point p = e.getLocation();
			Point next = new Point(Math.round((float) p.x/10)*10,Math.round((float) p.y/10)*10);
			Dimension delta = new Dimension(next.x-last.x,next.y-last.y);
			dragFigure.setLocation(dragFigure.getLocation().getTranslated(delta.width,delta.height));
			
			last.setLocation(next);
		}
	}
}