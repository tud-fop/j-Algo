/*
 * Created on 01.06.2005
 *
 */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.model.Edge;
import org.jalgo.module.dijkstraModule.gui.Controller;

/**
 * This class provides the functionality for deleting an Edge.
 * @author Hannes Strass
 *
 */
public class DeleteEdgeAction extends GraphAction
{
	private Edge m_Edge;
	
	/** Creates a new DeleteEdgeAction, which deletes an Edge.
	 * @param ctrl the Controller
	 * @param edge the Edge to delete
	 * @throws Exception
	 */
	public DeleteEdgeAction(Controller ctrl, Edge edge) throws Exception
	{
		super(ctrl);
		this.m_Edge = edge;
		this.registerAndDo(true);

	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstraModule.actions.Action#doAction()
	 */
	public boolean doAction() throws Exception
	{
		getController().getGraph().setAllChangedFlagsFalse();
		getController().getGraph().deleteEdge(m_Edge);
		getController().setGraph(getController().getGraph());
		return true;
	}



}
