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
 * Created on 01.06.2005
 *
 */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.model.Edge;
import org.jalgo.module.dijkstraModule.gui.Controller;

/**
 * This class provides the functionality for changing the weight of an Edge. 
 * @author Hannes Strass
 *
 */
public class WeighEdgeAction extends GraphAction
{
	private int m_iNewWeight;
	private Edge m_Edge;
	
	/** Creates a new WeighEdgeAction, which changes the weight of an Edge.
	 * @param ctrl the Controller
	 * @param edge the Edge to weigh
	 * @param newWeight the new weight
	 * @throws Exception
	 */
	public WeighEdgeAction(Controller ctrl, Edge edge, int newWeight) throws Exception
	{
		super(ctrl);
		this.m_Edge = edge;
		this.m_iNewWeight = newWeight;
		this.registerAndDo(true);
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstraModule.actions.Action#doAction()
	 */
	public boolean doAction() throws Exception
	{
		getController().getGraph().setAllChangedFlagsFalse();
		Edge edge = getController().getGraph().findEdge(m_Edge.getStartNode(),m_Edge.getEndNode());
		edge.setChanged(true);
		edge.setWeight(this.m_iNewWeight);
		getController().setGraph(getController().getGraph());
		return true;
	}


}