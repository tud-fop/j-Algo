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

package org.jalgo.main.util;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PrintFigureOperation;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.printing.Printer;
import org.eclipse.swt.widgets.Display;

/**
 * Class responsible for printing Figures.
 * 
 * @author Anne Kersten
 */
public class PrintScaledFigureOperation extends PrintFigureOperation {

/**
 * A print mode that resizes the printed image as the size of a page and then uses the 
 * existing layout of that figure to readjust the children.
 * 
 * Note: Works only for figures with a given layout.
 */
public static final int USE_LAYOUT = 5;

private int printMode = TILE;
private Color oldBGColor;
private Rectangle oldBounds;


/**
 * Constructor for PrintScaledFigureOperation.
 * <p>
 * Note: Descendants must call setPrintSource(IFigure) to set the IFigure that is to be 
 * printed.
 * @see org.eclipse.draw2d.PrintOperation#PrintOperation(Printer)
 * @see org.eclipse.draw2d.PrintFigureOperation#PrintFigureOperation(Printer)
 */
protected PrintScaledFigureOperation(Printer p) {
	super(p);
}

/**
 * Constructor for PrintScaledFigureOperation.
 * 
 * @param p Printer to print on
 * @param srcFigure Figure to print
 * @see org.eclipse.draw2d.PrintFigureOperation#PrintFigureOperation(Printer, IFigure)
 */
public PrintScaledFigureOperation(Printer p, IFigure srcFigure) {
	super(p);
	setPrintSource(srcFigure);
}

/**
 * Returns the current print mode.  The print mode is one of: {@link #FIT_HEIGHT},
 * {@link #FIT_PAGE}, {@link #FIT_WIDTH}, or {@link #USE_LAYOUT}.
 * @return the print mode
 */
protected int getPrintMode() {
	return printMode;
}


/**
 * @see org.eclipse.draw2d.PrintOperation#preparePrintSource()
 */
protected void preparePrintSource() {
	oldBGColor = getPrintSource().getLocalBackgroundColor();
	getPrintSource().setBackgroundColor(ColorConstants.white);
	
	double dpiScale = getPrinter().getDPI().x / Display.getCurrent().getDPI().x;
	Rectangle printRegion = getPrintRegion();
	// put the print region in display coordinates
	printRegion.width /= dpiScale;
	printRegion.height /= dpiScale;
	if (getPrintMode()==USE_LAYOUT)
	{
		oldBounds = getPrintSource().getBounds();
		getPrintSource().setBounds(printRegion);
		getPrintSource().validate();

	}
}



/**
 * @see org.eclipse.draw2d.PrintOperation#restorePrintSource()
 */
protected void restorePrintSource() {
	getPrintSource().setBackgroundColor(oldBGColor);
	oldBGColor = null;
	if(getPrintMode() == USE_LAYOUT)
	{
		getPrintSource().setBounds(oldBounds);
		oldBounds = null;
		getPrintSource().validate();
	}
}

/**
 * Sets the print mode.  Possible values are {@link #TILE}, {@link #FIT_HEIGHT}, 
 * {@link #FIT_WIDTH}, {@link #FIT_PAGE} and {@link #USE_LAYOUT}.
 * @param mode the print mode
 */
public void setPrintMode(int mode) {
	super.setPrintMode(mode);
}

}
