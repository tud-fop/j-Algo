/*
 * Created on 20.05.2005
 *
 */
package org.jalgo.module.dijkstraModule.actions;
import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.model.Graph;
import org.jalgo.module.dijkstraModule.util.EdgeListParser;
import org.jalgo.module.dijkstraModule.util.ParsingException;
/**
 * @author Frank Staudinger
 * This class uses the EdgeListParser to convert a string into a Graph and 
 * updates the Controllers model
 * 
 */
public class ApplyEdgeListAction extends ApplyGraphTextAction {


	
	/**
	 * @param ctrl Reference to the Controller object
	 * @param strEdgeList The text You want to parse.
	 * @throws Exception ParsingException thrown by the EdgeListParser
	 */
	public ApplyEdgeListAction(Controller ctrl,String strEdgeList) throws Exception
	{
		super(ctrl,strEdgeList);

	}
	
	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.demo.actions.Action#doAction()
	 */
	public boolean doAction() throws Exception
	{
		try
		{
		    
		    Graph gr = new EdgeListParser().getParsedEdgeList(this.m_strText,getOldGraph());
			this.getController().setGraph(gr);
		}
		catch(ParsingException e)
		{
			getController().setGraph(m_oldGraph);
			throw e;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.demo.actions.Action#undoAction()
	 */
	public boolean undoAction() throws Exception
	{
		this.getController().setGraph(getOldGraph());
		return true;
	}

	
}
