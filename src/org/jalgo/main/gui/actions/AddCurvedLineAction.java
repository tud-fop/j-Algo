package org.jalgo.main.gui.actions;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/*
 * Created on Apr 23, 2004
 */

/**
 * @author Cornelius Hald
 */
public class AddCurvedLineAction extends Action {

	private ConnectionAnchor source, target;

	public AddCurvedLineAction() {
		setText("Curved Line");
		setToolTipText("Add curved line.");
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/curved_connector.gif"));
	}

	public void run() {
	}
}
