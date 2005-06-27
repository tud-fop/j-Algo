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
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Control;

/**
 * @author Cornelius Hald
 * @author Michael Pradel
 */
public class MaximizeAction extends Action {

	private Control c;
	private SashForm sf;

	public MaximizeAction(Control c, SashForm sf) {
		
		this.c = c;
		this.sf = sf;

		setText("Maximize");
		setToolTipText("Maximize.");
		setImageDescriptor(ImageDescriptor.createFromFile(null, "pix/zoom-in.gif"));
	}

	public void run() {

		if (this.c.equals(this.sf.getMaximizedControl())) {
			this.sf.setMaximizedControl(null);
			//this.sf.setOrientation(SWT.HORIZONTAL);
			//this.sf.setOrientation(SWT.VERTICAL);
		}
		else
		{
			this.sf.setMaximizedControl(this.c);
		}

	}

}
