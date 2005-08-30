/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on Apr 23, 2004
 */
 
package org.jalgo.main.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.gui.widgets.GraphViewForm;
import org.jalgo.main.util.Messages;

/**
 * This Action can be used to zoom out of an Draw2D Figure - Typically this will be a Panel.
 * 
 * @author Cornelius Hald
 */

public class ZoomOutAction extends Action {

	private GraphViewForm form;

	/**
	 * Constructs a new ZoomInAction
	 * @param form
	 */
	public ZoomOutAction(GraphViewForm form) {
		this.form = form;
		setText(Messages.getString("main", "ui.Zoom_out")); //$NON-NLS-1$
		setToolTipText(Messages.getString("main", "ui.Zoom_out")); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor.createFromURL(
			getClass().getResource("/main_pix/zoom-out.gif")));
	}

	public void run() {
		form.zoomOut();
	}
}