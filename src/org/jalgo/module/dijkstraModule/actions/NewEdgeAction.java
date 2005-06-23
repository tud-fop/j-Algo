/*
 * Created on 01.06.2005
 *
 */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.model.Node;
import org.jalgo.module.dijkstraModule.model.Edge;
import org.jalgo.module.dijkstraModule.gui.Controller;

/**
 * This class provides the functionality for creating a new Edge.
 * @author Hannes Strass
 *
 */
public class NewEdgeAction extends GraphAction
{
	private Node m_StartNode;
	private Node m_EndNode;
	
	/** Creates a NewEdgeAction, which creates a new Edge.
	 * @param ctrl the Controller
	 * @param startNode the start Node
	 * @param endNode the end Node
	 * @throws Exception
	 */
	public NewEdgeAction(Controller ctrl, Node startNode, Node endNode) throws Exception
	{
		super(ctrl);
		this.m_StartNode = startNode;
		this.m_EndNode = endNode;
		this.registerAndDo(true);

	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstraModule.actions.Action#doAction()
	 */
	public boolean doAction() throws Exception
	{
		getController().getGraph().setAllChangedFlagsFalse();
		Edge newEdge = new Edge(this.m_StartNode, this.m_EndNode);
		getController().getGraph().addEdge(newEdge);
		getController().setGraph(getController().getGraph());
		
		return true;
	}
}
