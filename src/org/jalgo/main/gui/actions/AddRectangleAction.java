package org.jalgo.main.gui.actions;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.MouseEvent;
import org.eclipse.draw2d.MouseListener;
import org.eclipse.draw2d.RectangleFigure;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/*
 * Created on Apr 23, 2004
 */

/**
 * This action adds a Rectangle to a Figure.
 * 
 * @author Cornelius Hald
 * @author Hauke Menges
 */
public class AddRectangleAction extends Action {
	
	IFigure figure;

	/**
	 * Constructs a new Action.
	 * @param figure The <code>IFigure</code> to which the Rectangle should be added.
	 */
	public AddRectangleAction(IFigure figure) {
		this.figure = figure;
		setText("Rectangle");
		setToolTipText("Add rectangle.");
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/rectangle.gif"));
	}

	public void run() {
		RectangleFigure fig = new RectangleFigure();
		figure.addMouseListener(new MouseListener.Stub() {
			public void mousePressed(MouseEvent arg0) {
			}
		});
		figure.add(fig);
	}
}
