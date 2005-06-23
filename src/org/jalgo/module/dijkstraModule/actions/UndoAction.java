/*
 * Created on 09.05.2005
 *
  */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;

/**
 * @author Frank Staudinger
 *
 */
public class UndoAction  extends RedoAction 
{
	/**
	 * 
	 */
	public UndoAction(Controller ctrl) throws Exception
	{
		super(ctrl);
		
	}
	
	public boolean doAction() throws Exception
	{
		return super.undoAction();
	}

	public boolean undoAction() throws Exception
	{
		return super.doAction();
	}
}
