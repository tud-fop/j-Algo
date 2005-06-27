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

package org.jalgo.main.gfx;

import org.eclipse.draw2d.AbstractConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWT;

/**
 * @author Anne Kersten
 */
public class FixPointAnchor extends AbstractConnectionAnchor {
	


	private int style;
	
	/**
	 * Constructs a new FixPointAnchor with the given owner figure at a fixed point.
	 * @param source  The owner Figure
	 * @param style   SWT.TOP, SWT.LEFT, SWT.RIGHT, SWT.BOTTOM <p>  Location of the anchor in the Figure.
	 */
	public FixPointAnchor(IFigure source, int style){
		super(source);
		this.style=style;
	}
	 
	 /**
	  *  Returns the Point where the FixPointAnchor is located.
	  * @param reference The reference point
	  * @return The anchor location  
	  */
	 
	public Point getLocation(Point reference) {
		Rectangle r = getOwner().getBounds().getCopy();
		getOwner().translateToAbsolute(r);
		switch (style)
		{
			case(SWT.TOP):return r.getTop();
			case(SWT.RIGHT):return r.getRight();
			case(SWT.BOTTOM):return r.getBottom().translate(0,-1);
			case(SWT.LEFT): return r.getLeft();
			default:return r.getLeft(); 
		}
	}
}
