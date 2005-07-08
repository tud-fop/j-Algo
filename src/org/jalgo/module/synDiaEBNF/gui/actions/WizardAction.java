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
 * Created on 03.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gui.actions;

import java.io.Serializable;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Composite;
import org.jalgo.module.synDiaEBNF.IModeConstants;
import org.jalgo.module.synDiaEBNF.ModuleController;

/**
 * @author Michael Pradel
 */
public class WizardAction extends Action implements Serializable {

	private static final long serialVersionUID = -2670334124635815595L;
	private ModuleController mc;
	private Composite parent;

	public WizardAction(ModuleController mc, Composite parent) {
		this.mc = mc;
		this.parent = parent;
		setText(Messages.getString("WizardAction.Wizard_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("WizardAction.Starten_des_Wizards_2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/wizard.gif")); //$NON-NLS-1$
	}

	public void run() {
		if (MessageDialog
			.openQuestion(
				parent.getShell(),
				Messages.getString("WizardAction.Really_start_wizard__4"), //$NON-NLS-1$
				Messages.getString("WizardAction.Starting_the_wizard_will_destroy_all_you_have_done_with_this_module_up_to_now_!_5"))) { //$NON-NLS-1$
			mc.setMode(IModeConstants.NORMAL_VIEW_EMPTY);
		} else {
			return;
		}
	}

}
