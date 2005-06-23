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

/*
 * Created on 28.06.2004
 */
package org.jalgo.main.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.MessageBox;
import org.jalgo.main.gui.JalgoWindow;

/**
 * @author Christopher Friedrich
 */
public class AboutModuleAction extends Action {

	private JalgoWindow win;

	public AboutModuleAction(JalgoWindow win) {
		this.win = win;

		setText(Messages.getString("AboutModuleAction.About_jAlgo-Module_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("AboutModuleAction.Get_infos_about_jAlgo-Module._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/about.gif")); //$NON-NLS-1$
	}

	public void run() {
		MessageBox dia = new MessageBox(win.getShell());
		dia.setText(Messages.getString("AboutModuleAction.About_jAlgo-Module_4")); //$NON-NLS-1$
		dia
			.setMessage(
				win.getParent().getCurrentInstance().getModuleInfo().getName()
				+ Messages.getString("AboutModuleAction._-_Version__5") //$NON-NLS-1$
				+ win.getParent().getCurrentInstance().getModuleInfo().getVersion()
				+ "\n\n" //$NON-NLS-1$
				+ win.getParent().getCurrentInstance().getModuleInfo().getDescription()
				+ "\n\n" //$NON-NLS-1$
				+ Messages.getString("AboutModuleAction.Authors_8") //$NON-NLS-1$
				+ ":\n" //$NON-NLS-1$
				+ win.getParent().getCurrentInstance().getModuleInfo().getAuthor()
				+ "\n\n" //$NON-NLS-1$
				+ Messages.getString("AboutModuleAction.License_10") //$NON-NLS-1$
				+ ":\n" //$NON-NLS-1$
				+ win.getParent().getCurrentInstance().getModuleInfo().getLicense()
			/*
				+ "(c) Copyright jAlgo-Team 2004.  All rights reserved.\n"
				+ "Visit http://www.inf.tu-dresden.de/~swt04-p1\n"
				+ "\n"
				+ "This program is free software; you can redistribute it and/or modify\n"
				+ "it under the terms of the GNU General Public License as published by\n"
				+ "the Free Software Foundation; either version 2 of the License, or\n"
				+ "(at your option) any later version.\n"
				+ "\n"
				+ "This program is distributed in the hope that it will be useful,\n"
				+ "but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
				+ "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"
				+ "GNU General Public License for more details."
				*/
		);
		dia.open();
	}

}
