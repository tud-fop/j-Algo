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
	
	}
	
	public void mouseDoubleClicked(MouseEvent e) {
	
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