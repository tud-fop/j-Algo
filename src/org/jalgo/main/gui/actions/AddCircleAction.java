package org.jalgo.main.gui.actions;

import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/*
 * Created on Apr 23, 2004
 */

/**
 * This action adds a Figure to another Figure.
 * 
 * @author Cornelius Hald
 */
public class AddCircleAction extends Action {

	IFigure figure;

	/**
	 * Constructs a new Action
	 * @param figure The <code>IFigure</code> to which the Circle should be added.
	 */
	public AddCircleAction(IFigure figure) {
		this.figure = figure;
		setText("Circle");
		setToolTipText("Add circle.");
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/circle.gif"));
	}

	public void run() {
		figure.add(new Ellipse());
	}
}
