/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer science. It is written in Java and platform independent. j-Algo is developed with the help of Dresden University of Technology.
 *
 * Copyright (C) 2004-2005 j-Algo-Team, j-algo-development@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

/*
 * Created on 09.05.2005
 *
 */
package org.jalgo.module.dijkstra.actions;

import org.jalgo.module.dijkstra.gui.Controller;

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

public abstract class Action {
	protected Controller m_Controller;

	public Action(Controller ctrl) {
		m_Controller = ctrl;
	}

	/**
	 * @return Return m_Controller field
	 */
	public Controller getController() {
		return m_Controller;
	}

	/**
	 * It's intend that You call this function in the constructor of Your derived class.
	 * @param bRegisterInStack true if You want to register the action in the controller's action stack
	 * @return Return the result of doAction()
	 * @throws ActionException
	 */
	protected boolean registerAndDo(boolean bRegisterInStack) throws ActionException {
		return m_Controller.registerAndDoAction(this, bRegisterInStack);
	}

	/**
	 * Implement the actions You want to be performed.
	 * @return return true if the action was successful performed.
	 * @throws ActionException
	 */
	public abstract boolean doAction() throws ActionException;

	/**
	 * Provide code to undo the changes made by the doAction() function.
	 * @return return true if undoing the action was successful performed.
	 * @throws ActionException
	 */
	public abstract boolean undoAction() throws ActionException;

}
