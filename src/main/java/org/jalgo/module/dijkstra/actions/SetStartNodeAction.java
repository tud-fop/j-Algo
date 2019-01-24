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
 * Created on 31.05.2005 22:06:08
 *
 */
package org.jalgo.module.dijkstra.actions;

import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.model.*;

/**
 * @author Frank
 *
 */
public class SetStartNodeAction extends GraphAction {

	int m_nNewNode;

	int m_nOldNode = 0;

	int m_nOldStep = 0;

	/**
	 * @param ctrl Controller for this action
	 * @param nNode Index of the new start node
	 * @throws ActionException
	 */
	public SetStartNodeAction(Controller ctrl, int nNode) throws ActionException {
		super(ctrl);
		m_nNewNode = nNode;
		Graph gr = getController().getGraph();
		Node node = gr.getStartNode();
		if (node != null)
			m_nOldNode = node.getIndex();
		m_nOldStep = getController().getCurrentStep();
		super.registerAndDo(true);

	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.actions.Action#doAction()
	 */
	public boolean doAction() throws ActionException {

		Graph gr = getController().getGraph();
		Node node = gr.getStartNode();
		if (node != null)
			node.setStart(false);
		gr.findNode(m_nNewNode).setStart(true);
		getController().setGraph(gr);
		State state = getController().getState(0);
		if (state != null) {
			getController().setStatusbarText(state.getDescriptionEx());
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.actions.Action#undoAction()
	 */
	public boolean undoAction() throws ActionException {
		super.undoAction();
		getController().getGraph().findNode(m_nNewNode).setStart(false);
		if (m_nOldNode > 0)
			getController().getGraph().findNode(m_nOldNode).setStart(true);
		State state = getController().getState(m_nOldStep);
		if (state != null) {
			getController().setStatusbarText(state.getDescriptionEx());
		}
		return true;
	}

}
