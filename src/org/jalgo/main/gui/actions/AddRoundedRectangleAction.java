/*
 * Created on Apr 23, 2004
 */
package org.jalgo.main.gui.actions;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.RoundedRectangle;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author Cornelius Hald
 */
public class AddRoundedRectangleAction extends Action {

	private IFigure figure;

	/**
	 * Constructs a new Action
	 * @param figure The <code>IFigure</code> to which the Rectangle should be added.
	 */
	public AddRoundedRectangleAction(IFigure figure) {
		this.figure = figure;
		setText("Rounded rectangle");
		setToolTipText("Add rounded rectangle.");
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/rounded_rectangle.gif"));
	}

	public void run() {
		figure.add(new RoundedRectangle());
	}
}
