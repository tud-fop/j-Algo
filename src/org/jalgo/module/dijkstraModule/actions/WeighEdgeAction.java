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
