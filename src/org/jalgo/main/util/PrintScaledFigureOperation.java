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
private IFigure printSource;
private Rectangle oldBounds;


/**
 * Constructor for PrintScaledFigureOperation.
 * <p>
 * Note: Descendants must call setPrintSource(IFigure) to set the IFigure that is to be 
 * printed.
 * @see org.eclipse.draw2d.PrintOperation#PrintOperation(Printer)
 * @see org.eclipse.draw2d.PrintFigureOpration#PrintFigureOpertion(Printer)
 */
protected PrintScaledFigureOperation(Printer p) {
	super(p);
}

/**
 * Constructor for PrintScaledFigureOperation.
 * 
 * @param p Printer to print on
 * @param srcFigure Figure to print
 * @see org.eclipse.draw2d.PrintFigureOpration#PrintFigureOpertion(Printer, IFigure)
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
