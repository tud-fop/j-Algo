/*
 * Created on 31.05.2005
 *
 */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.model.Node;

/**
 * This class provides the functionality for deleting a Node.
 * @author Hannes Strass
 *
 */
public class DeleteNodeAction extends GraphAction
{

	private Node m_Node;
	
	/** Creates a new DeleteNodeAction, which deletes a Node.
	 * @param ctrl the Controller
	 * @param node the Node to delete
	 * @throws Exception
	 */
	public DeleteNodeAction(Controller ctrl, Node node) throws Exception
	{
		super(ctrl);
		this.m_Node = node;
		this.registerAndDo(true);
	}
	
	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstraModule.actions.Action#doAction()
	 */
	public boolean doAction() throws Exception
	{	
		getController().getGraph().setAllChangedFlagsFalse();	
		getController().getGraph().deleteNode(m_Node);		
		getController().getGraph().replaceMissingNodes();		
		getController().setGraph(getController().getGraph());
		return true;
	}

}
