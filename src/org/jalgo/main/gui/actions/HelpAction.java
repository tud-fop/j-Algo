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

/* Created on 26.05.2005 */
package org.jalgo.main.gui.actions;

import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.jalgo.main.AbstractModuleConnector;
import org.jalgo.main.JalgoMain;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.main.util.BrowserLauncher;
import org.jalgo.main.util.Messages;

/**
 * The class <code>HelpAction</code> defines an <code>Action</code> object,
 * which can be added to toolbars and menus. Performing this action opens the
 * online help file.<br>
 * This action comes with the standard key accelerator 'F1'.
 * 
 * @author Alexander Claus
 */
public class HelpAction
extends Action {

	private final String helpPathMain = System.getProperty("user.dir")+ //$NON-NLS-1$
		Messages.getString("main_res", "ui.Help_file"); //$NON-NLS-1$ //$NON-NLS-2$
	private final String helpPathAVL = System.getProperty("user.dir")+ //$NON-NLS-1$
		Messages.getString("main_res", "ui.Help_file_avl"); //$NON-NLS-1$ //$NON-NLS-2$
	private final String helpPathDijkstra = System.getProperty("user.dir")+ //$NON-NLS-1$
		Messages.getString("main_res", "ui.Help_file_dijkstra"); //$NON-NLS-1$ //$NON-NLS-2$
	private JalgoWindow appWin;
	private static final String lineSep = System.getProperty("line.separator");
	
	/**
	 * Constructs a <code>HelpAction</code> object with the given reference.
	 * 
	 * @param appWin the <code>JalgoWindow</code> instance
	 */
	public HelpAction(JalgoWindow appWin) {
		this.appWin = appWin;

		setText(Messages.getString("main", "ui.Help.Contents")); //$NON-NLS-1$ //$NON-NLS-2$
		setToolTipText(Messages.getString("main", "ui.Help_tooltip")); //$NON-NLS-1$ //$NON-NLS-2$
		setImageDescriptor(ImageDescriptor.createFromURL(
			Messages.getResourceURL("main", "ui.Help"))); //$NON-NLS-1$ //$NON-NLS-2$
		setAccelerator(SWT.F1);
	}

	/**
	 * Performs the action.
	 */
	public void run() {
		try {
			AbstractModuleConnector currentInstance =
				JalgoMain.getInstance().getCurrentInstance();
			if (currentInstance == null) BrowserLauncher.openURL(helpPathMain);
			else if (currentInstance.getModuleInfo().getName().equals(
				Messages.getString("avl", "Module_name")))
				BrowserLauncher.openURL(helpPathAVL);
			else if (currentInstance.getModuleInfo().getName().equals("Dijkstra"))
				BrowserLauncher.openURL(helpPathDijkstra);
		}
		catch (IOException ex) {
			appWin.showErrorMessage(Messages.getString(
				"main", "HelpAction.Error_1") + lineSep + //$NON-NLS-1$ //$NON-NLS-2$
				Messages.getString("main", "HelpAction.Error_2")); //$NON-NLS-1$ //$NON-NLS-2$			
		}
	}
}