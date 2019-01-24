/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer science. It is written in Java and platform independent. j-Algo is developed with the help of Dresden University of Technology.
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
 * Created on 30.05.2005 10:39:37
 */
package org.jalgo.module.dijkstra.actions;

import org.jalgo.module.dijkstra.gui.Controller;
//import org.jalgo.module.dijkstra.util.StatusbarText;

/**
 * @author Frank
 *
 */
public class ShowEditPageAction extends SetEditingModeAction {

	/**
	 * @param ctrl
	 * @throws ActionException
	 */
	public ShowEditPageAction(Controller ctrl) throws ActionException {
		super(ctrl, Controller.MODE_NO_TOOL_ACTIVE, true);

	}

	public boolean doAction() throws ActionException {
		super.doAction();
//		getController().setStatusbarText(new StatusbarText("", null)); //$NON-NLS-1$
		getController().showEditingPage();
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.actions.Action#Undo()
	 */
	public boolean undoAction() throws ActionException {
		super.undoAction();
		getController().showAlgorithmPage();
		return true;
	}
}
