package org.jalgo.main.gui.actions;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.gfx.RectangleLabel;

/*
 * Created on May 31, 2004
 */

/**
 * This action adds a RectangleLabel to a Figure.
 * 
 * @author Cornelius Hald
 */
public class AddRectangleLabelAction extends Action {

	IFigure figure;

	/**
	 * Constructs a new Action.
	 * @param figure The <code>IFigure</code> to which the RectangleLabel should be added.
	 */
	public AddRectangleLabelAction(IFigure figure) {
		this.figure = figure;
		setText("Circle");
		setToolTipText("Add circle.");
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/circle.gif"));
	}

	public void run() {
		figure.add(new RectangleLabel());
	}
}
