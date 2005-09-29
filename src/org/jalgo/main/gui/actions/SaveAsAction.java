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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.jalgo.main.JalgoMain;
import org.jalgo.main.util.Messages;

/**
 * @author Cornelius Hald
 */
public class SaveAsAction extends Action {
	
	private boolean wasSuccessful;
	
	public SaveAsAction() {
		setText(Messages.getString("main", "ui.Save_as")); //$NON-NLS-1$ //$NON-NLS-2$
		setId(Messages.getString("main", "ui.Save_as")); //$NON-NLS-1$ //$NON-NLS-2$
		setToolTipText(Messages.getString("main", "ui.Save_as")); //$NON-NLS-1$ //$NON-NLS-2$
		setImageDescriptor(ImageDescriptor.createFromURL(
			Messages.getResourceURL("main", "ui.Save_as"))); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public void run() {
		FileDialog fileChooser = new FileDialog(
			Display.getCurrent().getActiveShell(), SWT.SAVE);
		fileChooser.setText(Messages.getString("main", "ui.Save_as")); //$NON-NLS-1$ //$NON-NLS-2$
		fileChooser.setFilterPath(System.getProperty("user.dir")); //$NON-NLS-1$
		fileChooser.setFilterExtensions(new String[] {"*.jalgo"}); //$NON-NLS-1$ //$NON-NLS-2$
		fileChooser.setFilterNames(new String[] {
			Messages.getString("main", "OpenAction.jAlgo_files")}); //$NON-NLS-1$ //$NON-NLS-2$
		String filename = fileChooser.open();
		if (filename != null)
			wasSuccessful = JalgoMain.getInstance().saveFileAs(filename);
		else wasSuccessful = false;
	}

	public boolean wasSuccessful() {
		return wasSuccessful;
	}
}