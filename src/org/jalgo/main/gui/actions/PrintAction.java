/*
 * Created on Apr 23, 2004
 */

package org.jalgo.main.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * @author Cornelius Hald
 */
public class PrintAction extends Action {

	public PrintAction() {
		setText(Messages.getString("PrintAction.Print_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("PrintAction.Print_this_view_2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/print.gif")); //$NON-NLS-1$
	}

	public void run() {

	}

}
