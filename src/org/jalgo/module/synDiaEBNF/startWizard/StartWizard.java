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
 * Created on 12.05.2004
 */
 
package org.jalgo.module.synDiaEBNF.startWizard;

import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.jface.wizard.Wizard;
import org.jalgo.main.gui.JalgoWindow;
import org.jalgo.main.gui.actions.OpenAction;
import org.jalgo.module.synDiaEBNF.ModuleController;

/**
 * @author Michael Pradel
 */
public class StartWizard extends Wizard {

	private ApplicationWindow appWin;
	private ModuleController mc;
	
	public FirstChoice firstChoice;

	public StartWizard(ModuleController mc, ApplicationWindow appWin) {
		super();
		this.mc = mc;
		this.appWin = appWin;
	}

	public void addPages() {
		firstChoice = new FirstChoice("firstChoice"); //$NON-NLS-1$
		addPage(firstChoice);
	}

	public boolean performFinish() {

		switch (firstChoice.getSelected()) {
			case FirstChoice.LOADFILE :
				OpenAction oa = new OpenAction((JalgoWindow) appWin);
				oa.run(true);  // 'true' tells open action to use current instance of this module
				return true;
				
			case FirstChoice.PUTINSYNDIA :
				mc.setMode(3);
				return true;

			case FirstChoice.PUTINEBNF :
				mc.setMode(4);
				return true;

			default :
				return true;
		}
	}
	
	public boolean performCancel() {
		mc.setMode(2);
		return true;
	}
}
