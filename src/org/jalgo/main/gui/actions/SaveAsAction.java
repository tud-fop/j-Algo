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
 * Created on Apr 23, 2004
 */

package org.jalgo.main.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.jalgo.main.gui.JalgoWindow;

/**
 * @author Cornelius Hald
 */
public class SaveAsAction extends Action {
	
	private JalgoWindow win;
	
	public SaveAsAction(JalgoWindow win) {
		this.win = win;
		setText(Messages.getString("ui.Save_as")); //$NON-NLS-1$
		setId(Messages.getString("ui.Save_as")); //$NON-NLS-1$
		setToolTipText(Messages.getString("ui.Save_as")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/save_as.gif")); //$NON-NLS-1$
	}

	public void run() {
		FileDialog fileChooser = new FileDialog(win.getShell(), SWT.SAVE);
		fileChooser.setText(Messages.getString("ui.Save_as")); //$NON-NLS-1$

		fileChooser.setFilterExtensions(new String[] {Messages.getString("SaveAsAction.*.jalgo_6")}); //$NON-NLS-1$
		String filename = fileChooser.open();
		if (filename != null) {
			win.saveFileAs(filename);
		}
	}

}
