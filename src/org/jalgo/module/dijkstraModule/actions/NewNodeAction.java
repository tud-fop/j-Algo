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
 * Created on 31.05.2005
 *
 */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.model.Position;
import org.jalgo.module.dijkstraModule.model.Node;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;

/**
 * This class provides the functionality for creating a new Node.
 * @author Hannes Strass, Martin Winter
 *
 */
public class NewNodeAction extends GraphAction
{
	private Position position;
	/*
	 * Changed to Position position; because of this:
	 * Place a new node in the top right corner of the drawing rectangle
	 * Undo the last NewNodeAction 
	 * compact the jAlgo-Window
	 * Redo the last NewNodeAction
	 * -> the Node will be invisible
	 * 
	private Point m_screenCoordinates;
	private Rectangle m_Bounds;
	*/
	
	/** Creates a NewNodeAction, which creates a new Node
	 * 
	 * @param ctrl the Controller
	 * @param screenCoordinates Point on screen where user wants new Node to appear
	 * @param bounds bounds of the canvas where Nodes can be drawn
	 * @throws Exception 
	 */
	public NewNodeAction(Controller ctrl, Point screenCoordinates, Rectangle bounds) throws Exception
	{
		super(ctrl);
		/*
		this.m_screenCoordinates = screenCoordinates;
		this.m_Bounds = bounds;
		*/
		position = new Position(screenCoordinates, bounds);
		this.registerAndDo(true);
	}
	
	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstraModule.actions.Action#doAction()
	 */
	public boolean doAction() throws Exception
	{
		
		if (getController().getGraph().getNextNodeIndex() == 10) return false;
		getController().getGraph().setAllChangedFlagsFalse();
		//Position position = new Position(m_screenCoordinates, m_Bounds);
		getController().getGraph().addNode(new Node(getController().getGraph().getNextNodeIndex(), position));
		getController().setGraph(getController().getGraph());

		return true;
	}

}
