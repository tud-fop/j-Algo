/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004 j-Algo-Team, j-algo-development@lists.sourceforge.net
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

/*
 * Created on Mar 23, 2004
 */

/**
 * @author Christopher Friedrich
 */
public class AboutAction extends Action {

	private JalgoWindow win;

	public AboutAction(JalgoWindow win) {
		this.win = win;

		setText(Messages.getString("AboutAction.About_jAlgo_1")); //$NON-NLS-1$
		setToolTipText(Messages
				.getString("AboutAction.Get_infos_about_jAlgo._2")); //$NON-NLS-1$
		setImageDescriptor(ImageDescriptor
				.createFromFile(null, "pix/about.gif")); //$NON-NLS-1$
	}

	public void run() {

		MessageBox dia = new MessageBox(win.getShell());

		dia.setText(Messages.getString("AboutAction.About_jAlgo_4")); //$NON-NLS-1$
		dia.setMessage(Messages.getString("General.name") //$NON-NLS-1$
				+ " - " //$NON-NLS-1$
				+ Messages.getString("General.version") //$NON-NLS-1$
				+ "\n\n" //$NON-NLS-1$
				+ Messages.getString("AboutAction.jAlgo_Copyright") //$NON-NLS-1$
				+ "\n" //$NON-NLS-1$
				+ Messages.getString("AboutAction.jAlgo_Visit_URL") //$NON-NLS-1$
				+ "\n\n" //$NON-NLS-1$
				+ Messages.getString("AboutAction.Authors") //$NON-NLS-1$
				+ "\n" //$NON-NLS-1$
				+ Messages.getString("AboutAction.jAlgo_Author_Names") //$NON-NLS-1$
				+ "\n" //$NON-NLS-1$
				+ Messages.getString("AboutAction.License") //$NON-NLS-1$
				+ "\n" //$NON-NLS-1$
				+ Messages.getString("AboutAction.GPL") //$NON-NLS-1$
		);
		dia.open();
	}

}