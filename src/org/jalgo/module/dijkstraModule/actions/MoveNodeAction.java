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
