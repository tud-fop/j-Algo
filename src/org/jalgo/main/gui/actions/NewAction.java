/*
 * Created on Apr 23, 2004
 */
 
package org.jalgo.main.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.module.testModule.ModuleConnector;

/**
 * @author Cornelius Hald
 * @author Michael Pradel
 */
public class NewAction extends Action {

	private JalgoWindow win;

	private ModuleConnector modConn;

	public NewAction(JalgoWindow win) {
		this.win = win;

		setText(Messages.getString("NewAction.EBNF_and_syntactical_diagrams_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("NewAction.Load_new_instance_2")); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromFile(null, "pix/new.gif")); //$NON-NLS-1$
	}

	public void run() {
		// Start Module
		win.getParent().newInstance("synDiaEBNF"); //$NON-NLS-1$
	}

}
