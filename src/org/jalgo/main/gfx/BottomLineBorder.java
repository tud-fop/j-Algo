/*
 * Created on Jun 15, 2004
 */
 
package org.jalgo.main.gfx;

import org.eclipse.draw2d.AbstractBorder;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Insets;
import org.eclipse.draw2d.geometry.Point;

/**
 * @author Malte Blumberg
 */
public class BottomLineBorder extends AbstractBorder {
	/*
		private int textWidth;
		
		public BottomLineBorder(int textWidth)
		{
			this.textWidth=textWidth;
		}
	*/
	public Insets getInsets(IFigure arg0) {
		return new Insets(0);
	}

	public void paint(IFigure figure, Graphics graphics, Insets insets) {

		graphics.setLineWidth(1);
		Insets inset = new Insets(0);
		Point startPoint =
			new Point(
				getPaintRectangle(figure, insets).getBottomLeft().x,
				getPaintRectangle(figure, insets).getBottom().y - 1);
		Point endPoint =
			new Point(
				getPaintRectangle(figure, insets).getBottomRight().x,
				getPaintRectangle(figure, insets).getBottom().y - 1);

		graphics.drawLine(startPoint, endPoint);

	}

}