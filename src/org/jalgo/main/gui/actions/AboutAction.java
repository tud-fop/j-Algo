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
import org.eclipse.swt.widgets.MessageBox;
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
	private static final String lineSep = System.getProperty("line.separator"); 

	public AboutAction(JalgoWindow win) {
		this.win = win;

		setText(Messages.getString("main", "ui.About")); //$NON-NLS-1$ //$NON-NLS-2$
		setToolTipText(Messages.getString("main","ui.About_tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		setImageDescriptor(ImageDescriptor.createFromURL(
			Messages.getResourceURL("main", "ui.About"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void run() {
		MessageBox dia = new MessageBox(win.getShell());

		dia.setText(Messages.getString("main", "ui.About")); //$NON-NLS-1$ //$NON-NLS-2$
		dia.setMessage(Messages.getString("main", "General.name") //$NON-NLS-1$ //$NON-NLS-2$
				+ " - " //$NON-NLS-1$
				+ Messages.getString("main", "General.version") //$NON-NLS-1$ //$NON-NLS-2$
				+ lineSep + lineSep
				+ Messages.getString("main", "About.Copyright") //$NON-NLS-1$ //$NON-NLS-2$
				+ lineSep
				+ Messages.getString("main", "About.URL") //$NON-NLS-1$ //$NON-NLS-2$
				+ lineSep + lineSep
				+ Messages.getString("main", "About.Authors") //$NON-NLS-1$ //$NON-NLS-2$
				+ lineSep
				+ Messages.getString("main", "About.Author_Names") //$NON-N //$NON-NLS-2$LS-1$
				+ lineSep + lineSep
				+ Messages.getString("main", "About.License") //$NON-NLS-1$ //$NON-NLS-2$
				+ lineSep
				+ Messages.getString("main", "About.GPL") //$NON-NLS-1$ //$NON-NLS-2$
		);
		dia.open();
	}
}