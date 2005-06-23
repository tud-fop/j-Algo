/*
 * Created on 09.05.2005
 *
  */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;

/**
 * @author Frank
 *
  */
public class RedoAction extends Action {
	/**
	 * 
	 */
	public RedoAction(Controller ctrl) throws Exception
	{
		super(ctrl);
		registerAndDo(false);
	
	}

	public boolean doAction() throws Exception
	{
		return this.getController().redoAction();
	}

	public boolean undoAction() throws Exception
	{
		return this.getController().undoAction();
	}
}
