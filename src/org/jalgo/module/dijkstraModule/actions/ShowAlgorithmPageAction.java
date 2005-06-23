/*
 * Created on 30.05.2005 10:00:39
 *
  */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.model.Graph;
import org.jalgo.module.dijkstraModule.model.Node;
import org.jalgo.module.dijkstraModule.model.State;

/**
 * @author Frank
 *
 */
public class ShowAlgorithmPageAction extends SetEditingModeAction {
	
	/**
	 * @param ctrl
	 */
	Graph m_oldGraph = null;
	public ShowAlgorithmPageAction(Controller ctrl) throws Exception
	{
		super(ctrl, Controller.MODE_ALGORITHM,true);
		m_oldGraph = getController().getGraph();
	}
	
	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstraModule.actions.Action#Do()
	 */
	public boolean doAction() throws Exception
	{		
		getController().showAlgorithmPage();
		Graph gr = getController().getGraph();
		Node node = gr.getStartNode();
		if (node == null) {
			gr.findNode(1).setStart(true);
		}
		gr.getStartNode().getIndex();
		getController().setGraph(gr);
		super.doAction();
		State state = getController().getState(0);
		if(state != null)
		{
		    getController().setStatusbarText(state.getDescriptionEx());
		    getController().setModifiedFlag();
		}		
		return true;
	}
	
	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstraModule.actions.Action#Undo()
	 */
	public boolean undoAction() throws Exception 
	{
		super.undoAction();
		getController().setGraph(m_oldGraph);
		getController().showEditingPage();
		return true;
	}
	
}
