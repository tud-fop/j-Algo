/*
 * Created on 20.05.2005
 *
 *
 */
package org.jalgo.module.dijkstraModule.actions;

import java.util.ArrayList;

import org.jalgo.module.dijkstraModule.gui.Controller;
import org.jalgo.module.dijkstraModule.model.Graph;
import org.jalgo.module.dijkstraModule.util.NodeListParser;
import org.jalgo.module.dijkstraModule.util.ParsingException;

/**
 * @author Frank Staudinger
 * This class uses the NodeListParser to convert a string into a Graph and 
 * updates the Controllers model
 * 
 */
public class ApplyNodeListAction extends ApplyGraphTextAction {

	/**
	 * @param ctrl Reference to the Controller object
	 * @param strText The text You want to parse.
	 * @throws Exception ParsingException thrown by the NodeListParser
	 */	public ApplyNodeListAction(Controller ctrl,String strText) throws Exception
	{
		super(ctrl,strText);
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.demo.actions.Action#Do()
	 */
	public boolean doAction()  throws Exception
	{

		Graph gr = new Graph(new ArrayList(), new ArrayList());
		try
		{
			this.getController().setGraph(new NodeListParser().getParsedNodeList(this.m_strText,getOldGraph()));
		}
		catch(ParsingException e)
		{
			getController().setGraph(getOldGraph());
			throw e;
		}
		return true;
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstra.demo.actions.Action#Undo()
	 */
	public boolean undoAction() throws Exception
	{
		this.getController().setGraph(getOldGraph());
		return true;
	}

}
