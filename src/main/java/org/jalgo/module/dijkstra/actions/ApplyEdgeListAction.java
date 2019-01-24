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
 * Created on 20.05.2005
 *
 */
package org.jalgo.module.dijkstra.actions;

import org.jalgo.module.dijkstra.gui.Controller;
import org.jalgo.module.dijkstra.model.Graph;
import org.jalgo.module.dijkstra.util.EdgeListParser;
import org.jalgo.module.dijkstra.util.ParsingException;

/**
 * @author Frank Staudinger
 * This class uses the EdgeListParser to convert a string into a Graph and 
 * updates the Controllers model
 * 
 */
public class ApplyEdgeListAction extends ApplyGraphTextAction {

	/**
	 * @param ctrl Reference to the Controller object
	 * @param strEdgeList The text You want to parse.
	 * @throws ActionException ParsingException thrown by the EdgeListParser
	 */
	public ApplyEdgeListAction(Controller ctrl, String strEdgeList) throws ActionException {
		super(ctrl, strEdgeList);

	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.demo.actions.Action#doAction()
	 */
	public boolean doAction() throws ActionException {
		try {

			Graph gr = new EdgeListParser().getParsedEdgeList(this.m_strText, getOldGraph());
			this.getController().setGraph(gr);
		} catch (ParsingException e) {
			getController().setGraph(m_oldGraph);
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.demo.actions.Action#undoAction()
	 */
	public boolean undoAction() throws ActionException {
		this.getController().setGraph(getOldGraph());
		return true;
	}

}
