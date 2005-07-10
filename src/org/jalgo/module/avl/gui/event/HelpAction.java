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
package org.jalgo.module.avl.gui.event;

import java.io.IOException;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.jalgo.main.util.BrowserLauncher;
import org.jalgo.module.avl.gui.GUIConstants;
import org.jalgo.module.avl.gui.GUIController;

/**
 * The class <code>HelpAction</code> defines an <code>Action</code> object, which
 * can be added to toolbars and menus. Performing this action opens the online help
 * file. The path to this file is defined in <code>GUIConstants</code>.<br>
 * This action comes with the standard key accelerator 'F1', although it doesn't work
 * properly. Calling help with 'F1' should be provided by the jAlgo main program.
 * 
 * @author Alexander Claus
 */
public class HelpAction
extends Action
implements GUIConstants {

	private GUIController gui;
	private String helpPath;
	private static final String fileSep = System.getProperty("file.separator");
	private static final String lineSep = System.getProperty("line.separator");
	
	/**
	 * Constructs a <code>HelpAction</code> object with the given reference.
	 * 
	 * @param gui the <code>GUIController</code> instance of the AVL module
	 */
	public HelpAction(GUIController gui) {
		this.gui = gui;

		setText("Hilfe zum AVL-Modul");
		setToolTipText("Öffnet die Hilfe zum AVL-Modul");
		setImageDescriptor(ImageDescriptor.createFromURL(
			getClass().getResource("/main_pix/help.gif")));
		setAccelerator(SWT.F1);
		helpPath = System.getProperty("user.dir")+fileSep+HELP_FILE_NAME.
			replace(":",fileSep);
	}

	/**
	 * Performs the action.
	 */
	public void run() {
		try {BrowserLauncher.openURL(helpPath);}
		catch (IOException ex) {
			gui.showErrorMessage("Konnte Hilfedatei nicht öffnen!"+lineSep+
				"Bitte öffnen Sie die Hilfe manuell mit Ihrem Browser");			
		}
	}
}