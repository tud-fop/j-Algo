/*
 * Created on Apr 23, 2004
 */
 
package org.jalgo.main.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Widget;

/**
 * @author Cornelius Hald
 */
public class CloseAction extends Action {

	private Widget widget;

	public CloseAction(Widget w) {
		setText(Messages.getString("CloseAction.Close_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("CloseAction.Close._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/close.gif")); //$NON-NLS-1$
		this.widget = w;
	}

	public void run() {
		//widget.dispose();
	}

}
