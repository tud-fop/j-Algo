package org.jalgo.main.gui.actions;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.gfx.EllipseLabel;

/*
 * Created on May 31, 2004
 */

/**
 * This action adds a EllipseLabel to a Figure.
 * 
 * @author Cornelius Hald
 */
public class AddEllipseLabelAction extends Action {

	IFigure figure;

	/**
	 * Constructs a new Action.
	 * @param figure The <code>IFigure</code> to which the EllipseLabel should be added.
	 */
	public AddEllipseLabelAction(IFigure figure) {
		this.figure = figure;
		setText("Circle");
		setToolTipText("Add circle.");
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/circle.gif"));
	}

	public void run() {
		figure.add(new EllipseLabel());
	}
}
