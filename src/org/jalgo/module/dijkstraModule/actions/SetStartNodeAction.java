/*
 * Created on 31.05.2005 22:06:08
 *
  */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.model.*;

/**
 * @author Frank
 *
  */
public class SetStartNodeAction extends GraphAction {

    int m_nNewNode;
    int m_nOldNode = 0;
    int m_nOldStep = 0;
 

    /**
     * @param ctrl Controller for this action
     * @param nNode Index of the new start node
     * @throws Exception
     */
    public SetStartNodeAction(Controller ctrl, int nNode) throws Exception {
        super(ctrl);
        m_nNewNode = nNode;
        Graph gr = getController().getGraph();
        Node node = gr.getStartNode();
        if(node != null)
            m_nOldNode = node.getIndex();    
        m_nOldStep = getController().getCurrentStep();
        super.registerAndDo(true);
        
    }

    /* (non-Javadoc)
     * @see org.jalgo.module.dijkstraModule.actions.Action#doAction()
     */
    public boolean doAction() throws Exception {
 
        Graph gr = getController().getGraph();
        Node node = gr.getStartNode();
        if(node != null)
            node.setStart(false);
        gr.findNode(m_nNewNode).setStart(true);
        getController().setGraph(gr);
        State state = getController().getState(0);
        if(state != null)
        {
	        getController().setStatusbarText(state.getDescriptionEx());
        }        
        return true;
    }
    
	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstraModule.actions.Action#undoAction()
	 */
	public boolean undoAction() throws Exception {
		super.undoAction();
		getController().getGraph().findNode(m_nNewNode).setStart(false);
		if(m_nOldNode > 0)
		    getController().getGraph().findNode(m_nOldNode).setStart(true);
		State state = getController().getState(m_nOldStep);
	    if(state != null)
	    {
	        getController().setStatusbarText(state.getDescriptionEx());
	    }        		
		return true;
	}
 
}
