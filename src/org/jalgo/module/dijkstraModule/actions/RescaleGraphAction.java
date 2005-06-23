/*
 * Created on 06.06.2005
 *
 */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;

/**
 * @author Hannes Stra"s
 *
 */
public class RescaleGraphAction extends GraphAction
{
	public RescaleGraphAction(Controller ctrl) throws Exception
	{
		super(ctrl);
		this.registerAndDo(true);
	}

	/* (non-Javadoc)
	 * @see org.jalgo.module.dijkstraModule.actions.Action#doAction()
	 */
	public boolean doAction() throws Exception
	{
		getController().getGraph().rescale();
		getController().setGraph(getController().getGraph());
		return true;
	}
}
