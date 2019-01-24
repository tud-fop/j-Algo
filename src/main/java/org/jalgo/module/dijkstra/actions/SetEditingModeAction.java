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
 * Created on 30.05.2005 18:26:35
 *
 */
package org.jalgo.module.dijkstra.actions;

import org.jalgo.module.dijkstra.gui.Controller;

/**
 * @author Frank
 *
 */
public class SetEditingModeAction extends Action {

	protected int m_iMode;

	protected int m_iOldMode;

	/**
	 * @return Returns the m_iMode.
	 */
	protected int getMode() {
		return m_iMode;
	}

	/**
	 * @param mode The m_iMode to set.
	 */
	protected void setMode(int mode) {
		m_iMode = mode;
	}

	/**
	 * @param ctrl	Controller for this action
	 * @param iMode new Editing mode
	 * @throws ActionException
	 */
	public SetEditingModeAction(Controller ctrl, int iMode) throws ActionException {
		this(ctrl, iMode, false);
	}

	public SetEditingModeAction(Controller ctrl, int iMode, boolean bRegister) throws ActionException {
		super(ctrl);
		m_iMode = iMode;
		this.registerAndDo(bRegister);
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.actions.Action#doAction()
	 */
	public boolean doAction() throws ActionException {
		m_iOldMode = getController().getEditingMode();
		getController().setEditingMode(m_iMode);
		getController().getGraph().setAllChangedFlagsFalse(); // HS
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.actions.Action#undoAction()
	 */
	public boolean undoAction() throws ActionException {
		getController().setEditingMode(m_iOldMode);
		return true;
	}

}
