/* j-Algo - j-Algo is an algorithm visualization tool, especially useful for students and lecturers of computer sience. It is written in Java and platform independant. j-Algo is developed with the help of Dresden University of Technology.
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
	}
}
