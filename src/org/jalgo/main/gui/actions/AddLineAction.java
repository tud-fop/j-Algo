/*
 * Created on Apr 23, 2004
 */

package org.jalgo.main.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author Cornelius Hald
 */
public class AddLineAction extends Action {

	public AddLineAction() {
		setText("Line");
		setToolTipText("Add straight line.");
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/line.gif"));
	}

	public void run() {
	}

}
