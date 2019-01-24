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
 * Created on 01.06.2005
 *
 */
package org.jalgo.module.dijkstra.actions;

import org.jalgo.module.dijkstra.model.Node;
import org.jalgo.module.dijkstra.model.Edge;
import org.jalgo.module.dijkstra.gui.Controller;

/**
 * This class provides the functionality for creating a new Edge.
 * @author Hannes Strass
 *
 */
public class NewEdgeAction extends GraphAction {
	private Node m_StartNode;

	private Node m_EndNode;

	/** Creates a NewEdgeAction, which creates a new Edge.
	 * @param ctrl the Controller
	 * @param startNode the start Node
	 * @param endNode the end Node
	 * @throws ActionException
	 */
	public NewEdgeAction(Controller ctrl, Node startNode, Node endNode) throws ActionException {
		super(ctrl);
		this.m_StartNode = startNode;
		this.m_EndNode = endNode;
		this.registerAndDo(true);

	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.actions.Action#doAction()
	 */
	public boolean doAction() throws ActionException {
		getController().getGraph().setAllChangedFlagsFalse();
		Edge newEdge = new Edge(this.m_StartNode, this.m_EndNode);
		getController().getGraph().addEdge(newEdge);
		getController().setGraph(getController().getGraph());

		return true;
	}
}
