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

	public void mousePressed(MouseEvent e) {
		lastPoint = e.getLocation();
		ClickCollector.addItem(this);
		if (!ClickCollector.isCollecting()){
			 mouseExited(e); 
		}
	}

	public void mouseEntered(MouseEvent e) {
		oldColor = this.myFigure.getBackgroundColor();
		if (ClickCollector.isCollecting()) {
			try {
				GfxUtil.getAppShell().setCursor(
					new Cursor(
						GfxUtil.getAppShell().getDisplay(),
						SWT.CURSOR_UPARROW));
			} catch (NullPointerException exception) {
				// TODO: Catch Exception
			}
			
			// maybe change this back later on...
			//this.myFigure.setBackgroundColor(ColorConstants.lightBlue);
		}
	}

	public void mouseExited(MouseEvent e) {

		try {
			GfxUtil.getAppShell().setCursor(
				new Cursor(
					GfxUtil.getAppShell().getDisplay(),
					SWT.CURSOR_ARROW));
		} catch (NullPointerException exception) {
			// TODO Catch Exception
		}

		this.myFigure.setBackgroundColor(oldColor);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.MouseListener#mouseReleased(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseReleased(MouseEvent arg0) {
	}

	/* (non-Javadoc)
	 * @see org.eclipse.draw2d.MouseListener#mouseDoubleClicked(org.eclipse.draw2d.MouseEvent)
	 */
	public void mouseDoubleClicked(MouseEvent arg0) {
	}

}
