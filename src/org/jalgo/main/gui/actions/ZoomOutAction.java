/*
 * Created on Apr 23, 2004
 */
 
package org.jalgo.main.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.gui.widgets.GraphViewForm;

/**
 * This Action can be used to zoom out of an Draw2D Figure - Typically this will be a Panel.
 * 
 * @author Cornelius Hald
 */

public class ZoomOutAction extends Action {

	private GraphViewForm form;

	/**
	 * Constructs a new ZoomInAction
	 * @param panel The Panel which contents should be zoomed.
	 */
	public ZoomOutAction(GraphViewForm form) {
		this.form = form;
		setText(Messages.getString("ZoomOutAction.Zoom_Out_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("ZoomOutAction.Zoom_Out._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/zoom-out.gif")); //$NON-NLS-1$
	}

	public void run() {

		form.zoomOut();

	}

}
