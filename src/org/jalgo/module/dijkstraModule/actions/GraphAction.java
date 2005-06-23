/*
 * Created on 26.05.2005
 *
  */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.model.Graph;
/**
 * @author Frank Staudinger
 *
 */
public abstract class GraphAction extends Action {

	protected Graph m_oldGraph;
	protected Graph getOldGraph()
	{
	    return (m_oldGraph != null)?(Graph)m_oldGraph.clone():null;
	}
	
	protected void setOldGraph(Graph oldGraph)
	{
		if(oldGraph != null)
			m_oldGraph = (Graph)oldGraph.clone();
		else
		    m_oldGraph = null;
	}
	/**
	 * @param ctrl Controller for this action
	 */
	public GraphAction(Controller ctrl) {
		super(ctrl);
		setOldGraph(ctrl.getGraph());
	}
	
	

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstraModule.actions.Action#undoAction()
	 */
	public boolean undoAction() throws Exception {
		getController().setGraph(getOldGraph());
		return true;
	}
}
