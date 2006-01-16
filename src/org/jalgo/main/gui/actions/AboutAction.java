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

package org.jalgo.main.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.main.util.Messages;

/*
 * Created on Mar 23, 2004
 */

/**
 * @author Christopher Friedrich
 */
public class AboutAction
extends Action {

	private JalgoWindow win; 

	public AboutAction(JalgoWindow win) {
		this.win = win;

		setText(Messages.getString("main", "ui.About")); //$NON-NLS-1$ //$NON-NLS-2$
		setToolTipText(Messages.getString("main","ui.About_tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		setImageDescriptor(ImageDescriptor.createFromURL(
			Messages.getResourceURL("main", "ui.About"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void run() {

		win.showInfoMessage(
			Messages.getString("main", "ui.About"), //$NON-NLS-1$ //$NON-NLS-2$
			Messages.getJalgoInfo());
	}
}