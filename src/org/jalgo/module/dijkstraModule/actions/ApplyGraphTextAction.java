/*
 * Created on 20.05.2005
 *
 *
 */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;

/**
 * @author Frank Staudinger
 * Base for the ApplyEdgeListAction and ApplyNodeListAction
 */

public abstract class ApplyGraphTextAction extends GraphAction {

	protected String m_strText;
	
	/**
	 * @param ctrl Controller-Object
	 * @param strText current text of the nodelist/edgelist textfield
	 */
	public ApplyGraphTextAction(Controller ctrl,String strText) throws Exception
	{
		super(ctrl);
		m_strText = strText;
		registerAndDo(true);
	}


	

}
