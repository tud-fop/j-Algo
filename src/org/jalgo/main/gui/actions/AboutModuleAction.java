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
 * Created on 28.06.2004
 */
package org.jalgo.main.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.IModuleInfo;
import org.jalgo.main.JalgoMain;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.main.util.Messages;

/**
 * @author Christopher Friedrich
 */
public class AboutModuleAction
extends Action {

	private JalgoWindow win;
	private static final String lineSep = System.getProperty("line.separator");

	public AboutModuleAction(JalgoWindow win) {
		this.win = win;

		setText(Messages.getString("main", "ui.About_module")); //$NON-NLS-1$ //$NON-NLS-2$
		setToolTipText(Messages.getString("main", "ui.About_module_tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		setImageDescriptor(ImageDescriptor.createFromURL(
			Messages.getResourceURL("main", "ui.About_module"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void run() {
		IModuleInfo moduleInfo =
			JalgoMain.getInstance().getCurrentInstance().getModuleInfo();
		StringBuffer content = new StringBuffer();
		content.append(moduleInfo.getName());
		content.append(Messages.getString("main", "AboutModule.Version")); //$NON-NLS-1$ //$NON-NLS-2$
		content.append(moduleInfo.getVersion());
		content.append(lineSep).append(lineSep);
		content.append(moduleInfo.getDescription());
		content.append(lineSep).append(lineSep);
		content.append(Messages.getString("main", "About.Authors")); //$NON-NLS-1$ //$NON-NLS-2$
		content.append(lineSep);
		content.append(moduleInfo.getAuthor());
		content.append(lineSep).append(lineSep);
		content.append(Messages.getString("main", "About.License")); //$NON-NLS-1$ //$NON-NLS-2$
		content.append(lineSep);
		content.append(moduleInfo.getLicense());

		win.showInfoMessage(
			Messages.getString("main", "ui.About_module"), //$NON-NLS-1$ //$NON-NLS-2$
			content.toString());
	}
}