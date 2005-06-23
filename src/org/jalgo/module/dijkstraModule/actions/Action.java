/*
 * Created on 09.05.2005
 *
 */
package org.jalgo.module.dijkstraModule.actions;

import org.jalgo.module.dijkstraModule.gui.Controller;

/**
 * @author Frank
 *
 * Your derived Action class should look like this
 * 
 * class FooAction extends Action
 * {
 * 		FooAction(Controller ctrl)
 * 		{
 * 			super(ctrl);
 * 			registerAndDo(true);
 * 		}
 * }
 * 
 * 
 */

public abstract class Action  extends Object 
{
	protected Controller m_Controller;
	
	public Action(Controller ctrl)
	{
		m_Controller = ctrl;
	};
	
	/**
	 * @return Return m_Controller field
	 */
	public Controller getController()
	{
		return m_Controller;
	}
	
	
	
	/**
	 * It's intend that You call this function in the constructor of Your derived class.
	 * @param bRegisterInStack true if You want to register the action in the controller's action stack
	 * @return Return the result of doAction()
	 * @throws Exception
	 */
	protected boolean registerAndDo(boolean bRegisterInStack) throws Exception
	{
		return m_Controller.registerAndDoAction(this,bRegisterInStack);	
	}
	
	
	/**
	 * Implement the actions You want to be performed.
	 * @return return true if the action was successful performed.
	 * @throws Exception
	 */
	public abstract boolean doAction() throws Exception;
	/**
	 * Provide code to undo the changes made by the doAction() function.
	 * @return return true if undoing the action was successful performed.
	 * @throws Exception
	 */
	public abstract boolean undoAction() throws Exception;	
	
}
