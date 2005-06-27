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

import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.model.Position;
import org.jalgo.module.dijkstraModule.model.Node;

/**
 * This class provides the functionality for changing the Position of a Node.
 * @author Hannes Strass
 *
 */
public class MoveNodeAction extends GraphAction
{
	private Node m_Node;
	private Position m_NewPosition;

	/** Creates a MoveNodeAction, which moves a Node.
	 * @param ctrl the Controller
	 * @param node the Node to move
	 * @param newPosition the new Position of the Node
	 * @throws Exception
	 */
	public MoveNodeAction(Controller ctrl, Node node, Position newPosition) throws Exception
	{
		super(ctrl);
		this.m_Node = node;
		this.m_NewPosition = newPosition;
		this.registerAndDo(true);
	}
	
	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstraModule.actions.Action#doAction()
	 */
	public boolean doAction() throws Exception
	{	
		
		
		Node node = getController().getGraph().findNode(m_Node.getIndex());
		if(node.getPosition().equals(m_NewPosition))
		    return false;
		getController().getGraph().setAllChangedFlagsFalse();
		node.setChanged(true);
		node.setPosition(m_NewPosition);
		getController().setGraph(getController().getGraph());
		
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstraModule.actions.Action#undoAction()
	 */
	public boolean undoAction() throws Exception
	{
		getController().setGraph(getOldGraph());
		return true;
	}

}
