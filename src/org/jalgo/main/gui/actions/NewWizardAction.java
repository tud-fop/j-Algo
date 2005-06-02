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

//import java.util.Collection;

import org.eclipse.jface.action.Action;
/*
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.module.synDiaEBNF.ModuleController;
import org.jalgo.module.synDiaEBNF.startWizard.StartWizard;
import org.jalgo.module.synDiaEBNF.startWizard.StartWizardDialog;
*/

/**
 * @author Christopher Friedrich
 */
// TODO remove ths class because it is not needed anymore (Stephan)
public class NewWizardAction extends Action {

	/*
	private JalgoWindow win;
	private Collection knownModules;

	public NewWizardAction(JalgoWindow win, Collection knownModules) {
		this.win = win;
		this.knownModules = knownModules;

		setText("New Wizard");
		setToolTipText("New Wizard.");
		setImageDescriptor(ImageDescriptor.createFromFile(null, "pix/new.gif"));

	}
	*/
	
	public void run() {

		/*
		StartWizard wizard = new StartWizard((ModuleController)win.getCurrentInstance(), win);
		StartWizardDialog wizardDialog =
			new StartWizardDialog(win.getShell(), wizard);
		wizardDialog.open();
		
		// Start Module
		win.getParent().newInstance(0);
		*/
	}

}
