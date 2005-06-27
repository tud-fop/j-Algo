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

/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/


package org.jalgo.main.gfx;
import java.util.*;

import org.eclipse.draw2d.FlowLayout;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.*;

/**
 * Lays out children in rows or columns, wrapping when the current row/column is filled.
 * The aligment and spacing of rows in the parent can be configured.  The aligment and
 * spacing of children within a row can be configured. Wraps already 40 before the end 
 * of the row leaving place for hte connections.
 * 
 * @see org.eclipse.draw2d.FlowLayout
 * @author Anne Kersten
 */
public class BorderFlowLayout
	extends FlowLayout
{

/** The alignment along the minor axis. */
protected int minorAlignment = ALIGN_LEFTTOP;

/** The alignment along the major axis. */
protected int majorAlignment = ALIGN_LEFTTOP;

/** The spacing along the minor axis. */
protected int minorSpacing = 5;

/** The spacing along the major axis. */
protected int majorSpacing = 5;

private WorkingData data = null;

/**
 * Holds the necessary information for layout calculations.
 */
class WorkingData {
	int rowHeight, rowWidth, rowCount, rowX, rowY, maxWidth;
	Rectangle bounds[], area;
	IFigure row[];
}

/**
 * Constructs a BorderFlowLayout with horizontal orientation.
 */
public BorderFlowLayout() { }

/**
 * Constructs a BorderFlowLayout whose orientation is given in the input.
 * @param isHorizontal <code>true</code> if the layout should be horizontal
 * 
 */
public BorderFlowLayout(boolean isHorizontal) {
	super.setHorizontal(isHorizontal);
}

/**
 * Initializes state data for laying out children, based on the Figure given as input.
 *
 * @param parent the parent figure
 * @see org.eclipse.draw2d.FlowLayout#initVariables(IFigure) 
 */
private void initVariables(IFigure parent) {
	data.row = new IFigure[parent.getChildren().size()];
	data.bounds = new Rectangle[data.row.length];
	data.maxWidth = data.area.width;
}

/**
 * Initializes the state of row data, which is internal to the layout process.
 * @see org.eclipse.draw2d.FlowLayout#initRow() 
 */
private void initRow() {
	data.rowX = 0;
	data.rowHeight = 0;
	data.rowWidth = 0;
	data.rowCount = 0;
}

/**
 * Layouts one row of components. This is done based on the layout's orientation, minor 
 * alignment and major alignment.
 *
 * @param parent the parent figure
 * @since 2.0
 */
protected void layoutRow(IFigure parent) {
	int majorAdjustment = 0;
	int minorAdjustment = 0;
	int correctMajorAlignment = majorAlignment;
	int correctMinorAlignment = minorAlignment;

	majorAdjustment = data.area.width - data.rowWidth + getMinorSpacing();
	
	switch (correctMajorAlignment) {
		case ALIGN_LEFTTOP: 
			majorAdjustment = 0;
			break;
		case ALIGN_CENTER:
			majorAdjustment /= 2;
			break;
		case ALIGN_RIGHTBOTTOM:
			break;
	}

	for (int j = 0; j < data.rowCount; j++) {
		if (fill) {
			data.bounds[j].height = data.rowHeight;	
		} else {
			minorAdjustment = data.rowHeight - data.bounds[j].height;
			switch (correctMinorAlignment) {
				case ALIGN_LEFTTOP: 
					minorAdjustment = 0;
					break;
				case ALIGN_CENTER:
					minorAdjustment /= 2;
				break;
				case ALIGN_RIGHTBOTTOM:
					break;
			}
			data.bounds[j].y += minorAdjustment;
		}
		data.bounds[j].x += majorAdjustment;
		
		setBoundsOfChild(parent, data.row[j], transposer.t(data.bounds[j]));
	}
	data.rowY += getMajorSpacing() + data.rowHeight;
	initRow();
}


/** * @see org.eclipse.draw2d.LayoutManager#layout(IFigure)
 *  */
public void layout(IFigure parent) {
	data = new WorkingData();
	Rectangle relativeArea = parent.getClientArea();
	data.area = transposer.t(relativeArea);

	Iterator iterator = parent.getChildren().iterator();
	int dx;

	//Calculate the hints to be passed to children
	int wHint = -1;
	int hHint = -1;
	if (isHorizontal())
		wHint = parent.getClientArea().width;
	else
		hHint = parent.getClientArea().height;

	initVariables(parent);
	initRow();
	int i = 0; 
	while (iterator.hasNext()) {
		IFigure f = (IFigure)iterator.next();
		Dimension pref = transposer.t(getChildSize(f, wHint, hHint));
		Rectangle r = new Rectangle(0, 0, pref.width, pref.height);

		if (data.rowCount > 0) {
			if(horizontal)
			{if (data.rowWidth + pref.width + 40 > data.maxWidth)				
					layoutRow(parent);}
			else if (data.rowWidth + pref.width > data.maxWidth)				
			layoutRow(parent);
		}
		r.x = data.rowX;
		r.y = data.rowY;
		dx = r.width + getMinorSpacing();
		data.rowX += dx;
		data.rowWidth += dx;
		data.rowHeight = Math.max(data.rowHeight, r.height);
		data.row [data.rowCount] = f;
		data.bounds[data.rowCount] = r;
		data.rowCount++;
		i++;
	}
	if (data.rowCount != 0)
		layoutRow(parent);
	data = null;
}

}