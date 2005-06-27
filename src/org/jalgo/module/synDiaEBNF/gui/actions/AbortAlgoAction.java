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
package org.jalgo.module.synDiaEBNF.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.jalgo.module.synDiaEBNF.ModuleController;

/**
 * @author Michael Pradel
 */
public class AbortAlgoAction extends Action {
	
	private ModuleController mc;

	public AbortAlgoAction(ModuleController mc) {
		this.mc = mc;
		setText(Messages.getString("AbortAlgoAction.Abort_1")); //$NON-NLS-1$
		setToolTipText(Messages.getString("AbortAlgoAction.Abort_the_algorithm._2")); //$NON-NLS-1$
		setImageDescriptor(
			ImageDescriptor.createFromFile(null, "pix/stop.gif")); //$NON-NLS-1$
	}

	public void run() {
		mc.abortAlgo();
	}
}
