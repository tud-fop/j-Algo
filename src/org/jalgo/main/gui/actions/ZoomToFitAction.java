/*
 * Created on Apr 23, 2004
 */
 
package org.jalgo.main.gui.actions;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;

/**
 * This Action can be used to zoom a Panel, so that alle Figures can be seen. - Typically this will be a Panel.
 * 
 * @author Cornelius Hald
 */

public class ZoomToFitAction extends Action {
	
	/**
	 * Constructs a new ZoomInAction
	 * @param panel The Panel which contents should be zoomed.
	 */
	public ZoomToFitAction(IFigure panel) {
		setText(Messages.getString("ZoomToFitAction.Zoom_to_fit_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("ZoomToFitAction.Zoom_to_fit_2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/zoom_to_fit.gif")); //$NON-NLS-1$
	}

	public void run() {
	}

}
