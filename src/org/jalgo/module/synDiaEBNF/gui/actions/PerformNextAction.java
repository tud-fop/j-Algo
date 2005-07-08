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
 * Created on 02.06.2004
 */
 
package org.jalgo.module.synDiaEBNF.gui.actions;

import java.io.Serializable;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.module.synDiaEBNF.ModuleController;

/**
 * @author Cornelius Hald
 * @author Michael Pradel
 */
public class PerformNextAction extends Action implements Serializable {

	private static final long serialVersionUID = 977863639851843574L;
	private ModuleController mc; 
	
	public PerformNextAction(ModuleController mc) {
		this.mc = mc;
		setText(Messages.getString("PerformNextAction.Next_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("PerformNextAction.Perform_next_step._2")); //$NON-NLS-1$
		// TODO: find or design nice buttons
		setImageDescriptor(
					ImageDescriptor.createFromFile(null, "pix/performNext.gif")); //$NON-NLS-1$
		setId(Messages.getString("PerformNextAction.performNext_4")); //$NON-NLS-1$
	}

	public void run() {
		mc.performNextStep();
	}
}
