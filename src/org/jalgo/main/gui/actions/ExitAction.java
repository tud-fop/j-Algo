/*
 * Created on Mar 23, 2004
 */
 
package org.jalgo.main.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.ApplicationWindow;

/**
 * This class handles the "Exit" button
 * 
 * @author Cornelius Hald
 */
public class ExitAction extends Action {

	ApplicationWindow window;

	public ExitAction(ApplicationWindow win) {
		window = win;
		setText(Messages.getString("ExitAction.E&xit@Ctrl+W_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("ExitAction.Exit_Application._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/close.gif")); //$NON-NLS-1$
	}

	public void run() {
		window.close();
	}
}
