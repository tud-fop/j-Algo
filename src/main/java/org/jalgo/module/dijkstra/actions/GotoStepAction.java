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
 * Created on 31.05.2005 19:10:27
 *
 * 
 */
package org.jalgo.module.dijkstra.actions;

import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.model.*;

/**
 * @author Frank Staudinger
 *
 */
public class GotoStepAction extends Action {

	protected int m_nGotoStep;

	protected int m_nLastStep;

	/**
	 * @param ctrl
	 * @param nGotoStepIndex zero-based index for the next step in the algo 
	 * @param bExcuteNow true if the action should do it now
	 */
	public GotoStepAction(Controller ctrl, int nGotoStepIndex, boolean bExcuteNow) throws ActionException {
		this(ctrl, nGotoStepIndex);
		if (bExcuteNow == true)
			super.registerAndDo(true);
	}

	/**
	 * @param ctrl
	 * @param nGotoStepIndex zero-based index for the next step in the algo 
	 * 
	 */
	protected GotoStepAction(Controller ctrl, int nGotoStepIndex) throws ActionException {
		super(ctrl);
		m_nGotoStep = nGotoStepIndex;
		m_nLastStep = getController().getCurrentStep();
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.actions.Action#doAction()
	 */

	public boolean doAction() throws ActionException {

		State state = getController().getState(m_nGotoStep);
		if (state != null) {
			getController().setStatusbarText(state.getDescriptionEx());
			getController().setModifiedFlag();
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.actions.Action#undoAction()
	 */
	public boolean undoAction() throws ActionException {
		State state = getController().getState(m_nLastStep);
		if (state != null) {
			getController().setStatusbarText(state.getDescriptionEx());
			getController().setModifiedFlag();
		}
		return true;
	}

	/**
	 * @return Returns the m_nGotoStep.
	 */
	protected int getGotoStepIndex() {
		return m_nGotoStep;
	}

	/**
	 * @param gotoStep The m_nGotoStep to set.
	 */
	protected void setGotoStepIndex(int gotoStep) {
		m_nGotoStep = gotoStep;
	}
}
