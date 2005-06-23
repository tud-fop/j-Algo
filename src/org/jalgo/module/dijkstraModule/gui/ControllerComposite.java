/*
 * Created on 20.05.2005
 *
 
 */
package org.jalgo.module.dijkstraModule.gui;

import org.eclipse.swt.widgets.Composite;

/**
 * @author Frank Staudinger
 *
 *The ControllerComposite is the base for all Composites in the Dijkstra-module.
 */
public class ControllerComposite extends Composite {

	private Controller m_Controller;
	
	/**
	 * @return	Returns the m_Controller field
	 */
	protected Controller getController()
	{
		return this.m_Controller;
	}
	/**
	 * @param ctrl			Current controller for this composite
	 * @param cmpParent		Parent of this composite
	 * @param nStyle		Window style for this composite
	 */
	public ControllerComposite(Controller ctrl,Composite cmpParent, int nStyle)
	{
		super(cmpParent,nStyle);
		this.m_Controller = ctrl;
	};
}
